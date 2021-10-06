package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Cavalo extends ChessPiece {

	public Cavalo(Board board, Color color) {
		super(board, color);
	}

	@Override
	public String toString() {
		return "C";
	}

	private boolean canMove(Position pos) {
		ChessPiece p = (ChessPiece) getBoard().piece(pos);
		return (p == null) || (p.getColor() != getColor());
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position(0, 0);

		p.setValues(position.getRow() - 1, position.getColumn() - 2);
		if (getBoard().positionExists(p)) {
			mat[p.getRow()][p.getColumn()] = canMove(p);
		}

		p.setValues(position.getRow() - 1, position.getColumn() + 2);
		if (getBoard().positionExists(p)) {
			mat[p.getRow()][p.getColumn()] = canMove(p);
		}

		p.setValues(position.getRow() + 1, position.getColumn() - 2);
		if (getBoard().positionExists(p)) {
			mat[p.getRow()][p.getColumn()] = canMove(p);
		}

		p.setValues(position.getRow() + 1, position.getColumn() + 2);
		if (getBoard().positionExists(p)) {
			mat[p.getRow()][p.getColumn()] = canMove(p);
		}

		p.setValues(position.getRow() - 2, position.getColumn() + 1);
		if (getBoard().positionExists(p)) {
			mat[p.getRow()][p.getColumn()] = canMove(p);
		}

		p.setValues(position.getRow() - 2, position.getColumn() - 1);
		if (getBoard().positionExists(p)) {
			mat[p.getRow()][p.getColumn()] = canMove(p);
		}

		p.setValues(position.getRow() + 2, position.getColumn() + 1);
		if (getBoard().positionExists(p)) {
			mat[p.getRow()][p.getColumn()] = canMove(p);
		}

		p.setValues(position.getRow() + 2, position.getColumn() - 1);
		if (getBoard().positionExists(p)) {
			mat[p.getRow()][p.getColumn()] = canMove(p);
		}

		return mat;
	}

}
