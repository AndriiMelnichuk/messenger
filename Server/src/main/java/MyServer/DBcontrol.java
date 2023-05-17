package MyServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;




public class DBcontrol {
    private static String dbHost = "localhost";
    private static String dbPort = "3306";
    private static String dbUser = "root";
    private static String dbPass = "helloworld123";
    private static String dbName = "messenger";

    private static String LOGIN = "login";
    private static String PASWORD = "pasword";
    private static String IDMESSAGE = "idmessage";
    private static String USER1 = "user1";
    private static String USER2 = "user2";
    private static String TEXT = "text";

    private static String TUSER = "user";
    private static String TMESSAGE = "message";

    static Connection dbConnection;

    public static Connection getDbConnection() throws ClassNotFoundException, SQLException{
        if (dbConnection == null){
            String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
            dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        }
        return dbConnection;
    }

    public static void addUser(String login, String password) throws SQLException, ClassNotFoundException{
        String insert = "INSERT INTO " + TUSER +" ( " + 
        LOGIN +", " + PASWORD +")"+
        "VALUES(?,?)";
        PreparedStatement prSt = getDbConnection().prepareStatement(insert);
        prSt.setString(1, login);
        prSt.setString(2, password);

        prSt.executeUpdate();
    }
    public static void addNewMessege(String from, String to, String messege) throws ClassNotFoundException, SQLException{
        String insert = "INSERT INTO " + TMESSAGE +" ( " + 
        USER1 +", " + USER2 + ", "+ TEXT +")"+
        "VALUES(?,?,?)";
        PreparedStatement prSt = getDbConnection().prepareStatement(insert);
        prSt.setString(1, from);
        prSt.setString(2, to);
        prSt.setString(3, messege);
        prSt.executeUpdate();
    }

    public static ResultSet getUser(String login, String password) throws ClassNotFoundException, SQLException{
        String select = "SELECT * FROM " + TUSER + 
        " WHERE " + LOGIN + "=? AND " + PASWORD + "=?";
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, login);
        prSt.setString(2, password);
        return prSt.executeQuery();
    }
    public static ResultSet getUser(String login) throws ClassNotFoundException, SQLException{
        String select = "SELECT * FROM " + TUSER + " WHERE " + LOGIN + "=?" ;
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, login);
        return prSt.executeQuery();
    }
    public static ResultSet getAllUser(String login) throws ClassNotFoundException, SQLException{
        String select = "SELECT * FROM " + TUSER +
        " WHERE NOT " + LOGIN + "=?";
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, login);
        return prSt.executeQuery();
    }
    public static ResultSet getAllMesseges(String login) throws ClassNotFoundException, SQLException{
        String select = "SELECT * FROM " + TMESSAGE +
        " WHERE " + USER1 + " =? OR " + USER2 + "=?";
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, login);
        prSt.setString(2, login);
        return prSt.executeQuery();
    }
    public static ResultSet getAllMesseges(String user1, String user2) throws ClassNotFoundException, SQLException{
        String select = "SELECT * FROM " + TMESSAGE +
        " WHERE (" + USER1 + " =? AND " + USER2 + "=?) OR ( "
        + USER1 + " =? AND " + USER2 + "=?)";
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, user1);
        prSt.setString(2, user2);
        prSt.setString(3, user2);
        prSt.setString(4, user1);
        return prSt.executeQuery();
    }


    public static ResultSet getAllMesseges() throws ClassNotFoundException, SQLException{
        String select = "SELECT * FROM " + TMESSAGE;
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        return prSt.executeQuery();
    }
}
