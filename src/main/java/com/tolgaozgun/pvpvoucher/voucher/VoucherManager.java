package com.tolgaozgun.pvpvoucher.voucher;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.tolgaozgun.pvpvoucher.PluginMain;
import com.tolgaozgun.pvpvoucher.player.PPlayer;
import com.tolgaozgun.pvpvoucher.util.Constants;
import com.tolgaozgun.pvpvoucher.util.Messages;
import com.tolgaozgun.pvpvoucher.util.VoucherScoreboard;

import de.tr7zw.changeme.nbtapi.NBTItem;

public class VoucherManager {

    private PluginMain plugin;
    private FileConfiguration config;
    private File file;

    // Player UUID --> Voucher UUID
    private HashMap<UUID, Voucher> voucherList;

    public VoucherManager() {
	plugin = PluginMain.getInstance();
	config = plugin.getVouchersConfig();
	file = plugin.getVouchersFile();
	voucherList = new HashMap<>();
    }

    public void load(PPlayer player) {
	Player bukkitPlayer = player.getBukkitPlayer();
	UUID playerId = bukkitPlayer.getUniqueId();
	String path = "vouchers." + bukkitPlayer.getUniqueId();
	if (config.contains(path)) {
	    Voucher voucher = (Voucher) config.get(path);
	    voucherList.put(playerId, voucher);
	    player.setVoucher(voucher);
	    if (voucher.getTimeLeft() > 0) {
		plugin.getVoucherManager().setActive(voucher);
	    }
	}
    }

    public ItemStack createVoucher() {
	return createVoucher(plugin.getConfigManager().defaultDuration);
    }

    public ItemStack createVoucher(long duration) {
	ItemStack item = new ItemStack(plugin.getConfigManager().voucherMaterial);
	NBTItem nbti = new NBTItem(item);
	nbti.setBoolean(Constants.NK_VOUCHER, true);
	nbti.setLong(Constants.NK_DURATION, duration);
	item = nbti.getItem();
	ItemMeta meta = item.getItemMeta();
	meta.setDisplayName(Messages.VOUCHER_TITLE);
	List<String> lore = Messages.VOUCHER_LORE;
	for(int i = 0; i < lore.size(); i++) {
	    String line = lore.get(i);
	    line = line.replace("%duration%", duration + "");
	    lore.set(i, line);
	}
	meta.setLore(lore);
	item.setItemMeta(meta);
	return item;
    }

    public void quit(PPlayer player) {
	player.onQuit();
    }

    public void startVoucher(Player player, Voucher voucher) {
	startVoucher(player.getUniqueId(), voucher);
    }

    public void startVoucher(UUID uuid, Voucher voucher) {
	voucherList.put(uuid, voucher);
    }

    public void saveVoucher(Player player, Voucher voucher) {
	saveVoucher(player.getUniqueId(), voucher);
    }

    public void saveVoucher(UUID uuid, Voucher voucher) {
	voucher.setActive(false);
	config.set("vouchers." + uuid, voucher);
	voucherList.remove(uuid);
	saveConfig();
    }

    public void setActive(Voucher voucher) {
	if (plugin.getServer().getPlayer(getPlayer(voucher)) == null) {
	    return;
	}
	Player player = plugin.getServer().getPlayer(getPlayer(voucher));
	VoucherScoreboard scoreboard = new VoucherScoreboard(player, voucher);
	voucher.setVoucherScoreboard(scoreboard);
	voucher.setActive(true);
    }

    public void endVoucher(Player player, Voucher voucher) {
	voucherList.remove(player.getUniqueId());
	if (config.contains("vouchers." + player.getUniqueId())) {
	    config.set("vouchers." + player.getUniqueId(), null);
	    saveConfig();
	}
    }

    public Voucher getVoucher(Player player) {
	return voucherList.get(player.getUniqueId());
    }

    public UUID getPlayer(Voucher voucher) {
	for (int i = 0; i < voucherList.size(); i++) {
	    if (((Voucher) voucherList.values().toArray()[i]).equals(voucher)) {
		return (UUID) voucherList.keySet().toArray()[i];
	    }
	}
	return null;
    }

    public void removeVoucher(Player player) {
	Voucher voucher = voucherList.get(player.getUniqueId());
	if(voucher.getVoucherScoreboard() != null) {
	    voucher.getVoucherScoreboard().stop();
	}
	endVoucher(player, voucher);
    }

    private void saveConfig() {
	try {
	    config.save(file);
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
    }

}
