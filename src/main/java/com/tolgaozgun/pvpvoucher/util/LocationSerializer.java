package com.tolgaozgun.pvpvoucher.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSerializer {

    private final static String SEPERATOR = "_";

    public static String getSerializedLocation(Location loc) {
	String str = loc.getX() + SEPERATOR + loc.getY() + SEPERATOR + loc.getZ() + SEPERATOR
		+ loc.getWorld().getName();
	str = str.replace(".", "#");
	return str;
    }

    public static Location getDeserializedLocation(String s) {
	s = s.replace("#", ".");
	String[] parts = s.split(SEPERATOR);
	double x = Double.parseDouble(parts[0]);
	double y = Double.parseDouble(parts[1]);
	double z = Double.parseDouble(parts[2]);
	World w = Bukkit.getWorld(parts[3]);
	return new Location(w, x, y, z);
    }
}
