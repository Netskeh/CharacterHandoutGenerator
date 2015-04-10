/*
 * Please refer to the project's root folder to view the licence
 */
package com.mtfgaming.database;

import com.mtfgaming.model.GameType;

public interface I_DatabaseHandler {

	final static String DRIVER = "org.sqlite.JDBC";
	final static String DB_NAME = "jdbc:sqlite:test.db"; 
        final static String GAME_TYPES_INDEX_TABLE = "GameTypes";
	
	
	public void close();
        public boolean switchToGame(GameType gt);
        public void addGame(GameType gt);
        public void deleteGame(GameType gt);
	
	public void insert(String table, String key, String entry);
	public void delete(String table, String key);
	public void update(String table, String key, String entry);
	public String get(String table, String key);
	
	public void createTable(String name);
	public void removeTable(String name);
}
