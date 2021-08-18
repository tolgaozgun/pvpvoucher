package com.tolgaozgun.pvpvoucher;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BlacklistItem implements ConfigurationSerializable {

    private UUID id;
    private UUID attacker;
    private UUID attacked;
    private long timeOccurred;

    public BlacklistItem(UUID attacker, UUID attacked, long timeOccurred) {
        id = UUID.randomUUID();
        while (PluginMain.getInstance().getBountyManager().doesBlacklistHave(id)) {
            id = UUID.randomUUID();
        }
        this.attacker = attacker;
        this.attacked = attacked;
        this.timeOccurred = timeOccurred;
    }

    public BlacklistItem(UUID id, UUID attacker, UUID attacked, long timeOccurred) {
        this.id = id;
        this.attacker = attacker;
        this.attacked = attacked;
        this.timeOccurred = timeOccurred;
    }

    public UUID getAttacker() {
        return attacker;
    }

    public UUID getAttacked() {
        return attacked;
    }

    public long getTimeOccurred() {
        return timeOccurred;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id.toString());
        map.put("attacker", attacker.toString());
        map.put("attacked", attacked.toString());
        map.put("time", timeOccurred + "");
        return map;
    }

    public static BlacklistItem deserialize(Map<String, Object> map) {
        UUID id = UUID.fromString((String) map.get("id"));
        UUID attacker = UUID.fromString((String) map.get("attacker"));
        UUID attacked = UUID.fromString((String) map.get("attacked"));
        long time = Long.valueOf((String) map.get("time"));
        return new BlacklistItem(id, attacker, attacked, time);
    }
}
