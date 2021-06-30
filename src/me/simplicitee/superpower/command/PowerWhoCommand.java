package me.simplicitee.superpower.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.simplicitee.superpower.core.PowerManager;
import me.simplicitee.superpower.core.PowerUser;
import net.md_5.bungee.api.ChatColor;

public class PowerWhoCommand extends CommandBase {

	public PowerWhoCommand() {
		super("who", "Get information about a player!", "/superpower who [target]", Collections.singletonList("w"));
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
		
		PowerUser user = PowerManager.user(target);
		
		String power = (user.getPower() == null ? ChatColor.WHITE + "none" : user.getPower().getDisplayName());
		
		List<String> message = new ArrayList<>();
		message.add(target.getDisplayName() + ChatColor.GRAY + " (" + target.getName() + ")");
		message.add(ChatColor.DARK_GRAY + "- Power: " + power);
		
		sender.sendMessage(message.toArray(new String[0]));
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
