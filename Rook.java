import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Rook {
    public List<Point> getMoves(String[][] board, int row, int col, boolean whiteTurn) {
        List<Point> moves = new ArrayList<>();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] direction : directions) {
            int nextRow = row + direction[0];
            int nextCol = col + direction[1];
            while (isInside(nextRow, nextCol)) {
                String target = board[nextRow][nextCol];
                if (target == null) {
                    moves.add(new Point(nextRow, nextCol));
                } else {
                    if (isEnemy(target, whiteTurn)) {
                        moves.add(new Point(nextRow, nextCol));
                    }
                    break;
                }
                nextRow += direction[0];
                nextCol += direction[1];
            }
        }
        return moves;
    }

    private boolean isInside(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    private boolean isEnemy(String piece, boolean whiteTurn) {
        return (whiteTurn && piece.startsWith("b")) || (!whiteTurn && piece.startsWith("w"));
    }
}
