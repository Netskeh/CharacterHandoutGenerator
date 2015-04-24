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
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Netsky
 */
public class Character {
    
    private String name;
    private String characterTypeName;
    @XmlTransient
    private CharacterType characterType;
    private String gameTypeName;
    @XmlTransient
    private GameType gameType;
    private final Map<String,Integer> stats = new HashMap();
    private final Set<String> talents = new TreeSet();
    private String text;
    
    
    @XmlElement
    public String getGameTypeName() {
        return gameTypeName;
    }

    private void setGameTypeName(String str) {
        this.gameTypeName = str;
    }
    
    public void setGameType(GameType gt) {
        this.setGameTypeName(gt.getName());
        this.gameType = gt;
    }
    
    @XmlTransient
    public GameType getGameType() {
        if (gameType == null) {
            this.setGameType(gameTypeName);
        }
        return gameType;
    }
    
    public void setGameType(String gt) {
        this.setGameType(GameList.getInstance().getGame(gt));
    }
    
    @XmlElement
    public String getCharacterTypeName() {
        return characterTypeName;
    }
    
    private void setCharacterTypeName(String type) {
        this.characterTypeName = type;
    }

    public void setCharacterType(CharacterType type) {
        this.setCharacterTypeName(type.getName());
        this.characterType = type;
        
        Set<String> oldKeysDel = this.stats.keySet();
        oldKeysDel.removeAll(type.getStats().keySet());

        oldKeysDel.stream().forEach((str) -> stats.remove(str));
        type.getStats().forEach((str, value) -> stats.putIfAbsent(str,value));
    }
    
    public void setCharacterType(String type) {
        this.setCharacterType(this.getGameType().getCharacterType(type));
    }
    
    @XmlTransient
    public CharacterType getCharacterType() {
        if (characterType == null) {
            this.setCharacterType(characterTypeName);
        }
        return characterType;
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
        allTalents.addAll(characterType.getTalents());
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
    
    @XmlElement
    public String getOwnText() {
        return text;
    }
    
    public String getText() {
        return text + "\n" + characterType.getText();
    }

    public void setText(String text) {
        this.text = text;
    }
    
    
}
