/*
 * Please refer to the project's root folder to view the licence
 */
package com.mtfgaming.view;

import com.mtfgaming.model.Character;
import com.mtfgaming.model.CharacterType;
import com.mtfgaming.model.GameList;
import com.mtfgaming.model.GameType;
import com.mtfgaming.model.LookUpTable;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 *
 * @author Netsky
 */
public class MainScreen extends ListView implements Initializable {
    
    @FXML private ListView<String> gameTypeList;
    @FXML private ListView<String> tableList;
    @FXML private ListView<String> charTypeList;
    @FXML private ListView<String> charList;
    @FXML private TableView<Map> lookupTable;
    @FXML private TableColumn<Map, String> tableName;
    @FXML private TableColumn<Map, String> tableDesc;
    @FXML private TextField tableAddEntryKey;
    @FXML private TextField tableAddEntryValue;
    @FXML private Button tableAddEntryButton;
    
    private final GameList gl = GameList.getInstance();
    
    private String selectedGT;
    private String selectedTable;
    private String editedTableEntry;
    private String selectedCT;
    private String selectedChar;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableName.setCellValueFactory(new MapValueFactory(LookUpTable.TABLE_KEY));
        tableDesc.setCellValueFactory(new MapValueFactory(LookUpTable.TABLE_VALUE));
        this.reloadGameList();
        if(!gl.getObsList().isEmpty()) {
            gameTypeList.getSelectionModel().selectFirst();
            handleGTSelection();
        }
    }
    
    private void reloadGameList() {
        gameTypeList.setItems(gl.getObsList());
    }
    
    @FXML 
    public void handleGTSelection() {
        String item = gameTypeList.getSelectionModel().getSelectedItem();
        if(item != null) {
            this.selectedGT = item;
            this.loadGT();
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
            this.reloadGameList();
            gameTypeList.getSelectionModel().select(gt.getName());
            this.handleGTSelection();
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
                gt.setName(result.get());
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
                gl.removeGame(gl.getGame(item));
                this.reloadGameList();
                gameTypeList.getSelectionModel().selectFirst();
                handleGTSelection();
            }
        }
    }
    
    private void loadGT() {
        if (getSelectedGame() != null) {
            tableList.setItems(this.getSelectedGame().getTableObsList());
            this.tableAddEntryButton.setDisable(true);
            charTypeList.setItems(this.getSelectedGame().getCharTypeObsList());
            charList.setItems(this.getSelectedGame().getCharObsList());
        }
    }
    
    @FXML 
    public void handleTableSelection() {
        String item = tableList.getSelectionModel().getSelectedItem();
        if(item != null && !item.equals(selectedTable)) {
            this.tableAddEntryButton.setDisable(false);
            this.selectedTable = item;
            this.loadTable();
        }
    }
    
    @FXML 
    public void handleTableAddEntry() {
        if(!tableAddEntryKey.getText().isEmpty() && !tableAddEntryKey.getText().trim().equals("")) {
            this.getSelectedGame().getTable(selectedTable)
                    .addEntry(tableAddEntryKey.getText(), tableAddEntryValue.getText());
            tableAddEntryKey.setText("");
            tableAddEntryValue.setText("");
            this.reloadTable();
            gl.saveGame(getSelectedGame());
        }
    }
    
    @FXML 
    public void handleTableEditStart() {
        this.editedTableEntry = (String) lookupTable.getSelectionModel().getSelectedItem().get(LookUpTable.TABLE_KEY);
    }
    
    @FXML 
    public void handleTableEditCommit() {
        String name = (String) lookupTable.getSelectionModel().getSelectedItem().get(LookUpTable.TABLE_KEY);
        String desc = (String) lookupTable.getSelectionModel().getSelectedItem().get(LookUpTable.TABLE_VALUE);
        System.out.println(name + " " + desc);
        this.getSelectedGame().getTable(selectedTable).removeEntry(editedTableEntry);
        if(!name.isEmpty()) {
            this.getSelectedGame().getTable(selectedTable).addEntry(name, desc);
        }
        this.reloadTable();
        gl.saveGame(getSelectedGame());
    }
    
    private void loadTable() {
        Callback<TableColumn<Map, String>, TableCell<Map, String>> cellFactoryForMap;
        cellFactoryForMap = new Callback<TableColumn<Map, String>,TableCell<Map, String>>() {
            @Override
            public TableCell call(TableColumn p) {
                return new TextFieldTableCell(new StringConverter() {
                    @Override
                    public String toString(Object t) {
                        return t.toString();
                    }
                    @Override
                    public Object fromString(String string) {
                        return string;
                    }                                    
                });
            }
        };
        tableName.setCellFactory(cellFactoryForMap);
        tableDesc.setCellFactory(cellFactoryForMap);
        this.reloadTable();
    }
    
    private void reloadTable() {
        lookupTable.setItems(this.getSelectedGame().getTable(selectedTable).getObsList());
    }
    
    @FXML 
    public void handleTableAdd() {
        if (hasSelectedGame()) {
            TextInputDialog dialog = new TextInputDialog("New Table");
            dialog.setTitle("New Table");
            dialog.setHeaderText("Create a new Lookup Table");
            dialog.setContentText("Please enter a new name:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                this.getSelectedGame().addTable(result.get());
                gl.saveGame(getSelectedGame());
            }
        } else {
            this.noGameSelectedAlert();
        }    
    }
    
    @FXML 
    public void handleTableDelete() {
        if (hasSelectedGame()) {    
            String item = tableList.getSelectionModel().getSelectedItem();
            if(item != null && item.equals(selectedTable)) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Deleting " + item);
                alert.setHeaderText("You are about to delete a Table");
                alert.setContentText("Do you really want to delete " + item + "?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    this.getSelectedGame().removeTable(item);
                    gl.saveGame(getSelectedGame());
                }
            }
        } else {
            this.noGameSelectedAlert();
        }    
    }
    
    @FXML 
    public void handleCharTypeSelection() {
        String item = charTypeList.getSelectionModel().getSelectedItem();
        if(item != null) {
            this.selectedCT = item;
            //this.loadCharType();
        }
    }
    
    @FXML 
    public void handleCharSelection() {
        String item = charList.getSelectionModel().getSelectedItem();
        if(item != null) {
            this.selectedChar = item;
            //this.loadCharType();
        }
    }
    
    @FXML 
    public void handleCTAdd() {
        if (hasSelectedGame()) {
            TextInputDialog dialog = new TextInputDialog("New Character Type");
            dialog.setTitle("New Character Type");
            dialog.setHeaderText("Create a new Character Type");
            dialog.setContentText("Please enter a new name:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                CharacterType ct = new CharacterType();
                ct.setName(result.get());
                this.getSelectedGame().addCharacterType(ct);
                gl.saveGame(getSelectedGame());
            }
        } else {
            this.noGameSelectedAlert();
        }
    }
    
    @FXML 
    public void handleCTDelete() {
        if (hasSelectedGame()) {
            String item = charTypeList.getSelectionModel().getSelectedItem();
            if(item != null && item.equals(selectedCT)) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Deleting " + item);
                alert.setHeaderText("You are about to delete a Character Type");
                alert.setContentText("Do you really want to delete " + item + "?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    this.getSelectedGame().removeTable(item);
                    gl.saveGame(getSelectedGame());
                }
            }
        } else {
            this.noGameSelectedAlert();
        }
    }
    
    @FXML 
    public void handleCharAdd() {
        if (hasSelectedGame()) {
            if(this.getSelectedGame().getCharacterTypes() != null && !this.getSelectedGame().getCharacterTypes().isEmpty()) {

                Dialog dialog = new Dialog();
                dialog.setTitle("New Character");
                dialog.setHeaderText("Create a new Character");

                ButtonType createButtonType = new ButtonType("Create", ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

                TextField charName = new TextField();
                charName.setPromptText("Character Name");
                ChoiceBox charType = new ChoiceBox(this.getSelectedGame().getCharTypeObsList());
                charType.getSelectionModel().select(0);

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                grid.add(new Label("Please enter a new name:"), 0, 0);
                grid.add(charName, 1, 0);
                grid.add(new Label("Select a Character Type:"), 0, 1);
                grid.add(charType, 1, 1);

                Node loginButton = dialog.getDialogPane().lookupButton(createButtonType);
                loginButton.setDisable(true);

                charName.textProperty().addListener((observable, oldValue, newValue) -> loginButton.setDisable(newValue.trim().isEmpty()));

                dialog.getDialogPane().setContent(grid);

                Optional result = dialog.showAndWait();

                if (result.isPresent()) {
                    Character c = new Character();
                    c.setName(charName.getText());
                    c.setGameType(getSelectedGame());
                    c.setCharacterType(charType.getSelectionModel().getSelectedItem().toString());
                    getSelectedGame().addCharacter(c);
                    gl.saveGame(getSelectedGame());
                }
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("New Character");
                alert.setHeaderText("Please create a Character Type first");
                alert.showAndWait();
            }
        } else {
            this.noGameSelectedAlert();
        }
    }
    
    @FXML 
    public void handleCharDelete() {
        if (hasSelectedGame()) {
            String item = charList.getSelectionModel().getSelectedItem();
            if(item != null && item.equals(selectedChar)) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Deleting " + item);
                alert.setHeaderText("You are about to delete a Character");
                alert.setContentText("Do you really want to delete " + item + "?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    this.getSelectedGame().removeCharacter(item);
                    gl.saveGame(getSelectedGame());
                }
            }   
        } else {
            this.noGameSelectedAlert();
        }        
    }
    
    private GameType getSelectedGame() {
        if(selectedGT != null) {
            return gl.getGame(selectedGT);
        }
        System.out.println("GAME NOT FOUND: " + selectedGT);
        return null;
    }
    
    private boolean hasSelectedGame() {
        return getSelectedGame() != null;
    }
    
    private void noGameSelectedAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("No Context");
        alert.setHeaderText("Please select or create a Game Type first");
        alert.showAndWait();
    }
    
}
