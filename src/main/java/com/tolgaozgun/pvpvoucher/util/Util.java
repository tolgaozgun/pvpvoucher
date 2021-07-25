package com.tolgaozgun.pvpvoucher.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.NBTItem;

public class Util {

    public static boolean checkNBT(ItemStack item, String nbtKey) {
	NBTItem nbti = new NBTItem(item);
	if (nbti.hasKey(nbtKey) && nbti.getBoolean(nbtKey)) {
	    return true;
	}
	return false;
    }

    public static List<String> serializeLocList(List<Location> list) {
	List<String> stringList = new ArrayList<String>();
	if (list != null) {
	    for (Location location : list) {
		String string = LocationSerializer.getSerializedLocation(location);
		stringList.add(string);
	    }
	}
	return stringList;
    }

    public static List<Location> deserializeLocList(List<String> list) {
	List<Location> locList = new ArrayList<>();
	if (list != null) {
	    for (String string : list) {
		Location location = LocationSerializer.getDeserializedLocation(string);
		locList.add(location);
	    }
	}
	return locList;

    }

}
