package MyServer;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class main_controller implements Initializable{
    @FXML
    private ListView<String> ChatsList;
    @FXML
    private ListView<String> messages;
    private String selectedChat;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        transfer.setContr(this);
        Vector<String> a = new Vector<String>();
        try {
            a = transfer.getAllChats();
        } catch (Exception e) {}
        ChatsList.getItems().addAll(a);
        ChatsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                selectedChat = ChatsList.getSelectionModel().getSelectedItem();
                messages.getItems().clear();
                try {
                    Vector<String> a = transfer.getChat(selectedChat);
                    messages.getItems().setAll(a);
                } catch (Exception e) {}
            }
        });
    }

    public void addNewChat(String chat) {
        ChatsList.getItems().add(chat);
    }

    public void addNewMessege(String from, String to, String text) {
        if (selectedChat!=null && (selectedChat.equals(from + " <-> " + to) || selectedChat.equals(to + " <-> " + from)))
            messages.getItems().add(from + ": "+ text);
    }
}
