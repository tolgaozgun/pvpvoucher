package com.tolgaozgun.pvpvoucher.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import com.tolgaozgun.pvpvoucher.PluginMain;
import com.tolgaozgun.pvpvoucher.player.PPlayer;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class DamageListener implements Listener {

    private PluginMain plugin = PluginMain.getInstance();

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player bukkitPlayer = (Player) event.getEntity();
            PPlayer player = plugin.getPlayerManager().getPlayer(bukkitPlayer);
            if (player.getVoucher() != null && player.getVoucher().isActive()) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onKill(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player victim = (Player) event.getEntity();
            Player attacker = (Player) event.getDamager();
            UUID victimId = victim.getUniqueId();
            UUID attackerId = attacker.getUniqueId();
            Location location = victim.getLocation();

            long bounty = plugin.getBountyManager().getBountyAmount(victimId);
            if (bounty > 0) {
                if (victim.getHealth() - event.getDamage() <= 0) {
                    if (plugin.getBountyManager().isBlacklisted(attackerId, victimId)) {
                        attacker.sendMessage("Bounty is still in cooldown for " + victim.getName() + ".");
                        attacker.sendMessage("You need to wait " + plugin.getBountyManager().getTimeRemaining(attackerId, victimId) + " more seconds!");
                        return;
                    }
                    victim.sendMessage("Your bounty for " + bounty + " has dropped onto the ground!");
                    ItemStack soulItem = plugin.getBountyManager().getSoulItem(victim);
                    location.getWorld().dropItem(location, soulItem);
                    plugin.getBountyManager().addToBlacklist(attackerId, victimId);
                    return;
                }
            }
        }
    }

}
