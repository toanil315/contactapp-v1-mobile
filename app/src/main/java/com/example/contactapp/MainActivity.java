package com.example.contactapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.app.SearchManager;
import android.widget.SearchView.OnQueryTextListener;

import com.example.contactapp.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArrayList<Contact> contactList;
    private ContactAdapter contactAdapter;
    private ArrayList<Contact> contactsCopy = new ArrayList<Contact>();

    private ContactDao contactDao;
    private AppDatabase appDatabase;
    private static final int REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);

        appDatabase = AppDatabase.getInstance(this);
        contactDao = appDatabase.contactDao();

//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                Contact contact1 = new Contact("Nguyen Van A", "0905012303", "a@gmail.com");
//                Contact contact2 = new Contact("Nguyen Van B", "0905012304", "b@gmail.com");
//                contactDao.insertAll(contact1, contact2);
//            }
//        });

        contactList = new ArrayList<Contact>();
        contactList.add(new Contact("Nguyen Van A", "0905012303", "a@gmail.com"));
        contactList.add(new Contact("Nguyen Van B", "0905012304", "b@gmail.com"));
        contactList.add(new Contact("Nguyen Van D", "0905022304", "c@gmail.com"));

        contactAdapter = new ContactAdapter(contactList);
        contactsCopy.addAll(contactList);
        binding.rvContacts.setAdapter(contactAdapter);
        binding.rvContacts.setLayoutManager(new LinearLayoutManager(this));

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewActivity.class);
                startActivityForResult(intent,  REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            String name = data.getStringExtra("name");
            String phone = data.getStringExtra("phone");
            String email = data.getStringExtra("email");
            Contact newContact = new Contact(name, phone, email);
            contactList.add(newContact);
            contactsCopy.clear();
            contactsCopy.addAll(contactList);
            contactAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search contact");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                contactAdapter.getFilter().filter(newText);
                contactList.clear();
                if(newText.isEmpty()) {
                    contactList.addAll((contactsCopy));
                }
                else {
                    for(Contact item : contactsCopy) {
                        if (item.getName().toLowerCase().contains(newText.toString().toLowerCase())) {
                            contactList.add(item);
                        }
                    }
                }
                contactAdapter.notifyDataSetChanged();
                Log.d("eror","log mesage here: " + Integer.toString(contactsCopy.size()));
                return false;

            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}