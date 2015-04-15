/*
 * Please refer to the project's root folder to view the licence
 */
package characterhandoutgenerator;

import com.mtfgaming.model.Character;
import com.mtfgaming.model.CharacterType;
import com.mtfgaming.model.GameType;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Netsky
 */
public class CharacterHandoutGenerator extends Application {
    
    Stage primaryStage;
    
        
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        GameType gt = new GameType();
        gt.setName("Black Crusade");
        gt.addTable("Talents");
        gt.addEntry("Talents", "first", "desc");
        gt.addEntry("Talents", "second", "descfgiusisg");
        CharacterType ct = new CharacterType();
        ct.setName("NPC");
        ct.addStat("Weapon Skill");
        ct.addTalent("Die Hard");
        ct.addTalent("Leap Up");
        gt.addCharacterType(ct);
        Character ch = new Character();
        ch.setName("Belix");
        ch.setType(ct);
        ch.setStat("Weapon Skill", 50);
        gt.addCharacter(ch);
        
        createInterface();
        
        this.saveGameTypeDataToFile(gt);
        
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private void createInterface() {
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
                
            }
        });
        grid.add(button, 1, 3);
        
    }
    
    
    public void loadGameTypeDataFromFile(GameType gt) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(GameType.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            GameType gtnew = (GameType) um.unmarshal(this.getFilePath(gt.getFileName()));

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file");

            alert.showAndWait();
        }
    }

    public void saveGameTypeDataToFile(GameType gt) {
            JAXBContext context;
        try {
            context = JAXBContext.newInstance(GameType.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Marshalling and saving XML to the file.
            m.marshal(gt, getFilePath(gt.getFileName()));
        } catch (JAXBException ex) {
            Logger.getLogger(CharacterHandoutGenerator.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file");

            alert.showAndWait();
        } 
    }
    
    public File getFilePath(String name) {
        String filePath = System.getProperty("user.dir") + "\\" + name + ".xml";
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }
    
}
