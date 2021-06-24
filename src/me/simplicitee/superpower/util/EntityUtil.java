package me.simplicitee.superpower.util;

import java.util.Collection;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public final class EntityUtil {

	private EntityUtil() {}
	
	public static Collection<Entity> getNearby(Location loc, double radius, Predicate<Entity> filter) {
		return loc.getWorld().getNearbyEntities(loc, radius, radius, radius, filter);
	}
}
