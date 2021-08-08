package com.tolgaozgun.pvpvoucher.util;

import java.io.File;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import com.tolgaozgun.pvpvoucher.PluginMain;

public class ConfigManager {

    public int blackListDuration;

    // Voucher stuff
    public int defaultDuration;
    public Material voucherMaterial;

    // Well stuff
    public Material wellMaterial;


    // Souls stuff
    public Material soulMaterial;
    public int soulModelData;

    private PluginMain plugin;
    private File file;
    private FileConfiguration config;

    public ConfigManager() {
        plugin = PluginMain.getInstance();
        file = plugin.getConfigFile();
        config = plugin.getConfig();
        loadAll();
    }

    private void loadAll() {
        defaultDuration = config.getInt("defualt-duration");
        voucherMaterial = Material.valueOf(config.getString("voucher-material", "PAPER"));
        wellMaterial = Material.valueOf(config.getString("well-material", "HOPPER"));
        blackListDuration = config.getInt("blacklist-duration", 86400);
        soulMaterial = Material.valueOf(config.getString("soul-material", "TORCH"));
        if (config.get("soul-model-data") instanceof Integer) {
            soulModelData = config.getInt("soul-model-data");
        }
    }

}
