package me.simplicitee.superpower;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;

public final class PowerManager {

	private PowerManager() {}
	
	private static final Map<String, Power> POWERS = new HashMap<>();
	private static final Map<Class<? extends Power>, Power> CLASSES = new HashMap<>();
	
	public static <T extends Power> T register(T registree) {
		Validate.notNull(registree, "Cannot register null power!");
		Validate.notNull(registree.getName(), "Cannot register power with null name!");
		Validate.isTrue(!POWERS.containsKey(registree.internal()), "Cannot register '" + registree.getName() + "', name already used!");
		Validate.isTrue(!CLASSES.containsKey(registree.getClass()), "Cannot register '" + registree.getName() + "', class already registered!");
		
		POWERS.put(registree.internal(), registree);
		CLASSES.put(registree.getClass(), registree);
		return registree;
	}
}
