/*
 * Please refer to the project's root folder to view the licence
 */
package characterhandoutgenerator;

import com.mtfgaming.database.I_DatabaseHandler;
import com.mtfgaming.database.SQLite;
import com.mtfgaming.model.GameType;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Netsky
 */
public class CharacterHandoutGenerator extends Application {
    
    private I_DatabaseHandler db;
    
    @Override
    public void start(Stage primaryStage) {    
        db = SQLite.getInstance();
        GameType gt = new GameType("Black Crusade");
        /*db.addGame(gt);
        db.switchToGame(gt);
        db.createTable("Talents");
        createInterface(primaryStage);*/
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private void createInterface(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(25);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
                
        Scene scene = new Scene(grid, 300, 275);
        
        primaryStage.setTitle("Character Handout Generator");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Content:");
        grid.add(pw, 0, 2);

        TextArea contentField = new TextArea();
        grid.add(contentField, 1, 2);
        
        
        Button button = new Button("Accept");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
                button.setText("Accepted");
                db.insert("Talents", userTextField.getText(), contentField.getText());
            }
        });
        grid.add(button, 1, 3);
        
    }
    
}
