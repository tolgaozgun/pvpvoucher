package com.tolgaozgun.pvpvoucher.player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.tolgaozgun.pvpvoucher.PluginMain;

public class PlayerManager {

    private PluginMain plugin;
    private FileConfiguration config;
    private File file;

    private HashMap<UUID, PPlayer> playerList;

    public PlayerManager() {
	plugin = PluginMain.getInstance();
	config = plugin.getPlayersConfig();
	file = plugin.getPlayersFile();
	playerList = new HashMap<>();
    }

    public void onQuit(Player player) {
	update(getPlayer(player));
	playerList.remove(player.getUniqueId());
    }

    public PPlayer onJoin(Player player) {
	// Check if player is online.
	if (player == null || !player.isOnline()) {
	    return null;
	}
	PPlayer pPlayer;

	if (!config.contains("players." + player.getUniqueId().toString())) {
	    pPlayer = new PPlayer(player);
	    update(pPlayer);
	} else {
	    pPlayer = (PPlayer) config.get("players." + player.getUniqueId());
	}
	playerList.put(player.getUniqueId(), pPlayer);
	return pPlayer;

    }

    public void update(PPlayer player) {
	String uuidStr = player.getBukkitPlayer().getUniqueId().toString();
	config.set("players." + uuidStr, player);
	saveConfig();
    }

    public void onJoin(UUID uuid) {
	onJoin(plugin.getServer().getPlayer(uuid));
    }

    public boolean doesContain(PPlayer player) {
	return doesContain(player.getBukkitPlayer());
    }

    public boolean doesContain(UUID uuid) {
	return playerList.containsKey(uuid);
    }

    public boolean doesContain(Player player) {
	return doesContain(player.getUniqueId());
    }

    public PPlayer getPlayer(UUID uuid) {
	return playerList.get(uuid);
    }

    public PPlayer getPlayer(Player player) {
	return getPlayer(player.getUniqueId());
    }

    private void saveConfig() {
	try {
	    config.save(file);
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
    }

}
