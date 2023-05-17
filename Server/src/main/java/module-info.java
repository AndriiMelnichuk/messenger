module MyServer {
    requires javafx.controls;
    requires javafx.fxml;
    requires Java.WebSocket;
    requires java.sql;
    

    opens MyServer to javafx.fxml;
    exports MyServer;
}
