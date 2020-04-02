package patients;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) throws Exception{

        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/style/structure.fxml");

        loader.setLocation(xmlUrl);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/style/global_stylesheet.css").toExternalForm());

        stage.setScene(scene);
        InputStream iconStream = getClass().getResourceAsStream("/images/icon.png");
        Image image = new Image(iconStream);
        stage.getIcons().add(image);
        stage.setTitle("Registry service");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}