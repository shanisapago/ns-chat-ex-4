package com.example.nschat;
import java.util.List;
public class Chat {
    private int id;
    private List<User> users;
    private List<Message> messages;

    public Chat(int id, List<User> users, List<Message> messages) {
        this.id = id;
        this.users = users;
        this.messages = messages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", users=" + users +
                ", messages=" + messages +
                '}';
    }
}
