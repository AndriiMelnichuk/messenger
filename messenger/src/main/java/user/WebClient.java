package user;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Vector;


//import javax.print.attribute.standard.Media;

public class WebClient extends WebSocketClient{
    
    
    private String userName;
    
    public void SayMessage(String address, String message){
        String mes = "<Message> "+ "<add> " + address + " </add> " + "<mes> " + message + " </mes> " + "</Message>";
        send(mes);
    }
    
    public void NewMessage(String message){
        String[] arrString = message.split(" ");
        Vector<String> myVector = new Vector<String>();
        for (int i=0; i!=arrString.length; i++){
            myVector.add(arrString[i]);
        }
        String user = "";
        int b = myVector.indexOf("</User>");
        
        for(int i = 2; i!=b; i++){
            if (i==2)
                user += myVector.get(i);
            else 
                user += (" " + myVector.get(i));
        }
        for(int i = 0; i!=b; i++){
            myVector.remove(0);
        }

        
        String mes = "";
        b = myVector.indexOf("</mes>");
        for(int i = 2; i!=b; i++){
            if (i==1)
                mes += myVector.get(i);
            else 
                mes += (" " + myVector.get(i));
        }
        transfer.NewMessage(user,mes);
        
    }


    public WebClient(URI uri){
        super(uri);
        userName = transfer.GetName();
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
            transfer.AddUser(user);
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

        transfer.DeleteUser(user);
    }

    @Override
    public void onOpen(ServerHandshake handshake){
        // Добавь отпавку сообщения (ПОЛЬЗОВАТЕЛЬ ВОШЕЛ В СЕТЬ ПОД ИМЕНЕМ...)
        System.out.println("Connection opened");

        String request = "<LogIn> "+userName+" </LogIn>";
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
            case "<NewMes>":
            NewMessage(message);
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