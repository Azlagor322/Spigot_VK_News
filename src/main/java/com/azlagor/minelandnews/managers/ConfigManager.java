package com.azlagor.minelandnews.managers;

import com.azlagor.minelandnews.Minelandnews;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ConfigManager {
    public static class Config
    {
        public String token = "null";
        public int group_id;
        public int send_lines = 6;
        public int msg_delay = 2;
        public int update_news = 30;
    }

    private static final String configFile = "plugins/news.json";
    public static void loadMainConfig()
    {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        File file = new File(configFile);
        if (!file.exists())
        {
            try {
                FileWriter writer = new FileWriter(configFile, StandardCharsets.UTF_8);
                Config dataCfg = new Config();
                gson.toJson(dataCfg, writer);
                Minelandnews.config = dataCfg;
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile),
                        StandardCharsets.UTF_8));
                Minelandnews.config = gson.fromJson(reader, Config.class);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
