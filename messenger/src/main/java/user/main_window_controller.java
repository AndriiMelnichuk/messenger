package user;

import java.io.IOException;
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
    @FXML
    private TextField companion;
    private String CurrentUser;
    public void SayMessage(ActionEvent act) throws IOException{
        if (!(writeMessage.getText().trim().equals("")) && CurrentUser != null){
            messages.getItems().add("Вы: "+ writeMessage.getText().trim());
            transfer.SayMessage(CurrentUser,writeMessage.getText().trim());
            writeMessage.clear();
        }
    }
    public void NewMessage(String user, String mes){    
        if (CurrentUser != null && CurrentUser.equals(user))
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
    public void online(String user){
        if (companion.getText().equals(user))
            companion.setText(user+" (В сети)");
    }
    public void offlien(String user){
        if (companion.getText().equals(user+" (В сети)"))
            companion.setText(user);
    }
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        transfer.setContr2(this);
        UserName.setText(transfer.GetName());
        String[] a = {};
        UsersList.getItems().addAll(a);
        UsersList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
            CurrentUser = UsersList.getSelectionModel().getSelectedItem();
            messages.getItems().clear();
            transfer.setAllMessages(CurrentUser);
            if (transfer.isOnline(CurrentUser))
                companion.setText(CurrentUser + " (В сети)");
            else 
            companion.setText(CurrentUser);
            }  
        });
    }

}