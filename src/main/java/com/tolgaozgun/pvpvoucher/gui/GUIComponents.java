package com.tolgaozgun.pvpvoucher.gui;

import com.tolgaozgun.pvpvoucher.PluginMain;
import com.tolgaozgun.pvpvoucher.util.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GUIComponents {

    private static PluginMain plugin = PluginMain.getInstance();

    public static void placeInventoryFooter(Inventory inventory, Player player) {
        ItemStack item;
        ItemMeta meta;
        if (plugin.getGUIManager().hasPreviousGUI(player)) {
            item = SkullCreator.itemFromUrl("https://textures.minecraft.net/texture/cdc9e4dcfa4221a1fadc1b5b2b11d8beeb57879af1c42362142bae1edd5");
            meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + "Previous Menu");
            item.setItemMeta(meta);
            inventory.setItem(48, item);
        }

        item = new ItemStack(Material.BARRIER);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED + "✘ Close");
        item.setItemMeta(meta);
        inventory.setItem(49, item);

    }
}
