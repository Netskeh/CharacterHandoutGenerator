/*
 * Please refer to the project's root folder to view the licence
 */
package com.mtfgaming.model;

import com.mtfgaming.utils.FileInputOutput;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Netsky
 */
public class GameList {
    
    private static GameList gl;
    private final List<GameType> list;
    
    private GameList() {
        FileInputOutput fio = new FileInputOutput();
        list = fio.loadGameTypeDataFromFile();
        Collections.sort(list, new GameType());
    }
    
    public static GameList getInstance() {
        if (gl == null) {
            gl = new GameList();
        }
        return gl;
    }
    
    public List<GameType> getGameList() {
        return list;
    }
    
    public void addGame(GameType gt) {
        list.add(gt);
    }
    
    public void removeGame(GameType gt) {
        list.remove(gt);
    }
    
    public GameType getGame(String str) {
        for (GameType gt : list) {
            if (gt.getName().equals(str)) {
                return gt;
            }
        }
        return null;
    }
    
}
