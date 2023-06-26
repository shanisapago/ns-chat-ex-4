package com.example.nschat;
import android.content.Context;
import androidx.room.Room;
public class LocalMessagesDB {
    private static MessagesDB messdb;
    public static MessagesDB getInstance(Context context){
        return messdb= Room.databaseBuilder(context, MessagesDB.class, "try8DB")
                .allowMainThreadQueries()
                .build();

}

}
