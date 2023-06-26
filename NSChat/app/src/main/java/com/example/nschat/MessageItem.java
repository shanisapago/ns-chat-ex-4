package com.example.nschat;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class MessageItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int chatId;
    private  String messageContent;
    private String time;
    private String sender;
    private String reciver;
    private String displayNameSender;
    private String profilePic;
    public MessageItem(String messageContent,String time,String sender,String reciver,int id,String displayNameSender,String profilePic)
    {
        this.messageContent=messageContent;
        this.time=time;
        this.sender=sender;
        this.reciver=reciver;
        this.chatId=id;
        this.displayNameSender=displayNameSender;
        this.profilePic=profilePic;
    }
    public String getDisplayNameSender() {
        return displayNameSender;
    }

    public String getProfilePic() {
        return profilePic;
    }
    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getSender() {
        return sender;
    }

    public String getReciver() {
        return reciver;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public  String getTime()
    {
        return  this.time;
    }
    public String getMessageContent()
    {
        return this.messageContent;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
