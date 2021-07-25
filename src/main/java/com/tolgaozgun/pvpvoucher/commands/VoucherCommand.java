package com.tolgaozgun.pvpvoucher.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.tolgaozgun.pvpvoucher.PluginMain;
import com.tolgaozgun.pvpvoucher.util.Messages;

public class VoucherCommand implements CommandExecutor {

    private PluginMain plugin = PluginMain.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	if (sender instanceof Player) {
	    Player player = (Player) sender;
	    if (!player.isOp() && !player.hasPermission("voucher.admin.vouchercommand")) {
		player.sendMessage(Messages.NO_PERM);
		return true;
	    }
	}

	if (args.length == 0) {
	    sendHelp(sender);
	    return true;
	}

	switch (args[0].toLowerCase()) {
	case "give":
	    if (args.length < 2) {
		String message = Messages.WRONG_USAGE;
		message = message.replace("%usage%", "/voucher give <Player> (<Duration>)");
		sender.sendMessage(message);
		return true;
	    }
	    if (Bukkit.getPlayer(args[1]) == null) {
		String message = Messages.PLAYER_NOT_FOUND;
		message = message.replace("%player%", args[1]);
		sender.sendMessage(message);
		return true;
	    }
	    Player target = Bukkit.getPlayer(args[1]);

	    long duration;
	    if (args.length > 2) {
		try {
		    duration = Long.parseLong(args[2]);
		} catch (Exception | Error ex) {
		    String message = Messages.WRONG_USAGE;
		    message = message.replace("%usage%", "/voucher give <Player> (<Duration>)");
		    sender.sendMessage(message);
		    return true;
		}
	    } else {
		duration = plugin.getConfigManager().defaultDuration;
	    }
	    target.getInventory().addItem(plugin.getVoucherManager().createVoucher(duration));
	    return true;
	default:
	    sendHelp(sender);
	    return true;
	}
    }

    private void sendHelp(CommandSender sender) {
	sender.sendMessage("/voucher give <Player> (<Duration>)");
    }

}
