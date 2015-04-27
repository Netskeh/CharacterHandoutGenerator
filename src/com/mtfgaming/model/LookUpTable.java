/*
 * Please refer to the project's root folder to view the licence
 */
package com.mtfgaming.model;

import java.util.TreeMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Netsky
 */
public class LookUpTable {
    
    private String name;
    private final Map<String,String> table = new TreeMap();
    private ObservableList<Map> obsList;

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
    
    public ObservableList<Map> getObsList() {
        if(obsList == null) {
            this.generateDataInMap();
        }
        return obsList;
    }
    
    private ObservableList<Map> generateDataInMap() {
        obsList = FXCollections.observableArrayList();
        ObservableMap<String,String> map = FXCollections.observableMap(this.getTable());
        map.keySet().stream().map((item) -> {
            Map<String, String> dataRow = new TreeMap<>();
            String value1 = item;
            String value2 = map.get(item);
            dataRow.put("Name", value1);
            dataRow.put("Desc", value2);
            return dataRow; 
        }).forEach((dataRow) -> {
            obsList.add(dataRow);
        });
        return obsList;
    }
    
    
}
