package com.example.nschat.api;
import com.example.nschat.AppToken;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class AppTokenAPI {
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    public AppTokenAPI(String url) {
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }
    public void post(String username, String token) {
        AppToken appToken=new AppToken(username, token);

        Call<Void> call = webServiceAPI.createPost(appToken);
        Thread t=new Thread((() -> {
            try{
                call.execute();
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
    }
    public void delete(String id) {
        Call<Void> call = webServiceAPI.deleteToken(id);
        Thread t=new Thread((() -> {
            try{
                call.execute();
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
    }
}
