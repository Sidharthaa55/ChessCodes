import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class King {
    public List<Point> getMoves(String[][] board, int row, int col, boolean whiteTurn) {
        List<Point> moves = new ArrayList<>();
        int[][] offsets = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

        for (int[] offset : offsets) {
            int nextRow = row + offset[0];
            int nextCol = col + offset[1];
            if (isInside(nextRow, nextCol)) {
                String target = board[nextRow][nextCol];
                if (target == null || isEnemy(target, whiteTurn)) {
                    moves.add(new Point(nextRow, nextCol));
                }
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
