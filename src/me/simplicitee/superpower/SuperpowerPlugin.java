package me.simplicitee.superpower;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

public class SuperpowerPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		
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
