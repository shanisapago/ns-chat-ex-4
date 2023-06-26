package com.example.nschat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
public class Setting extends AppCompatActivity {
    boolean nightMode;
    String url;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        ImageButton serverBtn=(ImageButton) findViewById(R.id.serverBtn);
        serverBtn.setOnClickListener(v->{
            EditText serverAddress=(EditText) findViewById(R.id.serverAddress);
            url=serverAddress.getText().toString();
            Intent i=new Intent(this,Submit.class);
            i.putExtra("url",url);
            startActivity(i);
        });
        Switch switchBtn=findViewById(R.id.switchBtn);
        sharedPreferences = getSharedPreferences("MODE",Context.MODE_PRIVATE);
        nightMode=sharedPreferences.getBoolean("nightMode", false);
        if(nightMode){
            switchBtn.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        switchBtn.setOnClickListener(v -> {
            if(nightMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor=sharedPreferences.edit();
                editor.putBoolean("nightMode", false);
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor=sharedPreferences.edit();
                editor.putBoolean("nightMode", true);
            }
            editor.apply();
        });
    }
}