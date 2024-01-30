import chess.*;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessPiece.PieceType.PAWN, ChessGame.TeamColor.WHITE);
        System.out.println("♕ 240 Chess Server: " + piece);
    }
}