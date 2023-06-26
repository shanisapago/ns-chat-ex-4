package com.example.nschat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;
public class NSChatService extends FirebaseMessagingService {
    public NSChatService() {
    }
    private void sendMessgaeToActivity(String message,String sender,String time){
        Intent intent=new Intent("com.example.MESSAGE_RECEIVED");
        intent.putExtra("message",message);
        intent.putExtra("sender",sender);
        intent.putExtra("time",time);
        sendBroadcast(intent);
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        JSONObject obj;
        String msg;
       try {
            obj = new JSONObject(message.getNotification().getBody());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            msg=obj.getString("message");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        if (message.getNotification() != null) {
            createNotificationChanel();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(message.getNotification().getTitle())
                    .setContentText(msg)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            notificationManagerCompat.notify(1, builder.build());
        }
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int min = Calendar.getInstance().get(Calendar.MINUTE);
        String minutes;
        if (min < 10)
            minutes = "0" + min;
        else
            minutes = String.valueOf(min);
        String username;
        try {
            username=obj.getString("username");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String hourAndMinutes = hour + 12 + ":" + minutes;
        sendMessgaeToActivity(msg,username,hourAndMinutes);
    }
    private void createNotificationChanel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            int importance= NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel= null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channel = new NotificationChannel("1","My channel",importance);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channel.setDescription("Demo channel");
            }
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}