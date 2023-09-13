package com.azlagor.minelandnews;

import com.azlagor.minelandnews.Parser.NewsParser;
import com.azlagor.minelandnews.db.DataBase;
import com.azlagor.minelandnews.managers.ConfigManager;
import com.azlagor.minelandnews.managers.Gui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player player = (Player) commandSender;
        if(args.length == 0)
        {
            Gui.CustomInventoryHolder ci = new Gui.CustomInventoryHolder();
            Inventory inventory = ci.getInventory();
            player.openInventory(inventory);
            return true;
        }
        if(args.length == 1)
        {
            if(args[0].equals("parse"))
            {
                //вручную скачать посты с вк
                NewsParser.parseNews();
                return true;
            }
            if(args[0].equals("reload"))
            {
                player.sendMessage("Перезагрузка конфига");
                ConfigManager.loadMainConfig();
                return true;
            }
        }
        return false;
    }
}
