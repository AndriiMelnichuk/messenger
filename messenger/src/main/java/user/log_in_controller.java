package user;


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
    
    public void Log_In(ActionEvent act){
        String login = log_inField.getText();
        if (!login.equals("")){
            // Здесь должен быть запрос серверу. Запрашиваем кто в сети (Возможно запрашиваем регистрацию)
            try{
                // Замена полотна - переходим на вкладку мессенджера.
                FXMLLoader loader = new FXMLLoader(getClass().getResource("user_main_window.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage)((Node)act.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                // Настраеваем класс transfer
                transfer.SetName(login);
                
                
                transfer.Init(this,loader.getController());
                // Закрываем сокет при закрытии приложения
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                     System.out.println("Stage is closing");
                     transfer.CloseConnection();
                     }
                    });
                // Здесь добавляем список возможных соединений
                stage.show();
            } catch(Exception e){
                e.printStackTrace();
            }
            
        }

        
    }
    public void Sign_in(ActionEvent act){

    }
}
