package com.example.nschat.adapters;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.nschat.R;
import com.example.nschat.UserChat;
import java.util.List;

public class ChatListAdapter extends BaseAdapter {

    List<UserChat> rowsUsers;

    private class ViewHolder{
        ImageView img;
       TextView name;
       TextView lastMessage;
       TextView time;

    }

    public ChatListAdapter(List<UserChat> rows) { this.rowsUsers=rows; }

    @Override
    public int getCount() { return rowsUsers.size(); }

    public Bitmap photoToBitmap(String photo){
        byte[] decodeString = Base64.decode(photo,Base64.DEFAULT);
        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        return decodeByte;
    }
    @Override
    public Object getItem(int position) { return rowsUsers.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chatrow_layout, parent, false);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.img= convertView.findViewById(R.id.imgContact);
            viewHolder.name = convertView.findViewById(R.id.nameContact);
            viewHolder.lastMessage = convertView.findViewById(R.id.lastMessageContact);
            viewHolder.time = convertView.findViewById(R.id.timeContact);



            convertView.setTag(viewHolder);
        }

        UserChat p = rowsUsers.get(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.name.setText(p.getUser().getDisplayName());
        if(p.getLastMessage()!=null){
            viewHolder.time.setText(p.getLastMessage().getCreated());
            viewHolder.lastMessage.setText(p.getLastMessage().getContent());
        }
        else {
            viewHolder.time.setText(null);
            viewHolder.lastMessage.setText(null);
        }
        viewHolder.img.setImageBitmap(photoToBitmap(p.getUser().getProfilePic()));



        return convertView;
    }
    public void setChats(List<UserChat>chats){
        rowsUsers.clear();
        rowsUsers.addAll(chats);
    }
}