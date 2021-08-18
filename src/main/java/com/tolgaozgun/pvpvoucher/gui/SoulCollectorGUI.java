package com.tolgaozgun.pvpvoucher.gui;

import com.tolgaozgun.pvpvoucher.PluginMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class SoulCollectorGUI implements StackableGUI {
    private PluginMain plugin = PluginMain.getInstance();
    private Player player;
    private Inventory inventory;
    private int pageNo;
    private boolean lastPage;

    public SoulCollectorGUI(Player player) {
        this.player = player;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public SoulCollectorGUI(Player player, int pageNo) {
        this.player = player;
        this.pageNo = pageNo;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void openInventory() {
        openInventory(player);
    }


    @Override
    public void openInventory(Player target) {
        List<ItemStack> souls = plugin.getBountyManager().getSoulsFromInventory(target.getInventory());

        while (pageNo != 0 && pageNo * 28 >= souls.size()) {
            pageNo--;
        }
        inventory = Bukkit.createInventory(target, 54, "Soul Collector | Page: " + (pageNo + 1));
        GUIComponents.placeBlackBorder(inventory);

        if (souls != null) {
            int j = 0;
            for (int i = pageNo * 28; i < (pageNo + 1) * 28 && i < souls.size() && j < 54; i++) {
                if (j % 9 == 0) {
                    j++;
                } else if (j % 9 == 8) {
                    j += 2;
                }
                inventory.setItem(j, souls.get(i));
                j++;
            }
            lastPage = true;
            if (pageNo * 28 < souls.size()) {
                GUIComponents.placeNextPage(inventory);
                lastPage = false;
            }

            if (pageNo > 0) {
                GUIComponents.placePrevPage(inventory);
            }
        }

        GUIComponents.placeInventoryFooter(inventory, target);
        target.openInventory(inventory);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        try {
            if (!event.getInventory().equals(inventory)) {
                return;
            }
            Player player = (Player) event.getWhoClicked();

            int index = event.getSlot();
            event.setCancelled(true);
            switch (index) {
                case 34:
                    return;
                case 45:
                    if (pageNo > 0) {
                        pageNo--;
                        player.openInventory(inventory);
                    }
                    return;
                case 48:
                    if (plugin.getGUIManager().hasPreviousGUI(player)) {
                        StackableGUI stackableGUI = plugin.getGUIManager().getPreviousGUI(player);
                        stackableGUI.openInventory(player);
                    }
                    return;
                case 49:
                    player.closeInventory();
                    return;
                case 53:
                    if (!lastPage) {
                        pageNo++;
                        player.openInventory(inventory);
                    }
                    return;
                default:
                    return;
            }

        } catch (NullPointerException ex) {
            // Player clicked out of bounds.
        }
    }
}
