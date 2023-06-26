package com.example.nschat.repository;
import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.nschat.LocalMessagesDB;
import com.example.nschat.Message;
import com.example.nschat.MessagesDB;
import com.example.nschat.MessagesDao;
import com.example.nschat.api.ChatsAPI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
public class RepositoryMessges {

    private ChatsAPI chatsAPI;
    private MessagesDao messagesDao;
    private RepositoryMessges.MessageListData messagesListData;
    private String className;
    private String url;

    public RepositoryMessges(Context context, String url){
        LocalMessagesDB localMessagesDB=new LocalMessagesDB();
        MessagesDB messagesDB=LocalMessagesDB.getInstance(context);
        messagesDao=messagesDB.messagesDao();
        messagesListData =new RepositoryMessges.MessageListData();
        className=null;
        this.url=url;
        chatsAPI=new ChatsAPI(url);

    }
    public RepositoryMessges(Context context, String url,String t){
        LocalMessagesDB localMessagesDB=new LocalMessagesDB();
        MessagesDB messagesDB=LocalMessagesDB.getInstance(context);
        messagesDao=messagesDB.messagesDao();
        messagesListData =new RepositoryMessges.MessageListData(t);
        className=null;
        this.url=url;
        chatsAPI=new ChatsAPI(url);

    }
    public void setUrl(String url){
        this.url=url;
    }
    public MutableLiveData<List<Message>> getMessageListData(){
        return messagesListData;
    }

    class MessageListData extends MutableLiveData<List<Message>> {
    public MessageListData() {
        super();
        setValue(new LinkedList<Message>());
    }
    public void add(Message m){
        List<Message>mk=new ArrayList<>();
        mk.add(m);
        this.postValue(mk);
    }
    public MessageListData(String t) {
        super();
        postValue(new LinkedList<Message>());
    }
    @Override
    protected void onActive() {
        super.onActive();
    }

}
    public MessagesDao getMessageDao(){
        return messagesDao;
    }

    public LiveData<List<Message>> getAll() {
        return messagesListData;
    }
    public void add(final int id, final String token,final String msg){
        chatsAPI.post(id, token, msg);
    }
    public void reload(final int id, final String token){
        chatsAPI.getMessage(messagesListData, id, token);
    }
}