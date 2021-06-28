package me.simplicitee.superpower.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.command.PluginCommand;

import me.simplicitee.superpower.SuperpowerPlugin;

public final class CommandManager {

	private static final Map<String, CommandBase> CMDS = new HashMap<>();
	private static boolean init = false;
	
	public static void init(SuperpowerPlugin plugin) {
		if (init) {
			return;
		}
		
		enable(plugin.getCommand("superpower"), new PowerCommand());
		registerCore();
		
		init = true;
	}
	
	private static void enable(PluginCommand pl, PowerCommand cmd) {
		pl.setExecutor(cmd);
		pl.setTabCompleter(cmd);
	}
	
	private static void registerCore() {
		register(new PowerSetCommand());
		register(new PowerToggleCommand());
	}
	
	public static void register(CommandBase command) {
		if (CMDS.containsKey(command.getName().toLowerCase())) {
			return;
		}
		
		CMDS.put(command.getName().toLowerCase(), command);
		
		for (String alias : command.getAliases()) {
			CMDS.putIfAbsent(alias.toLowerCase(), command);
		}
	}
	
	public static CommandBase get(String cmd) {
		return CMDS.get(cmd.toLowerCase());
	}
	
	public static <T> List<T> listAll(Function<CommandBase, T> map) {
		return CMDS.values().stream().map(map).collect(Collectors.toList());
	}
}
