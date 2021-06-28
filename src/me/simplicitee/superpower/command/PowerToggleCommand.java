package me.simplicitee.superpower.command;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.simplicitee.superpower.core.PowerManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class PowerToggleCommand extends CommandBase {
	
	private static final TextComponent ON = new TextComponent(ChatColor.AQUA + "!> Power Toggled " + ChatColor.GREEN + "ON" + ChatColor.AQUA  + "<!");
	private static final TextComponent OFF = new TextComponent(ChatColor.AQUA + "!> Power Toggled " + ChatColor.RED + "OFF" + ChatColor.AQUA  + "<!");

	public PowerToggleCommand() {
		super("toggle", "Toggle the target's power on or off!", "/superpower toggle [target]", Collections.singletonList("t"));
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
		
		target.spigot().sendMessage(PowerManager.user(target).toggle() ? ON : OFF);
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
