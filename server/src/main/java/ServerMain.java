import chess.*;
import server.Server;

public class ServerMain {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        int port;

        Server myServer = new Server();
        if (args == null || args.length == 0) {
            port = 8080;
        }
        else {
            port = Integer.parseInt(args[0]);
        }

        myServer.run(port);
    }
}
