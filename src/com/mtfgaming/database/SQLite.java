/*
 * Please refer to the project's root folder to view the licence
 */
package com.mtfgaming.database;

import com.mtfgaming.model.GameType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLite implements I_DatabaseHandler {
	
    private static SQLite sqlite;
    private Connection conn;
    private String gameTypePrefix;

    private SQLite() {
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(DB_NAME);
            String sql = "CREATE TABLE IF NOT EXISTS " 
                    + GAME_TYPES_INDEX_TABLE 
                    + "(KEY TEXT PRIMARY KEY NOT NULL, ENTRY TEXT NOT NULL)";
            conn.createStatement().executeUpdate(sql);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SQLite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static SQLite getInstance() {
        if (sqlite == null) {
            sqlite = new SQLite();
        }
        return sqlite;
    }

    @Override
    public void close() {
        try {
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean switchToGame(GameType gt) {
        String result = null;
        String sql = "SELECT * FROM " 
                    + GAME_TYPES_INDEX_TABLE + 
                    " WHERE KEY = ?;";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, gt.getName());
            ResultSet rs = ps.executeQuery();
            while ( rs.next() ) {
                result = rs.getString("ENTRY");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (result != null) {
            this.gameTypePrefix = result;
            return true;
        }
        return false;
    }
    
    @Override
    public void addGame(GameType gt) {
        String sql = "INSERT INTO " + GAME_TYPES_INDEX_TABLE + " (KEY,ENTRY) " 
                + "VALUES (?, ?);"; 
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, gt.getName());
            ps.setString(2, gt.getDBName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void deleteGame(GameType gt) {
        String sql = "DELETE from " + GAME_TYPES_INDEX_TABLE 
                + " where KEY = ?;";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, gt.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public List<String> getGames() {
        List<String> list = new ArrayList<String>();
        String sql = "SELECT * FROM " + GAME_TYPES_INDEX_TABLE + ";";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while ( rs.next() ) {
                list.add(rs.getString("KEY"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void insert(String table, String key, String entry) {
        String sql = "INSERT INTO " + this.gameTypePrefix + table
                + " (KEY,ENTRY) " + "VALUES (?, ?);"; 
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, key);
            ps.setString(2, entry);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }	
    }

    @Override
    public void delete(String table, String key) {
        String sql = "DELETE from " + this.gameTypePrefix + table
                + " where KEY = ?;";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, key);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }		
    }

    @Override
    public void update(String table, String key, String entry) {
        String sql = "UPDATE " + this.gameTypePrefix + table
                + " set ENTRY = ? where KEY = ?;";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, key);
            ps.setString(2, entry);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String get(String table, String key) {
        String result = null;
        String sql = "SELECT * FROM " + this.gameTypePrefix + table 
                + " WHERE KEY = ?;";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, key);
            ResultSet rs = ps.executeQuery();
            while ( rs.next() ) {
                result = rs.getString("ENTRY");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void createTable(String name) {
        String sql = "CREATE TABLE " + this.gameTypePrefix + name
                + " (KEY TEXT PRIMARY KEY NOT NULL, ENTRY TEXT NOT NULL)"; 
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void removeTable(String name) {
        String sql = "DROP TABLE " + this.gameTypePrefix + name + ";"; 
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }		
    }

}
