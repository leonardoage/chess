package chess;

import java.util.ArrayList;
import java.util.List;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> piecesCaptured = new ArrayList<>();

	private int turn;
	private Color currentPlayer;

	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}

	public int getTurn() {
		return turn;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}

	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
	}

	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.BLACK ? Color.WHITE : Color.BLACK);
	}

	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position p = sourcePosition.toPosition();
		validateSourcePosition(p);
		return board.piece(p).possibleMoves();
	}

	public ChessPiece performChessMove(ChessPosition origem, ChessPosition destino) {
		Position source = origem.toPosition();
		Position target = destino.toPosition();

		validateSourcePosition(source);
		validateTargetPosition(source, target);

		nextTurn();
		return (ChessPiece) makeMove(source, target);

	}

	private void validateSourcePosition(Position pos) {
		if (!board.thereIsAPiece(pos)) {
			throw new ChessException("Nao ha peca na posicao informada (" + pos.toString() + ")");
		}
		if (currentPlayer != ((ChessPiece) board.piece(pos)).getColor()) {
			throw new ChessException("A peca escolhida não é sua.");
		}
		if (!board.piece(pos).isThereAnyPossibleMove()) {
			throw new ChessException("Nao ha movimento possivel para a peca na posicao informada.");
		}
	}

	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("A peca nao pode ser movida para o destino desejado");
		}
	}

	private Piece makeMove(Position origem, Position destino) {
		Piece p = board.removePiece(origem);
		Piece captured = board.removePiece(destino);
		board.placePiece(p, destino);
		if (captured != null) { 
			piecesOnTheBoard.remove(captured); 
			piecesCaptured.add(captured); 
		}
		return captured;
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece); 
	}

	private void initialSetup() {
		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
		placeNewPiece('c', 2, new Rook(board, Color.WHITE));
		placeNewPiece('d', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 1, new Rook(board, Color.WHITE));
		placeNewPiece('d', 1, new King(board, Color.WHITE));

		placeNewPiece('c', 7, new Rook(board, Color.BLACK));
		placeNewPiece('c', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 8, new King(board, Color.BLACK));
	}
}
