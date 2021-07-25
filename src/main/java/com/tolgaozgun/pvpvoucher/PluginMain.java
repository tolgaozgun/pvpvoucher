package com.tolgaozgun.pvpvoucher;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.tolgaozgun.pvpvoucher.commands.VoucherCommand;
import com.tolgaozgun.pvpvoucher.listeners.DamageListener;
import com.tolgaozgun.pvpvoucher.listeners.InteractListener;
import com.tolgaozgun.pvpvoucher.listeners.PlayerChatListener;
import com.tolgaozgun.pvpvoucher.listeners.PlayerConnectionListener;
import com.tolgaozgun.pvpvoucher.listeners.VoucherListener;
import com.tolgaozgun.pvpvoucher.listeners.WellListener;
import com.tolgaozgun.pvpvoucher.player.PPlayer;
import com.tolgaozgun.pvpvoucher.player.PlayerManager;
import com.tolgaozgun.pvpvoucher.util.ConfigManager;
import com.tolgaozgun.pvpvoucher.util.Messages;
import com.tolgaozgun.pvpvoucher.util.SignMenuFactory;
import com.tolgaozgun.pvpvoucher.util.WellManager;
import com.tolgaozgun.pvpvoucher.voucher.Voucher;
import com.tolgaozgun.pvpvoucher.voucher.VoucherManager;

import net.milkbowl.vault.economy.Economy;

public class PluginMain extends JavaPlugin {

    private FileConfiguration config;
    private FileConfiguration localeConfig;
    private FileConfiguration playersConfig;
    private FileConfiguration voucherConfig;
    private FileConfiguration wellConfig;

    private File configFile;
    private File localeFile;
    private File voucherFile;
    private File playersFile;
    private File wellFile;

    private ConfigManager configManager;
    private VoucherManager voucherManager;
    private PlayerManager playerManager;
    private WellManager wellManager;
    
    private SignMenuFactory signMenuFactory;

    private static Economy econ = null;

    private static final Logger log = Logger.getLogger("Minecraft");
    private static PluginMain instance;

    public void onEnable() {
	instance = this;
	registerSerializable();
	loadFiles();
	Messages.load();
	loadManagers();
	loadListeners();
	loadCommands();
	onReload();

	if (!setupEconomy()) {
	    log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
	    getServer().getPluginManager().disablePlugin(this);
	    return;
	}
    }

    private void onReload() {
	for (Player bukkitPlayer : Bukkit.getOnlinePlayers()) {
	    PPlayer player = getPlayerManager().onJoin(bukkitPlayer);
	    getVoucherManager().load(player);
	}
    }

    public void onDisable() {
	for (Player bukkitPlayer : Bukkit.getOnlinePlayers()) {
	    getVoucherManager().quit(getPlayerManager().getPlayer(bukkitPlayer));
	    getPlayerManager().onQuit(bukkitPlayer);
	}
    }

    private void registerSerializable() {
	ConfigurationSerialization.registerClass(PPlayer.class);
	ConfigurationSerialization.registerClass(Voucher.class);
    }

    private void loadCommands() {
	getServer().getPluginCommand("voucher").setExecutor(new VoucherCommand());
    }

    private void loadListeners() {
	getServer().getPluginManager().registerEvents(new InteractListener(), this);
	getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
	getServer().getPluginManager().registerEvents(new PlayerConnectionListener(), this);
	getServer().getPluginManager().registerEvents(new DamageListener(), this);
	getServer().getPluginManager().registerEvents(new VoucherListener(), this);
	getServer().getPluginManager().registerEvents(new WellListener(), this);
    }

    private void loadManagers() {
	configManager = new ConfigManager();
	playerManager = new PlayerManager();
	voucherManager = new VoucherManager();
	wellManager = new WellManager();
	signMenuFactory = new SignMenuFactory(this);
    }

    private boolean setupEconomy() {
	if (getServer().getPluginManager().getPlugin("Vault") == null) {
	    return false;
	}
	RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
	if (rsp == null) {
	    return false;
	}
	econ = rsp.getProvider();
	return econ != null;
    }
    
    public SignMenuFactory getSignMenuFactory() {
	return signMenuFactory;
    }

    public ConfigManager getConfigManager() {
	return configManager;
    }

    public VoucherManager getVoucherManager() {
	return voucherManager;
    }

    public PlayerManager getPlayerManager() {
	return playerManager;
    }

    public WellManager getWellManager() {
	return wellManager;
    }

    public static PluginMain getInstance() {
	return instance;
    }

    public FileConfiguration getConfig() {
	return config;
    }

    public FileConfiguration getLocaleConfig() {
	return localeConfig;
    }

    public FileConfiguration getPlayersConfig() {
	return playersConfig;
    }

    public FileConfiguration getVouchersConfig() {
	return voucherConfig;
    }

    public FileConfiguration getWellConfig() {
	return wellConfig;
    }

    public File getWellFile() {
	return wellFile;
    }

    public File getVouchersFile() {
	return voucherFile;
    }

    public File getConfigFile() {
	return configFile;
    }

    public File getLocaleFile() {
	return localeFile;
    }

    public File getPlayersFile() {
	return playersFile;
    }

    public static Economy getEconomy() {
	return econ;
    }

    private void loadFiles() {
	localeFile = new File(getDataFolder(), "locale.yml");
	playersFile = new File(getDataFolder(), "players.yml");
	configFile = new File(getDataFolder(), "config.yml");
	voucherFile = new File(getDataFolder(), "vouchers.yml");
	wellFile = new File(getDataFolder(), "wells.yml");

	if (!playersFile.exists()) {
	    playersFile.getParentFile().mkdirs();
	    saveResource("players.yml", true);
	}

	if (!localeFile.exists()) {
	    localeFile.getParentFile().mkdirs();
	    saveResource("locale.yml", true);
	}

	if (!configFile.exists()) {
	    configFile.getParentFile().mkdirs();
	    saveResource("config.yml", true);
	}

	if (!voucherFile.exists()) {
	    voucherFile.getParentFile().mkdirs();
	    saveResource("vouchers.yml", true);
	}

	if (!wellFile.exists()) {
	    wellFile.getParentFile().mkdirs();
	    saveResource("wells.yml", true);
	}

	config = new YamlConfiguration();
	playersConfig = new YamlConfiguration();
	localeConfig = new YamlConfiguration();
	voucherConfig = new YamlConfiguration();
	wellConfig = new YamlConfiguration();

	try {
	    playersConfig.load(playersFile);
	    localeConfig.load(localeFile);
	    config.load(configFile);
	    voucherConfig.load(voucherFile);
	    wellConfig.load(wellFile);
	} catch (IOException | InvalidConfigurationException e) {
	    e.printStackTrace();
	}
    }
}
