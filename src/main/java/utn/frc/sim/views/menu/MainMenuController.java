package utn.frc.sim.views.menu;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


public class MainMenuController {


    @FXML
    private Pane mainPane;

    private static Logger logger = LogManager.getLogger(MainMenuController.class);

    @FXML
    void openListGeneratorDialog(ActionEvent event) {
        setListGeneratorDialog();
    }


    private void setListGeneratorDialog(){
        try {
            mainPane.getChildren().setAll((AnchorPane)FXMLLoader.load(getClass().getResource("/number-generator.fxml")));
        } catch (IOException e) {
            logger.error("Problem opening.",e);
        }
    }



}
