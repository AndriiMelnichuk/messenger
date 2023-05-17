package user;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;


public class WebClient extends WebSocketClient{
// Отправка сообщений
    public void SayMessage(String address, String message) throws IOException{
        if (isOpen()){
            String request = "<message> <from> " + transfer.GetName() + " </from> <to> " + address + " </to> <text> " + message + " </text> </message>";
            send(request);
        }
        else
            transfer.connectionEror();
    }
    public void isValidUser(String login, String password) throws IOException{
        if (isOpen()){
            String request = "<LogIn> " + "<login> " + login + " </login>" + " <password> " + password + " </password>" + " </LogIn>";
            send(request);
        }
        else
            transfer.connectionEror();
    }
    public void sign_in(String login, String password) throws IOException {
        if (isOpen()){
            String mes = "<recording> " + "<login> " + login + " </login> " + "<password> " + password + " </password>" + " </recording>";
            send(mes);
        }
        else 
            transfer.connectionEror();

    }

    private String parse(String from, String to, String message){
        String[] arrStrings = message.split(" ");
        int a =  Arrays.asList(arrStrings).indexOf(from);
        int b =  Arrays.asList(arrStrings).indexOf(to);
        String res = "";
        for (int i = a+1; i!=b;i++){
            if(i==a+1)
                res += arrStrings[i];
            else
                res += (" "+arrStrings[i]);
        }
        return res;
    }
    public WebClient(URI uri){
        super(uri);
    }
    @Override
    public void onOpen(ServerHandshake handshake){}
    @Override
    public void onClose(int code, String reason, boolean remote) {}
    @Override
    public void onError(Exception ex) {
        try {
            transfer.connectionEror();
        } catch (IOException e) {}
    }   

    // Обработка сообщений
    @Override
    public void onMessage(String message){
        String[] arrString = message.split(" ");
       try{
            switch (arrString[0]){
                case "<allUsers>":
                allUsersHandler(message);
                break;
                case "<online>":
                onlineHandler(message);
                break;
                case "<OldMessages>":
                oldMessagesHandler(message);
                break;
                case "<ValidLogInStatus>":
                validLogInStatusHandler(message);
                break;
                case "<ValidRecordingStatus>":
                validRecordingStatusHandler(message);
                break;
                case "<NewMessage>":
                newMessageHandler(message);
                break;
                case "<offline>":
                offlineHandler(message);
                break;
            }
        } catch(Exception e){}

    }
    private void allUsersHandler(String message){
        message = message.substring(11);
        while (!message.equals("</allUsers>")){
            String user = parse("<User>", "</User>", message);
            int i = message.indexOf("</User>") ;
            message = message.substring(i + 8);
            transfer.AddUser(user);
        }
    }
    private void onlineHandler(String message){
        String user = parse("<User>", "</User>", message);
        transfer.online(user);
        
    }
    private void oldMessagesHandler(String message) {
        message = message.substring(14);
        while(!message.equals("</OldMessages>")){
            String userMessage = parse("<message>","</message>",message);
            String from = parse("<from>", "</from>", userMessage);
            String to = parse("<to>", "</to>", userMessage);
            String text = parse("<text>", "</text>", userMessage);
            transfer.oldMes(from, to, text);
            int i = message.indexOf("</message>");
            message = message.substring(i + 11);
        }
    }
    private void validLogInStatusHandler(String message) throws IOException{
        String[] arrString = message.split(" ");
        transfer.ValidStatus(arrString[1]);
    }
    private void validRecordingStatusHandler(String message) throws IOException {
        String status = message.split(" ")[1];
        transfer.recordingStatus(status);
    }
    private void newMessageHandler(String message){
        String from = parse("<from>", "</from>", message);
        String text = parse("<text>", "</text>", message);
        transfer.NewMessage(from,text); 
    }
    private void offlineHandler(String message){
        String user = parse("<User>","</User>",message);
        transfer.offline(user);
    }
}