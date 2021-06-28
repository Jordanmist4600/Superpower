package me.simplicitee.superpower.storage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import me.simplicitee.superpower.SuperpowerPlugin;

public final class Database {
	
	public static ConnectedDatabase connect(DatabaseOptions o) {
		if (o.getType().equalsIgnoreCase("mysql")) {
			return connectMySQL(o.getHost(), o.getPort(), o.getDatabase(), o.getUsername(), o.getPassword());
		} else if (o.getType().equalsIgnoreCase("sqlite")) {
			return connectSQLite("projectkorra", SuperpowerPlugin.instance().getDataFolder());
		}
		
		return null;
	}
	
	public static ConnectedDatabase connectMySQL(String host, int port, String database, String user, String pass) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			Connection c = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, pass);
			
			if (c != null && !c.isClosed()) {
				return new ConnectedDatabase(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static ConnectedDatabase connectSQLite(String database, File folder) {
		try {
			if (!folder.exists()) {
				folder.mkdirs();
			}
			
			Class.forName("org.sqlite.JDBC");
			
			Connection c = DriverManager.getConnection("jdbc:sqlite:" + new File(folder, database + ".db").getAbsolutePath());
			
			if (c != null && !c.isClosed()) {
				return new ConnectedDatabase(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
