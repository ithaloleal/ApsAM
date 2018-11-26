package projeto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by ithalo on 26/11/2018.
 */
public class SelecionaImagem  extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Aps Maicon");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
