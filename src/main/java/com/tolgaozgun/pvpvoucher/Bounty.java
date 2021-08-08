package com.tolgaozgun.pvpvoucher;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bounty implements ConfigurationSerializable {

    private List<BountyTransaction> transactionList;

    public Bounty() {
        transactionList = new ArrayList<>();
    }

    public Bounty(List<BountyTransaction> transactionList) {
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

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("transaction-list", transactionList);
        return map;
    }

    public static Bounty deserialize(Map<String, Object> map) {
        List<BountyTransaction> list = (List<BountyTransaction>) map.get("transaction-list");
        return new Bounty(list);
    }
}
