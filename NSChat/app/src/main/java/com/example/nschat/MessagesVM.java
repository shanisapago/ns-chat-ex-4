package com.example.nschat;
import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.nschat.repository.RepositoryMessges;
import java.util.List;
public class MessagesVM extends ViewModel {
    private RepositoryMessges repository;
    private LiveData<List<Message>> listMessagesChat;
    public MessagesVM (Context context,String url) {
        repository = new RepositoryMessges(context,url);
        listMessagesChat = repository.getAll();
    }
    public RepositoryMessges getRepository(){
        return repository;
    }

    public LiveData<List<Message>> get() { return listMessagesChat; }

    public void add(int id, String token, String msg) { repository.add(id,token, msg); }
    public void reload(int id,String token) { repository.reload(id, token); }
    public LiveData<List<Message>> getAll() { return repository.getAll(); }

}
