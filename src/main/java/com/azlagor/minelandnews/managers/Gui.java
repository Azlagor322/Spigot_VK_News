package com.azlagor.minelandnews.managers;

import com.azlagor.minelandnews.Minelandnews;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Gui {
    public void action(Object objEvent)
    {
        //проверка является ли ивент нашим холдером
        InventoryClickEvent event = null;
        if(objEvent instanceof InventoryDragEvent)
        {
            InventoryDragEvent dragEvent = (InventoryDragEvent) objEvent;
            InventoryHolder holder = dragEvent.getInventory().getHolder();
            if (!(holder instanceof CustomInventoryHolder)) {
                return;
            }
            int lockIndex = dragEvent.getView().countSlots() - 36 - 6;
            for(int slot : dragEvent.getRawSlots())
            {
                if (slot <= lockIndex) dragEvent.setCancelled(true);
            }
            return;
        }
        if(objEvent instanceof InventoryClickEvent) event = (InventoryClickEvent) objEvent;
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof CustomInventoryHolder)) {
            return;
        }
        int lockIndex = event.getView().countSlots() - 36 - 6;
        if(event.getRawSlot() <= lockIndex)
            event.setCancelled(true);
        if(event.getCurrentItem() == null) return;

        ItemStack item = event.getCurrentItem();
        ItemMeta im = item.getItemMeta();
        String text = ChatColor.stripColor(im.getDisplayName());
        int id = CacheManager.getKeyByValue(text);
        if(id != 0)
        {
            event.getWhoClicked().closeInventory();
            TextComponent main = new TextComponent("§6[Нажми чтобы открыть новость]");
            main.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://vk.com/wall-" +Minelandnews.config.group_id+"_"+id));
            TextComponent hoverText = new TextComponent("Открыть новость");
            main.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] {hoverText}));
            event.getWhoClicked().spigot().sendMessage(main);
        }
    }
    public static class CustomInventoryHolder implements InventoryHolder {
        public CustomInventoryHolder() {

        }
        @Override
        public Inventory getInventory() {
            Inventory inv = Bukkit.createInventory(this, 54, "News");
            List<String> list = CacheManager.getNRecordsFromCache(20);
            for(String str : list)
            {
                ItemStack itemStack = new ItemStack(Material.PAPER);
                ItemMeta im = itemStack.getItemMeta();
                String[] lines = str.split("\n");
                List<String> lore = new ArrayList<>();
                for(int i = 0; i < lines.length; i++)
                {
                    boolean setDispaly = false;
                    if(i > Minelandnews.config.send_lines) break;
                    List<String> splitLine = splitString(lines[i]);
                    for(String line : splitLine)
                    {
                        if(i == 0 && !setDispaly)
                        {
                            im.setDisplayName("§r§6"+line);
                            setDispaly = true;
                        }
                        else
                            lore.add("§r§f"+line);
                    }
                }
                im.setLore(lore);
                itemStack.setItemMeta(im);
                inv.addItem(itemStack);
            }
            return inv;
        }

    }


    //разбиваю длинные строки <30 символов, чтобы в Lore помещалось на экране
    public static List<String> splitString(String input) {
        List<String> result = new ArrayList<>();
        int maxLength = 30;

        while (input.length() > maxLength) {
            int spaceIndex = input.lastIndexOf(' ', maxLength);
            if (spaceIndex == -1) {
                // Если нет пробелов, разбиваем строку по 30 символов
                result.add(input.substring(0, maxLength));
                input = input.substring(maxLength);
            } else {
                // Разбиваем строку по последнему пробелу перед 30 символами
                result.add(input.substring(0, spaceIndex));
                input = input.substring(spaceIndex + 1);
            }
        }

        if (!input.isEmpty()) {
            result.add(input);
        }

        return result;
    }
}
