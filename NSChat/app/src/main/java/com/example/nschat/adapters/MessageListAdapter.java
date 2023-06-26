package com.example.nschat.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.nschat.Message;
import com.example.nschat.R;
import java.util.Collections;
import java.util.List;
public class MessageListAdapter extends BaseAdapter {

    List<Message> messages;
    String currentusername;
    private class ViewHolder{
        TextView contentMess;
        TextView time;

    }

    public MessageListAdapter(List<Message> messages,String currentusername) {
        Collections.reverse(messages);
        this.messages =messages;
        this.currentusername=currentusername;
    }

    @Override
    public int getCount() { return messages.size(); }

    @Override
    public Object getItem(int position) { return messages.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        return message.getSender().getUsername().equals(currentusername) ? 0 : 1;
    }
    @Override
    public int getViewTypeCount() {
        return 2; // Two types of views: one for user 1 and another for user 2
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (convertView == null) {
            LayoutInflater inflater= LayoutInflater.from(parent.getContext());
            if(viewType == 0) {
                convertView=inflater.inflate(R.layout.green_message, parent, false);
            }
            else
            { convertView=inflater.inflate(R.layout.white_message, parent, false);}
        }

        Message p = messages.get(position);
        TextView contentMess=convertView.findViewById(R.id.textMess);
        contentMess.setText(p.getContent());
        TextView time=convertView.findViewById(R.id.time);
        time.setText(p.getCreated());
        return convertView;
    }
    public void setMessages(List<Message>mess){
        messages.clear();
        Collections.reverse(mess);
        messages.addAll(mess);
    }
}











