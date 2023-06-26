package com.example.nschat.api;
import androidx.lifecycle.MutableLiveData;
import com.example.nschat.Chat;
import com.example.nschat.ConvertToJSON;
import com.example.nschat.ConvertToJsonChat;
import com.example.nschat.Message;
import com.example.nschat.NewChat;
import com.example.nschat.UserChat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.List;

public class ChatsAPI {

        private Retrofit retrofit;
        private WebServiceAPI webServiceAPI;
        private Message newMsg;
        private Chat chat;
        private NewChat newChat;

        public ChatsAPI(String url) {


            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            webServiceAPI = retrofit.create(WebServiceAPI.class);
        }
        public void getMessage(MutableLiveData <List<Message>> msg, int id, String token) {
            Call<List<Message>> call = webServiceAPI.getMessages(id,token);
            call.enqueue(new Callback<List<Message>>() {
                @Override
                public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                    msg.setValue(response.body());
                }
                @Override
                public void onFailure(Call<List<Message>> call, Throwable t) {
                }
            });
        }

        public Message post(int id, String token, String msg) {
            ConvertToJSON s=new ConvertToJSON(msg);
            Call<Message> call = webServiceAPI.createPost(id,token,s);
            Thread t=new Thread((() -> {
                try{
                    newMsg=call.execute().body();
                }
                catch (Exception e){
                }
            }));
            t.start();
            try {
                t.join();
            }
            catch (Exception e){
            }
            return newMsg;
        }

    public void getChats(MutableLiveData <List<UserChat>> chats, String token) {
        Call<List<UserChat>> call = webServiceAPI.getChats(token);
        call.enqueue(new Callback<List<UserChat>>() {
            @Override
            public void onResponse(Call<List<UserChat>> call, Response<List<UserChat>> response) {
                chats.setValue(response.body());
            }
            @Override
            public void onFailure(Call<List<UserChat>> call, Throwable t) {
            }
        });
    }

    public Chat getChatById(int id, String token) {
        Call<Chat> call = webServiceAPI.getChatsById(id,token);
        Thread t=new Thread((() -> {
            try{
                chat=call.execute().body();
            }
            catch (Exception e){
            }
        }));
        t.start();
        try {
            t.join();
        }
        catch (Exception e){
        }
        return chat;
    }
    public NewChat createChat (String username, String token) {
        ConvertToJsonChat user=new ConvertToJsonChat(username);
        Call<NewChat> call = webServiceAPI.createChat(token,user);

        Thread t=new Thread((() -> {
            try{
                newChat=call.execute().body();
            }
            catch (Exception e){
            }
        }));
        t.start();
        try {
            t.join();
        }
        catch (Exception e){
        }
        return newChat;
    }
}
