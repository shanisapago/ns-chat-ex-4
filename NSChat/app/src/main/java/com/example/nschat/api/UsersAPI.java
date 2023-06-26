package com.example.nschat.api;
import com.example.nschat.User;
import com.example.nschat.UserPassName;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class UsersAPI {
    Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private User user;
    private Boolean successful=false;

    public UsersAPI(String url) {
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public User get(String username, String token) {
        Call<User> call = webServiceAPI.getUsers(username,token);
        Thread t=new Thread((() -> {
            try{
                user=call.execute().body();
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
        return user;
    }
    public Boolean post(String username, String password, String displayname, String profilePic) {
        UserPassName user = new UserPassName(username, password, displayname, profilePic);
        Call<Void> call = webServiceAPI.createPost(user);
        Thread t = new Thread((() -> {
            try {
                if(call.execute().code()==200)
                    successful=true;
            } catch (Exception e) {
            }
        }));
        t.start();
        try {
            t.join();
        } catch (Exception e) {
        }
        return successful;
    }
}