package com.tolgaozgun.pvpvoucher.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.tolgaozgun.pvpvoucher.PluginMain;
import com.tolgaozgun.pvpvoucher.voucher.Voucher;

public class VoucherScoreboard {

    private PluginMain plugin = PluginMain.getInstance();
    private ScoreboardManager m;
    private Scoreboard b;
    private Objective o;
    private Player player;
    private Voucher voucher;
    private BukkitTask task;

    @SuppressWarnings("deprecation")
    public VoucherScoreboard(Player player, Voucher voucher) {
	this.player = player;
	this.voucher = voucher;
	m = Bukkit.getScoreboardManager();
	b = m.getNewScoreboard();
	o = b.registerNewObjective("voucher", "");
	o.setDisplaySlot(DisplaySlot.SIDEBAR);
	o.setDisplayName(ChatColor.GOLD + Messages.SCOREBOARD_TITLE);
	scoreGame();
    }

    public void stop() {
	if (!task.isCancelled()) {
	    task.cancel();
	    plugin.getServer().getScheduler().cancelTask(task.getTaskId());
	    player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
    }

    public void scoreGame() {
	task = plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {

	    @Override
	    public void run() {
		if (player == null) {
		    return;
		}
		if (voucher == null) {
		    return;
		}
		if (voucher.getTimeLeft() <= 0) {
		    player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		    stop();
		    return;
		}
		for (String entry : b.getEntries()) {
		    b.resetScores(entry);
		}
		long timeLeft = voucher.getTimeLeft();
		long duration = voucher.getDuration();

		List<String> lines = new ArrayList<String>(Messages.SCOREBOARD_LINES);
		Collections.reverse(lines);
		for (int i = 0; i < lines.size(); i++) {
		    String line = lines.get(i);
		    line = line.replace("%timeLeft%", timeLeft + "").replace("%duration%", duration + "");
		    Score score = o.getScore(line);
		    score.setScore(i);
		}
		player.setScoreboard(b);

	    }

	}, 0L, 10L);

    }

}
