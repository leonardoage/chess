package chess;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.Cavalo;
import chess.pieces.King;
import chess.pieces.Peao;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> piecesCaptured = new ArrayList<>();

	private int turn;
	private Color currentPlayer;
	private boolean check;
	private boolean checkMate;
	private ChessPiece enPassantVulnable;
	private ChessPiece promoted;

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

	public ChessPiece getpromoted() {
		return promoted;
	}

	public boolean isCheckMate() {
		return checkMate;
	}

	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnable;
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

		ChessPiece movedPiece = (ChessPiece) board.piece(target);

		// #special move promotion
		if (movedPiece instanceof Peao) {
			if (movedPiece.getColor() == Color.WHITE && target.getRow() == 0
					|| movedPiece.getColor() == Color.BLACK && target.getRow() == 7) {
				promoted = (ChessPiece) board.piece(target);
				promoted = replacePromotedPiece("Q");
			}

		}

		check = testCheck(Opponent(currentPlayer));

		if (check && testCheckMate(Opponent(currentPlayer))) {
			checkMate = true;

		} else {
			nextTurn();
		}

		// enPassant
		if (movedPiece instanceof Peao
				&& (target.getRow() == source.getRow() + 2 || target.getRow() == source.getRow() - 2)) {
			enPassantVulnable = movedPiece;
		} else {
			enPassantVulnable = null;
		}

		return (ChessPiece) captured;

	}

	public ChessPiece replacePromotedPiece(String type) {
		if (promoted == null) {
			throw new IllegalStateException("There is no piece to be promoted");
		}

		if (type.toString().toUpperCase().equals("B") && type.toString().toUpperCase().equals("N")
				&& type.toString().toUpperCase().equals("R") && type.toString().toUpperCase().equals("Q")) {
			throw new InvalidParameterException("Invalid type for promotion");
		}

		Position pos = promoted.getChessPosition().toPosition();
		Piece p = board.removePiece(pos);
		piecesOnTheBoard.remove(p);

		ChessPiece newPiece = newPiece(type, promoted.getColor());
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);

		return newPiece;
	}

	private ChessPiece newPiece(String type, Color color) {
		if (type.toString().toUpperCase().equals("B"))
			return new Bishop(board, color);
		if (type.toString().toUpperCase().equals("N"))
			return new Cavalo(board, color);
		if (type.toString().toUpperCase().equals("B"))
			return new Queen(board, color);
		return new Rook(board, color);
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
		ChessPiece p = (ChessPiece) board.removePiece(origem);
		p.increaseMoveCount();
		Piece captured = board.removePiece(destino);
		board.placePiece(p, destino); // Upcasting by default
		if (captured != null) {
			piecesOnTheBoard.remove(captured);
			piecesCaptured.add(captured);
		}

		// special move castling king side rook
		if (p instanceof King && destino.getColumn() == origem.getColumn() + 2) {
			Position origemT1 = new Position(origem.getRow(), origem.getColumn() + 3);
			Position destinoT2 = new Position(origem.getRow(), origem.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(origemT1);
			board.placePiece(rook, destinoT2);
			rook.increaseMoveCount();
		}

		// special move castling queen side rook
		if (p instanceof King && destino.getColumn() == origem.getColumn() - 2) {
			Position origemT1 = new Position(origem.getRow(), origem.getColumn() - 4);
			Position destinoT2 = new Position(origem.getRow(), origem.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(origemT1);
			board.placePiece(rook, destinoT2);
			rook.increaseMoveCount();
		}

		// #special move
		if (p instanceof Peao) {
			if (origem.getColumn() != destino.getColumn() && captured == null) {
				Position posPeao;
				if (p.getColor() == Color.WHITE) {
					posPeao = new Position(destino.getRow() + 1, destino.getColumn());
				} else {
					posPeao = new Position(destino.getRow() - 1, destino.getColumn());

				}
				captured = board.removePiece(posPeao);
				piecesOnTheBoard.remove(captured);
				piecesCaptured.add(captured);
			}
		}

		return captured;

	}

	private void undoMove(Position origem, Position destino, Piece pecaCapturada) {
		ChessPiece p = (ChessPiece) board.removePiece(destino);
		p.decreaseMoveCount();
		board.placePiece(p, origem); // Upcasting por padrão
		if (pecaCapturada != null) {
			board.placePiece(pecaCapturada, destino);
			piecesCaptured.remove(pecaCapturada);
			piecesOnTheBoard.add(pecaCapturada);
		}

		// special move castling king side rook
		if (p instanceof King && destino.getColumn() == origem.getColumn() + 2) {
			Position origemT1 = new Position(origem.getRow(), origem.getColumn() + 3);
			Position destinoT2 = new Position(origem.getRow(), origem.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(destinoT2);
			board.placePiece(rook, origemT1);
			rook.decreaseMoveCount();
		}

		// special move castling queen side rook
		if (p instanceof King && destino.getColumn() == origem.getColumn() - 2) {
			Position origemT1 = new Position(origem.getRow(), origem.getColumn() - 4);
			Position destinoT2 = new Position(origem.getRow(), origem.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(destinoT2);
			board.placePiece(rook, origemT1);
			rook.decreaseMoveCount();
		}

		// #special move enPassant
		if (p instanceof Peao) {
			if (origem.getColumn() != destino.getColumn() && pecaCapturada == enPassantVulnable) {
				ChessPiece peao = (ChessPiece) board.removePiece(destino);
				Position posPeao;
				if (p.getColor() == Color.WHITE) {
					posPeao = new Position(3, destino.getColumn());
				} else {
					posPeao = new Position(4, destino.getColumn());

				}
				board.placePiece(peao, posPeao);
			}
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
		List<Piece> opponentPieces = piecesOnTheBoard.stream()
				.filter(x -> ((ChessPiece) x).getColor() == Opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			boolean[][] movimentos = p.possibleMoves();
			if (movimentos[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}

	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}
		List<Piece> pecas = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());
		for (Piece p : pecas) {
			boolean[][] mat = p.possibleMoves();
			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getColumns(); j++) {
					if (mat[i][j]) {
						Position origem = ((ChessPiece) p).getChessPosition().toPosition();
						Position destino = new Position(i, j);
						Piece captured = makeMove(origem, destino);
						boolean testCheck = testCheck(color);
						undoMove(origem, destino, captured);

						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;

	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}

	private void initialSetup() {
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('b', 1, new Cavalo(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('d', 1, new Queen(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE, this));
		placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('g', 1, new Cavalo(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
		placeNewPiece('a', 2, new Peao(board, Color.WHITE, this));
		placeNewPiece('b', 2, new Peao(board, Color.WHITE, this));
		placeNewPiece('c', 2, new Peao(board, Color.WHITE, this));
		placeNewPiece('d', 2, new Peao(board, Color.WHITE, this));
		placeNewPiece('e', 2, new Peao(board, Color.WHITE, this));
		placeNewPiece('f', 2, new Peao(board, Color.WHITE, this));
		placeNewPiece('g', 2, new Peao(board, Color.WHITE, this));
		placeNewPiece('h', 2, new Peao(board, Color.WHITE, this));

		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('b', 8, new Cavalo(board, Color.BLACK));
		placeNewPiece('d', 8, new Queen(board, Color.BLACK));
		placeNewPiece('e', 8, new King(board, Color.BLACK, this));
		placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('g', 8, new Cavalo(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 7, new Peao(board, Color.BLACK, this));
		placeNewPiece('b', 7, new Peao(board, Color.BLACK, this));
		placeNewPiece('c', 7, new Peao(board, Color.BLACK, this));
		placeNewPiece('d', 7, new Peao(board, Color.BLACK, this));
		placeNewPiece('e', 7, new Peao(board, Color.BLACK, this));
		placeNewPiece('f', 7, new Peao(board, Color.BLACK, this));
		placeNewPiece('g', 7, new Peao(board, Color.BLACK, this));
		placeNewPiece('h', 7, new Peao(board, Color.BLACK, this));

		performChessMove(new ChessPosition('e', 2), new ChessPosition('e', 4));
		performChessMove(new ChessPosition('b', 7), new ChessPosition('b', 5));
		performChessMove(new ChessPosition('e', 4), new ChessPosition('e', 5));
//		performChessMove( new ChessPosition('f', 7), new ChessPosition('f', 5)); 

	}

}
