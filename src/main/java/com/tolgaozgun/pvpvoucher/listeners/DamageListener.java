package com.tolgaozgun.pvpvoucher.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.tolgaozgun.pvpvoucher.PluginMain;
import com.tolgaozgun.pvpvoucher.player.PPlayer;

public class DamageListener implements Listener {

    private PluginMain plugin = PluginMain.getInstance();

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
	if (event.getEntity() instanceof Player) {
	    Player bukkitPlayer = (Player) event.getEntity();
	    PPlayer player = plugin.getPlayerManager().getPlayer(bukkitPlayer);
	    if (player.getVoucher() != null && player.getVoucher().isActive()) {
		event.setCancelled(true);
		return;
	    }
	}
    }

}
