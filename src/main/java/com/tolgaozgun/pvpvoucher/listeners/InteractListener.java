package com.tolgaozgun.pvpvoucher.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.tolgaozgun.pvpvoucher.PluginMain;
import com.tolgaozgun.pvpvoucher.events.VoucherUseEvent;
import com.tolgaozgun.pvpvoucher.events.WellUseEvent;
import com.tolgaozgun.pvpvoucher.util.Constants;
import com.tolgaozgun.pvpvoucher.util.Util;

public class InteractListener implements Listener {

    private PluginMain plugin = PluginMain.getInstance();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getClickedBlock() != null && plugin.getWellManager().isWell(event.getClickedBlock().getLocation())) {
            event.setCancelled(true);
            plugin.getServer().getPluginManager()
                    .callEvent(new WellUseEvent(player, event.getClickedBlock().getLocation()));
        }

        if (event.getItem() == null) {
            return;
        }
        ItemStack item = event.getItem();

        if (item.getType() != Material.AIR && Util.checkNBT(item, Constants.NK_VOUCHER)) {
            event.setCancelled(true);
            plugin.getServer().getPluginManager().callEvent(new VoucherUseEvent(player, item));
        }

    }

}
