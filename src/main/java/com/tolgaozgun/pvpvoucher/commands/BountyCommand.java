package com.tolgaozgun.pvpvoucher.commands;

import com.tolgaozgun.pvpvoucher.PluginMain;
import com.tolgaozgun.pvpvoucher.gui.MainGUI;
import com.tolgaozgun.pvpvoucher.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BountyCommand implements CommandExecutor {

    private PluginMain plugin = PluginMain.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp() && !player.hasPermission("voucher.user.command.bounty")) {
                player.sendMessage(Messages.NO_PERM);
                return true;
            }
            MainGUI mainGUI = new MainGUI(player);
            mainGUI.openInventory();
        } else {
            sender.sendMessage(Messages.CONSOLE_NOT_AVAILABLE);
        }

        return true;
    }

}
