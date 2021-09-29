package chess;

import boardgame.Position;

public class ChessPosition {
	private char column;
	private int row;

	public ChessPosition(char column, int row) {

		if (column < 'a' || column > 'h' || row < 1 || row > 8) {
			throw new ChessException(
					"Error instantiating ChessPosition. Valid values colum a - h and row between 1 and 8");
		}
		this.column = column;
		this.row = row;

	}

	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	protected Position toPosition() { 
		return new Position(8 - row, column - 'a'); 
	}
	
	protected static ChessPosition fromPosition(Position p) {
		return new ChessPosition( (char)('a'+ p.getColumn()), 8 - p.getRow()); 
	}
	
	@Override
	public String toString() { 
		return "" + column + row; // String vazio para forçar concatenação de string
		
	}
	

}
