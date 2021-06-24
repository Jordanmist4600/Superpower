package me.simplicitee.superpower.util;

import org.bukkit.Location;

public final class LocationUtil {

	private LocationUtil() {}
	
	public static Location moveLeft(Location loc, double distance) {
		float angle = loc.getYaw() / 60;
		return loc.add(distance * Math.cos(angle), 0, distance * Math.sin(angle));
	}
	
	public static Location moveRight(Location loc, double distance) {
		return moveLeft(loc, -distance);
	}
}
