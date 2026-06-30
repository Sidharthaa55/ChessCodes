import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class ChessMovements {
    private final Pawn pawnHelper = new Pawn();
    private final Rook rookHelper = new Rook();
    private final Knight knightHelper = new Knight();
    private final Bishop bishopHelper = new Bishop();
    private final Queen queenHelper = new Queen();
    private final King kingHelper = new King();

    public List<Point> getLegalMoves(String[][] board, int row, int col, boolean whiteTurn) {
        String piece = board[row][col];
        if (piece == null) {
            return new ArrayList<>();
        }

        String type = piece.substring(1);
        return switch (type) {
            case "P" -> pawnHelper.getMoves(board, row, col, whiteTurn);
            case "R" -> rookHelper.getMoves(board, row, col, whiteTurn);
            case "N" -> knightHelper.getMoves(board, row, col, whiteTurn);
            case "B" -> bishopHelper.getMoves(board, row, col, whiteTurn);
            case "Q" -> queenHelper.getMoves(board, row, col, whiteTurn);
            case "K" -> kingHelper.getMoves(board, row, col, whiteTurn);
            default -> new ArrayList<>();
        };
    }
}
