package application;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

public class UI {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public static ChessPosition readChessPosition(Scanner sc) {
		try {
			String s = sc.nextLine();
			char column = s.toLowerCase().charAt(0);
			int row = Integer.parseInt(s.substring(1));
			return new ChessPosition(column, row);

		} catch (RuntimeException e) {
			throw new InputMismatchException("Erro lendo posicao da peca. Valores validos sao de A1 a H8");
		}

	}

	public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
		System.out.println("  a b c d e f g h  ");
		for (int i = 0; i < pieces.length; i++) {
			System.out.print(pieces.length - i + " ");
			for (int j = 0; j < pieces.length; j++) {
				printPiece(pieces[i][j], possibleMoves[i][j]);
			}
			System.out.println();
		}

	}

	public static void printMatch(ChessMatch partida, List<ChessPiece> pecasCapturadas) {
		printBoard(partida.getPieces());
		System.out.println();
		printCapturesPieces(pecasCapturadas);
		System.out.println();
		System.out.println("Turn: " + partida.getTurn());
		System.out.println("Wating player: " + (partida.getCurrentPlayer() == Color.BLACK? ANSI_YELLOW : ANSI_WHITE) +  partida.getCurrentPlayer() + ANSI_RESET) ;
		
		if (partida.getCheck()) {
			System.out.println("Check!!!");
			
		}		
	}

	public static void printBoard(ChessPiece[][] pieces) {
		System.out.println("  a b c d e f g h  ");
		for (int i = 0; i < pieces.length; i++) {
			System.out.print(pieces.length - i + " ");
			for (int j = 0; j < pieces.length; j++) {
				printPiece(pieces[i][j], false);
			}
			System.out.println();
		}

	}

	private static void printPiece(ChessPiece piece, boolean background) {

		if (background) {
			System.out.print(ANSI_BLUE_BACKGROUND);
		}

		if (piece == null) {
			System.out.print("-" + ANSI_RESET);
		} else {
			if (piece.getColor() == Color.WHITE) {
				System.out.print(ANSI_WHITE + piece + ANSI_RESET);
			} else {
				System.out.print(ANSI_YELLOW + piece + ANSI_RESET);
			}
		}
		System.out.print(" ");
	}

	private static void printCapturesPieces(List<ChessPiece> captured) {

		List<ChessPiece> whitePieces = captured.stream().filter(x -> x.getColor() == Color.WHITE)
				.collect(Collectors.toList());
		List<ChessPiece> blackPieces = captured.stream().filter(x -> x.getColor() == Color.BLACK)
				.collect(Collectors.toList());

		System.out.println("Pecas Capturadas: ");
		System.out.print("BRANCAS: ");

		System.out.print(ANSI_WHITE);
		System.out.print(Arrays.toString(whitePieces.toArray()));
		System.out.print(ANSI_RESET);
		System.out.println(); 
		System.out.print("PRETAS:  ");
		System.out.print(ANSI_YELLOW);
		System.out.print(Arrays.toString(blackPieces.toArray()));
		System.out.print(ANSI_RESET);
		System.out.println(); 
		

	}
}
