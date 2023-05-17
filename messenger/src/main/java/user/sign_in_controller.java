package user;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class sign_in_controller {
    @FXML
    private TextField log_inField;
    @FXML
    private TextField passwordField1;
    @FXML
    private TextField passwordField2;

    public void Sign_in(ActionEvent act) throws IOException{
        transfer.setContr3(this);
        String login = log_inField.getText().trim();
        String pas1 = passwordField1.getText().trim();
        String pas2 = passwordField2.getText().trim();
        if (pas1.equals(pas2) && !(login.equals("")) && !(pas1.equals("")))
            transfer.sign_in(login, pas1);
    }
    public void mis() throws IOException{
        App.setRoot("sign_in_excep");
    }
    public void valid() throws IOException{
        transfer.SetName(log_inField.getText().trim());
        App.setRoot("user_main_window");
        //transfer.getUsers();
    }
}
