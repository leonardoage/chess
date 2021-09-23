package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {

	public King(Board board, Color color) {
		super(board, color);
	}

	@Override
	public String toString() {
		return "K";
	}

	private boolean canMove(Position pos) {
		ChessPiece p = (ChessPiece) getBoard().piece(pos);
		return (p == null) || (p.getColor() != getColor());
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position(0, 0);

		p.setValues(position.getRow() + 1, position.getColumn());
		if (getBoard().positionExists(p)) {
			mat[p.getRow()][p.getColumn()] = canMove(p);
		}

		// below
		p.setValues(position.getRow() - 1, position.getColumn());
		if (getBoard().positionExists(p)) {
			mat[p.getRow()][p.getColumn()] = canMove(p);
		}

		// left
		p.setValues(position.getRow(), position.getColumn() - 1);
		if (getBoard().positionExists(p)) {
			mat[p.getRow()][p.getColumn()] = canMove(p);
		}

		// right
		p.setValues(position.getRow(), position.getColumn() + 1);
		if (getBoard().positionExists(p)) {
			mat[p.getRow()][p.getColumn()] = canMove(p);
		}

		// nw
		p.setValues(position.getRow() - 1, this.position.getColumn() - 1);
		if (getBoard().positionExists(p)) {
			mat[p.getRow()][p.getColumn()] = canMove(p);
		}

		// ne
		p.setValues(position.getRow() - 1, this.position.getColumn() + 1);
		if (getBoard().positionExists(p)) {
			mat[p.getRow()][p.getColumn()] = canMove(p);
		}

		// sw
		p.setValues(position.getRow() + 1, this.position.getColumn() - 1);
		if (getBoard().positionExists(p)) {
			mat[p.getRow()][p.getColumn()] = canMove(p);
		}

		// se
		p.setValues(position.getRow() + 1, this.position.getColumn() + 1);
		if (getBoard().positionExists(p)) {
			mat[p.getRow()][p.getColumn()] = canMove(p);
		}

		return mat;
	}

}
