package me.simplicitee.superpower.storage;

import java.io.File;

import me.simplicitee.superpower.SuperpowerPlugin;
import me.simplicitee.superpower.configuration.Configurable;
import me.simplicitee.superpower.configuration.Configure;

public class DatabaseOptions implements Configurable {

	@Configure("Database.Type")
	private String type = "sqlite";
	
	@Configure("MySQL.Database")
	private String database = "minecraft";
	
	@Configure("MySQL.Host")
	private String host = "localhost";
	
	@Configure("MySQL.Port")
	private int port = 3306;
	
	@Configure("MySQL.Username")
	private String user = "root";
	
	@Configure("MySQL.Password")
	private String pass = "";
	
	public String getType() {
		return type;
	}
	
	public String getDatabase() {
		return database;
	}
	
	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getUsername() {
		return user;
	}
	
	public String getPassword() {
		return pass;
	}
	
	@Override
	public File getFile() {
		return new File(SuperpowerPlugin.instance().getDataFolder(), "/configuration/storage.yml");
	}
	
}
