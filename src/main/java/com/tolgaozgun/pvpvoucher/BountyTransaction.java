package com.tolgaozgun.pvpvoucher;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BountyTransaction implements ConfigurationSerializable {

    private long amount;
    private String description;

    public BountyTransaction(long amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    public long getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("amount", amount + "");
        map.put("description", description);
        return map;
    }

    public static BountyTransaction deserialize(Map<String, Object> map) {
        UUID id = UUID.fromString((String) map.get("id"));
        long amount = Long.valueOf((String) map.get("amount"));
        String description = (String) map.get("description");
        return new BountyTransaction(amount, description);
    }
}
