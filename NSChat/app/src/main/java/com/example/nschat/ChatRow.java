package com.example.nschat;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import java.util.ArrayList;
import java.util.List;

@Entity
@TypeConverters(EntityBListConverter.class)
public class ChatRow
{
    private String username;
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int idChat;
    private String time;
    private String name;

    private String lastMessage;
    private String pic;
    @ColumnInfo(name = "entity_b_list")
    private List<MessageItem> messages;
    public ChatRow(String username, String time, String name, String lastMessage, String pic,int idChat)
    {
        this.idChat=idChat;
        this.username=username;
        this.time=time;
        this.name=name;
        this.lastMessage=lastMessage;
        this.pic=pic;
        this.messages=new ArrayList<>();
    }
    public int getIdChat() {
        return idChat;
    }
    public void setMessages(List<MessageItem> messages) {
        this.messages = messages;
    }
    public List<MessageItem> getMessages() {
        return messages;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTime()
    {
        return this.time;
    }

    public String getName()
    {
        return this.name;
    }
    public String getLastMessage()
    {
        return this.lastMessage;
    }
    public String getPic() {
        return this.pic;
    }
}
