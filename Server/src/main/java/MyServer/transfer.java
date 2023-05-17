package MyServer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class transfer {
    private static main_controller contr;
    private static Server server;

    public static void setContr(main_controller a){
        contr = a;
    }
    public static void startServer(){
        int post = 2294;
        server = new Server(post);
        server.start();
    }
    public static void stopServer() throws InterruptedException{
        server.stop();
    }
    
    public static Vector<String> getChat(String chat) throws ClassNotFoundException, SQLException { 
        Vector<String> resVector = new Vector<String>();
        String s = " <->";
        int i = chat.indexOf(s);
        String user1 = chat.substring(0,i);
        String user2 = chat.substring(i+s.length()+1);
        ResultSet resSet = DBcontrol.getAllMesseges(user1, user2);
        while (resSet.next()){
            String a = resSet.getString(2) + ": " + resSet.getString(4);
            resVector.add(a);
        }
        return resVector;
    }

    public static Vector<String> getAllChats() throws ClassNotFoundException, SQLException  {
        ResultSet resSet = DBcontrol.getAllMesseges();
        Vector<String> resultVector = new Vector<String>();
        String a;
        String b;
        while(resSet.next()){
            a = resSet.getString(2) +" <-> " + resSet.getString(3);
            b = resSet.getString(3) +" <-> " + resSet.getString(2);
            if (!resultVector.contains(a) && !resultVector.contains(b))
                resultVector.add(a);
        }
        return resultVector;
    }
    public static void NewMessage(String from, String to, String text) throws ClassNotFoundException, SQLException {
        ResultSet resSet = DBcontrol.getAllMesseges(from,to);
        int counter = 0;
        while (resSet.next())
            counter++;
        if (counter == 1)
            contr.addNewChat(from + " <-> " + to);
        else
            contr.addNewMessege(from, to, text);
    }





}
