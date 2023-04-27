//import javafx.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PointOfEntry extends Application{
    @Override
    public void start(Stage primaryStage){
        try{
            Parent parent_xml = FXMLLoader.load(getClass().getResource("/interface/log_in.fxml"));
            
            Scene scene = new Scene(parent_xml);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}