import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

class Pawn {
    public List<Point> getMoves(String[][] board, int row, int col, boolean whiteTurn) {
        List<Point> moves = new ArrayList<>();
        int direction = whiteTurn ? -1 : 1;
        int startRow = whiteTurn ? 6 : 1;

        int nextRow = row + direction;
        if (nextRow >= 0 && nextRow < 8 && board[nextRow][col] == null) {
            moves.add(new Point(nextRow, col));
            if (row == startRow) {
                int twoStepRow = row + 2 * direction;
                if (twoStepRow >= 0 && twoStepRow < 8 && board[twoStepRow][col] == null) {
                    moves.add(new Point(twoStepRow, col));
                }
            }
        }

        if (nextRow >= 0 && nextRow < 8) {
            for (int deltaCol : new int[]{-1, 1}) {
                int nextCol = col + deltaCol;
                if (nextCol >= 0 && nextCol < 8) {
                    String target = board[nextRow][nextCol];
                    if (target != null && isEnemy(target, whiteTurn)) {
                        moves.add(new Point(nextRow, nextCol));
                    }
                }
            }
        }

        return moves;
    }

    private boolean isEnemy(String piece, boolean whiteTurn) {
        return (whiteTurn && piece.startsWith("b")) || (!whiteTurn && piece.startsWith("w"));
    }
}
