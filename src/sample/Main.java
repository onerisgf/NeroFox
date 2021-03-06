package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("NeroFox");
        primaryStage.setScene(new Scene(root, 880, 540));  //CRIO MEU PALCO COM RESOLUÇÃO ESPECIFICA
        primaryStage.setFullScreen(true); // DEFINO TRUE PARA FULLSCREEN AO INICIAR
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
