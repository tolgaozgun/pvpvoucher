package com.tolgaozgun.pvpvoucher.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class VoucherUseEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancel;
    private Player player;
    private ItemStack item;

    public VoucherUseEvent(Player player, ItemStack item) {
	cancel = false;
	this.player = player;
	this.item = item;
    }

    @Override
    public HandlerList getHandlers() {
	return HANDLERS;
    }

    public static HandlerList getHandlerList() {
	return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
	return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
	this.cancel = cancel;
    }

    public Player getPlayer() {
	return player;
    }
    
    public ItemStack getItemStack() {
	return item;
    }
    
    public Material getMaterial() {
	return item.getType();
    }

}
