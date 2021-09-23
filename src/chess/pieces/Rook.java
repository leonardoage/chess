package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Rook extends ChessPiece{

	public Rook(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() { 
		return "R"; 
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0, 0); 
		
		//above 
		p.setValues(this.position.getRow() - 1, position.getColumn());
		 
		//Marcando todas as posições enquando não existir peça
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) { 
			mat[p.getRow()][p.getColumn()] = true; 
			p.setValues(p.getRow() - 1, p.getColumn());
		}
		
		//Ao encontrar a primeira peça, verificar se a peça é do oponente e validar acesso 
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) { 
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//below 
		p.setValues(this.position.getRow() + 1, position.getColumn());
		 
		// Marcando todas as posições enquando não existir peça
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) { 
			mat[p.getRow()][p.getColumn()] = true; 
			p.setValues(p.getRow() + 1, p.getColumn());
		}
		
		// Ao encontrar a primeira peça, verificar se a peça é do oponente e validar acesso 
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) { 
			mat[p.getRow()][p.getColumn()] = true;
		}
		

		//left  
		p.setValues(this.position.getRow(), position.getColumn() - 1);
		 
		//Marcando todas as posições enquando não existir peça
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) { 
			mat[p.getRow()][p.getColumn()] = true; 
			p.setValues(p.getRow() , p.getColumn() - 1);
		}
		
		//Ao encontrar a primeira peça, verificar se a peça é do oponente e validar acesso 
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) { 
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//Right
		p.setValues(this.position.getRow(), position.getColumn() + 1);
		 
		//Marcando todas as posições enquando não existir peça
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) { 
			mat[p.getRow()][p.getColumn()] = true; 
			p.setValues(p.getRow() , p.getColumn() + 1);
		}
		
		//Ao encontrar a primeira peça, verificar se a peça é do oponente e validar acesso 
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) { 
			mat[p.getRow()][p.getColumn()] = true;
		}		
		return mat;
	}

}
