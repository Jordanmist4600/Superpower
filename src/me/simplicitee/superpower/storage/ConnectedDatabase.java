package me.simplicitee.superpower.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConnectedDatabase {

	private Connection connection;
	
	public ConnectedDatabase(Connection connection) {
		this.connection = connection;
	}

	public synchronized ResultSet query(String query) {
		ResultSet rs = null;
		
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			
			if (stmt.execute()) {
				rs = stmt.getResultSet();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rs;
	}
}
