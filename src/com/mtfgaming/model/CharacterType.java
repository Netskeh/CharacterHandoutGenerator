/*
 * Please refer to the project's root folder to view the licence
 */
package com.mtfgaming.model;

import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Netsky
 */
public class CharacterType {
    
    private String name;
    private final Set<String> talents = new HashSet();
    private final Set<String> stats = new HashSet();

    
    @XmlElement
    public Set<String> getStats() {
        return stats;
    }
    
    public void addStat(String name) {
        stats.add(name);
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
