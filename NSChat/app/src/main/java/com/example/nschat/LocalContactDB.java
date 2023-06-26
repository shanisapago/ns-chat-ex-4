package com.example.nschat;
import android.content.Context;
import androidx.room.Room;
public class LocalContactDB {
    private static ContactsDB db;
    public LocalContactDB(){
    }
    public static ContactsDB getInstance(Context context){
        return db=Room.databaseBuilder(context, ContactsDB.class, "tryContacts8DB")
                .allowMainThreadQueries()
                .build();
    }
}
