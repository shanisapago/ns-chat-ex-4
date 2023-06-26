package com.example.nschat;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
@Dao
public interface ContactsDao {
    @Query("SELECT * FROM chatrow")
    List<ChatRow> index();
    @Query("SELECT * FROM chatrow WHERE id = :id")
    ChatRow get(int id);
    @Query("DELETE FROM chatrow")
    public void deleteTable();
    @Query("SELECT * FROM chatrow WHERE username = :username")
    ChatRow get(String username);
    @Insert
    void insert(ChatRow...contacts);
    @Update
    void update(ChatRow...contacts);
}
