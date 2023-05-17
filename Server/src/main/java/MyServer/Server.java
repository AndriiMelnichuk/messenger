package MyServer;

import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class Server extends WebSocketServer{
    private Vector<UserConnections> connections;
    
    private class UserConnections{
        private WebSocket soc;
        private String user;
        public WebSocket getConn(){
            return soc;
        }
        public String GetUser(){
            return user;
        }
        public UserConnections(WebSocket a, String b){
            soc = a;
            user = b;
        }
    }

    // Обработка сообщений
    @Override
    public void onMessage(WebSocket conn, String message){
        String[] arrStrings = message.split(" ");
        try{
            switch (arrStrings[0]){
                case "<LogIn>":
                    logInHandler(message,conn);
                break;
                case "<message>":
                    messageHandler(message,conn);
                break;
                case "<recording>":
                    recordingHandler(message, conn);
                break;
            }
        }catch(Exception e){}
        
    }
    private void logInHandler(String message, WebSocket conn) throws ClassNotFoundException, SQLException {
        String login = parse("<login>", "</login>", message);
        String password = parse("<password>", "</password>", message);
        
        ResultSet ResSet = DBcontrol.getUser(login, password);
        int count = 0;
        while(ResSet.next())
            count++;
        if (count == 1){
            conn.send("<ValidLogInStatus> valid </ValidLogInStatus>");
            UserConnections us = new UserConnections(conn,login);
            connections.add(us);
            sendAllMessageAndUsers(login, conn);
            }
        else
            conn.send("<ValidLogInStatus> invalid </ValidLogInStatus>");
        
    }
    private void messageHandler(String message, WebSocket conn) throws ClassNotFoundException, SQLException{
        UserConnections user = new UserConnections(null, null);
        String from = parse("<from>", "</from>", message);
        String to = parse("<to>", "</to>", message);
        String text = parse("<text>", "</text>", message);
        for (int i=0; i!=connections.size();i++)
            if (connections.elementAt(i).GetUser().equals(to))
                user = connections.elementAt(i);
        if (user.getConn() != null){
            String NewMes = "<NewMessage> <from> " + from + " </from> <text> " + text + " </text> </NewMessage>";
            user.getConn().send(NewMes);
        }
        DBcontrol.addNewMessege(from, to, text);
        transfer.NewMessage(from,to,text);
    }
    private void recordingHandler(String message, WebSocket conn) throws ClassNotFoundException, SQLException {
        String login = parse("<login>","</login>",message);
        String password = parse("<password>","</password>",message);
        ResultSet ResSet = DBcontrol.getUser(login);
        int counter = 0;
        String mes;
        while(ResSet.next()){
            counter++;
        }
        if (counter == 0){
            mes = "<ValidRecordingStatus> " + "valid" + " </ValidRecordingStatus>";            
            conn.send(mes);
            UserConnections us = new UserConnections(conn,login);
            connections.add(us);
            DBcontrol.addUser(login, password);
            sendAllMessageAndUsers(login, conn);
            for (int i = 0; i!=connections.size(); i++)
                if (!connections.elementAt(i).getConn().equals(conn))
                    connections.elementAt(i).getConn().send("<allUsers> <User> "+ login + " </User> </allUsers>");
        }
        else{
            mes = "<ValidRecordingStatus> " + "invalid" + " </ValidRecordingStatus>";
            conn.send(mes);
        }

    }
    
    // Вспомогательные функции
    private void sendAllMessageAndUsers(String login,WebSocket conn) throws ClassNotFoundException, SQLException{
        // Отправка всех пользователей
        ResultSet ResSet = DBcontrol.getAllUser(login);
        Vector <String> allUsers = new Vector<String>();
        while(ResSet.next())
            allUsers.add(ResSet.getString(1));
        conn.send(getUsers(allUsers));
        // Отправка всех сообщений
        ResSet = DBcontrol.getAllMesseges(login);
        String NewMes = "<OldMessages> ";
        while(ResSet.next()){
            NewMes += "<message> <from> " + ResSet.getString(2) + " </from> <to> " + ResSet.getString(3) + " </to> <text> " + ResSet.getString(4) + " </text> </message> ";
        }
        NewMes+= "</OldMessages>";
        conn.send(NewMes);
        // Отправка всех пользователей всети
        for (int i = 0; i != connections.size(); i++ )
            if (!connections.elementAt(i).GetUser().equals(login)){
                conn.send("<online> <User> " + connections.elementAt(i).GetUser() + " </User> </online>");
                connections.elementAt(i).getConn().send("<online> <User> " + login + " </User> </online>");
            }
                
        
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
    private String getUsers(Vector <String> allUsers){
        String ans ="<allUsers> ";
        for(int i=0; i!=allUsers.size();i++)
            if (i==0)
                ans += ("<User> " + allUsers.elementAt(i) + " </User>");
            else 
                ans += (" <User> " + allUsers.elementAt(i) + " </User>");
        ans += " </allUsers>";
        return ans;
    }

    // Оповещение пользователя о закрытии сокета пользователя
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote){
        UserConnections user = new UserConnections(conn,"");
        for(int i=0; i!=connections.size(); i++){
            if (conn.equals(connections.elementAt(i).getConn())){
                user = connections.elementAt(i);
                break;
            }
        }
        for (int i=0; i!=connections.size();i++)
            if (!connections.elementAt(i).equals(user))
                connections.elementAt(i).getConn().send("<offline> <User> "+user.GetUser()+" </User> </offline>");
        connections.remove(user);
    }

    // Пустые функции
    @Override
    public void onError(WebSocket conn, Exception ex){
    }
    public Server(int port){
        super(new InetSocketAddress(port));
        connections = new Vector<UserConnections>();
    }
    @Override
    public void onStart() {
        
    }
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake){
               
    }



}

