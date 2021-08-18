package com.tolgaozgun.pvpvoucher;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Bounty implements ConfigurationSerializable {

    private UUID player;
    private List<BountyTransaction> transactionList;

    public Bounty(UUID player) {
        transactionList = new ArrayList<>();
        this.player = player;
    }

    public Bounty(UUID player, List<BountyTransaction> transactionList) {
        this.player = player;
        this.transactionList = transactionList;
    }

    public void addTransaction(BountyTransaction transaction) {
        transactionList.add(transaction);
    }

    public long getTotal() {
        long total = 0L;
        for (BountyTransaction transaction : transactionList) {
            total += transaction.getAmount();
        }
        return total;
    }

    public UUID getPlayerId(){
        return player;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("player", player.toString());
        map.put("transaction-list", transactionList);
        return map;
    }

    public static Bounty deserialize(Map<String, Object> map) {
        UUID player = UUID.fromString((String) map.get("player"));
        List<BountyTransaction> list = (List<BountyTransaction>) map.get("transaction-list");
        return new Bounty(player, list);
    }
}
