/*
 * Please refer to the project's root folder to view the licence
 */
package com.mtfgaming.model;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Netsky
 */
@XmlRootElement(name = "GameType")
public class GameType {
    
    private String name;
    private final Map<String,LookUpTable> tables = new HashMap();
    
   
    @XmlAttribute
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @XmlElement
    public Map<String,LookUpTable> getTables() {
        return tables;
    }
    
    public void addTable(String name) {
        LookUpTable table = new LookUpTable();
        table.setName(name);
        tables.put(name,table);
    }
    
    public void removeTable(String name) {
        if(tables.containsKey(name)) {
            tables.remove(name);
        }
    }
    
    public void getTable(String name) {
        if(tables.containsKey(name)) {
            tables.get(name);
        }
    }
    
    public void addEntry(String table, String key, String description) {
        if(tables.containsKey(table)) {
            this.tables.get(table).addEntry(key, description);
        }
    }
    
    public void removeEntry(String table, String key) {
        if(tables.containsKey(table)) {
            this.tables.get(table).removeEntry(key);
        }
    }
    
    public void getEntry(String table, String key) {
        if(tables.containsKey(table)) {
            this.tables.get(table).getEntry(key);
        }
    }
    
}
