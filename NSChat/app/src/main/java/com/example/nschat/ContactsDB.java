package com.example.nschat;
import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities={ChatRow.class} ,version=1)
public abstract class ContactsDB extends RoomDatabase{
    public abstract ContactsDao contactsDao();
}
