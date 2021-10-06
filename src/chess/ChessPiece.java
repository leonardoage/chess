package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece {


	private Color color;
	private int moveCount; 
	

	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	
	public int getMoveCount() {
		return moveCount;
	}


	public Color getColor() {
		return color;
	}
	
	public void increaseMoveCount() { 
		moveCount++; 
	}
	public void decreaseMoveCount() { 

		moveCount--; 
	}

	protected boolean isThereOpponentPiece(Position pos)  { 
		ChessPiece p = (ChessPiece) getBoard().piece(pos); 
		return  (p != null) && ( p.getColor() != this.color); 
	}
	
	
	public ChessPosition getChessPosition() { 
		return ChessPosition.fromPosition(position);
	}
}
