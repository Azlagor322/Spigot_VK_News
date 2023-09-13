package com.azlagor.minelandnews.parser;

import com.azlagor.minelandnews.Minelandnews;
import com.azlagor.minelandnews.db.DataBase;
import com.azlagor.minelandnews.managers.CacheManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jooq.impl.QOM;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class NewsParser {
    public static void parseNews()
    {
        String accessToken = Minelandnews.config.token;
        int groupId = Minelandnews.config.group_id;
        // Количество постов для получения
        int count = 20;
        try {
            String apiUrl = "https://api.vk.com/method/wall.get?owner_id=-" + groupId + "&count=" + count + "&access_token=" + accessToken + "&v=5.131";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                parseContent(response.toString());
            } else {
                Minelandnews.plugin.getLogger().log(Level.WARNING,"Ошибка при выполнении запроса: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void parseContent(String content)
    {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(content, JsonObject.class);
        JsonObject checkError = jsonObject.getAsJsonObject("error");
        if(checkError != null)
        {
            Minelandnews.plugin.getLogger().log(Level.WARNING,content);
            return;
        }
        JsonArray itemsArray = jsonObject.getAsJsonObject("response").getAsJsonArray("items");
        for(JsonElement post : itemsArray.asList())
        {
            JsonObject postObject  = post.getAsJsonObject();
            String postText = postObject.get("text").getAsString();
            int id = postObject.get("id").getAsInt();
            long date = postObject.get("date").getAsLong();
            initData(id,postText,date);
        }
    }
    private static void initData(int id, String text, long date)
    {
        CacheManager.clear();
        CacheManager.putCache(id,text);
        DataBase.insert(id,date);
    }
}
