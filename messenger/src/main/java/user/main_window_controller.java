package user;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;


public class main_window_controller implements Initializable{
    
    @FXML
    private ListView<String> UsersList;
    @FXML
    private ListView<String> messages;
    @FXML
    private TextField UserName;
    @FXML
    private TextField writeMessage;
    private String CurrentUser;
    public void SayMessage(ActionEvent act){
        if (!(writeMessage.getText().equals("")) && CurrentUser != null){
            messages.getItems().add("Вы: "+ writeMessage.getText());
            transfer.SayMessage(CurrentUser,writeMessage.getText());
            writeMessage.clear();
        }
    }
    public void NewMessage(String user, String mes){
        if (CurrentUser != null)
            if (CurrentUser.equals(user))
                messages.getItems().add(mes);
        
    }
    public void AddUsers(String obj){
        UsersList.getItems().add(obj);        
    }
    public void addMessage(String obj){
        messages.getItems().add(obj);
    }
    public void DeleteUser(String obj){
        UsersList.getItems().remove(obj);
    }
    public void SetName(String name){
        UserName.setText(name);
        UserName.setEditable(false);
    }
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        String[] a = {};
        UsersList.getItems().addAll(a);
        UsersList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
            CurrentUser = UsersList.getSelectionModel().getSelectedItem();
            messages.getItems().clear();
            transfer.setAllMessages(CurrentUser);
            }  
        });
    }
}