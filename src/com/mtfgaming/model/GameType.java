/*
 * Please refer to the project's root folder to view the licence
 */
package com.mtfgaming.model;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private String fileName;
    private final Map<String,LookUpTable> tables = new TreeMap();
    private final Set<CharacterType> characterTypes = new HashSet();
    private final List<Character> character = new ArrayList();
    
   
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
    
    public LookUpTable getTable(String name) {
        if(tables.containsKey(name)) {
             return tables.get(name);
        }
        return null;
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
    }
    
    public void removeCharacterType(CharacterType ct) {
        this.characterTypes.remove(ct);
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
    
    public void addCharacter(Character c) {
        c.setGameType(this.getName());
        this.character.add(c);
    }
    
    public Character getCharacter(int i) {
        return this.character.get(i);
    }
    
    public void removeCharacter(Character c) {
        this.character.remove(c);
    }

    @XmlElement
    public List<Character> getCharacter() {
        return character;
    }
    
    
    
}
