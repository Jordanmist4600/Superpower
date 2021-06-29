package me.simplicitee.superpower.core;

import static me.simplicitee.superpower.event.AbilityStopEvent.Reason.FORCED;
import static me.simplicitee.superpower.event.AbilityStopEvent.Reason.NATURAL;

import java.sql.PreparedStatement;
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
import me.simplicitee.superpower.event.PowerActivationEvent;
import me.simplicitee.superpower.powers.superpower.SuperPower;
import me.simplicitee.superpower.storage.ConnectedDatabase;
import me.simplicitee.superpower.storage.Database;
import me.simplicitee.superpower.storage.DatabaseOptions;
import me.simplicitee.superpower.util.EventUtil;

public final class PowerManager {

	private PowerManager() {}
	
	private static ConnectedDatabase db;
	private static PreparedStatement getUser, newUser, updUser, createTable;
	
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
		
		db = Database.connect(Configurator.process(new DatabaseOptions()));
		
		if (db == null) {
			plugin.getLogger().warning("Error loading configured database, resetting to defaults");
			db = Database.connect(new DatabaseOptions());
		}
		
		switch (db.getServerType()) {
		case MYSQL:
			createTable = db.prepare("CREATE TABLE IF NOT EXISTS superpower_users (uuid VARCHAR(16) PRIMARY KEY, power VARCHAR(255) DEFAULT '', toggled BOOLEAN DEFAULT 'true');");
			break;
		case SQLITE:
			createTable = db.prepare("CREATE TABLE IF NOT EXISTS superpower_users (uuid TEXT PRIMARY KEY, power TEXT DEFAULT '', toggled NUMERIC DEFAULT 'true');");
			break;
		}
		
		try {
			db.query(createTable);
			getUser = db.prepare("SELECT * FROM superpower_users WHERE uuid = ?;");
			newUser = db.prepare("INSERT INTO superpower_users (uuid) VALUES (?);");
			updUser = db.prepare("UPDATE superpower_users SET power = ?, toggled = ? WHERE uuid = ?;");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		registerCore();
		
		init = true;
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, PowerManager::tick, 0, 1);
		return true;
	}
	
	private static void registerCore() {
		register(new SuperPower());
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
	
	private static void stop(Ability ability, AbilityStopEvent.Reason reason) {
		EventUtil.call(new AbilityStopEvent(ability, reason));
		ability.stop();
	}
	
	public static PowerUser user(Player player) {
		return USERS.computeIfAbsent(player, PowerManager::loadOrCreateUser);
	}
	
	private static PowerUser loadOrCreateUser(Player player) {
		PowerUser user = new PowerUser(player);
		
		try {
			getUser.setString(1, player.getUniqueId().toString());
			db.query(getUser, (rs) -> {
				try {
					if (rs.next()) {
						user.setPower(POWERS.get(rs.getString("power")));
						user.setToggled(rs.getBoolean("toggled"));
					} else {
						newUser.setString(1, player.getUniqueId().toString());
						db.query(newUser);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	public static void saveUser(PowerUser user) {
		if (user == null) {
			return;
		}
		
		try {
			updUser.setString(1, user.getPower().getName());
			updUser.setBoolean(2, user.isToggled());
			updUser.setString(3, user.getPlayer().getUniqueId().toString());
			db.query(updUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		
		try {
			for (PowerUser user : USERS.values()) {
				updUser.setString(1, user.getPower().getName());
				updUser.setBoolean(2, user.isToggled());
				updUser.setString(3, user.getPlayer().getUniqueId().toString());
				updUser.addBatch();
			}
			
			db.query(updUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void disable() {
		saveUsers();
		ACTIVE.forEach((a) -> a.stop());
		ACTIVE.clear();
		USERS.clear();
		POWERS.clear();
		CLASSES.clear();
		db.close();
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
