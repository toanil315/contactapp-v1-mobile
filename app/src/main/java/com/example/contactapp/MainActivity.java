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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArrayList<Contact> contactList;
    private ContactAdapter contactAdapter;
    private ArrayList<Contact> contactsCopy = new ArrayList<Contact>();
    private Intent intentMain;

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

        contactList = new ArrayList<Contact>();
        contactAdapter = new ContactAdapter(contactList);
        binding.rvContacts.setAdapter(contactAdapter);
        binding.rvContacts.setLayoutManager(new LinearLayoutManager(this));

        getListContact();
        contactAdapter.notifyDataSetChanged();

//        intentMain = getIntent();
//        String content = intentMain.getStringExtra("UpdateSuccess");
//        if(content != null && content.equals("OK")) {
//            Log.d("DEBUG1", "UPDATE SUCCESS");
//            getListContact();
//            contactAdapter.notifyDataSetChanged();
//        }

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentMain = new Intent(MainActivity.this, NewActivity.class);
                startActivityForResult(intentMain,  REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String content = data.getStringExtra("Content");
                    if(content != null) {
                        getListContact();
                        contactAdapter.notifyDataSetChanged();
                    }
                }
            });
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
                return false;

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void getListContact() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<Contact> listContact = contactDao.getAllContact();
                contactList.clear();
                contactList.addAll(listContact);
                contactsCopy.clear();
                contactsCopy.addAll(listContact);
            }
        });
    }
}