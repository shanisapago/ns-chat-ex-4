package com.example.nschat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.example.nschat.adapters.MessageListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
public class ChatWUser extends AppCompatActivity {
    private ChatRow contact;
    public static MessageListAdapter adapter;
    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.example.MESSAGE_RECEIVED")){
                String message=intent.getStringExtra("message");
                String sender=intent.getStringExtra("sender");
                String time=intent.getStringExtra("time");
                MessageItem m=new MessageItem(message,time,sender,currentUsername,contact.getId(),contact.getName(),contact.getPic());
                List<MessageItem>newList=messagesVM.getRepository().getMessageDao().index();
                Collections.reverse(newList);
                newList.add(m);
                messagesVM.getRepository().getMessageDao().deleteTable();
                Collections.reverse(newList);
                for(int i=0;i<newList.size();i++){
                    messagesVM.getRepository().getMessageDao().insert(newList.get(i));
                }
                List<MessageItem>messageItemList=messagesVM.getRepository().getMessageDao().getMessages(currentUsername,sender);
                List<Message>messageList=new ArrayList<>();

                for(MessageItem mi:messageItemList)
                {
                    int idChat=mi.getChatId();
                    String created=mi.getTime();
                    User sender2=new User(mi.getSender(),mi.getDisplayNameSender(),mi.getProfilePic());
                    String content= mi.getMessageContent();
                    messageList.add(new Message(idChat,created,sender2,content));
                }
                if(messageList.size()>0) {
                    adapter.setMessages(messageList);
                    adapter.notifyDataSetChanged();
                }

            }
        }
    };
    private ContactsDao contactsDao;
    private String currentUsername;
    private ContactsDB db;
    private String url;
    private String displayName;
    private String profilePic;
    private MessagesVM messagesVM;
    public static ListView lvMessages;
    String id;
    public Bitmap photoToBitmap(String photo){
        byte[] decodeString = Base64.decode(photo,Base64.DEFAULT);
        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        return decodeByte;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_w_user);
        IntentFilter filter=new IntentFilter("com.example.MESSAGE_RECEIVED");
        registerReceiver(broadcastReceiver,filter);
        db = Room.databaseBuilder(getApplicationContext(), ContactsDB.class, "tryContacts8DB")
                .allowMainThreadQueries()
                .build();
        contactsDao = db.contactsDao();
        if(getIntent().getExtras()!=null){
            url=getIntent().getExtras().getString("url");
        }
        messagesVM=new MessagesVM(getApplicationContext(),url);
        currentUsername=getIntent().getExtras().getString("username");
        adapter = new MessageListAdapter(messagesVM.getAll().getValue(),currentUsername);
        lvMessages = findViewById(R.id.lvMessages);
        lvMessages.setAdapter(adapter);
        if (getIntent().getExtras() != null) {
            id = getIntent().getExtras().getString("id");
            contact = contactsDao.get(id);
            currentUsername=getIntent().getExtras().getString("username");
            displayName=getIntent().getExtras().getString("displayName");
            profilePic=getIntent().getExtras().getString("profilePic");
            TextView name = findViewById(R.id.name);
            name.setText(contact.getName());
            de.hdodenhof.circleimageview.CircleImageView imgUser = findViewById(R.id.imgUser);
            imgUser.setImageBitmap(photoToBitmap(contact.getPic()));
            FloatingActionButton btnSend = findViewById(R.id.buttonSend);
            btnSend.setOnClickListener(view -> {
                int chatId = getIntent().getExtras().getInt("chatId");
                EditText etSend = findViewById(R.id.etSend);
                String messageContent = etSend.getText().toString();
                etSend.getText().clear();
                int hour = Calendar.getInstance().get(Calendar.HOUR);
                int min = Calendar.getInstance().get(Calendar.MINUTE);
                String minutes;
                if (min < 10)
                    minutes = "0" + min;
                else
                    minutes = String.valueOf(min);

                String hourAndMinutes = hour + 12 + ":" + minutes;
                MessageItem messageToAdd = new MessageItem(messageContent, hourAndMinutes, currentUsername, contact.getUsername(),chatId,displayName,profilePic);
                messagesVM.getRepository().getMessageDao().insert(messageToAdd);
                int idChat=messageToAdd.getChatId();
                String created=messageToAdd.getTime();
                User sender=new User(messageToAdd.getSender(),messageToAdd.getDisplayNameSender(),messageToAdd.getProfilePic());
                String content= messageToAdd.getMessageContent();
                messagesVM.getRepository().getMessageListData().getValue().add(new Message(idChat,created,sender,content));
                adapter.notifyDataSetChanged();
                contact.setLastMessage(messageContent);
                contact.setTime(hourAndMinutes);
                contactsDao.update(contact);
                String token = getIntent().getExtras().getString("token");
                messagesVM.add(chatId, token, messageToAdd.getMessageContent());
            });
        }
    }
    protected void onResume(){
        super.onResume();
        List<MessageItem>messageItemList=messagesVM.getRepository().getMessageDao().getMessages(currentUsername, contact.getUsername());
        List<Message>messageList=new ArrayList<>();

        for(MessageItem mi:messageItemList)
        {
            int idChat=mi.getChatId();
            String created=mi.getTime();
            User sender=new User(mi.getSender(),mi.getDisplayNameSender(),mi.getProfilePic());
            String content= mi.getMessageContent();
            messageList.add(new Message(idChat,created,sender,content));
        }
        if(messageList.size()>0) {
            adapter.setMessages(messageList);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}