package com.example.nschat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import com.example.nschat.api.AppTokenAPI;
import com.example.nschat.api.TokensAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;
public class Login extends AppCompatActivity {
    VideoView videoViewUp;
    VideoView videoViewMiddle;
    VideoView videoViewDown;
    private ContactsDB db;
    private ContactsDao contactsDao;
    private MessagesDB messDB;
    private MessagesDao messageDao;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        db= Room.databaseBuilder(getApplicationContext(),ContactsDB.class,"tryContacts8DB")
                .allowMainThreadQueries()
                .build();
        contactsDao=db.contactsDao();
        contactsDao.deleteTable();
        messDB= Room.databaseBuilder(getApplicationContext(),MessagesDB.class,"try8DB")
                .allowMainThreadQueries()
                .build();
        messageDao=messDB.messagesDao();
        messageDao.deleteTable();
        url=getIntent().getExtras().getString("url");
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(nightModeFlags==Configuration.UI_MODE_NIGHT_YES) {
            videoViewUp = (VideoView) findViewById(R.id.videoViewUp);
            videoViewMiddle = (VideoView) findViewById(R.id.videoViewMiddle);
            videoViewDown = (VideoView) findViewById(R.id.videoViewDown);
            String path = "android.resource://com.example.nschat/" + R.raw.video_bg;
            Uri uri = Uri.parse(path);
            videoViewUp.setVideoURI(uri);
            videoViewUp.start();
            videoViewMiddle.setVideoURI(uri);
            videoViewMiddle.start();
            videoViewDown.setVideoURI(uri);
            videoViewDown.start();
            videoViewUp.setOnPreparedListener(mp -> mp.setLooping(true));
            videoViewMiddle.setOnPreparedListener(mp -> mp.setLooping(true));
            videoViewDown.setOnPreparedListener(mp -> mp.setLooping(true));
        }

        TextView clickHere=findViewById(R.id.clickHereLogin);
        clickHere.setOnClickListener(v -> {
            Intent i=new Intent(this, Submit.class);
            startActivity(i);
        });
        FloatingActionButton setting=findViewById(R.id.settingBtn);
        setting.setOnClickListener(v->{
            Intent i=new Intent(this, Setting.class);
            i.putExtra("screen","login");
            startActivity(i);
        });
        Button loginBtn = findViewById(R.id.buttonLogin);
        loginBtn.setOnClickListener(v->{
            EditText username=findViewById(R.id.usernameLogin);
            EditText password = findViewById(R.id.passwordLogin);
            String usernameLogin=username.getText().toString();
            String passwordLogin=password.getText().toString();
            if(usernameLogin.length()>0&&passwordLogin.length()>0){
                TokensAPI tokensAPI=new TokensAPI(url);
                String token=tokensAPI.post(usernameLogin, passwordLogin);
                if(token!=null){
                    token="bearer "+token;
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Login.this, instanceIdResult -> {
                        String newToken=instanceIdResult.getToken();
                        Log.i("Key", newToken);
                        AppTokenAPI appTokensAPI=new AppTokenAPI(url);
                        appTokensAPI.post(usernameLogin, newToken);
                    });
                    Intent i=new Intent(this, MainActivity.class);
                    i.putExtra("token",token);
                    i.putExtra("username",usernameLogin);
                    i.putExtra("url",url);
                    startActivity(i);
                }
                else{
                    TextView error=findViewById(R.id.text_error_username_password);
                    ImageView errorIcon=findViewById(R.id.errorPasswordUsername);
                    error.setVisibility(View.VISIBLE);
                    errorIcon.setVisibility(View.VISIBLE);
                    username.setText("");
                    password.setText("");
                }
            }
        });
        SpannableString content = new SpannableString("Click here");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        clickHere.setText(content);
    }
}