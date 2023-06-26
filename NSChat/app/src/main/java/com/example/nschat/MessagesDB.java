package com.example.nschat;
import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities={MessageItem.class} ,version=1)
public abstract class MessagesDB extends RoomDatabase {
    public abstract MessagesDao messagesDao();
}
