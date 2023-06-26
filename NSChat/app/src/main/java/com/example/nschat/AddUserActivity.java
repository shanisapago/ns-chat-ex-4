package com.example.nschat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.example.nschat.api.ChatsAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.concurrent.atomic.AtomicBoolean;
public class AddUserActivity extends AppCompatActivity {
    private ContactsDB db;
    private ContactsDao contactsDao;
    private String currentUsername;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        db= Room.databaseBuilder(getApplicationContext(),ContactsDB.class,"tryContacts8DB")
                .allowMainThreadQueries()
                .build();
        contactsDao=db.contactsDao();
        if(getIntent().getExtras()!=null){
            url=getIntent().getExtras().getString("url");
        }
        Button btnSave=findViewById(R.id.btnSave);
        btnSave.setOnClickListener(view ->{
            EditText etItem=findViewById(R.id.etItem);
            String usernameToAdd=etItem.getText().toString();
            if(getIntent().getExtras()!=null) {
                currentUsername = getIntent().getExtras().getString("username");
                if (!currentUsername.equals(etItem.getText().toString())) {
                String token = getIntent().getExtras().getString("token");
                ChatsAPI chatsAPI = new ChatsAPI(url);
                ChatsVM chatsVM = new ChatsVM(getApplicationContext(), url);
                chatsVM.getRepository().setClassName("addUserActivity");
                chatsVM.reload(token);
                AtomicBoolean flag = new AtomicBoolean(false);
                chatsVM.get().observe(this, userChats -> {
                    if (!flag.get()) {
                        flag.set(true);
                        boolean exist = false;
                        for (UserChat chat : userChats) {
                            if (chat.getUser().getUsername().equals(usernameToAdd)) {
                                exist = true;
                                String username = chat.getUser().getUsername();
                                String name = chat.getUser().getDisplayName();
                                String pic = chat.getUser().getProfilePic();
                                int idChat = chat.getId();
                                ChatRow chatRow = new ChatRow(username, null, name, null, pic, idChat);
                                if (chatsVM.getRepository().getContactsDao().get(username) == null)
                                    contactsDao.insert(chatRow);
                            }
                        }
                        if (!exist) {
                            NewChat newChat = chatsAPI.createChat(usernameToAdd, token);
                            if (newChat != null) {
                                if (newChat.getUser() != null) {
                                    ChatRow userToAdd = new ChatRow(newChat.getUser().getUsername(), null, newChat.getUser().getDisplayName(), null, newChat.getUser().getProfilePic(), newChat.getId());
                                    contactsDao.insert(userToAdd);
                                }
                            } else {
                                etItem.setText("");
                            }
                        }
                    }
                });
            }
            }
            finish();
        });
    }
}