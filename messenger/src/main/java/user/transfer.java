package user;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class transfer {
    private static String name;
    private static log_in_controller contr1;
    private static main_window_controller contr2;
    private static WebClient client;
    private static Map<String, Vector<String>> messages;

    public static void SetName(String Nname){
        if(name == null){
            name = Nname;  
        }    
        
    }
    public static String GetName(){
        return name;
    }
    public static void Init(log_in_controller cont1, main_window_controller cont2) throws URISyntaxException{
        contr1 = cont1;
        contr2 = cont2;
        URI serverUri = new URI("ws://192.168.0.109:2294");
        //localhost
        client = new WebClient(serverUri);
        client.connect();
        messages = new HashMap<>();
        contr2.SetName(name);
    }
    public static void CloseConnection(){
        client.close();
    }
    public static void SayMessage(String Address, String message){
        client.SayMessage(Address, message);
        Vector<String> a;
        if(messages.get(Address) == null)
            a = new Vector<String>();
        else
            a = messages.get(Address);
        a.add("Вы: "+message);
        messages.put(Address,a);
        
    }
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
        if (!a.isEmpty()){
            for (int i = 0; i!=messages.size();i++)
                contr2.addMessage(a.get(i));
        }
    }
}
