package com.tolgaozgun.pvpvoucher.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.tolgaozgun.pvpvoucher.voucher.Voucher;

public class VoucherEndEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private Player player;
    private Voucher voucher;
    
    
    public VoucherEndEvent(UUID uuid, Voucher voucher) {
	this.player = Bukkit.getPlayer(uuid);
	this.voucher = voucher;
    }

    public VoucherEndEvent(Player player, Voucher voucher) {
	this.player = player;
	this.voucher = voucher;
    }

    @Override
    public HandlerList getHandlers() {
	return HANDLERS;
    }

    public static HandlerList getHandlerList() {
	return HANDLERS;
    }

    public Player getPlayer() {
	return player;
    }

    public Voucher getItemStack() {
	return voucher;
    }

}
