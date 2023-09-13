package com.azlagor.minelandnews.parser;

import com.azlagor.minelandnews.Minelandnews;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;


public class TaskParser {
    private static BukkitTask runnable;
    public static void start()
    {
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                //скачиваем посты с вк
                NewsParser.parseNews();
            }
        }.runTaskTimer(Minelandnews.plugin, 0, Minelandnews.config.update_news * 60 * 20);
    }
    public static void stop()
    {
        runnable.cancel();
    }
}
