package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class program {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		ChessMatch game = new ChessMatch();
		List<ChessPiece> pecasCapturadas = new ArrayList<>();

		while (true) {
			try {
				UI.clearScreen();
				UI.printMatch(game, pecasCapturadas);
				System.out.println();
				System.out.print("Origem: ");
				ChessPosition source = UI.readChessPosition(sc);
				
				UI.clearScreen();
				UI.printBoard(game.getPieces(), game.possibleMoves(source));
				
				System.out.println();
				System.out.print("Destino: ");
				ChessPosition target = UI.readChessPosition(sc);

				ChessPiece captured = game.performChessMove(source, target);
				if (captured != null) {
					pecasCapturadas.add(captured); 
				}

			} 
			catch (ChessException e) {
				System.out.println(e.getMessage());
				System.out.println("[Tecle ENTER para continuar]");
				sc.nextLine();
			} 
			catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				System.out.println("[Tecle ENTER para continuar]");
				sc.nextLine();
			}

		}
		
		

	}

}
