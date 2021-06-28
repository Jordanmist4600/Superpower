package me.simplicitee.superpower.command;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.simplicitee.superpower.core.Power;
import me.simplicitee.superpower.core.PowerManager;
import net.md_5.bungee.api.ChatColor;

public class PowerSetCommand extends CommandBase {

	public PowerSetCommand() {
		super("set", "Set the target's power! No target = set own power", "/superpower set <power> [target]", Collections.singletonList("s"));
	}

	@Override
	protected void onExecute(CommandSender sender, List<String> args) {
		Optional<Power> power = PowerManager.get(args.get(0));
		
		if (!power.isPresent()) {
			sender.sendMessage(ChatColor.RED + "Unknown power!");
			return;
		}
		
		Player target = null;
		
		if (args.size() == 2) {
			target = Bukkit.getPlayer(args.get(1));
			
			if (target == null) {
				sender.sendMessage(ChatColor.RED + "No player by name '" + args.get(1) + "'");
				return;
			}
		} else if (sender instanceof Player) {
			target = (Player) sender;
		} else {
			sender.sendMessage(ChatColor.RED + "Console cannot have a power!");
			return;
		}
		
		PowerManager.user(target).setPower(power.get());
		String powerName = power.get().getDisplayName();
		if (target == sender) {
			target.sendMessage(ChatColor.GREEN + "You've set your power to '" + powerName + ChatColor.GREEN + "'");
		} else {
			target.sendMessage(ChatColor.GREEN + sender.getName() + " has set your power to '" + powerName + ChatColor.GREEN + "'");
		}
	}
	
	@Override
	public List<String> completeTab(CommandSender sender, List<String> args) {
		if (args.size() == 1) {
			return PowerManager.list((p) -> p.getName().startsWith(args.get(0)), Power::getName);
		} 
		
		return null;
	}

	@Override
	public int getMaxArgs() {
		return 2;
	}

	@Override
	public int getMinArgs() {
		return 1;
	}
}
