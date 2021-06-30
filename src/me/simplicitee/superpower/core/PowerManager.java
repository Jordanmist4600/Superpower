package me.simplicitee.superpower.core;

import static me.simplicitee.superpower.event.AbilityStopEvent.Reason.FORCED;
import static me.simplicitee.superpower.event.AbilityStopEvent.Reason.NATURAL;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import me.simplicitee.superpower.SuperpowerPlugin;
import me.simplicitee.superpower.configuration.Configurator;
import me.simplicitee.superpower.event.AbilityStartEvent;
import me.simplicitee.superpower.event.AbilityStopEvent;
import me.simplicitee.superpower.event.AbilityStopEvent.Reason;
import me.simplicitee.superpower.event.PowerActivationEvent;
import me.simplicitee.superpower.powers.superpower.SuperPower;
import me.simplicitee.superpower.powers.superspeed.Superspeed;
import me.simplicitee.superpower.util.EventUtil;

public final class PowerManager {

	private PowerManager() {}
	
	private static Configurator storage;
	
	private static final Map<String, Power> POWERS = new HashMap<>();
	private static final Map<Class<? extends Power>, Power> CLASSES = new HashMap<>();
	private static final Map<Player, PowerUser> USERS = new HashMap<>();
	
	private static final Set<Ability> ACTIVE = new HashSet<>();
	
	private static long prevTime = System.currentTimeMillis();
	private static boolean init = false;
	
	public static boolean init(SuperpowerPlugin plugin) {
		if (init) {
			return false;
		}
		
		storage = new Configurator(new File(plugin.getDataFolder(), "storage.yml"), "NOT TO BE MODIFIED");
		
		registerCore();
		
		init = true;
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, PowerManager::tick, 0, 1);
		return true;
	}
	
	private static void registerCore() {
		register(new SuperPower());
		register(new Superspeed());
	}
	
	public static <T extends Power> T register(T registree) {
		Validate.notNull(registree, "Cannot register null power!");
		Validate.notNull(registree.getName(), "Cannot register power with null name!");
		Validate.isTrue(!POWERS.containsKey(registree.getName().toLowerCase()), "Cannot register '" + registree.getName() + "', name already used!");
		Validate.isTrue(!CLASSES.containsKey(registree.getClass()), "Cannot register '" + registree.getName() + "', class already registered!");
		
		POWERS.put(registree.getName().toLowerCase(), registree);
		CLASSES.put(registree.getClass(), registree);
		return Configurator.process(registree);
	}
	
	public static <T> List<T> list(Predicate<Power> filter, Function<Power, T> func) {
		return POWERS.values().stream().filter(filter).map(func).collect(Collectors.toList());
	}
	
	public static Optional<Power> get(String name) {
		if (name == null) {
			return Optional.empty();
		}
		
		return Optional.ofNullable(POWERS.get(name.toLowerCase()));
	}
	
	public static <T extends Power> Optional<T> get(Class<T> clazz) {
		if (clazz == null) {
			return Optional.empty();
		}
		
		return Optional.ofNullable(clazz.cast(CLASSES.get(clazz)));
	}
	
	public static boolean activate(Player player, Activation trigger, Event provider) {
		return activate(user(player), trigger, provider);
	}
	
	public static boolean activate(PowerUser user, Activation trigger, Event provider) {
		if (user == null || trigger == null) {
			return false;
		} else if (EventUtil.call(new PowerActivationEvent(user, trigger, provider)).isCancelled()) {
			return false;
		}
		
		return user.usePower(trigger, provider).map((a) -> start(a)).orElse(false);
	}
	
	public static boolean start(Ability ability) {
		if (ability == null) {
			return false;
		} else if (ACTIVE.contains(ability)) {
			return false;
		} else if (EventUtil.call(new AbilityStartEvent(ability)).isCancelled()) {
			return false;
		}
		
		ACTIVE.add(ability);
		ability.start();
		return true;
	}
	
	public static void remove(Ability ability) {
		if (ability == null) {
			return;
		}
		
		stop(ability, FORCED);
		ACTIVE.remove(ability);
	}
	
	static void stop(Ability ability, Reason reason) {
		EventUtil.call(new AbilityStopEvent(ability, reason));
		ability.stop();
	}
	
	public static PowerUser user(Player player) {
		return USERS.computeIfAbsent(player, PowerManager::loadOrCreateUser);
	}
	
	private static PowerUser loadOrCreateUser(Player player) {
		PowerUser user = new PowerUser(player);
		
		String uuid = player.getUniqueId().toString();
		
		if (storage.getConfig().contains(uuid)) {
			user.setPower(POWERS.get(storage.getConfig().getString(uuid + ".power").toLowerCase()));
			user.setToggled(storage.getConfig().getBoolean(uuid + ".toggled"));
		}
		
		return user;
	}
	
	public static void saveUser(PowerUser user) {
		if (user == null) {
			return;
		}
		
		storage.getConfig().set(user.getPlayer().getUniqueId().toString() + ".power", user.getPower().getName());
		storage.getConfig().set(user.getPlayer().getUniqueId().toString() + ".toggled", user.isToggled());
		storage.save();
	}
	
	public static void saveUser(Player player) {
		saveUser(USERS.get(player));
	}
	
	public static void clearUser(PowerUser user) {
		if (user == null) {
			return;
		}
		
		saveUser(user);
		user.clear();
		USERS.remove(user.getPlayer());
	}
	
	public static void clearUser(Player player) {
		clearUser(USERS.get(player));
	}
	
	public static void saveUsers() {
		if (USERS.isEmpty()) {
			return;
		}
		
		USERS.values().forEach(PowerManager::saveUser);
	}
	
	public static void disable() {
		saveUsers();
		ACTIVE.forEach((a) -> a.stop());
		ACTIVE.clear();
		USERS.clear();
		POWERS.clear();
		CLASSES.clear();
	}
	
	static void tick() {
		double timeDelta = (System.currentTimeMillis() - prevTime) / 1000D;
		
		Iterator<Ability> iter = ACTIVE.iterator();
		while (iter.hasNext()) {
			Ability ability = iter.next();
			
			if (ability.update(timeDelta)) {
				continue;
			}
			
			iter.remove();
			stop(ability, NATURAL);
		}
		
		prevTime = System.currentTimeMillis();
	}
}
