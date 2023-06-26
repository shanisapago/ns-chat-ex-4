package com.example.nschat;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import com.example.nschat.api.UsersAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
public class Submit extends AppCompatActivity {
    private final int GALLERY_REQ_CODE = 1000;
    ImageView imgGallery;
    String photo64;
    VideoView videoViewUp;
    VideoView videoViewMiddle;
    VideoView videoViewDown;
    private ContactsDB db;
    private ContactsDao contactsDao;
    private MessagesDB messDB;
    private MessagesDao messageDao;
    private String url="http://10.0.2.2:3000/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit);
        if(getIntent().getExtras()!=null){
            url=getIntent().getExtras().getString("url");
        }
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
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(nightModeFlags==Configuration.UI_MODE_NIGHT_YES){
            videoViewUp = (VideoView) findViewById(R.id.videoViewUp2);
            videoViewMiddle = (VideoView) findViewById(R.id.videoViewMiddle2);
            videoViewDown = (VideoView) findViewById(R.id.videoViewDown2);
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
        Button submitButton=findViewById(R.id.registerBtn);
        submitButton.setOnClickListener(v ->  {
            boolean photo;
            EditText password=findViewById(R.id.passwordSubmit);
            EditText confirmPassword=findViewById(R.id.confirmSubmit);
            EditText displayName=findViewById(R.id.displayNameSubmit);
            ImageView img=findViewById(R.id.imgGallery);
            if(img.getDrawable()==null){
                findViewById(R.id.errorPhoto).setVisibility(View.VISIBLE);
                findViewById(R.id.checkPhoto).setVisibility(View.GONE);
                findViewById(R.id.text_error_photo).setVisibility(View.VISIBLE);
                photo=false;
            }
            else {
                findViewById(R.id.errorPhoto).setVisibility(View.GONE);
                findViewById(R.id.checkPhoto).setVisibility(View.VISIBLE);
                findViewById(R.id.text_error_photo).setVisibility(View.INVISIBLE);
                photo=true;
            }
            DisplayName display=new DisplayName(displayName, displayName.getText().toString(), findViewById(R.id.errorDisplayName), findViewById(R.id.checkDisplayName), findViewById(R.id.text_error_display_name));
            Password pass=new Password(password.getText().toString(), findViewById(R.id.errorPassword), findViewById(R.id.checkPassword), findViewById(R.id.text_error_password));
            ConfirmPassword confirm=new ConfirmPassword(password.getText().toString(), confirmPassword.getText().toString(), findViewById(R.id.errorConfirm), findViewById(R.id.checkConfirm), findViewById(R.id.text_error_confirm));
            EditText username=findViewById(R.id.usernameSubmit);
            String usernameString=username.getText().toString();
            boolean passwordBoolean=pass.isValid();
            boolean displayNameBoolean=display.isValid();
            boolean confirmBoolean=confirm.isValid();
            if((passwordBoolean&&displayNameBoolean&&photo&&confirmBoolean)&&(usernameString.length()>0)){
                UsersAPI usersAPI=new UsersAPI(url);
                Boolean success=usersAPI.post(usernameString, password.getText().toString(), displayName.getText().toString(), photo64);
                if(success){
                    Intent i=new Intent(this, Login.class);
                    i.putExtra("url",url);
                    startActivity(i);
                }
                else{
                    username.setText("");
                }
            }
        });
        FloatingActionButton setting=findViewById(R.id.settingBtn);
        setting.setOnClickListener(v->{
            Intent i=new Intent(this, Setting.class);
            i.putExtra("screen","submit");
            startActivity(i);
        });
        TextView clickHere=findViewById(R.id.clickHereBtn);
        clickHere.setOnClickListener(v -> {
            Intent i=new Intent(this, Login.class);
            i.putExtra("url",url);
            startActivity(i);
        });
        SpannableString content = new SpannableString("Click here");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        clickHere.setText(content);
        imgGallery = findViewById(R.id.imgGallery);
        ImageButton btnGallery = findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(v -> {
            Intent iGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(iGallery, GALLERY_REQ_CODE);
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
    static byte[] getByteFromURL(Context context, Uri uri){
        InputStream inStream = null;
        try{
            inStream = context.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte [] buf = new byte[bufferSize];
            int leng = 0;
            while ((leng = inStream.read(buf))!=-1){
                byteBuff.write(buf,0,leng);
            }
            return byteBuff.toByteArray();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((resultCode == RESULT_OK)&&(data!=null)) {
            if (requestCode == GALLERY_REQ_CODE) {
                imgGallery.setImageURI(data.getData());
                Uri uri=data.getData();
                byte[] imageBytes = getByteFromURL(getApplicationContext(),uri);
                String base64Encoded = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                photo64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            }
        }
    }
}