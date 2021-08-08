package com.tolgaozgun.pvpvoucher.voucher;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.scheduler.BukkitTask;

import com.tolgaozgun.pvpvoucher.PluginMain;
import com.tolgaozgun.pvpvoucher.events.VoucherEndEvent;
import com.tolgaozgun.pvpvoucher.util.VoucherScoreboard;

/**
 * Active vouchers
 *
 * @author tolgaozgun
 */
public class Voucher implements ConfigurationSerializable {

    private PluginMain plugin = PluginMain.getInstance();
    private long duration;
    private long endTime;
    private long timeLeft;
    private boolean isActive;
    private VoucherScoreboard voucherScoreboard;
    private BukkitTask endTask;

    public Voucher(long duration) {
        this.duration = duration;
        timeLeft = duration;
        endTime = -1;
        isActive = false;
    }

    public Voucher(long duration, long timeLeft) {
        this.duration = duration;
        this.timeLeft = timeLeft;
        endTime = -1;
        isActive = false;
    }

    public VoucherScoreboard getVoucherScoreboard() {
        return voucherScoreboard;
    }

    public void setVoucherScoreboard(VoucherScoreboard voucherScoreboard) {
        this.voucherScoreboard = voucherScoreboard;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getTimeLeft() {
        if (endTime == -1) {
            return timeLeft;
        } else if (System.currentTimeMillis() >= endTime) {
            System.out.println("System.currentTimeMillis()" + System.currentTimeMillis() + "\nendTime" + endTime);
            plugin.getServer().getPluginManager()
                    .callEvent(new VoucherEndEvent(plugin.getVoucherManager().getPlayer(this), this));
            timeLeft = 0;
        } else {
            timeLeft = (endTime - System.currentTimeMillis()) / 1000L;
        }
        return timeLeft;

    }

    public void setTimeLeft(long timeLeft) {
        this.timeLeft = timeLeft;
        if (isActive) {
            endTime = System.currentTimeMillis() + (timeLeft * 1000);
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        // Disabled -> Enabled
        if (!this.isActive && isActive) {
            endTime = System.currentTimeMillis() + (timeLeft * 1000);
            startTask();
        }
        // Enabled -> Disabled
        if (this.isActive && !isActive) {
            getTimeLeft();
            endTask.cancel();
            plugin.getServer().getScheduler().cancelTask(endTask.getTaskId());
            endTime = -1;
        }
        this.isActive = isActive;
    }

    private void startTask() {
        endTask = plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {

            @Override
            public void run() {
                Voucher voucher = Voucher.this;
                plugin.getServer().getPluginManager()
                        .callEvent(new VoucherEndEvent(plugin.getVoucherManager().getPlayer(voucher), voucher));

            }

        }, timeLeft * 20L);
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("duration", String.valueOf(duration));
        map.put("timeLeft", String.valueOf(getTimeLeft()));
        return map;
    }

    public static Voucher deserialize(Map<String, Object> map) {
        long duration = Long.valueOf((String) map.get("duration"));
        long timeLeft = Long.valueOf((String) map.get("timeLeft"));
        return new Voucher(duration, timeLeft);

    }

}
