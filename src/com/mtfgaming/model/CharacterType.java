/*
 * Please refer to the project's root folder to view the licence
 */
package com.mtfgaming.model;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Netsky
 */
public class CharacterType {
    
    private String name;
    private final Set<String> talents = new TreeSet();
    private final Map<String,Integer> stats = new HashMap();

    
    @XmlElement
    public Map<String,Integer> getStats() {
        return stats;
    }
    
    public void addStat(String name,int value) {
        stats.put(name, value);
    }
    
    public void removeStat(String name) {
        stats.remove(name);
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

    public void addTalent(String talent) {
        this.talents.add(talent);
    }
    
    public void removeTalent(String talent) {
        if (talents.contains(talent)) {
            this.talents.remove(talent);
        }
    }
       
    
    
    
}
