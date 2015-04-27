/*
 * Please refer to the project's root folder to view the licence
 */
package com.mtfgaming.model;

import com.mtfgaming.utils.FileInputOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Netsky
 */
public class GameList {
    
    private static GameList gl;
    private final List<GameType> list;
    private final FileInputOutput fio;
    private ObservableList<String> obsList;
    
    private GameList() {
        fio = new FileInputOutput();
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
        getObsList().add(gt.getName());
        this.saveGame(gt);
    }
    
    public void removeGame(GameType gt) {
        list.remove(gt);
        getObsList().remove(gt.getName());
        fio.deleteGameTypeData(gt);
    }
    
    public GameType getGame(String str) {
        for (GameType gt : list) {
            if (gt.getName().equals(str)) {
                return gt;
            }
        }
        return null;
    }
    
    public void saveGame(String name) {
        fio.saveGameTypeDataToFile(this.getGame(name));
    }
    
    public void saveGame(GameType gt) {
        fio.saveGameTypeDataToFile(gt);
    }
    
    public ObservableList<String> getObsList() {
        if(obsList == null) {
            List<String> gameListNames = new ArrayList();
            getGameList().stream().forEach(item -> gameListNames.add(item.getName()));
            obsList = FXCollections.observableList(gameListNames);
        }
        FXCollections.sort(obsList);
        return obsList;
    }
    
}
