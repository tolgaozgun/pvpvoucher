package com.tolgaozgun.pvpvoucher;

import com.tolgaozgun.pvpvoucher.player.PPlayer;
import com.tolgaozgun.pvpvoucher.util.Messages;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.util.*;

public class BountyManager {

    public static final String NBT_BOUNTY_PLAYER = "BOUNTY_PLY";
    public static final String NBT_BOUNTY_AMOUNT = "BOUNTY_AMT";
    private List<BlacklistItem> blackList;
    private PluginMain plugin;
    private FileConfiguration config;
    private File file;

    public BountyManager() {
        plugin = PluginMain.getInstance();
        file = plugin.getBountyFile();
        config = plugin.getBountyConfig();
        blackList = new ArrayList<>();
        loadAll();
    }

    public ItemStack getSoulItem(Player bukkitPlayer) {
        UUID id = bukkitPlayer.getUniqueId();
        ItemStack item = new ItemStack(plugin.getConfigManager().soulMaterial);
        PPlayer player = plugin.getPlayerManager().getPlayer(id);
        long bounty = player.getBounty().getTotal();
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(plugin.getConfigManager().soulModelData);
        String title = Messages.SOUL_TITLE.replace("%victim%", bukkitPlayer.getName());
        List<String> lore = Messages.SOUL_LORE;
        ListIterator<String> iterator = lore.listIterator();
        while (iterator.hasNext()) {
            String str = iterator.next();
            str = str.replace("%victim%", bukkitPlayer.getName());
            iterator.set(str);
        }
        meta.setDisplayName(title);
        meta.setLore(lore);
        item.setItemMeta(meta);

        NBTItem nbti = new NBTItem(item);
        nbti.setLong(NBT_BOUNTY_AMOUNT, bounty);
        nbti.setString(NBT_BOUNTY_PLAYER, id.toString());
        item = nbti.getItem();
        return item;
    }

    public List<ItemStack> getSoulsFromInventory(Inventory inventory) {
        List<ItemStack> items = new ArrayList<>();
        if (inventory.getContents() == null) {
            return items;
        }
        for (ItemStack item : inventory.getContents()) {
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }
            NBTItem nbti = new NBTItem(item);
            if (nbti.hasKey(NBT_BOUNTY_PLAYER)) {
                items.add(item);
            }
        }
        return items;
    }

    public boolean doesExist(@org.jetbrains.annotations.NotNull UUID uuid) {
        return config.contains("blacklist." + uuid.toString());
    }

    public boolean isBlacklisted(UUID attacker, UUID attacked) {
        Iterator<BlacklistItem> iterator = blackList.iterator();
        while (iterator.hasNext()) {
            BlacklistItem item = iterator.next();
            if (item.getAttacked().equals(attacked) && item.getAttacker().equals(attacker)) {
                if (item.getTimeOccurred() + (1000 * plugin.getConfigManager().blackListDuration) <= System.currentTimeMillis()) {
                    iterator.remove();
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public void addToBlacklist(UUID attacker, UUID attacked) {
        BlacklistItem item = new BlacklistItem(attacker, attacked, System.currentTimeMillis());
        blackList.add(item);
    }

    public long getTimeRemaining(UUID attacker, UUID attacked) {

        Iterator<BlacklistItem> iterator = blackList.iterator();
        while (iterator.hasNext()) {
            BlacklistItem item = iterator.next();
            if (item.getAttacked().equals(attacked) && item.getAttacker().equals(attacker)) {
                if (item.getTimeOccurred() + (1000L * plugin.getConfigManager().blackListDuration) <= System.currentTimeMillis()) {
                    iterator.remove();
                    return 0L;
                } else {
                    return (System.currentTimeMillis() - (item.getTimeOccurred() + plugin.getConfigManager().blackListDuration * 1000L) / 1000L);
                }
            }
        }
        return 0L;
    }

    private void loadAll() {
        try {
            for (String key : Objects.requireNonNull(config.getConfigurationSection("blacklist")).getKeys(false)) {
                BlacklistItem item = (BlacklistItem) config.get("blacklist." + key);
                blackList.add(item);
            }
        } catch (NullPointerException ex) {
            plugin.getLogger().warning("Error loading bounty config, ignore if empty!");
            return;
        }
    }

    public void onClose() {
        saveBlacklist();
    }

    private void saveBlacklist() {
        for (BlacklistItem item : blackList) {
            config.set("blacklist." + item.getId().toString(), item);
        }
        saveConfig();
    }

    private void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
