/*
 * Please refer to the project's root folder to view the licence
 */
package com.mtfgaming;

import com.mtfgaming.model.Character;
import com.mtfgaming.model.CharacterType;
import com.mtfgaming.model.GameList;
import com.mtfgaming.model.GameType;
import com.mtfgaming.utils.FileInputOutput;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;

/**
 *
 * @author Netsky
 */
public class CharacterHandoutGenerator extends Application {
    
    public static Stage primaryStage;
    GameList gl = GameList.getInstance();
    
        
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;        
        
        //System.out.println(gl.getGame("White Crusade").getCharacter(0).getTalents());
        /*FileInputOutput fio = new FileInputOutput();
        
        GameType gt = new GameType();
        gt.setName("Brown Crusade");
        gt.addTable("Talents");
        gt.addEntry("Talents", "first", "desc");
        gt.addEntry("Talents", "second", "descfgiusisg");
        gl.addGame(gt);
        
        CharacterType ct = new CharacterType();
        ct.setName("NPC");
        ct.addStat("Weapon Skill",50);
        ct.addTalent("Die Hard");
        ct.addTalent("Leap Up");
        gt.addCharacterType(ct);
        
        Character ch = new Character();
        ch.setName("Belix");
        ch.setCharacterType(ct);
        ch.setStat("Weapon Skill", 50);
        ch.addTalent("Bananaking");
        ch.addTalent("Zynisch");
        gt.addCharacter(ch);
        
        fio.saveGameTypeDataToFile(gt);*/
        
        createInterface();
        
        /*List<GameType> list = fio.loadGameTypeDataFromFile();
        list.stream().forEach(gtle -> System.out.println(gtle));*/
        //System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private void createInterface() {
        
        Control rootLayout = null;
        try {
            rootLayout = FXMLLoader.load(CharacterHandoutGenerator.class.getResource("view/MainScreen.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(CharacterHandoutGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Scene scene = new Scene(rootLayout);
        
        primaryStage.setTitle("Character Handout Generator");
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }
    
    
}
