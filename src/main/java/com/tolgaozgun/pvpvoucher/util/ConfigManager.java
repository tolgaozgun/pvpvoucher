package com.tolgaozgun.pvpvoucher.util;

import java.io.File;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import com.tolgaozgun.pvpvoucher.PluginMain;

public class ConfigManager {

    // Voucher stuff
    public int defaultDuration;
    public Material voucherMaterial;

    // Well stuff
    public Material wellMaterial;
    public int wellChatTimer;

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
	wellChatTimer = config.getInt("well-chat-duration", 10);
    }

}
