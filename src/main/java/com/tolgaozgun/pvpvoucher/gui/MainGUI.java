package com.tolgaozgun.pvpvoucher.gui;

import com.tolgaozgun.pvpvoucher.PluginMain;
import com.tolgaozgun.pvpvoucher.util.Messages;
import com.tolgaozgun.pvpvoucher.util.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.ListIterator;

public class MainGUI implements StackableGUI {

    private PluginMain plugin = PluginMain.getInstance();
    private Player player;
    private Inventory inventory;

    public MainGUI(Player player) {
        this.player = player;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void openInventory() {
        openInventory(player);
    }

    public void openInventory(Player target) {
        inventory = Bukkit.createInventory(target, 54, "Bounty");

        ItemStack blackPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta bPaneMeta = blackPane.getItemMeta();
        bPaneMeta.setDisplayName(ChatColor.BLACK + "");
        blackPane.setItemMeta(bPaneMeta);
        for (int i = 0; i < 54; i++) {
            inventory.setItem(i, blackPane);
        }

        ItemStack item = SkullCreator.itemFromUrl("");
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Messages.MG_BOUNTIES_TITLE);
        meta.setLore(Messages.MG_BOUNTIES_LORE);
        item.setItemMeta(meta);
        inventory.setItem(21, item);

        item = SkullCreator.itemFromUrl("");
        meta = item.getItemMeta();
        meta.setDisplayName(Messages.MG_SOUL_COLLECTOR_TITLE);
        List<String> lore = Messages.MG_SOUL_COLLECTOR_LORE;
        ListIterator<String> iterator = lore.listIterator();
        while (iterator.hasNext()) {
            String line = iterator.next();

            //TODO: Change soul collector lore placeholders.
            iterator.set(line);
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        inventory.setItem(23, item);

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
                case 21:
                    BountyListGUI bountyListGUI = new BountyListGUI(player);
                    plugin.getGUIManager().addHistory(player, this);
                    bountyListGUI.openInventory();
                    return;
                case 23:
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
                default:
                    return;
            }

        } catch (NullPointerException ex) {
            // Player clicked out of bounds.
        }
    }
}
