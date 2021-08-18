package com.tolgaozgun.pvpvoucher.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.tolgaozgun.pvpvoucher.PluginMain;

public class WellManager {

    private PluginMain plugin;
    private File file;
    private FileConfiguration config;
    private List<Location> wells;
    private List<UUID> chatPlayers;

    public WellManager() {
        plugin = PluginMain.getInstance();
        file = plugin.getWellFile();
        config = plugin.getWellConfig();
        wells = new ArrayList<>();
        loadAll();
    }


    public void removeChatPlayer(Player player) {
        chatPlayers.remove(player.getUniqueId());
    }

    public boolean isWell(Location location) {
        return wells.contains(location);
    }

    public void addWell(Location location) {
        wells.add(location);
        saveConfig();
    }

    public void removeWell(Location location) {
        if (wells.contains(location)) {
            wells.remove(location);
        }
        saveConfig();
    }

    private void loadAll() {
        List<String> list = config.getStringList("wells");
        wells = Util.deserializeLocList(list);
    }

    private void saveConfig() {
        config.set("wells", Util.serializeLocList(wells));
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
