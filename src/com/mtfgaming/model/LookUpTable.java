/*
 * Please refer to the project's root folder to view the licence
 */
package com.mtfgaming.model;

import java.util.TreeMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Netsky
 */
public class LookUpTable {
    
    private String name;
    private final Map<String,String> table = new TreeMap();

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "entry")
    public Map<String, String> getTable() {
        return table;
    }

    public void addEntry(String key, String description) {
        this.table.put(key, description);
    }
    
    public void removeEntry(String key) {
        this.table.remove(key);
    }
    
    public String getEntry(String key) {
        return this.table.get(key);
    }
    
    
    
    
}
