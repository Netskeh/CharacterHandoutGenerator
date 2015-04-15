/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mtfgaming.model;


import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Netsky
 */
@XmlRootElement(namespace = "com.mtfgaming.model")
public class GameTypeWrapper {
    
    private List<GameType> gt;
    
    @XmlElement(name = "GameType", type=GameType.class)
    public List<GameType> getGameTypes() {
        return gt;
    }
    
    public void setGameTypes(List<GameType> gt) {
        this.gt = gt;
    }
}
