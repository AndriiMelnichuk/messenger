package user;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class transfer {
    private static String name;
    private static log_in_controller contr1;
    private static main_window_controller contr2;
    private static sign_in_controller contr3;
    private static WebClient client;
    private static Map<String, Vector<String>> messages = new HashMap<>();
    private static Vector<String> onlineUsers= new Vector<String>();
    // Работа с именем
    public static void SetName(String Nname){
        name = Nname;
    }
    public static String GetName(){
        return name;
    }
    // Инициализация переменных 
    public static void setContr1(log_in_controller a){
        if (contr1 ==null)
            contr1 = a;
    }
    public static void setContr2(main_window_controller a){
        if (contr2 == null)
            contr2 = a;
    }
    public static void setContr3(sign_in_controller a){
        if (contr3 == null)
            contr3 = a;
    }
    public static void setWebSocket() throws URISyntaxException{
        if (client == null){
            URI serverUri = new URI("ws://194.56.80.9:2294");
            //URI serverUri = new URI("ws://localhost:2294");
            client = new WebClient(serverUri);
            client.connect();
        }
    }
    // Работа с клиентом
    public static void CloseConnection(){
        client.close();
    }
    public static void sign_in(String login, String password) throws IOException {
        client.sign_in(login, password);
    }
    public static void isValidUser(String login, String password) throws IOException{
        client.isValidUser(login, password);
    }
    public static void SayMessage(String Address, String message) throws IOException{
        client.SayMessage(Address, message);
        Vector<String> a;
        if(messages.get(Address) == null)
            a = new Vector<String>();
        else
            a = messages.get(Address);
        a.add("Вы: "+message);
        messages.put(Address,a);   
    } 
    public static void ConnectionRestart(){
        if (client.isClosed())
            client.reconnect();
    }

    // Работа с контроллером 1
    public static void ValidStatus(String status) throws IOException{
        if (status.equals("valid"))
            contr1.draw();
        else
            contr1.mis();
    }
    public static void connectionEror() throws IOException {
        if (contr1 != null)
            contr1.connectionEror();
    }

    // Работа с контроллером 2
    public static void AddUser(String user){
        contr2.AddUsers(user);
    }
    public static void DeleteUser(String user){
        contr2.DeleteUser(user);
    }
    public static void NewMessage(String user, String mes){
        Vector<String> a;
        if(messages.get(user) == null)
            a = new Vector<String>();
        else
            a = messages.get(user);
        a.add(user+": "+mes);
        messages.put(user,a);
        contr2.NewMessage(user,user+": "+mes);
    }
    public static void setAllMessages(String curUser){
        Vector<String> a = messages.get(curUser);
        if (a != null)
            for (int i = 0; i!=a.size();i++)
                contr2.addMessage(a.get(i)); 
    }
    public static void oldMes(String from, String to, String mes){
        Vector<String> a;
        if (name.equals(to)){
            if(messages.get(from) == null)
                a = new Vector<String>();
            else
                a = messages.get(from);
            a.add(from+": "+mes);
            messages.put(from,a);
            contr2.NewMessage(from,from+": "+mes);
        } else{
            if(messages.get(to) == null)
                a = new Vector<String>();
            else
                a = messages.get(to);
            a.add("Вы: "+mes);
            messages.put(to,a);
            contr2.NewMessage(to,"Вы: "+mes);
        }

    }
    public static void online(String user) {
        onlineUsers.add(user);
        contr2.online(user);
    }
    public static void offline(String user) {
        onlineUsers.remove(user);
        contr2.offlien(user);
    }
    public static boolean isOnline(String user){
        return onlineUsers.contains(user);
    }
    // Работа с контроллером 3
    public static void recordingStatus(String status) throws IOException {
        if (status.equals("valid"))
            contr3.valid();
        else
            contr3.mis();
    }

}
