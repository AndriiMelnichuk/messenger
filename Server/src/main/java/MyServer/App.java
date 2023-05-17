package MyServer;


import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application{
    private static Scene scene;
    
    @Override
    public void start(Stage stage) throws Exception{
        transfer.startServer();
        scene = new Scene(loadFXML("main_window"));
        stage.setScene(scene);
        // Закрываем сокет при закрытии приложения
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
            try {
                transfer.stopServer();
            } catch (InterruptedException e) {}
            }
            });
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}
