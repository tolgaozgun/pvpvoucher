package com.tolgaozgun.pvpvoucher.listeners;

import java.util.Arrays;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.tolgaozgun.pvpvoucher.PluginMain;
import com.tolgaozgun.pvpvoucher.events.WellUseEvent;
import com.tolgaozgun.pvpvoucher.util.Messages;
import com.tolgaozgun.pvpvoucher.util.SignMenuFactory;

public class WellListener implements Listener {

    private PluginMain plugin = PluginMain.getInstance();

    @EventHandler
    public void onWellUse(WellUseEvent event) {
	Player player = event.getPlayer();
	if (!player.hasPermission("donationwell.user.use") && !player.isOp()) {
	    player.sendMessage(Messages.NO_PERM);
	    return;
	}
	SignMenuFactory.Menu menu = plugin.getSignMenuFactory()
		.newMenu(Arrays.asList("", "^^^^^^", "Enter your", "search query!")).reopenIfFail(false)
		.response((anotherPlayer, lines) -> {
		    long value;
		    try {
			value = Long.parseLong(lines[0]);
		    }catch(Exception | Error e) {
			player.sendMessage("Invalid value: " + lines[0]);
			return false;
		    }
		    player.sendMessage("Value entered: " + value);
		    return false;
		});
	
	menu.open(player);

    }

}
