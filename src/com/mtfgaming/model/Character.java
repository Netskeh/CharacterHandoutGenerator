/*
 * Please refer to the project's root folder to view the licence
 */
package com.mtfgaming.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Netsky
 */
public class Character {
    
    private String name;
    private String characterType;
    private String gameType;
    private final Map<String,Integer> stats = new HashMap();
    private final Set<String> talents = new TreeSet();
    
    
    @XmlElement
    public String getGameType() {
        return gameType;
    }

    public void setGameType(String str) {
        this.gameType = str;
    }
    
    @XmlElement
    public String getCharacterType() {
        return characterType;
    }
    
    private void setCharacterType(String type) {
        this.characterType = type;
    }

    public void setCharacterType(CharacterType type) {
        this.setCharacterType(type.getName());
        
        Set<String> oldKeysDel = this.stats.keySet();
        oldKeysDel.removeAll(type.getStats().keySet());

        oldKeysDel.stream().forEach((str) -> stats.remove(str));
        type.getStats().forEach((str, value) -> stats.putIfAbsent(str,value));
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @XmlElement
    public Set<String> getOwnTalents() {
        return talents;
    }
    
    public Set<String> getTalents() {
        Set<String> allTalents = new TreeSet();
        allTalents.addAll(getOwnTalents());
        allTalents.addAll(GameList.getInstance().getGame(gameType).getCharacterType(characterType).getTalents());
        return allTalents;
    }
    
    public void addTalent(String entry) {
        talents.add(entry);
    }
    
    public void removeTalent(String entry) {
        talents.remove(entry);
    }
    
    public Integer getStat(String name) {
        return stats.get(name);
    }
    
    @XmlElement
    public Map<String,Integer> getStats() {
        return stats;
    }
    
    public void setStat(String name, int value) {
        stats.put(name, value);
    }
    
    
}
