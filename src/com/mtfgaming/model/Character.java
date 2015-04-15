/*
 * Please refer to the project's root folder to view the licence
 */
package com.mtfgaming.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Netsky
 */
public class Character {
    
    private String name;
    private String type;
    private final Map<String,Integer> stats = new HashMap();
    private final Set<String> talents = new HashSet();
    private final Set<String> abilities = new HashSet();
    

    @XmlElement
    public String getType() {
        return type;
    }

    public void setType(CharacterType type) {
        this.type = type.getName();
        
        Set<String> oldKeysDel = this.stats.keySet();
        oldKeysDel.removeAll(type.getStats());

        for(String str : oldKeysDel) {
            stats.remove(str);
        }
        
        for(String str : type.getStats()) {
            stats.putIfAbsent(str,0);
        }
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @XmlElement
    public Set<String> getTalents() {
        return talents;
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
    
    @XmlElement
    public Set<String> getAbilities() {
        return abilities;
    }
    
    public void addAbility(String entry) {
        abilities.add(entry);
    }
    
    public void removeAbility(String entry) {
        abilities.remove(entry);
    }
    
    
}
