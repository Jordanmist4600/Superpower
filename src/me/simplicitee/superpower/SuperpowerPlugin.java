package me.simplicitee.superpower;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import me.simplicitee.superpower.command.CommandManager;
import me.simplicitee.superpower.core.PowerManager;

@Plugin(name = "Superpower", version = "1.0.0")
@Description("Fun superpowers in minecraft!")
@Author("Simplicitee")
@Command(name = "superpower", desc = "Superpower command", aliases = {"super", "power", "sp"}, usage = "/<command>")
public class SuperpowerPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		CommandManager.init(this);
		PowerManager.init(this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static SuperpowerPlugin instance() {
		return JavaPlugin.getPlugin(SuperpowerPlugin.class);
	}
	
	public static File configurationFolder(String folder) {
		return new File(instance().getDataFolder() + File.pathSeparator + "/configuration/" + File.pathSeparator + "/" + folder + "/");
	}
}
