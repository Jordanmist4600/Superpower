package me.simplicitee.superpower.command;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.simplicitee.superpower.core.PowerManager;
import net.md_5.bungee.api.ChatColor;

public class PowerRemoveCommand extends CommandBase {

	public PowerRemoveCommand() {
		super("remove", "Remove the target's power!", "/superpower remove [target]", Arrays.asList("r", "clear"));
	}

	@Override
	protected void onExecute(CommandSender sender, List<String> args) {
		Player target = null;
		
		if (args.size() == 1) {
			target = Bukkit.getPlayer(args.get(0));
			
			if (target == null) {
				sender.sendMessage(ChatColor.RED + "No player by name '" + args.get(0) + "'");
				return;
			}
		} else if (sender instanceof Player) {
			target = (Player) sender;
		} else {
			sender.sendMessage(ChatColor.RED + "Player only command!");
			return;
		}
		
		PowerManager.user(target).setPower(null);
		if (target == sender) {
			target.sendMessage(ChatColor.GREEN + "You've removed your power!");
		} else {
			target.sendMessage(ChatColor.GREEN + sender.getName() + " has removed your power!");
		}
	}

	@Override
	public int getMaxArgs() {
		return 1;
	}

	@Override
	public int getMinArgs() {
		return 0;
	}

}
