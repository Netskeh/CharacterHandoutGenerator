/*
 * Please refer to the project's root folder to view the licence
 */
package com.mtfgaming.view;

import com.mtfgaming.model.GameList;
import com.mtfgaming.model.GameType;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

/**
 *
 * @author Netsky
 */
public class MainScreen extends ListView implements Initializable {
    
    @FXML 
    private ListView<String> gameTypeList;
    private GameList gl = GameList.getInstance();
    private ObservableList<String> obsList;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<String> gameListNames = new ArrayList();
        gl.getGameList().stream().forEach(item -> gameListNames.add(item.getName()));
        obsList = FXCollections.observableList(gameListNames);
        gameTypeList.setItems(obsList);
    }
    
    @FXML 
    public void handleSelection() {
        String item = gameTypeList.getSelectionModel().getSelectedItem();
        if(item != null) {
            System.out.println(item);
        }
    }
    
    @FXML 
    public void handleGTAdd() {
        TextInputDialog dialog = new TextInputDialog("New Game Type");
        dialog.setTitle("New Game Type");
        dialog.setHeaderText("Create a new Game Type");
        dialog.setContentText("Please enter a new name:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            GameType gt = new GameType();
            gt.setName(result.get());
            gl.addGame(gt);
            obsList.add(result.get());
        }
    }
    
    @FXML 
    public void handleGTRename() {
        String item = gameTypeList.getSelectionModel().getSelectedItem();
        if(item != null) {
            TextInputDialog dialog = new TextInputDialog(item);
            dialog.setTitle("Rename Game Type");
            dialog.setHeaderText("Rename Game Type");
            dialog.setContentText("Please enter a new name:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                GameType gt = gl.getGame(item);
                gl.removeGame(gt);
                obsList.remove(item);
                gt.setName(result.get());
                obsList.add(result.get());
                gl.addGame(gt);
            }
        }
    }
    
    @FXML 
    public void handleGTDelete() {
        String item = gameTypeList.getSelectionModel().getSelectedItem();
        if(item != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Deleting " + item);
            alert.setHeaderText("You are about to delete a Game Type");
            alert.setContentText("Do you really want to delete " + item + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                obsList.remove(item);
                gl.removeGame(gl.getGame(item));
            }
        }
    }
    
    
}
