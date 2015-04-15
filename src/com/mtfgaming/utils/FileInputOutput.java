/*
 * Please refer to the project's root folder to view the licence
 */
package com.mtfgaming.utils;

import characterhandoutgenerator.CharacterHandoutGenerator;
import com.mtfgaming.model.GameType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Netsky
 */
public class FileInputOutput {
    
    public List<GameType> loadGameTypeDataFromFile() {
        List<GameType> list = new ArrayList();
        
        try {
            JAXBContext context = JAXBContext.newInstance(GameType.class);
            Unmarshaller um = context.createUnmarshaller();
            Files.walk(Paths.get(this.getFilePath())).forEach(filePath -> {
                if (Files.isRegularFile(filePath) && Files.isReadable(filePath)) {
                    try {
                        System.out.println(filePath);
                        list.add((GameType) um.unmarshal(filePath.toFile()));
                    } catch (JAXBException ex) {
                        Logger.getLogger(FileInputOutput.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }   catch (IOException ex) {
            Logger.getLogger(FileInputOutput.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(FileInputOutput.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(FileInputOutput.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file");

            alert.showAndWait();
        }
        return list;
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file");

            alert.showAndWait();
        } 
    }
    
    
    private String getFilePath() {
        String pathString = System.getProperty("user.dir") + "\\data\\";
        Path path = Paths.get(pathString);
        if (Files.notExists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException ex) {
                Logger.getLogger(FileInputOutput.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return pathString;
    }
    
    private File getFilePath(String name) {
        String filePath = this.getFilePath() + name + ".xml";
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }
    
}
