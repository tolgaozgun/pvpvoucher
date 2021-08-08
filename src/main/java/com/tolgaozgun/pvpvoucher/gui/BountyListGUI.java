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

public class BountyListGUI implements StackableGUI {

    private PluginMain plugin = PluginMain.getInstance();
    private Player player;
    private Inventory inventory;
    private int pageNo;


    public BountyListGUI(Player player) {
        this.player = player;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public BountyListGUI(Player player, int pageNo) {
        this.player = player;
        this.pageNo = pageNo;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void openInventory() {
        openInventory(player);
    }


    @Override
    public void openInventory(Player target) {
        inventory = Bukkit.createInventory(target, 54, "Bounty List");

        int[] blackBarIndexes = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 50, 51, 52, 53};
        ItemStack blackPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta bPaneMeta = blackPane.getItemMeta();
        bPaneMeta.setDisplayName(ChatColor.BLACK + "");
        blackPane.setItemMeta(bPaneMeta);
        for (int index : blackBarIndexes) {
            inventory.setItem(index, blackPane);
        }

        //TODO: Get bounty list and place player heads.

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
                case 48:
                    if (plugin.getGUIManager().hasPreviousGUI(player)) {
                        StackableGUI stackableGUI = plugin.getGUIManager().getPreviousGUI(player);
                        stackableGUI.openInventory(player);
                    }
                case 49:
                    player.closeInventory();
                    return;
                default:
                    return;
            }

        } catch (NullPointerException ex) {
            // Player clicked out of bounds.
        }
    }
}
