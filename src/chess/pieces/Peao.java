package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Peao extends ChessPiece {

	private ChessMatch partida;

	public Peao(Board board, Color color, ChessMatch partida) {
		super(board, color);
		this.partida = partida;
	}

	@Override
	public boolean[][] possibleMoves() {

		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

		Position p = new Position(0, 0);
		Position p2 = new Position(0, 0);

		if (getColor() == Color.WHITE) {
			p.setValues(position.getRow() - 1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}

			p2.setValues(position.getRow() - 2, position.getColumn());
			if (getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0
					&& !getBoard().thereIsAPiece(p)) {
				mat[p2.getRow()][p2.getColumn()] = true;
			}

			p.setValues(position.getRow() - 1, position.getColumn() - 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow() - 1, position.getColumn() + 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}

			// enPassant branca
			if (position.getRow() == 3) {
				Position left = new Position(position.getRow(), position.getColumn() - 1);
				if (getBoard().positionExists(left) && isThereOpponentPiece(left)
						&& getBoard().piece(left) == partida.getEnPassantVulnerable()) {
					mat[left.getRow() - 1][left.getColumn()] = true;
				}
				
				Position rigth = new Position(position.getRow(), position.getColumn() + 1);
				if (getBoard().positionExists(rigth) && isThereOpponentPiece(rigth)
						&& getBoard().piece(rigth) == partida.getEnPassantVulnerable()) {
					mat[rigth.getRow() - 1][rigth.getColumn()] = true;
				}
			}

		} else {
			p.setValues(position.getRow() + 1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}

			p2.setValues(position.getRow() + 2, position.getColumn());
			if (getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0
					&& !getBoard().thereIsAPiece(p)) {
				mat[p2.getRow()][p2.getColumn()] = true;
			}

			p.setValues(position.getRow() + 1, position.getColumn() - 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow() + 1, position.getColumn() + 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}

			// enPassant black
			if (position.getRow() == 4) {
				Position left = new Position(position.getRow(), position.getColumn() - 1);
				if (getBoard().positionExists(left) && isThereOpponentPiece(left)
						&& getBoard().piece(left) == partida.getEnPassantVulnerable()) {
					mat[left.getRow() + 1][left.getColumn()] = true;
				}
				Position rigth = new Position(position.getRow(), position.getColumn() + 1);
				if (getBoard().positionExists(rigth) && isThereOpponentPiece(rigth)
						&& getBoard().piece(rigth) == partida.getEnPassantVulnerable()) {
					mat[rigth.getRow() + 1][rigth.getColumn()] = true;
				}
			}
		}

		return mat;

	}

	@Override
	public String toString() {
		return "P";

	}

}
