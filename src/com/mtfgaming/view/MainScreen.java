/*
 * Please refer to the project's root folder to view the licence
 */
package com.mtfgaming.view;

import com.mtfgaming.model.GameList;
import com.mtfgaming.model.GameType;
import com.mtfgaming.model.LookUpTable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    
    @FXML 
    private ListView<String> gameTypeList;
    @FXML 
    private ListView<String> tableList;
    @FXML
    private TableView<Map> lookupTable;
    @FXML
    private TableColumn<Map, String> tableName;
    @FXML
    private TableColumn<Map, String> tableDesc;
    
    private final GameList gl = GameList.getInstance();
    
    private ObservableList<String> obsListGT;
    private ObservableList<String> obsListTables;
    private String selectedGT;
    private String selectedTable;
    private String editedTableEntry;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<String> gameListNames = new ArrayList();
        gl.getGameList().stream().forEach(item -> gameListNames.add(item.getName()));
        obsListGT = FXCollections.observableList(gameListNames);
        gameTypeList.setItems(obsListGT);
        tableName.setCellValueFactory(new MapValueFactory("Name"));
        tableDesc.setCellValueFactory(new MapValueFactory("Desc"));
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
            obsListGT.add(result.get());
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
                obsListGT.remove(item);
                gt.setName(result.get());
                obsListGT.add(result.get());
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
                obsListGT.remove(item);
                gl.removeGame(gl.getGame(item));
            }
        }
    }
    
    private void loadGT() {
        List<String> list = new ArrayList();
        gl.getGame(selectedGT).getTables().keySet().stream().forEach(str -> list.add(str));
        obsListTables = FXCollections.observableList(list);
        tableList.setItems(obsListTables);
    }
    
    @FXML 
    public void handleTableSelection() {
        String item = tableList.getSelectionModel().getSelectedItem();
        if(item != null && !item.equals(selectedTable)) {
            this.selectedTable = item;
            this.loadTable();
        }
    }
    
    @FXML 
    public void handleTableEditStart() {
        this.editedTableEntry = (String) lookupTable.getSelectionModel().getSelectedItem().get("Name");
    }
    
    @FXML 
    public void handleTableEditCommit() {
        String name = (String) lookupTable.getSelectionModel().getSelectedItem().get("Name");
        String desc = (String) lookupTable.getSelectionModel().getSelectedItem().get("Desc");
        System.out.println(name + " " + desc);
        gl.getGame(selectedGT).getTable(selectedTable).removeEntry(editedTableEntry);
        if(!name.isEmpty()) {
            gl.getGame(selectedGT).getTable(selectedTable).addEntry(name, desc);
        }
        gl.saveGame(selectedGT);
        this.reloadTable();
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
        lookupTable.setItems(generateDataInMap());
    }
    
    private ObservableList<Map> generateDataInMap() {
        ObservableList<Map> allData = FXCollections.observableArrayList();
        ObservableMap<String,String> map = FXCollections.observableMap(gl.getGame(selectedGT).getTable(selectedTable).getTable());
        map.keySet().stream().map((name) -> {
            Map<String, String> dataRow = new TreeMap<>();
            String value1 = name;
            String value2 = map.get(name);
            dataRow.put("Name", value1);
            dataRow.put("Desc", value2);
            return dataRow; 
        }).forEach((dataRow) -> {
            allData.add(dataRow);
        });
        return allData;
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
            obsListTables.add(result.get());
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
                obsListTables.remove(item);
                gl.getGame(selectedGT).removeTable(item);
                gl.saveGame(selectedGT);
            }
        }
    }
    
}
