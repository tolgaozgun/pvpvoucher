package com.tolgaozgun.pvpvoucher.util;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import com.tolgaozgun.pvpvoucher.PluginMain;

import net.md_5.bungee.api.ChatColor;

public class Messages {

    private static PluginMain plugin = PluginMain.getInstance();
    private static FileConfiguration config = plugin.getLocaleConfig();

    // Strings
    public static String CONSOLE_NOT_AVAILABLE;
    public static String NO_PERM;
    public static String WRONG_USAGE;
    public static String VOUCHER_ALREADY_ACTIVE;
    public static String PLAYER_NOT_FOUND;
    public static String SCOREBOARD_TITLE;
    public static String VOUCHER_TITLE;
    public static String SOUL_TITLE;
    public static String MG_BOUNTIES_TITLE;
    public static String MG_SOUL_COLLECTOR_TITLE;
    public static List<String> VOUCHER_LORE;
    public static List<String> SOUL_LORE;
    public static List<String> MG_BOUNTIES_LORE;
    public static List<String> MG_SOUL_COLLECTOR_LORE;
    public static List<String> SCOREBOARD_LINES;

    public static void load() {
        CONSOLE_NOT_AVAILABLE = getString("CONSOLE_NOT_AVAILABLE");
        NO_PERM = getString("NO_PERM");
        WRONG_USAGE = getString("WRONG_USAGE");
        VOUCHER_ALREADY_ACTIVE = getString("VOUCHER_ALREADY_ACTIVE");
        PLAYER_NOT_FOUND = getString("PLAYER_NOT_FOUND");
        SCOREBOARD_TITLE = getString("SCOREBOARD_TITLE");
        SOUL_TITLE = getString("SOUL_TITLE");
        VOUCHER_TITLE = getString("VOUCHER_TITLE");
        MG_BOUNTIES_TITLE = getString("MG_BOUNTIES_TITLE");
        MG_SOUL_COLLECTOR_TITLE = getString("MG_SOUL_COLLECTOR_TITLE");
        VOUCHER_LORE = getStringList("VOUCHER_LORE");
        SOUL_LORE = getStringList("VOUCHER_LORE");
        MG_BOUNTIES_LORE = getStringList("MG_BOUNTIES_LORE");
        MG_SOUL_COLLECTOR_LORE = getStringList("MG_SOUL_COLLECTOR_LORE");
        SCOREBOARD_LINES = getStringList("SCOREBOARD_LINES");
    }

    private static List<String> getStringList(String path) {
        List<String> list = config.getStringList(path);
        for (int i = 0; i < list.size(); i++) {
            String text = list.get(i);
            list.set(i, ChatColor.translateAlternateColorCodes('&', text));
        }
        return list;

    }

    private static String getString(String path) {
        return ChatColor.translateAlternateColorCodes('&', config.getString(path));
    }

}
