module user {
    requires javafx.controls;
    requires javafx.fxml;
    requires Java.WebSocket;
    //import org.java_websocket.client.WebSocketClient;
    //import org.java_websocket.handshake.ServerHandshake;
    
    //opens user to javafx.fxml;
    //opens user to Java.WebSocket;
    opens user;
    exports user;
}
