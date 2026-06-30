import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BasicAI {
    private final ChessMovements movementEngine = new ChessMovements();
    private final Random random = new Random();

    public Move chooseMove(String[][] board) {
        List<Move> moves = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                String piece = board[row][col];
                if (piece == null || !piece.startsWith("b")) {
                    continue;
                }

                List<Point> legalMoves = movementEngine.getLegalMoves(board, row, col, false);
                for (Point move : legalMoves) {
                    if (isMoveSafe(board, row, col, (int) move.getX(), (int) move.getY())) {
                        moves.add(new Move(row, col, (int) move.getX(), (int) move.getY()));
                    }
                }
            }
        }

        if (moves.isEmpty()) {
            return null;
        }

        return moves.get(random.nextInt(moves.size()));
    }

    private boolean isMoveSafe(String[][] state, int fromRow, int fromCol, int toRow, int toCol) {
        String[][] nextBoard = copyBoard(state);
        String piece = nextBoard[fromRow][fromCol];
        nextBoard[fromRow][fromCol] = null;
        nextBoard[toRow][toCol] = piece;
        return !isKingInCheck(nextBoard, false);
    }

    private String[][] copyBoard(String[][] source) {
        String[][] copy = new String[8][8];
        for (int row = 0; row < 8; row++) {
            System.arraycopy(source[row], 0, copy[row], 0, 8);
        }
        return copy;
    }

    private boolean isKingInCheck(String[][] state, boolean whiteSide) {
        Point kingPosition = findKing(state, whiteSide);
        if (kingPosition == null) {
            return false;
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                String piece = state[row][col];
                if (piece == null) {
                    continue;
                }
                boolean pieceIsWhite = piece.startsWith("w");
                if (whiteSide && pieceIsWhite) {
                    continue;
                }
                if (!whiteSide && !pieceIsWhite) {
                    continue;
                }

                List<Point> attacks = new ChessMovements().getLegalMoves(state, row, col, !whiteSide);
                if (attacks.contains(new Point((int) kingPosition.getX(), (int) kingPosition.getY()))) {
                    return true;
                }
            }
        }
        return false;
    }

    private Point findKing(String[][] state, boolean whiteSide) {
        String target = whiteSide ? "wK" : "bK";
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (target.equals(state[row][col])) {
                    return new Point(row, col);
                }
            }
        }
        return null;
    }

    public static class Move {
        public final int fromRow;
        public final int fromCol;
        public final int toRow;
        public final int toCol;

        public Move(int fromRow, int fromCol, int toRow, int toCol) {
            this.fromRow = fromRow;
            this.fromCol = fromCol;
            this.toRow = toRow;
            this.toCol = toCol;
        }
    }
}
