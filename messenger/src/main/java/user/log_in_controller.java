package user;
import java.io.IOException;
import java.net.URI;
//import java.net.http.WebSocket;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class log_in_controller {
    @FXML
    private TextField log_inField;
    private WebClient client;
    public void Log_In(ActionEvent act) throws IOException{
        String login = log_inField.getText();
        if (!login.equals("")){
            // Здесь должен быть запрос серверу. Запрашиваем кто в сети (Возможно запрашиваем регистрацию)
            try{
                // Замена полотна - переходим на вкладку мессенджера.

                FXMLLoader loader = new FXMLLoader(getClass().getResource("user_main_window.fxml"));
                Parent root = loader.load();

                main_window_controller myMain_window_controller = loader.getController();
                myMain_window_controller.SetName(login);
                //Parent root = FXMLLoader.load(getClass().getResource("user_main_window.fxml"));
                Stage stage = (Stage)((Node)act.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);

                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                     System.out.println("Stage is closing");
                     client.close();
                     }
                    });
                // Здесь добавляем список возможных соединений

                
                URI serverUri = new URI("ws://localhost:8080");
                //127.0.0.1
                client = new WebClient(serverUri,login,myMain_window_controller);
                client.connect();
                stage.show();
            } catch(Exception e){
                e.printStackTrace();
            }
            
        }

        
    }
    public void Sign_in(ActionEvent act){

    }
    public void SayMessage(String address, String message){}
    public void GetMessages(){
        client.getMessages();
    }
}
