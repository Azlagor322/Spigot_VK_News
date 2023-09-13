package com.azlagor.minelandnews;

import com.azlagor.minelandnews.managers.CacheManager;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class ScheduledTask {
    private static BukkitTask runnable;
    private static String lastMsg;
    public static void start()
    {
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                List<String> list = CacheManager.getNRecordsFromCache(5);
                if(list.size() == 0) return;
                if (lastMsg == null) {
                    lastMsg = list.get(0);
                } else {
                    int currentIndex = list.indexOf(lastMsg);
                    if (currentIndex != -1) {
                        int nextIndex = (currentIndex + 1) % list.size();
                        lastMsg = list.get(nextIndex);
                    } else {
                        lastMsg = list.get(0); // Если последняя - берем первую запись
                    }
                }
                int maxLength = Minelandnews.config.send_lines;
                String[] lines = lastMsg.split("\n");
                int id = CacheManager.getKeyByValue(lastMsg);
                List<String> textList = new ArrayList<>();
                for(int i = 0; i < lines.length; i++)
                {
                    if(i > maxLength) break;
                    if(i == 0)
                        textList.add("§6" + lines[i]);
                    else
                        textList.add("§f" + lines[i]);
                }
                String text = String.join("\n", textList);
                if(id != 0)
                {
                    TextComponent main = new TextComponent(text+"\n");
                    TextComponent message = new TextComponent("§7[Читать далее...]");
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://vk.com/wall-" +Minelandnews.config.group_id+"_"+id));
                    TextComponent hoverText = new TextComponent("Открыть в барузере");
                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] {hoverText}));
                    main.addExtra(message);
                    Bukkit.getServer().spigot().broadcast(main);
                }
                else
                {
                    Bukkit.getServer().broadcastMessage(text);
                }
            }
        }.runTaskTimer(Minelandnews.plugin, 0, Minelandnews.config.msg_delay * 60 * 20);
    }
    public static void stop()
    {
        runnable.cancel();
    }
}
