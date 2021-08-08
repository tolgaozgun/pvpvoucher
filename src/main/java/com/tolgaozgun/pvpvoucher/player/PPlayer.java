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
    private Bounty bounty;

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

    public PPlayer(Player player, Voucher voucher, Bounty bounty) {
        plugin = PluginMain.getInstance();
        this.uuid = player.getUniqueId();
        this.voucher = voucher;
        this.bounty = bounty;
    }

    public PPlayer(UUID uuid, Voucher voucher, Bounty bounty) {
        plugin = PluginMain.getInstance();
        this.uuid = uuid;
        this.voucher = voucher;
        this.bounty = bounty;
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

    public Bounty getBounty() {
        return bounty;
    }

    public long getBountyAmount() {
        return bounty.getTotal();
    }

    public void addBounty(BountyTransaction bountyTransaction) {
        bounty.addTransaction(bountyTransaction);
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uuid", uuid.toString());
        map.put("voucher", voucher);
        map.put("bounty", bounty);
        return map;
    }

    public static PPlayer deserialize(Map<String, Object> map) {
        UUID uuid = UUID.fromString((String) map.get("uuid"));
        Voucher voucher = (Voucher) map.get("voucher");
        Bounty bounty = ((Bounty) map.get("bounty"));
        return new PPlayer(uuid, voucher, bounty);

    }

}