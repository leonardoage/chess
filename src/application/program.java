package application;

import java.util.InputMismatchException;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class program {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		ChessMatch game = new ChessMatch();

		while (true) {
			try {
				UI.clearScreen();
				UI.printBoard(game.getPieces());
				System.out.println();
				System.out.print("Origem: ");
				ChessPosition source = UI.readChessPosition(sc);
				System.out.println();
				System.out.print("Destino: ");
				ChessPosition target = UI.readChessPosition(sc);

				ChessPiece captured = game.performChessMove(source, target);
			} catch (ChessException e) {
				System.out.println(e.getMessage());
				System.out.println("[Tecle ENTER para continuar]");
				sc.nextLine();
			} catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				System.out.println("[Tecle ENTER para continuar]");
				sc.nextLine();
			}

		}

	}

}
