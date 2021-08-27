package boardgame;

public class Board {

	private Integer rows;
	private Integer columns;
	private Piece[][] pieces;

	public Board(Integer rows, Integer columns) {
		if (rows <= 0 || columns <= 0) {
			throw new BoardException("Erro criando o tabuleiro: precisamos a medida m�nima � de 1 linha e 1 coluna ");
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	public Integer getRows() {
		return rows;
	}

	public Integer getColumns() {
		return columns;
	}

	public Piece piece(int row, int column) {
		if (!positionExists(row, column)) { 
			throw new BoardException("Position not on the board.");
		}
		return pieces[row][column];
	}

	public Piece piece(Position position) {
		if (!positionExists(position)) { 
			throw new BoardException("Position not on the board.");
		}
		
		return pieces[position.getRow()][position.getColumn()];
	}

	public void placePiece(Piece piece, Position position) {
		if (thereIsAPiece(position)) { 
			throw new BoardException("There is a piece on position " + position); 
		}
		pieces[position.getRow()][position.getColumn()] = piece;
		piece.position = position;
	}

	public Piece removePiece(Position position) {
		return null;
	}

	private boolean positionExists(int row, int column) {
		return row >= 0 && row < this.rows && column >= 0 && column < this.columns;
	}

	public Boolean positionExists(Position position) {
		// Chamou a fun��o privada para confirmar se a posi��o existe,
		// pergunto-me se n�o seria o caso de escrever diretamente a valida��o
		// aqui nesse m�todo ao inv�s de chamar a fun��oprivada
		return positionExists(position.getRow(), position.getColumn());
	}

	public Boolean thereIsAPiece(Position position) {
		if (!positionExists(position)) { 
			throw new BoardException("Position not on the board.");
		}
				
		return piece(position) != null;
	}
}
