package com.example.contactapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.example.contactapp.databinding.ActivityNewBinding;

public class NewActivity extends AppCompatActivity {
    private ActivityNewBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("New Contact");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new, menu);
        MenuItem menuItem = menu.findItem(R.id.action_save);


        menuItem.setOnMenuItemClickListener(e ->{
            Intent intent = getIntent();
            if(intent != null) {
                String firstname = binding.tvFirstName.getText().toString();
                String lastName = binding.tvLastName.getText().toString();
                String phone = binding.tvPhone.getText().toString();
                String email = binding.tvEmail.getText().toString();
                intent.putExtra("name", lastName + " " + firstname);
                intent.putExtra("phone", phone);
                intent.putExtra("email", email);
                setResult(RESULT_OK, intent);
                finish();
            }
            return true;
        });

        return super.onCreateOptionsMenu(menu);
    }
}
