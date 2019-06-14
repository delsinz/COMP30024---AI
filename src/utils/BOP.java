package utils;

import aiproj.slider.Move;

/** Bunch of Functions useful for creating and printing the board 
 * as well as numbers corresponding to what each digit represents
 * */

public class BOP { // short for board ops
	// numbers corresponding to tile type
	public static final int FREE = 0;
	public static final int BLCK = 1;
	public static final int VERT = 2;
	public static final int HORI = 3;
	public static final char[] BLOCKS = {'+', 'B', 'V', 'H'};
	
	public static byte[][] convertBoard(int dimension, String board){
		byte[][] a = new byte[dimension][dimension];
		int s = 0;
		
		String[] rows = board.replaceAll(" ", "").split("\n");
		
		
		for(int y = dimension - 1; y >= 0; --y){
			for(int x = 0; x < dimension; ++x){
				switch(rows[s].charAt(x)){
				case '+':
					a[x][y] = FREE;
					break;
				case 'B':
					a[x][y] = BLCK;
					break;
				case 'V':
					a[x][y] = VERT;
					break;
				case 'H':
					a[x][y] = HORI;
					break;
				}
			}
			++s;
		}
		return a;
	}
	
	public static void movePiece(byte[][] board, int x, int y, Move.Direction d){
		switch(d){
		// just account for moving up and right off the board, assume player gives correct moves
		case UP:
			if(y+1 < board.length - 1){
				// only replace with value if not moving off board
				board[x][y] = FREE;
			}
			else{
				board[x][y+1] = board[x][y];
			}
			break;
		case RIGHT:
			if(x+1 < board.length - 1){
				board[x][y] = FREE;
			}
			else{
				board[x+1][y] = board[x][y];
			}
			break;
		case DOWN:
			board[x][y-1] = board[x][y];
			break;
		case LEFT:
			board[x-1][y] = board[x][y];
			break;
		}
		board[x][y] = FREE;
	}
	// returns (x,y, type) of first collision starting from (x,y), casting vertically
	// if type is 0, signifies end of board (still stays within bounds)
	public static byte[] castVert(byte[][] board, int x, int y, boolean forward){
		int dir = forward ? 1 : -1;
		
		while(withinBounds(board,x,y + dir) && board[x][y + dir] == FREE){
			y += dir;
		}
		if(!withinBounds(board,x,y + dir)){
			return new byte[] {(byte)x,(byte)y,board[x][y]};
		} 
		y += dir;
		return new byte[] {(byte)x,(byte)(y),board[x][y]};
	}
	
	// returns (x,y, type) of first collision starting from (x,y), casting Horizontally
	// if type is 0, signifies end of board (still stays within bounds)
	public static byte[] castHori(byte[][] board, int x, int y, boolean right){
		int dir = right ? 1 : -1;
		
		while(withinBounds(board, x + dir, y) && board[x + dir][y] == FREE){
			x += dir;
		}
		if(!withinBounds(board,x + dir,y)){
			return new byte[] {(byte)x,(byte)y,board[x][y]};
		}
		x += dir;
		return new byte[] {(byte)(x),(byte)y,board[x][y]};
	}
	
	// returns (x,y, type) of first collision starting from (x,y), casting Diagonally along / line
	// if type is 0, signifies end of board (still stays within bounds)
	public static byte[] castDiagUp(byte[][] board, int x, int y, boolean up_right){
		int dir = up_right ? 1 : -1;
		
		while(withinBounds(board, x + dir, y + dir) && board[x + dir][y + dir] == FREE){
			x += dir;
			y += dir;
		}
		if(!withinBounds(board,x + dir, y + dir)){
			return new byte[] {(byte)x,(byte)y,board[x][y]};
		}
		x += dir;
		y += dir;
		return new byte[] {(byte)x,(byte)y,board[x][y]};
	}
	
	// returns (x,y, type) of first collision starting from (x,y), casting Diagonally along \ line
	// if type is 0, signifies end of board (still stays within bounds)
	public static byte[] castDiagDown(byte[][] board, int x, int y, boolean down_right){
		int dir = down_right ? 1 : -1;
		
		while(withinBounds(board, x + dir, y - dir) && board[x + dir][y - dir] == FREE){
			x += dir;
			y -= dir;
		}
		if(!withinBounds(board,x + dir, y - dir)){
			return new byte[] {(byte)x,(byte)y,board[x][y]};
		}
		x += dir;
		y -= dir;
		return new byte[] {(byte)x,(byte)y,board[x][y]};
	}
	
	// checks if a given x and y are within bounds
	public static boolean withinBounds(byte[][] board, int x, int y){
		if(x > board.length - 1 || x < 0 || y > board.length - 1 || y < 0){
			return false;
		}
		return true;
	}
	
	public static void printBoard(byte[][] board){
		for(int y = board.length - 1; y >= 0; --y){
			for(int x = 0; x < board.length; ++x){
				System.out.print(BLOCKS[board[x][y]]);
				if(x != board.length-1){
					System.out.print(" ");
				}
			}
			System.out.println();
		}
	}
	/*
	// int digit manipulation
	public static int[] convertBoard(int dimension, String board){
		int[] iBoard = new int[dimension];
		
		String[] rows = board.replaceAll(" ", "").split("\n");
		
		int r = 0;
		
		for(int i = dimension - 1; i >= 0; --i){
			iBoard[r++] = convertRow(rows[i]);
		}
		return iBoard;
		
	}
	
	public static int convertRow(String row){
		int iRow = 0;
		int rowLen = row.length();
		for(int i = 0; i < rowLen; i++){
			switch(row.charAt(i)){
			case '+':
				iRow = Row.append(iRow, FREE);
				break;
			case 'B':
				iRow = Row.append(iRow, BLCK);
				break;
			case 'V':
				iRow = Row.append(iRow, VERT);
				break;
			case 'H':
				iRow = Row.append(iRow, HORI);
				break;
			default:
				break;
			}
		}
		
		return iRow;
	}
	
	public static void movePiece(int[] iBoard, int x, int y, char move){
		switch(move){
		// just account for moving up and right off the board, assume player gives correct moves
		case 'u':
			if(y+1 < iBoard.length - 1){
				// only replace with value if not moving off board
				Row.replaceVal(iBoard, Row.getVal(iBoard, x, y), x, y+1);
			}
			break;
		case 'r':
			if(x+1 < iBoard.length - 1){
				Row.replaceVal(iBoard, Row.getVal(iBoard, x, y), x+1, y);
			}
			break;
		case 'd':
			Row.replaceVal(iBoard, Row.getVal(iBoard, x, y), x, y-1);
			break;
		case 'l':
			Row.replaceVal(iBoard, Row.getVal(iBoard, x, y), x-1, y);
			break;
		}
		
		Row.replaceVal(iBoard, Board.FREE, x, y);
	}
	
	public static void printiBoard(int[] iBoard){
		// prints board from top left corner to bottom right
		// bottom left corner is (0,0)
		// to access digit by digit we increment y slowest
		// i.e. for(y){for(x){}}
		for(int r = iBoard.length - 1; r >= 0; --r){
			System.out.println(iBoard[r]);
		}
	}
	*/
}
