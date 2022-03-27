package com.example.contactapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.contactapp.databinding.ActivityNewBinding;

import java.io.ByteArrayOutputStream;

public class NewActivity extends AppCompatActivity {
    private ActivityNewBinding binding;
    private ContactDao contactDao;
    private AppDatabase appDatabase;
    private Intent intentNew;
    private String idContact; //Check: if idContact exist -> UPDATE, ELSE -> CREATE NEW CONTACT

    private static final int REQUEST_CODE_CAPTURE = 100;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);

        appDatabase = AppDatabase.getInstance(this);
        contactDao = appDatabase.contactDao();

        ActionBar actionBar = getSupportActionBar();


//        if(ContextCompat.checkSelfPermission(NewActivity.this,
//                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(NewActivity.this, new String[] {
//                    Manifest.permission.CAMERA
//            }, REQUEST_CODE_CAPTURE);
//        }

        intentNew = getIntent();
        idContact = intentNew.getStringExtra("idContact");
        if(idContact != null) {
            Log.d("DEBUG1", "id contact from detail: " + idContact);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() { // if idContact exist -> get contact from db to show.
                    Contact contact = contactDao.findContactById(Integer.parseInt(idContact));
                    byte[] byteImage = contact.getImage();
                    binding.tvName.setText(contact.getName());
                    binding.tvPhone.setText(contact.getPhone());
                    binding.tvEmail.setText(contact.getEmail());
                    binding.ivAvatar.setImageBitmap(BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length));
                }
            });
            actionBar.setTitle("Update Contact");
        }
        else {
            actionBar.setTitle("New Contact");
        }

        actionBar.setDisplayHomeAsUpEnabled(true);

        binding.ibPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentCapture, REQUEST_CODE_CAPTURE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new, menu);
        MenuItem menuItem = menu.findItem(R.id.action_save);
        menuItem.setOnMenuItemClickListener(e ->{
            intentNew = new Intent(NewActivity.this, MainActivity.class);
            if(intentNew != null) {
                //GET Bitmap image from image view
                binding.ivAvatar.invalidate();
                BitmapDrawable drawable = (BitmapDrawable) binding.ivAvatar.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] byteImage = baos.toByteArray();
                //Info contact
                String name = binding.tvName.getText().toString();
                String phone = binding.tvPhone.getText().toString();
                String email = binding.tvEmail.getText().toString();

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        if(idContact != null) { // != null -> UPDATE
                            Contact contact = contactDao.findContactById(Integer.parseInt(idContact));
                            contact.setName(name);
                            contact.setPhone(phone);
                            contact.setEmail(email);
                            contact.setImage(byteImage);
                            contactDao.updateContacts(contact);
                            intentNew.putExtra("UpdateSuccess", "OK");
                            startActivity(intentNew);
                        }
                        else { // idContact == null -> CREATE
                            contactDao.insertAll(new Contact(name, phone, email, byteImage));
                            intentNew.putExtra("Content", "OK");
                            setResult(MainActivity.RESULT_OK, intentNew);
                            finish();
                            Log.d("DEBUG1", "CREATE SUCCESS");
                        }
                    }
                });
            }
            return true;
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_CAPTURE) {
            //GET CAPTURE Image
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            //SET CAPTURE Image
            binding.ivAvatar.setImageBitmap(captureImage);
        }
    }
}
