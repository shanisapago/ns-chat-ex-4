package com.example.nschat;
import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
public class EntityBListConverter {
    @TypeConverter
    public String fromEntityBList(List<MessageItem> entityBList) {
        Gson gson = new Gson();
        return gson.toJson(entityBList);
    }
    @TypeConverter
    public List<MessageItem> toEntityBList(String entityBListString) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<MessageItem>>() {}.getType();
        return gson.fromJson(entityBListString, type);
    }
}
