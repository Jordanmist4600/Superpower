package me.simplicitee.superpower.configuration;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public final class Configurator {

	private File file;
	private FileConfiguration config;
	private String header;
	
	public Configurator(File file) {
		this(file, null);
	}
	
	/**
	 * Create a configurator for the given file with a custom header
	 * @param file which file used for configuration
	 * @param header header text for the configuration file, null uses the default header
	 */
	public Configurator(File file, String header) {
		this.file = file;
		this.config = YamlConfiguration.loadConfiguration(file);
		this.header = header == null ? config.options().header() : header;
	}
	
	public File getFile() {
		return file;
	}
	
	public FileConfiguration getConfig() {
		return config;
	}
	
	/**
	 * Gets the header text for the configuration file, can be null
	 * @return header text, null if nothing
	 */
	public String getHeader() {
		return header;
	}
	
	/**
	 * Sets the header text for the configuration file, can be null
	 * @param header text at the top of the file, null for nothing
	 */
	public void setHeader(String header) {
		this.header = header;
	}
	
	public void save() {
		config.options().copyDefaults(true).header(header);
		
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			
			config.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
