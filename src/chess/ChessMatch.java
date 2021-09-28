package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	private boolean check;

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
	
	public boolean getCheck() { 
		return check; 
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

		Piece captured = makeMove(source, target);
		
		if (testCheck(currentPlayer)) { 
			undoMove(source, target, captured);
			throw new ChessException("Voce nao pode se colocar em check."); 
		}
		
		check =  testCheck(Opponent(currentPlayer)); 
		
		nextTurn();
		return (ChessPiece) captured;

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

	private void undoMove(Position origem, Position destino, Piece pecaCapturada) {
		Piece p = board.removePiece(destino);
		board.placePiece(p, origem);
		if (pecaCapturada != null) {
			board.placePiece(pecaCapturada, destino);
			piecesCaptured.remove(pecaCapturada);
			piecesOnTheBoard.add(pecaCapturada);
		}
	}

	private Color Opponent(Color color) {
		return (color == Color.BLACK ? Color.WHITE : Color.BLACK);
	}

	private ChessPiece getKingPiece(Color color) {
		List<Piece> l = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());
		for (Piece p : l) {
			if (p instanceof King) {
				return (ChessPiece) p;
			}
		}
		throw new IllegalStateException("Não há Rei na cor " + color + " no tabuleiro.");
	}
	
	private boolean testCheck(Color color) { 
		Position kingPosition = getKingPiece(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == Opponent(color)).collect(Collectors.toList()); 
		for (Piece p : opponentPieces) { 
			boolean[][] movimentos = p.possibleMoves(); 
			if (movimentos[kingPosition.getRow()][kingPosition.getColumn()]) { 
				return true;
			}
		}
		return false;
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
