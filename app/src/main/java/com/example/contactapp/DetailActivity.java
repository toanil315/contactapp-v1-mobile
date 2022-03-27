package com.example.contactapp;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactapp.databinding.ActivityDetailBinding;
import com.example.contactapp.databinding.ActivityMainBinding;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private ContactDao contactDao;
    private AppDatabase appDatabase;
    private Intent intentDetail;
    private String idContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Detail");
        actionBar.setDisplayHomeAsUpEnabled(true);

        appDatabase = AppDatabase.getInstance(this);
        contactDao = appDatabase.contactDao();

        intentDetail = getIntent();
        idContact = intentDetail.getStringExtra("idContact");

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
               Contact contact = contactDao.findContactById(Integer.parseInt(idContact));
               byte[] byteImage = contact.getImage();
               binding.tvName.setText(contact.getName());
               binding.tvPhone.setText(contact.getPhone());
               binding.ivAvatar.setImageBitmap(BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length));
               Log.d("DEBUG1", "id contact receive: " + idContact);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_update);
        menuItem.setOnMenuItemClickListener(e ->{
            intentDetail = new Intent(DetailActivity.this, NewActivity.class);
            intentDetail.putExtra("idContact", idContact);
            startActivity(intentDetail);
            return true;
        });
        return super.onCreateOptionsMenu(menu);
    }
}
