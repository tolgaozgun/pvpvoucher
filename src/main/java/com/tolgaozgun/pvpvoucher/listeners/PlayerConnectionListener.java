package com.tolgaozgun.pvpvoucher.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.tolgaozgun.pvpvoucher.PluginMain;
import com.tolgaozgun.pvpvoucher.player.PPlayer;

public class PlayerConnectionListener implements Listener {

    private PluginMain plugin = PluginMain.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
	Player bukkitPlayer = event.getPlayer();
	PPlayer player = plugin.getPlayerManager().onJoin(bukkitPlayer);
	plugin.getVoucherManager().load(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
	Player player = event.getPlayer();
	plugin.getVoucherManager().quit(plugin.getPlayerManager().getPlayer(player));
	plugin.getPlayerManager().onQuit(player);
    }

}
