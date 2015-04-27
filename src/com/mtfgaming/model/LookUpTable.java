/*
 * Please refer to the project's root folder to view the licence
 */
package com.mtfgaming.model;

import java.util.Comparator;
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
    
    public final static String TABLE_KEY = "Name";
    public final static String TABLE_VALUE = "Desc";
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
        Map<String, String> temp = new TreeMap<>();
        temp.put(TABLE_KEY, key);
        temp.put(TABLE_VALUE, description);
        getObsList().add(temp);
    }
    
    public void removeEntry(String key) {
        String desc = this.table.get(key);
        this.table.remove(key);
        Map<String, String> temp = new TreeMap<>();
        temp.put(TABLE_KEY, key);
        temp.put(TABLE_VALUE, desc);
        getObsList().remove(temp);
    }
    
    public String getEntry(String key) {
        return this.table.get(key);
    }
    
    public ObservableList<Map> getObsList() {
        if(obsList == null) {
            this.generateDataInMap();
        }
        FXCollections.sort(obsList, mapComparator);
        return obsList;
    }
    
    private ObservableList<Map> generateDataInMap() {
        obsList = FXCollections.observableArrayList();
        ObservableMap<String,String> map = FXCollections.observableMap(this.getTable());
        map.keySet().stream().map((item) -> {
            Map<String, String> dataRow = new TreeMap<>();
            String value1 = item;
            String value2 = map.get(item);
            dataRow.put(TABLE_KEY, value1);
            dataRow.put(TABLE_VALUE, value2);
            return dataRow; 
        }).forEach((dataRow) -> {
            obsList.add(dataRow);
        });
        return obsList;
    }
    
    private final Comparator<Map> mapComparator = (Map m1, Map m2) -> ((String)m1.get(TABLE_KEY)).compareTo((String)m2.get(TABLE_KEY));
        
}
