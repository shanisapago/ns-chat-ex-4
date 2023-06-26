package com.example.nschat;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
@Dao
public interface MessagesDao {
    @Query("SELECT * FROM messageitem")
    List<MessageItem> index();
    @Query("SELECT * FROM messageitem WHERE id = :id")
    MessageItem get(int id);
    @Query("SELECT * FROM messageitem WHERE (sender = :user1 AND reciver= :user2) OR (sender = :user2 AND reciver= :user1)")
    List<MessageItem> getMessages(String user1,String user2);
    @Query("DELETE FROM messageitem")
    public void deleteTable();
    @Insert
    void insert(MessageItem...messages);
    @Update
    void update(MessageItem...messages);
}
