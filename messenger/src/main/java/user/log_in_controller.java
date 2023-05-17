package user;


import java.io.IOException;
import java.net.URISyntaxException;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class log_in_controller{
    @FXML
    private TextField log_inField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label LException;
    // trim - удаление лишних пробелов.

    public void Log_In(ActionEvent act) throws URISyntaxException, IOException{
        transfer.setContr1(this);
        transfer.ConnectionRestart();
        transfer.SetName(log_inField.getText().trim());
        String login = log_inField.getText().trim();
        String password = passwordField.getText().trim();
        if (login.equals("") || login.equals(""))
            mis();
        else
            transfer.isValidUser(login, password);
    }
    public void Sign_in(ActionEvent act) throws IOException{
        App.setRoot("sign_in");
    }

    public void draw() throws IOException{
        App.setRoot("user_main_window");
        //transfer.getUsers();
    }
    public void mis() throws IOException {
        App.setRoot("log_in_excep");
    }
    public void connectionEror() throws IOException {
        App.setRoot("log_in_coner");
    }

}
