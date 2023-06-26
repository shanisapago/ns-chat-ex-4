package com.example.nschat.repository;
import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.nschat.ContactsDB;
import com.example.nschat.ContactsDao;
import com.example.nschat.LocalContactDB;
import com.example.nschat.UserChat;
import com.example.nschat.api.ChatsAPI;
import java.util.LinkedList;
import java.util.List;
public class RepositoryChat {
    private String url;
    private ChatsAPI chatsAPI;
    private ContactsDao contactsDao;
    private UserListData userListData;
    private String className;

    public RepositoryChat(Context context, String url){
        LocalContactDB localContactDB=new LocalContactDB();
        ContactsDB contactsDB=LocalContactDB.getInstance(context);
        contactsDao=contactsDB.contactsDao();
        userListData =new UserListData();
        chatsAPI=new ChatsAPI(url);
        this.url=url;
        className=null;
    }
    public void setUrl(String url){
        this.url=url;
    }
    public String getClassName(){
        return this.className;
    }
    public void setClassName(String className){
        this.className=className;
    }
    public MutableLiveData<List<UserChat>> getUserListData(){
        return userListData;
    }

    class UserListData extends MutableLiveData<List<UserChat>> {
        public UserListData() {
        super();
        setValue(new LinkedList<UserChat>());
        }

        @Override
        protected void onActive() {
        super.onActive();
        }

    }
    public ContactsDao getContactsDao(){
        return contactsDao;
    }

    public LiveData<List<UserChat>> getAll() {
        return userListData;
    }
    public void add(final String usernameToAdd, final String token){
        chatsAPI.createChat(usernameToAdd, token);
    }
    public void reload(final String token){
        chatsAPI.getChats(userListData, token);
    }

}
