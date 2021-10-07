package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {

	private ChessMatch partida;

	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		partida = chessMatch;
	}

	@Override
	public String toString() {
		return "K";
	}

	private boolean canMove(Position pos) {
		ChessPiece p = (ChessPiece) getBoard().piece(pos);
		return (p == null) || (p.getColor() != getColor());
	}

	private boolean testRookCasting(Position pos) {
		ChessPiece p = (ChessPiece) getBoard().piece(pos);
		return p != null && p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;
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

		// #especial move Castiling
		if (getMoveCount() == 0 && !partida.getCheck()) {
			// especial castiling king
			Position PosT1 = new Position(position.getRow(), position.getColumn() + 3);
			if (testRookCasting(PosT1)) {
				Position p1 = new Position(position.getRow(), position.getColumn() + 1);
				Position p2 = new Position(position.getRow(), position.getColumn() + 2);
				if (getBoard().piece(p1) == null && getBoard().piece(p2) == null) {
					mat[position.getRow()][position.getColumn() + 2] = true;
				}
			}

			// especial castiling king
			Position PosT2 = new Position(position.getRow(), position.getColumn() - 4);
			if (testRookCasting(PosT2)) {
				Position p1 = new Position(position.getRow(), position.getColumn() - 1);
				Position p2 = new Position(position.getRow(), position.getColumn() - 2);
				Position p3 = new Position(position.getRow(), position.getColumn() - 3);
				if (getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3) == null) {
					mat[position.getRow()][position.getColumn() - 2] = true;
				}
			}
		}

		return mat;
	}

}
