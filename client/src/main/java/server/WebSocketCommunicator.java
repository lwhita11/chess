//package server;
//import com.google.gson.Gson;
//import websocket.messages.ServerMessage;
//import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import org.eclipse.jetty.websocket.api.Session;
//
//
//public class WebSocketCommunicator {
//
//    Session session;
//    private final String url;
//
//    public WebSocketCommunicator(String url) {
//        this.url = url;
//    }
//
//    public void onMessage (String message) {
//        try {
//            ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);
//            //observer.notify(message) ??
//        } catch (Exception ex) {
//            //observer.notify(new ErrorMessage (ex.getMessage)) ??
//        }
//    }
//}
