package me.simplicitee.superpower;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.simplicitee.superpower.command.CommandManager;
import me.simplicitee.superpower.core.ActivationListener;
import me.simplicitee.superpower.core.PowerManager;

public class SuperpowerPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		CommandManager.init(this);
		
		if (!PowerManager.init(this)) {
			Bukkit.getPluginManager().disablePlugin(this);
		}
		
		getServer().getPluginManager().registerEvents(new ActivationListener(), this);
	}
	
	@Override
	public void onDisable() {
		PowerManager.disable();
	}
	
	public static SuperpowerPlugin instance() {
		return JavaPlugin.getPlugin(SuperpowerPlugin.class);
	}
	
	public static File configurationFolder(String folder) {
		return new File(instance().getDataFolder() + "/configuration/" + folder + "/");
	}
}
