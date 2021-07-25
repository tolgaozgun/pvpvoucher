package com.tolgaozgun.pvpvoucher.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WellUseEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancel;
    private Player player;
    private Location location;

    public WellUseEvent(Player player, Location location) {
	cancel = false;
	this.player = player;
	this.location = location;
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

    public Location getWellLocation() {
	return location;
    }

}
