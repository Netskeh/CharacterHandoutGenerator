/*
 * Please refer to the project's root folder to view the licence
 */
package com.mtfgaming.model;

/**
 *
 * @author Netsky
 */
public class GameType {
    
    private final String name;
    private final String db_name;
    
    public GameType(String name) {
        this.name = name;
        this.db_name = name.toLowerCase().trim().replace(' ', '_') + "_";
    }
    
    public String getName() {
        return name;
    }
    
    public String getDBName() {
        return db_name;
    }
    
}
