package com.example.contactapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM Contact")
    public List<Contact> getAllContact();

    @Query("SELECT * FROM Contact WHERE Contact.id = :contactId")
    public Contact findContactById(int contactId);

    @Insert
    public void insertAll(Contact... contact);

    @Delete
    public void deleteContacts(Contact... contact);

    @Update
    public void updateContacts(Contact... contact);
}
