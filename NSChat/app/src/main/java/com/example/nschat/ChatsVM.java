package com.example.nschat;
import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.nschat.repository.RepositoryChat;
import java.util.List;
public class ChatsVM extends ViewModel {
        private RepositoryChat repository;
        private LiveData<List<UserChat>> listUserChat;
        public ChatsVM (Context context,String url) {
            repository = new RepositoryChat(context,url);
            listUserChat = repository.getAll();
            }
        public RepositoryChat getRepository(){
            return repository;
        }

        public LiveData<List<UserChat>> get() { return listUserChat; }

        public void add(String username, String token) { repository.add(username,token); }
        public void reload(String token) { repository.reload(token); }
        public LiveData<List<UserChat>> getAll() { return repository.getAll(); }
}
