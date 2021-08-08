package com.tolgaozgun.pvpvoucher.gui;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Stack;
import java.util.UUID;

public class GUIManager {

    private HashMap<UUID, Stack<StackableGUI>> guiList;

    public GUIManager() {
        guiList = new HashMap<>();
    }

    public void addHistory(Player player, StackableGUI gui) {
        Stack<StackableGUI> stack = new Stack<>();
        if (guiList.containsKey(player.getUniqueId())) {
            stack = guiList.get(player.getUniqueId());
        }
        stack.push(gui);
        guiList.put(player.getUniqueId(), stack);
    }

    public StackableGUI getPreviousGUI(Player player) {
        if (!guiList.containsKey(player.getUniqueId())) {
            return null;
        }
        if (guiList.get(player.getUniqueId()) == null || guiList.get(player.getUniqueId()).size() < 1) {
            return null;
        }
        return guiList.get(player.getUniqueId()).pop();
    }

    public void removePlayer(Player player) {
        guiList.remove(player.getUniqueId());
    }

    public boolean hasPreviousGUI(Player player) {
        return guiList.containsKey(player.getUniqueId()) && guiList.get(player.getUniqueId()) != null && guiList.get(player.getUniqueId()).size() > 0;
    }
}
