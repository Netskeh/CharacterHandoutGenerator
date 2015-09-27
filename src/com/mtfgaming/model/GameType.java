/*
 * Please refer to the project's root folder to view the licence
 */
package com.mtfgaming.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Netsky
 */
@XmlRootElement(name = "GameType")
public class GameType implements Comparator<GameType> {
    
    private String name;
    private String fileName;
    private final Map<String,LookUpTable> tables = new TreeMap();
    private final Set<CharacterType> characterTypes = new HashSet();
    private final List<Character> character = new ArrayList();
    private ObservableList<String> obsTableList;
    private ObservableList<String> obsCharTypeList;
    private ObservableList<String> obsCharList;
    
   
    @XmlAttribute
    public String getName() {
        return name;
    }
    
    @XmlAttribute
    public String getFileName() {
        return fileName;
    }
    
    public void setName(String name) {
        this.name = name;
        this.fileName = name.toLowerCase().trim().replace(' ', '_');
        this.character.stream().forEach(c -> c.setGameType(this));
    }
    
    @XmlElement
    public Map<String,LookUpTable> getTables() {
        return tables;
    }
    
    public void addTable(String name) {
        LookUpTable table = new LookUpTable();
        table.setName(name);
        tables.put(name,table);
        getTableObsList().add(name);
    }
    
    public void removeTable(String name) {
        if(tables.containsKey(name)) {
            tables.remove(name);
            getTableObsList().remove(name);
        }
    }
    
    public LookUpTable getTable(String name) {
        return tables.get(name);
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
    
    public String getEntry(String table, String key) {
        if(tables.containsKey(table)) {
            return this.tables.get(table).getEntry(key);
        }
        return null;
    }

    public void addCharacterType(CharacterType ct) {
        this.characterTypes.add(ct); 
        this.getCharTypeObsList().add(ct.getName());
    }
    
    public boolean removeCharacterType(CharacterType ct) {
        boolean canBeDeleted = true;
        for(Character c : character) {
            if(c.getCharacterType().equals(ct)) {
                canBeDeleted = false;
                break;
            }
        }
        if (canBeDeleted) {
            this.characterTypes.remove(ct);
            this.getCharTypeObsList().remove(ct.getName());
        }
        return canBeDeleted;
    }
    
    @XmlElement
    public Set<CharacterType> getCharacterTypes() {
        return characterTypes;
    }
    
    public CharacterType getCharacterType(String type) {
        for (CharacterType ct : characterTypes) {
            if (type.equals(ct.getName())) {
                return ct;
            }
        }
        return null;
    }
    
    public boolean addCharacter(Character c) {
        if (!character.contains(c)) {
            boolean sameName = true;
            while(sameName) {
                sameName = false;
                for (Character ch : character) {
                    if (ch.getName().equals(c.getName())) {
                        sameName = true;
                    }
                }
                if (sameName) {
                    c.setName(c.getName() + " - Copy");
                }
            }
            c.setGameType(this);
            this.character.add(c);
            this.getCharObsList().add(c.getName());
            return true;
        }
        return false;
    }
    
    public Character getCharacter(int i) {
        return this.character.get(i);
    }
    
    public void removeCharacter(Character c) {
        this.character.remove(c);
        this.getCharObsList().remove(c.getName());
    }
    
    public void removeCharacter(String name) {
        this.removeCharacter(this.getCharacter(name));
    }

    @XmlElement
    public List<Character> getCharacter() {
        return character;
    }
    
    public Character getCharacter(String name) {
        for (Character c : character) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public int compare(GameType gt1, GameType gt2) {
        return gt1.getName().compareTo(gt2.getName());
    }
    
    public ObservableList<String> getTableObsList() {
        if(obsTableList == null) {
            List<String> list = new ArrayList();
            this.getTables().keySet().stream().forEach(str -> list.add(str));
            obsTableList = FXCollections.observableList(list);
        }
        FXCollections.sort(obsTableList);
        return obsTableList;
    }
    
    public ObservableList<String> getCharTypeObsList() {
        if(obsCharTypeList == null) {
            List<String> list = new ArrayList();
            this.characterTypes.stream().forEach(ct -> list.add(ct.getName()));
            obsCharTypeList = FXCollections.observableList(list);
        }
        FXCollections.sort(obsCharTypeList);
        return obsCharTypeList;
    }
    
    public ObservableList<String> getCharObsList() {
        if(obsCharList == null) {
            List<String> list = new ArrayList();
            this.character.stream().forEach(c -> list.add(c.getName()));
            obsCharList = FXCollections.observableList(list);
        }
        FXCollections.sort(obsCharList);
        return obsCharList;
    }
    
    
    
}
