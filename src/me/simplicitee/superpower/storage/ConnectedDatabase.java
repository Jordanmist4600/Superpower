package me.simplicitee.superpower.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;

public class ConnectedDatabase {

	private Connection connection;
	private ServerType serverType;
	
	public ConnectedDatabase(Connection connection, ServerType serverType) {
		this.connection = connection;
		this.serverType = serverType;
	}
	
	public ServerType getServerType() {
		return serverType;
	}
	
	public PreparedStatement prepare(String query) {
		try {
			return connection.prepareStatement(query);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void close() {
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Process the given query and then apply the given function to the {@link ResultSet}. If the
	 * query is not some kind of read action, the {@link ResultSet} passed to the function will be null.
	 * @param <T> return type
	 * @param query database call to run
	 * @param operation function to apply to the result of the query
	 * @return the return of the given operation
	 */
	public <T> T query(PreparedStatement query, Function<ResultSet, T> operation) throws ExecutionException, InterruptedException {
		if (query == null || operation == null) {
			return null;
		}
		
		return async(query).thenApply(operation).get();
	}
	
	public <T> T query(String query, Function<ResultSet, T> operation) throws ExecutionException, InterruptedException {
		return query(prepare(query), operation);
	}
	
	/**
	 * Process the given query and then apply the given consumer to the {@link ResultSet}. If the
	 * query is not some kind of read action, the {@link ResultSet} passed to the function will be null.
	 * @param query database call to run
	 * @param operation function to apply to the result of the query
	 */
	public void query(PreparedStatement query, Consumer<ResultSet> operation) throws ExecutionException, InterruptedException {
		if (query == null || operation == null) {
			return;
		}
		
		async(query).thenAccept(operation).get();
	}
	
	public void query(String query, Consumer<ResultSet> operation) throws ExecutionException, InterruptedException {
		query(prepare(query), operation);
	}
	
	/**
	 * Process the given query, mostly useful for modify actions
	 * @param query database call to run
	 * @param replacements will be used in order to replace <code>?</code> in the query
	 */
	public void query(PreparedStatement query) throws ExecutionException, InterruptedException {
		if (query == null) {
			return;
		}
		
		async(query).get();
	}
	
	public void query(String query) throws ExecutionException, InterruptedException {
		query(prepare(query));
	}
	
	public boolean tableExists(String name) {
		try {
			return connection.getMetaData().getTables(null, null, name, null).next();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private CompletableFuture<ResultSet> async(PreparedStatement stmt) {
		return CompletableFuture.supplyAsync(() -> {
			ResultSet rs = null;
			
			try {
				if (stmt.execute()) {
					rs = stmt.getResultSet();
				}
				stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return rs;
		});
	}
}
