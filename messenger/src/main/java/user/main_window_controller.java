package user;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class main_window_controller implements Initializable{
    
    @FXML
    private ListView<String> UsersList;
    @FXML
    private ListView<String> messages;
    @FXML
    private TextField UserName;
    @FXML
    private TextField writeMessage;
    private log_in_controller myLog_in_controller;

    public void AddInUsers(String obj){
        UsersList.getItems().add(obj);        
    }
    public void SayMessage(ActionEvent act){
        if (!writeMessage.getText().equals("")){
            myLog_in_controller.SayMessage(null,writeMessage.getText());
        }
        
    }
    public void DeleteUser(String obj){
        UsersList.getItems().remove(obj);
    }
    public void SetName(String name){
        UserName.setText(name);
    }
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        String[] a = {};
        UsersList.getItems().addAll(a);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("log_in.fxml"));
        myLog_in_controller = loader.getController();
    }
}