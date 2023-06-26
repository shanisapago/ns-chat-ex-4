package com.example.nschat.api;
import com.example.nschat.AppToken;
import com.example.nschat.Chat;
import com.example.nschat.ConvertToJSON;
import com.example.nschat.ConvertToJsonChat;
import com.example.nschat.Message;
import com.example.nschat.NewChat;
import com.example.nschat.User;
import com.example.nschat.UserChat;
import com.example.nschat.UserPass;
import com.example.nschat.UserPassName;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface WebServiceAPI {
    @GET("Users/{id}")
    Call<User> getUsers(@Path("id") String id, @Header("authorization") String token);

    @POST("Users")
    Call<Void> createPost(@Body UserPassName user);

    @POST("Tokens")
    Call<String> createPost(@Body UserPass user);

    @GET("Chats/{id}/Messages")
    Call<List<Message>> getMessages(@Path("id") int id, @Header("authorization") String token);

    @POST("Chats/{id}/Messages")
    Call<Message> createPost(@Path("id") int id, @Header("authorization") String token, @Body ConvertToJSON msg);

    @GET("Chats/{id}")
    Call<Chat> getChatsById(@Path("id") int id, @Header("authorization") String token);

    @GET("Chats")
    Call<List<UserChat>> getChats(@Header("authorization") String token);

    @POST("Chats")
    Call<NewChat> createChat(@Header("authorization") String token, @Body ConvertToJsonChat username);

    @POST("AppToken")
    Call<Void> createPost(@Body AppToken appToken);
    @DELETE("AppToken/{id}")
    Call<Void> deleteToken(@Path("id") String id);

    @GET
    Call<Void> getUser(@Url String url);

}