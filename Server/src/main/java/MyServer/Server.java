package MyServer;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Vector;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class Server extends WebSocketServer{
    //private Vector<String> users;
    private Vector<UserConnections> connections;
    // Класс контейнер
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
    // Используем для генирации сообщения 
    private String getUsers(){
        String ans ="";
        for(int i=0; i!=connections.size();i++){
            if (i==0)
                ans += ("<NewUser> " + connections.elementAt(i).GetUser() + " </NewUser>");
            else 
                ans += (" <NewUser> " + connections.elementAt(i).GetUser() + " </NewUser>");
        }
        
        return ans;
    }
    // Используем для рассылки сообщений пользователям о входе или выходе.
    private void UpdateListForAll(UserConnections user, String type){
        for (int i=0; i!=connections.size();i++)
            if (type.equals("Add")){
                if (!connections.elementAt(i).equals(user))
                    connections.elementAt(i).getConn().send("<NewUser> "+user.GetUser()+" </NewUser>");
            }else if (type.equals("Remove")){
                if (!connections.elementAt(i).equals(user))
                    connections.elementAt(i).getConn().send("<LogOutUser> "+user.GetUser()+" </LogOutUser>");
            }
    }
    
    private void LogIn(String message, WebSocket conn) {
        String[] arrStrings = message.split(" ");
        String user ="";
        int b = Arrays.asList(arrStrings).indexOf("</LogIn>");
        for (int i = 1; i!=b;i++){
            if(i==1)
                user += arrStrings[i];
            else
                user += (" "+arrStrings[i]);
        }
        conn.send(getUsers());
        UserConnections us = new UserConnections(conn,user);
        connections.add(us);
        UpdateListForAll(us,"Add");
    }
    
    private void Message(String message, WebSocket conn){
        UserConnections user = new UserConnections(null, null);
        String add = "";
        String mes = "";
        String name = "";
        String[] StringArr = message.split(" ");
        int b = Arrays.asList(StringArr).indexOf("</add>");
        int a = Arrays.asList(StringArr).indexOf("<mes>");
        for (int i=0; i!=connections.size();i++)
            if (connections.elementAt(i).getConn().equals(conn))
                name = connections.elementAt(i).GetUser();
        for (int i=2; i!= b; i++)
            if (i==2)
                add += StringArr[i];
            else
                add += (" " + StringArr[i]);
        b = Arrays.asList(StringArr).indexOf("</mes>");
        for (int i = a+1; i!=b; i++)
            if (i == a+1)
                mes += StringArr[i];
            else
                mes += (" " + StringArr[i]); 
        for (int i=0; i!=connections.size();i++)
            if (connections.elementAt(i).GetUser().equals(add))
                user = connections.elementAt(i);
        String NewMes = "<NewMes> "+ "<User> " + name + " </User> " + "<mes> " + mes + " </mes> " + "</NewMes>";
        user.getConn().send(NewMes);
        
    }
    // Методы перегруженные (основные методы сокета)
    // Ничего не происходит
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake){
        // При соединении
        System.out.println("New connection from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());        
    }
    // Оповещаем о закрытии остальных пользователей, удаляем пользователя из памяти
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote){
        // При закрытии
        System.out.println("Closed connection to " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
        UserConnections user = new UserConnections(conn,"");
        for(int i=0; i!=connections.size(); i++){
            if (conn.equals(connections.elementAt(i).getConn())){
                user = connections.elementAt(i);
                break;
            }
        }
        UpdateListForAll(user, "Remove");
        connections.remove(user);
    }
    /* Парсим сообщение, возможные исходы:
        GetUsers - запрашивается когда пользователь логинится. Возвращает спикок всех сетевых соединений.
        LogIn - отправляем всем информацию, что пользователь в сети.
    */   
    @Override
    public void onMessage(WebSocket conn, String message){
        // При Получении сообщения
        System.out.println("Received message from " + conn.getRemoteSocketAddress().getAddress().getHostAddress() + ": " + message);
        String[] arrStrings = message.split(" ");
        // Выполныем разные действия в зависимости от команды
        switch (arrStrings[0]){
            case "<LogIn>":
            LogIn(message,conn);
            break;
            case "<Message>":
            Message(message,conn);
            break;
        }
        System.out.println();
    }
    // Ничего не происходит
    @Override
    public void onError(WebSocket conn, Exception ex){
        // При Ошибке
        System.err.println("Error occurred on connection to "+conn.getRemoteSocketAddress().getAddress().getHostAddress() +":"+ex);
    }
    // Ничего не происходит
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'onStart'");
    }
    // Конструктор
    public Server(int port){
        super(new InetSocketAddress(port));
        //users = new Vector<String>();
        connections = new Vector<UserConnections>();
    }
 
    // Точка входа
    public static void main(String[] args) throws InterruptedException {
        int post = 2294;
        Server server = new Server(post);
        server.start();
    }


}

