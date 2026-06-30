# ChessCodes

This project contains a playable chess game built with Java Swing.

## Files

- Chess.java - main game window, turn handling, UI, animations, and game-state popups
- ChessMovements.java - movement dispatcher for the pieces
- Pawn.java - pawn movement logic
- Rook.java, Knight.java, Bishop.java, Queen.java, King.java - separate movement classes for each piece
- BasicAI.java - a simple beginner AI that makes a random legal move for the black side

## Run it

From the project folder, run:

```bash
javac Chess.java ChessMovements.java Pawn.java Rook.java Knight.java Bishop.java Queen.java King.java BasicAI.java
java Chess
```

## Features

- Simple 8x8 chessboard
- Click-to-select and click-to-move gameplay
- Highlighted legal moves
- Smooth basic piece animation
- Check detection with on-screen popup alerts
- Checkmate and stalemate detection with game-over popups
- Basic AI opponent for single-player play

