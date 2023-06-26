package com.example.nschat;

public class Message {
    private int id;
    private String created;
    private User sender;
    private String content;

    public Message(int id, String created, User sender, String content) {
        this.id = id;
        this.created = created;
        this.sender = sender;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public User getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "id=" + id +
                ", created='" + created + '\'' +
                ", sender=" + sender +
                ", content='" + content + '\'' +
                '}';
    }
}
