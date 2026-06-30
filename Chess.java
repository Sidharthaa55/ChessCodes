import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Chess extends JFrame {
    private static final int SIZE = 8;
    private static final int CELL = 80;
    private static final int BOARD_SIZE = SIZE * CELL;

    private final String[][] board = new String[SIZE][SIZE];
    private final List<Point> legalMoves = new ArrayList<>();
    private final ChessMovements movementEngine = new ChessMovements();
    private final BasicAI basicAI = new BasicAI();

    private int selectedRow = -1;
    private int selectedCol = -1;
    private boolean whiteTurn = true;
    private boolean animating = false;
    private boolean gameOver = false;
    private String movingPiece = null;
    private int fromRow;
    private int fromCol;
    private int toRow;
    private int toCol;
    private int animationStep = 0;
    private static final int TOTAL_ANIMATION_STEPS = 10;
    private Timer animationTimer;

    private final JLabel statusLabel = new JLabel("White to move", SwingConstants.CENTER);
    private final BoardPanel boardPanel = new BoardPanel();

    public Chess() {
        setTitle("Basic Chess");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(44, 44, 44));

        boardPanel.setPreferredSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                handleClick(event.getX(), event.getY());
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(44, 44, 44));
        topPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));
        topPanel.add(statusLabel);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(44, 44, 44));
        content.add(boardPanel, BorderLayout.CENTER);
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(topPanel, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);

        initializeBoard();
    }

    private void initializeBoard() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                board[r][c] = null;
            }
        }

        String[] whiteBackRank = {"wR", "wN", "wB", "wQ", "wK", "wB", "wN", "wR"};
        String[] blackBackRank = {"bR", "bN", "bB", "bQ", "bK", "bB", "bN", "bR"};

        for (int c = 0; c < SIZE; c++) {
            board[0][c] = blackBackRank[c];
            board[1][c] = "bP";
            board[6][c] = "wP";
            board[7][c] = whiteBackRank[c];
        }

        repaint();
    }

    private void handleClick(int x, int y) {
        if (animating || gameOver) {
            return;
        }

        int row = y / CELL;
        int col = x / CELL;

        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            return;
        }

        if (selectedRow == -1) {
            if (isSelectablePiece(row, col)) {
                selectedRow = row;
                selectedCol = col;
                legalMoves.clear();
                legalMoves.addAll(getSafeMoves(row, col));
                statusLabel.setText("Select a destination");
                repaint();
            } else {
                statusLabel.setText("Choose one of your pieces");
            }
            return;
        }

        if (row == selectedRow && col == selectedCol) {
            clearSelection();
            statusLabel.setText((whiteTurn ? "White" : "Black") + " to move");
            return;
        }

        if (legalMoves.contains(new Point(row, col))) {
            playMove(selectedRow, selectedCol, row, col);
            clearSelection();
            return;
        }

        if (isSelectablePiece(row, col)) {
            selectedRow = row;
            selectedCol = col;
            legalMoves.clear();
            legalMoves.addAll(getSafeMoves(row, col));
            statusLabel.setText("Select a destination");
            repaint();
        } else {
            clearSelection();
            statusLabel.setText("Illegal move");
        }
    }

    private boolean isSelectablePiece(int row, int col) {
        String piece = board[row][col];
        if (piece == null) {
            return false;
        }
        return (whiteTurn && piece.startsWith("w")) || (!whiteTurn && piece.startsWith("b"));
    }

    private void playMove(int fromRow, int fromCol, int toRow, int toCol) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        movingPiece = board[fromRow][fromCol];
        board[fromRow][fromCol] = null;
        animating = true;
        animationStep = 0;

        animationTimer = new Timer(40, event -> {
            animationStep++;
            if (animationStep >= TOTAL_ANIMATION_STEPS) {
                board[toRow][toCol] = movingPiece;
                movingPiece = null;
                animating = false;
                animationTimer.stop();
                whiteTurn = !whiteTurn;
                clearSelection();
                updateGameStatus();

                if (!gameOver && !whiteTurn) {
                    SwingUtilities.invokeLater(this::makeAIMove);
                }
            }
            repaint();
        });
        animationTimer.start();
    }

    private void makeAIMove() {
        if (animating || whiteTurn) {
            return;
        }

        BasicAI.Move move = basicAI.chooseMove(board);
        if (move == null) {
            statusLabel.setText("No legal AI moves");
            return;
        }

        playMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
    }

    private List<Point> getSafeMoves(int row, int col) {
        List<Point> safeMoves = new ArrayList<>();
        for (Point move : movementEngine.getLegalMoves(board, row, col, whiteTurn)) {
            if (isMoveSafe(row, col, (int) move.getX(), (int) move.getY(), whiteTurn)) {
                safeMoves.add(move);
            }
        }
        return safeMoves;
    }

    private boolean isMoveSafe(int fromRow, int fromCol, int toRow, int toCol, boolean sideToMoveIsWhite) {
        String[][] nextBoard = copyBoard(board);
        String piece = nextBoard[fromRow][fromCol];
        nextBoard[fromRow][fromCol] = null;
        nextBoard[toRow][toCol] = piece;
        return !isKingInCheck(nextBoard, sideToMoveIsWhite);
    }

    private String[][] copyBoard(String[][] source) {
        String[][] copy = new String[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            System.arraycopy(source[row], 0, copy[row], 0, SIZE);
        }
        return copy;
    }

    private boolean isKingInCheck(String[][] state, boolean whiteSide) {
        Point kingPosition = findKing(state, whiteSide);
        if (kingPosition == null) {
            return false;
        }

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
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

                List<Point> attacks = movementEngine.getLegalMoves(state, row, col, !whiteSide);
                if (attacks.contains(new Point((int) kingPosition.getX(), (int) kingPosition.getY()))) {
                    return true;
                }
            }
        }
        return false;
    }

    private Point findKing(String[][] state, boolean whiteSide) {
        String target = whiteSide ? "wK" : "bK";
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (target.equals(state[row][col])) {
                    return new Point(row, col);
                }
            }
        }
        return null;
    }

    private boolean hasAnyLegalMove(String[][] state, boolean whiteSide) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String piece = state[row][col];
                if (piece == null) {
                    continue;
                }
                boolean pieceIsWhite = piece.startsWith("w");
                if ((whiteSide && !pieceIsWhite) || (!whiteSide && pieceIsWhite)) {
                    continue;
                }

                for (Point move : getSafeMovesForBoard(state, row, col, whiteSide)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<Point> getSafeMovesForBoard(String[][] state, int row, int col, boolean sideToMoveIsWhite) {
        List<Point> safeMoves = new ArrayList<>();
        for (Point move : movementEngine.getLegalMoves(state, row, col, sideToMoveIsWhite)) {
            if (isMoveSafeOnBoard(state, row, col, (int) move.getX(), (int) move.getY(), sideToMoveIsWhite)) {
                safeMoves.add(move);
            }
        }
        return safeMoves;
    }

    private boolean isMoveSafeOnBoard(String[][] state, int fromRow, int fromCol, int toRow, int toCol, boolean sideToMoveIsWhite) {
        String[][] nextBoard = copyBoard(state);
        String piece = nextBoard[fromRow][fromCol];
        nextBoard[fromRow][fromCol] = null;
        nextBoard[toRow][toCol] = piece;
        return !isKingInCheck(nextBoard, sideToMoveIsWhite);
    }

    private void updateGameStatus() {
        String sideName = whiteTurn ? "White" : "Black";
        if (isKingInCheck(board, whiteTurn)) {
            if (!hasAnyLegalMove(board, whiteTurn)) {
                gameOver = true;
                statusLabel.setText(sideName + " is checkmated!");
                showResultDialog(sideName + " is checkmated! Game over.");
            } else {
                statusLabel.setText(sideName + " is in check");
                showResultDialog(sideName + " is in check!");
            }
        } else if (!hasAnyLegalMove(board, whiteTurn)) {
            gameOver = true;
            statusLabel.setText(sideName + " is stalemated");
            showResultDialog(sideName + " is stalemated! It's a draw.");
        } else {
            statusLabel.setText(sideName + " to move");
        }
    }

    private void showResultDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Game Update", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearSelection() {
        selectedRow = -1;
        selectedCol = -1;
        legalMoves.clear();
        repaint();
    }

    private class BoardPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    Color color = (row + col) % 2 == 0 ? new Color(240, 217, 181) : new Color(181, 136, 99);
                    g2.setColor(color);
                    g2.fillRect(col * CELL, row * CELL, CELL, CELL);

                    if (selectedRow == row && selectedCol == col) {
                        g2.setColor(new Color(0, 180, 120, 120));
                        g2.fillRect(col * CELL, row * CELL, CELL, CELL);
                    }

                    if (legalMoves.contains(new Point(row, col))) {
                        g2.setColor(new Color(76, 175, 80, 130));
                        g2.fillOval(col * CELL + 28, row * CELL + 28, 24, 24);
                    }
                }
            }

            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    String piece = board[row][col];
                    if (piece == null) {
                        continue;
                    }
                    if (animating && row == fromRow && col == fromCol) {
                        continue;
                    }
                    drawPiece(g2, piece, col * CELL, row * CELL);
                }
            }

            if (animating && movingPiece != null) {
                int currentX = fromCol * CELL + (toCol - fromCol) * CELL * animationStep / TOTAL_ANIMATION_STEPS;
                int currentY = fromRow * CELL + (toRow - fromRow) * CELL * animationStep / TOTAL_ANIMATION_STEPS;
                drawPiece(g2, movingPiece, currentX, currentY);
            }

            g2.dispose();
        }
    }

    private void drawPiece(Graphics2D g2, String piece, int x, int y) {
        String symbol = "?";
        if (piece.substring(1).equals("P")) {
            symbol = "♙";
        } else if (piece.substring(1).equals("R")) {
            symbol = "♖";
        } else if (piece.substring(1).equals("N")) {
            symbol = "♘";
        } else if (piece.substring(1).equals("B")) {
            symbol = "♗";
        } else if (piece.substring(1).equals("Q")) {
            symbol = "♕";
        } else if (piece.substring(1).equals("K")) {
            symbol = "♔";
        }

        if (piece.startsWith("b")) {
            if (piece.substring(1).equals("P")) {
                symbol = "♟";
            } else if (piece.substring(1).equals("R")) {
                symbol = "♜";
            } else if (piece.substring(1).equals("N")) {
                symbol = "♞";
            } else if (piece.substring(1).equals("B")) {
                symbol = "♝";
            } else if (piece.substring(1).equals("Q")) {
                symbol = "♛";
            } else if (piece.substring(1).equals("K")) {
                symbol = "♚";
            }
        }

        g2.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 44));
        g2.setColor(piece.startsWith("w") ? Color.WHITE : new Color(40, 40, 40));
        g2.drawString(symbol, x + 15, y + 55);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Chess chess = new Chess();
            chess.setVisible(true);
        });
    }
}
