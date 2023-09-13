package com.azlagor.minelandnews;

import com.azlagor.minelandnews.Parser.TaskParser;
import com.azlagor.minelandnews.managers.ConfigManager;
import com.azlagor.minelandnews.managers.Gui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Minelandnews extends JavaPlugin implements Listener {

    public static ConfigManager.Config config;
    public static Plugin plugin;
    @Override
    public void onEnable() {
        plugin = this;
        this.getCommand("news").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(this, this);
        ConfigManager.loadMainConfig();
        ScheduledTask.start();
        TaskParser.start();
    }

    @Override
    public void onDisable() {
        ScheduledTask.stop();
        TaskParser.stop();
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        new Gui().action(event);
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        new Gui().action(event);
    }
}
