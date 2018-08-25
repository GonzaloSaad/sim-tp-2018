package utn.frc.sim;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main-menu.fxml"));
        primaryStage.setTitle("UTN - FRC - SIM - Trabajo Practico");
        primaryStage.setScene(new Scene(root, 1016,566));
        primaryStage.setResizable(Boolean.FALSE);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
