package me.simplicitee.superpower.configuration;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public final class Configurator {
	
	private static final Map<Configurable, Configurator> CACHE = new HashMap<>();

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
	
	/**
	 * Gets the cached {@link Configurator} for the {@link Configurable}
	 * object, or creates and caches a new one if nonexistent and returns that instead.
	 * @param obj Object to be configured
	 * @return configurator of the given configurable object
	 */
	public static Configurator from(Configurable obj) {
		return CACHE.computeIfAbsent(obj, (o) -> new Configurator(o.getFile()));
	}
	
	/**
	 * Processes the given {@link Configurable} object, creating defaults
	 * for and setting any fields with the {@link Configure} annotation in
	 * the object to their respective values from the configuration file.
	 * @param obj configurable object to process
	 * @return the original object but configured
	 */
	public static <T extends Configurable> T process(T obj) {
		FileConfiguration config = from(obj).getConfig();
		
		for (Field field : obj.getClass().getDeclaredFields()) {
			if (!field.isAnnotationPresent(Configure.class)) {
				continue;
			}
			
			String path = field.getAnnotation(Configure.class).value();
			
			try {
				if (!config.contains(path)) {
					config.addDefault(path, field.get(obj));
				} else {
					field.set(obj, config.get(path));
				}
			} catch (Exception e) {
				continue;
			}
		}
		
		from(obj).save();
		
		return obj;
	}
}
