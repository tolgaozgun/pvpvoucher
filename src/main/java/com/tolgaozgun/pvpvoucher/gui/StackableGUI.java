package com.tolgaozgun.pvpvoucher.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public interface StackableGUI extends Listener {

    void openInventory(Player player);
}
