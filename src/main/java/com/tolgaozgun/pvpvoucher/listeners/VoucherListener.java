package com.tolgaozgun.pvpvoucher.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.tolgaozgun.pvpvoucher.PluginMain;
import com.tolgaozgun.pvpvoucher.events.VoucherEndEvent;
import com.tolgaozgun.pvpvoucher.events.VoucherUseEvent;
import com.tolgaozgun.pvpvoucher.player.PPlayer;
import com.tolgaozgun.pvpvoucher.util.Constants;
import com.tolgaozgun.pvpvoucher.util.Messages;
import com.tolgaozgun.pvpvoucher.voucher.Voucher;

import de.tr7zw.changeme.nbtapi.NBTItem;

public class VoucherListener implements Listener {

    private PluginMain plugin = PluginMain.getInstance();

    @EventHandler
    public void onVoucherUse(VoucherUseEvent event) {
	Player bukkitPlayer = event.getPlayer();
	PPlayer player = plugin.getPlayerManager().getPlayer(bukkitPlayer);
	if (player.isVoucherActive()) {
	    bukkitPlayer.sendMessage(Messages.VOUCHER_ALREADY_ACTIVE);
	    event.setCancelled(true);
	    return;
	}

	ItemStack item = event.getItemStack();
	NBTItem nbti = new NBTItem(item);
	long duration = nbti.getLong(Constants.NK_DURATION);
	Voucher voucher = new Voucher(duration);
	plugin.getVoucherManager().startVoucher(bukkitPlayer, voucher);
	plugin.getVoucherManager().setActive(voucher);
	player.setVoucher(voucher);
	item.setAmount(item.getAmount() - 1);
	bukkitPlayer.sendMessage("Used voucher for " + duration + " seconds!");
    }

    @EventHandler
    public void onVoucherEnd(VoucherEndEvent event) {
	Player bukkitPlayer = event.getPlayer();
	PPlayer player = plugin.getPlayerManager().getPlayer(bukkitPlayer);
	player.setVoucher(null);
	plugin.getVoucherManager().removeVoucher(bukkitPlayer);
	bukkitPlayer.sendMessage("Your voucher has expired!");
    }

}
