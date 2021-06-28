package me.simplicitee.superpower.command;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public abstract class CommandBase {
	
	private String name, desc, usage;
	private Set<String> aliases;
	
	public CommandBase(String name, String desc, String usage, List<String> aliases) {
		this.name = name;
		this.desc = desc;
		this.usage = usage;
		this.aliases = aliases == null ? new HashSet<>() : aliases.stream().map(String::toLowerCase).collect(Collectors.toSet());
	}
	
	protected abstract void onExecute(CommandSender sender, List<String> args);
	public abstract int getMaxArgs();
	public abstract int getMinArgs();
	
	final Set<String> getAliases() {
		return aliases;
	}
	
	public final String getName() {
		return name;
	}
	
	public final String getDescription() {
		return desc;
	}
	
	public final String getUsage() {
		return usage;
	}
	
	public final boolean isAlias(String alias) {
		return aliases.contains(alias.toLowerCase());
	}
	
	public final void execute(CommandSender sender, List<String> args) {
		if (args.size() < getMinArgs() || args.size() > getMaxArgs()) {
			sender.sendMessage(ChatColor.RED + "Incorrect args amount!");
			sender.sendMessage(ChatColor.AQUA + "Usage: " + ChatColor.WHITE + usage);
			return;
		}
		
		onExecute(sender, args);
	}
	
	public List<String> completeTab(CommandSender sender, List<String> args) {
		return null;
	}
}
