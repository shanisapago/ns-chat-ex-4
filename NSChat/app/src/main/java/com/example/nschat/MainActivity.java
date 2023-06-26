package com.example.nschat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.nschat.adapters.ChatListAdapter;
import com.example.nschat.api.AppTokenAPI;
import com.example.nschat.api.UsersAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    private ChatListAdapter adapter;
    private String currentUsername;
    private String displayName;
    private String profilePic;
    String token;
    private ChatsVM chatsVM;
    private MessagesVM messagesVM;
    private String url;
    private final int REQUEST_PERMISSION_NOTIFICATION_STATE=1;
    public Bitmap photoToBitmap(String photo){
        byte[] decodeString = Base64.decode(photo,Base64.DEFAULT);
        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        return decodeByte;
    }
    private void showPhoneStatePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.POST_NOTIFICATIONS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.POST_NOTIFICATIONS)) {
                showExplanation("Permission shani", "Rationale", android.Manifest.permission.POST_NOTIFICATIONS, REQUEST_PERMISSION_NOTIFICATION_STATE);
            } else {
                requestPermission(Manifest.permission.POST_NOTIFICATIONS, REQUEST_PERMISSION_NOTIFICATION_STATE);
            }
        } else {
            Toast.makeText(MainActivity.this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_NOTIFICATION_STATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }
    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        requestPermission(permission, permissionRequestCode);
    }
    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showPhoneStatePermission();
        if(getIntent().getExtras()!=null){
            url=getIntent().getExtras().getString("url");
        }
        ListView lstUsers=findViewById(R.id.lstUsers);
        chatsVM=new ChatsVM(getApplicationContext(),url);
        messagesVM=new MessagesVM(getApplicationContext(),url);
        adapter = new ChatListAdapter(chatsVM.getAll().getValue());
        lstUsers.setAdapter(adapter);
        chatsVM.get().observe(this, userChats -> {
            for(UserChat chat : userChats){
                    String username=chat.getUser().getUsername();
                    String name=chat.getUser().getDisplayName();
                    String pic=chat.getUser().getProfilePic();
                    int idChat=chat.getId();
                    ChatRow chatRow;
                    if(chat.getLastMessage()!=null&&chat.getLastMessage().getCreated()!=null){
                        String[] date=chat.getLastMessage().getCreated().split("T");
                        String[] timeArray = date[date.length-1].split(":");
                        String time=timeArray[0]+":"+timeArray[1];
                        String lastMessage=chat.getLastMessage().getContent();
                        chatRow=new ChatRow(username, time, name, lastMessage, pic, idChat);
                    }
                    else{
                        chatRow=new ChatRow(username, null, name, null, pic, idChat);
                    }
                if(chatsVM.getRepository().getContactsDao().get(username)==null)
                    chatsVM.getRepository().getContactsDao().insert(chatRow);
            }
            adapter.setChats(userChats);
            adapter.notifyDataSetChanged();
        });
        token=getIntent().getExtras().getString("token");
        chatsVM.getRepository().setClassName("MainActivity");
        chatsVM.reload(token);
        FloatingActionButton setting=findViewById(R.id.btnSetting);
        setting.setOnClickListener(v->{
            Intent i=new Intent(this, Setting.class);
            i.putExtra("screen","chatList");
            startActivity(i);
        });
        FloatingActionButton logout=findViewById(R.id.Logout);
        logout.setOnClickListener(v->{
            AppTokenAPI appTokensAPI=new AppTokenAPI(url);
            appTokensAPI.delete(currentUsername.toString());
            Intent i=new Intent(this, Login.class);
            i.putExtra("url",url);
            startActivity(i);
        });
        FloatingActionButton btnAdd=findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(view ->{
            Intent i =new Intent(this, AddUserActivity.class);
            i.putExtra("token",token);
            i.putExtra("username",currentUsername);
            i.putExtra("url",url);
            startActivity(i);
        });
        lstUsers.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(this, ChatWUser.class);
            messagesVM.reload(chatsVM.getAll().getValue().get(i).getId(),token);
            messagesVM.get().observe(this, messages -> {
            String messageContent=null;
            String time=null;
            messagesVM.getRepository().getMessageDao().deleteTable();
                String sender=null;
                String reciver=null;
            for(Message m:messages)
            {
                messageContent=m.getContent();
                String[] date=m.getCreated().split("T");
                String[] timeArray = date[1].split(":");
                time=timeArray[0]+":"+timeArray[1];
                if(m.getSender().getUsername().equals(currentUsername)) {
                    sender = currentUsername;
                    reciver= chatsVM.getAll().getValue().get(i).getUser().getUsername(); //change!
                }
                else {
                    sender = chatsVM.getAll().getValue().get(i).getUser().getUsername();
                    reciver= currentUsername;
                }
                MessageItem messageItem=new MessageItem(messageContent,time,sender,reciver,m.getId(),m.getSender().getDisplayName(),m.getSender().getProfilePic());
                messagesVM.getRepository().getMessageDao().insert(messageItem);
                chatsVM.getAll().getValue().get(i).getLastMessage().setContent(messageContent);
                 chatsVM.getAll().getValue().get(i).getLastMessage().setCreated(time);
                 chatsVM.getRepository().getContactsDao().update(chatsVM.getRepository().getContactsDao().get(chatsVM.getAll().getValue().get(i).getUser().getUsername()));
            }
                if(sender!=null) {
                    List<MessageItem> messageItemList = messagesVM.getRepository().getMessageDao().getMessages(sender, reciver);
                    List<Message> messageList = new ArrayList<>();
                    for (MessageItem mi : messageItemList) {
                        int idChat = mi.getChatId();
                        String created = mi.getTime();
                        User sender2 = new User(mi.getSender(), mi.getDisplayNameSender(), mi.getProfilePic());
                        String content = mi.getMessageContent();
                        messageList.add(new Message(idChat, created, sender2, content));
                    }
                    if (messageList.size() > 0&&ChatWUser.adapter!=null) {
                        ChatWUser.adapter.setMessages(messageList);
                        ChatWUser.adapter.notifyDataSetChanged();
                    }
                }
            });
            intent.putExtra("id", chatsVM.getAll().getValue().get(i).getUser().getUsername());
            intent.putExtra("token",token);
            intent.putExtra("chatId",chatsVM.getAll().getValue().get(i).getId());
            intent.putExtra("username",currentUsername);
            intent.putExtra("displayName",displayName);
            intent.putExtra("profilePic",profilePic);
            intent.putExtra("url",url);
            startActivity(intent);
        });
        if(getIntent().getExtras()!=null)
        {
            String username=getIntent().getExtras().getString("username");
            currentUsername=getIntent().getExtras().getString("username");
            UsersAPI usersAPI=new UsersAPI(url);
            User user=usersAPI.get(username, token);
            TextView name=findViewById(R.id.tvName);
            name.setText(user.getDisplayName());
            displayName=user.getDisplayName();
            de.hdodenhof.circleimageview.CircleImageView image=findViewById(R.id.imageView4);
            image.setImageBitmap(photoToBitmap(user.getProfilePic()));
            profilePic=user.getProfilePic();
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        chatsVM.getAll().getValue().clear();
        List<ChatRow>listChatRow=chatsVM.getRepository().getContactsDao().index();
        List<UserChat>userChatList=new ArrayList<>();
        for(int i=0;i<listChatRow.size();i++){
            User user=new User(listChatRow.get(i).getUsername(),listChatRow.get(i).getName(),listChatRow.get(i).getPic());
            Message message=new Message(0,listChatRow.get(i).getTime(),user,listChatRow.get(i).getLastMessage());
            int id=listChatRow.get(i).getIdChat();
            UserChat newUser=new UserChat(id,user,message);
            userChatList.add(newUser);
        }
        chatsVM.getAll().getValue().addAll(userChatList);
        if(userChatList.size()>0){
            chatsVM.getRepository().getUserListData().setValue(userChatList);
            adapter.notifyDataSetChanged();
        }
    }
}