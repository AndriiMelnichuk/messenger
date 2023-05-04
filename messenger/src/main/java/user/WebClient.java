package user;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.net.URI;
import java.util.Vector;


//import javax.print.attribute.standard.Media;

public class WebClient extends WebSocketClient{
    
    private main_window_controller myMain_window_controller;
    private String userName;
    private Vector<String> users;
    
    public String[] getMessages(){

        String[] a = {"hi"};
        return a;
    }

    public WebClient(URI uri, String Name, main_window_controller mc){
        super(uri);
        myMain_window_controller = mc;
        userName = Name;
        users = new Vector<>();
    }
    
    private void setNewUser(String message){
        String[] arrString = message.split(" ");
        Vector<String> myVector = new Vector<String>();
        for (int i=0; i!=arrString.length; i++){
            myVector.add(arrString[i]);
        }

        while (!myVector.isEmpty()){
            String user = "";
            
            int b = myVector.indexOf("</NewUser>");
            
            for(int i = 1; i!=b; i++){
                if (i==1)
                    user += myVector.get(i);
                else 
                    user += (" " + myVector.get(i));
            }
            myVector.remove("<NewUser>");
            myVector.remove("</NewUser>");
            for(int i = 0; i!=b-1; i++){
                myVector.remove(0);
            }
            users.add(user);
            myMain_window_controller.AddInUsers(user);
            
        }
    }

    private void LogOutUser(String message){
        String[] arrString = message.split(" ");
        Vector<String> myVector = new Vector<String>();
        for (int i=0; i!=arrString.length; i++){
            myVector.add(arrString[i]);
        }
        String user = "";
        int b = myVector.indexOf("</LogOutUser>");
        for(int i = 1; i!=b; i++){
            if (i==1)
                user += myVector.get(i);
            else 
                user += (" " + myVector.get(i));
        }

        users.remove(user);
        myMain_window_controller.DeleteUser(user);
    }

    @Override
    public void onOpen(ServerHandshake handshake){
        // Добавь отпавку сообщения (ПОЛЬЗОВАТЕЛЬ ВОШЕЛ В СЕТЬ ПОД ИМЕНЕМ...)
        System.out.println("Connection opened");

        String request = "<userName> "+userName+" </userName> "+"<request> "+"LogIn"+" </request>";
        send(request);

    }
    
    @Override
    public void onMessage(String message){
        System.out.println("Received message: " + message);
        String[] arrString = message.split(" ");
        switch (arrString[0]){
            case "<NewUser>":
            setNewUser(message);
            break;
            case "<LogOutUser>":
            LogOutUser(message);
            break;
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        //String request = "<userName> "+userName+" </userName> "+"<request> "+"CloseConnection"+" </request>";
        //send(request);
        System.out.println("Connection closed");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
   
}