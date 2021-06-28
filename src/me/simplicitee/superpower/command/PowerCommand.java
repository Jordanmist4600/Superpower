package me.simplicitee.superpower.command;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class PowerCommand implements CommandExecutor, TabCompleter {
	
	PowerCommand() {}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(CommandManager.listAll(CommandBase::getUsage).toArray(new String[0]));
			return true;
		}
		
		CommandManager.get(args[0]).execute(sender, Arrays.asList(args).subList(1, args.length));
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1) {
			return CommandManager.listAll(CommandBase::getName);
		} else if (args.length > 1) {
			return CommandManager.get(args[0]).completeTab(sender, Arrays.asList(args).subList(2, args.length));
		}
		
		return null;
	}
}
