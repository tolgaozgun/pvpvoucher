package com.tolgaozgun.pvpvoucher.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.tolgaozgun.pvpvoucher.Bounty;
import com.tolgaozgun.pvpvoucher.BountyTransaction;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import com.tolgaozgun.pvpvoucher.PluginMain;
import com.tolgaozgun.pvpvoucher.voucher.Voucher;

public class PPlayer implements ConfigurationSerializable {

    private PluginMain plugin;
    private UUID uuid;
    private Voucher voucher;

    public PPlayer(Player player) {
        plugin = PluginMain.getInstance();
        this.uuid = player.getUniqueId();
        voucher = null;
    }

    public PPlayer(UUID uuid) {
        plugin = PluginMain.getInstance();
        this.uuid = uuid;
        voucher = null;
    }

    public PPlayer(Player player, Voucher voucher) {
        plugin = PluginMain.getInstance();
        this.uuid = player.getUniqueId();
        this.voucher = voucher;
    }

    public PPlayer(UUID uuid, Voucher voucher) {
        plugin = PluginMain.getInstance();
        this.uuid = uuid;
        this.voucher = voucher;
    }

    public Player getBukkitPlayer() {
        Player player = plugin.getServer().getPlayer(uuid);
        return player;
    }

    public void onQuit() {
        if (voucher != null && voucher.getTimeLeft() > 0) {
            plugin.getVoucherManager().saveVoucher(uuid, voucher);
        }
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public boolean isVoucherActive() {
        return voucher != null && voucher.getTimeLeft() > 0;
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uuid", uuid.toString());
        map.put("voucher", voucher);
        return map;
    }

    public static PPlayer deserialize(Map<String, Object> map) {
        UUID uuid = UUID.fromString((String) map.get("uuid"));
        Voucher voucher = (Voucher) map.get("voucher");
        return new PPlayer(uuid, voucher);

    }

}