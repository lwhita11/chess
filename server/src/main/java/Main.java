import chess.*;
import server.Server;
import server.WebSocketHandler;
import spark.Spark;
import java.util.*;
import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        Spark.webSocket("/ws", WebSocketHandler.class);
        System.out.println("♕ 240 Chess Server: " + piece);
        Server server = new Server();
        server.run(8080);
    }
}

