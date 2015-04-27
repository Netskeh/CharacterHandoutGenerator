/*
 * Please refer to the project's root folder to view the licence
 */
package com.mtfgaming.view;

import com.mtfgaming.model.GameList;
import com.mtfgaming.model.GameType;
import com.mtfgaming.model.LookUpTable;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 *
 * @author Netsky
 */
public class MainScreen extends ListView implements Initializable {
    
    @FXML private ListView<String> gameTypeList;
    @FXML private ListView<String> tableList;
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
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableName.setCellValueFactory(new MapValueFactory(LookUpTable.TABLE_KEY));
        tableDesc.setCellValueFactory(new MapValueFactory(LookUpTable.TABLE_VALUE));
        this.reloadGameList();
    }
    
    private void reloadGameList() {
        gameTypeList.setItems(gl.getObsList());
    }
    
    @FXML 
    public void handleGTSelection() {
        String item = gameTypeList.getSelectionModel().getSelectedItem();
        if(item != null && !item.equals(selectedGT)) {
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
            }
        }
    }
    
    private void loadGT() {
        tableList.setItems(gl.getGame(selectedGT).getObsList());
        this.tableAddEntryButton.setDisable(true);
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
            gl.getGame(selectedGT).getTable(selectedTable)
                    .addEntry(tableAddEntryKey.getText(), tableAddEntryValue.getText());
            tableAddEntryKey.setText("");
            tableAddEntryValue.setText("");
            this.reloadTable();
            gl.saveGame(selectedGT);
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
        gl.getGame(selectedGT).getTable(selectedTable).removeEntry(editedTableEntry);
        if(!name.isEmpty()) {
            gl.getGame(selectedGT).getTable(selectedTable).addEntry(name, desc);
        }
        this.reloadTable();
        gl.saveGame(selectedGT);
    }
    
    private void loadTable() {
        Callback<TableColumn<Map, String>, TableCell<Map, String>>
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
        lookupTable.setItems(gl.getGame(selectedGT).getTable(selectedTable).getObsList());
    }
    
    @FXML 
    public void handleTableAdd() {
        TextInputDialog dialog = new TextInputDialog("New Table");
        dialog.setTitle("New Table");
        dialog.setHeaderText("Create a new Lookup Table");
        dialog.setContentText("Please enter a new name:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            gl.getGame(selectedGT).addTable(result.get());
            gl.saveGame(selectedGT);
        }
    }
    
    @FXML 
    public void handleTableDelete() {
        String item = tableList.getSelectionModel().getSelectedItem();
        System.out.println(item);
        System.out.println(selectedTable);
        if(item != null && item.equals(selectedTable)) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Deleting " + item);
            alert.setHeaderText("You are about to delete a Table");
            alert.setContentText("Do you really want to delete " + item + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                gl.getGame(selectedGT).removeTable(item);
                gl.saveGame(selectedGT);
            }
        }
    }
    
}
