package com.tolgaozgun.pvpvoucher.commands;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.tolgaozgun.pvpvoucher.PluginMain;
import com.tolgaozgun.pvpvoucher.util.Messages;

public class WellCommand implements CommandExecutor {

    private PluginMain plugin = PluginMain.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	if (sender instanceof Player) {
	    Player player = (Player) sender;
	    if (!player.isOp() && !player.hasPermission("voucher.admin.wellcommand")) {
		player.sendMessage(Messages.NO_PERM);
		return true;
	    }
	}

	if (args.length == 0) {
	    sendHelp(sender);
	    return true;
	}

	switch (args[0].toLowerCase()) {
	case "set":

	    if (!(sender instanceof Player)) {
		sender.sendMessage(Messages.CONSOLE_NOT_AVAILABLE);
		return true;
	    }
	    Player player = (Player) sender;
	    Block block = player.getTargetBlockExact(10);
	    if(block == null || block.getType() != plugin.getConfigManager().wellMaterial) {
		player.sendMessage("The block is not correct material!");
		return true;
	    }
	    plugin.getWellManager().addWell(block.getLocation());
	    player.sendMessage("Well added!");
	    return true;
	default:
	    sendHelp(sender);
	    return true;
	}
    }

    private void sendHelp(CommandSender sender) {
	sender.sendMessage("/well set");
    }

}
