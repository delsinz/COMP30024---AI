package board;

import java.util.ArrayList;

import aiproj.slider.Move;
import utils.MOPS;

public class Board {
	
	public static final int FREE = 0; // Free block
	public static final int BLCK = 1; // Blocked block
	public static final int VERT = 2; // Vertical player piece
	public static final int HORI = 3; // Horizontal player piece
	public static final char[] BLOCKS = {'+', 'B', 'V', 'H'}; // character representation of tile use as BLOCKS[board[x][y]]
	
	private byte[][] board;
	
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
	
	public Board(byte[][] bBoard){
		int dim = bBoard.length;
		
		this.board = new byte[dim][dim];
		
		for(int y = 0; y < dim; ++y){
			for(int x = 0; x < dim; ++x){
				this.board[x][y] = bBoard[x][y];
			}
		}
	}
	
	public Board(byte[][] bBoard, Move m){
		// create a new board updated with a new move
		
		int dim = bBoard.length;
		
		this.board = new byte[dim][dim];
		
		for(int y = 0; y < dim; ++y){
			for(int x = 0; x < dim; ++x){
				this.board[x][y] = bBoard[x][y];
			}
		}
		this.movePiece(m.i, m.j, m.d);
		
	}
	
	// blindly moves a piece, up to user to give a valid move
	public void movePiece(int x, int y, Move.Direction d){
		switch(d){
		// just account for moving up and right off the board, assume player gives correct moves
		case UP:
			if(y+1 > this.board.length - 1){
				// replace with free if moving off board
				this.board[x][y] = FREE;
			}
			else{
				this.board[x][y+1] = this.board[x][y];
			}
			break;
		case RIGHT:
			if(x+1 > this.board.length - 1){
				// replace with free if moving off board
				this.board[x][y] = FREE;
			}
			else{
				this.board[x+1][y] = this.board[x][y];
			}
			break;
		case DOWN:
			this.board[x][y-1] = this.board[x][y];
			break;
		case LEFT:
			this.board[x-1][y] = this.board[x][y];
			break;
		}
		this.board[x][y] = FREE;
	}
	
	// checks if a given x and y are within bounds
	public boolean withinBounds(int x, int y){
		//System.out.println("b"+x+y);
		if(x > this.board.length - 1 || x < 0 || y > this.board.length - 1 || y < 0){
			return false;
		}
		return true;
	}
	
	// publically useable version of withinBounds
	// pass it dimension of board and an x,y coord
	public static boolean withinBounds(int d, int x, int y){
		if(x > d - 1 || x < 0 || y > d - 1 || y < 0){
			return false;
		}
		return true;
	}
	
	public boolean validMove(int x, int y, Move.Direction dir){
		
		int curr = this.board[x][y];
		
		// in case you check a piece that isn't a player piece
		switch(curr){
		case FREE:
			return false;
		case BLCK:
			return false;
		case VERT:
			switch(dir){
			case UP:
				if(y == board.length - 1 || withinBounds(x, y + 1) && board[x][y+1] == Board.FREE){
					return true;
				}
				break;
			case RIGHT:
				if(withinBounds(x + 1, y) && board[x+1][y] == Board.FREE){
					return true;
				}
				break;
			case LEFT:
				if(withinBounds(x - 1, y) && board[x-1][y] == Board.FREE){
					return true;
				}
				break;
			default:
				return false;
			}
			break;
		case HORI:
			switch(dir){
			case RIGHT:
				if(x == board.length - 1 || withinBounds(x + 1, y) && board[x+1][y] == Board.FREE){
					return true;
				}
				break;
			case DOWN:
				if(withinBounds(x, y - 1) && board[x][y-1] == Board.FREE){
					return true;
				}
				break;
			case UP:
				if(withinBounds(x, y + 1) && board[x][y+1] == Board.FREE){
					return true;
				}
				break;
			default:
				return false;
			}
			break;
		default:
			return false;
		}
		
		return false;
	}
	
	/**
	 * Evaluation function ideally should look at the relative difference between you and your opponent
	 * All eval functions should return difference between opponent's state and your state
	 * So that minimax will know which move is "better"
	 * since an opponent should always move to put current player at a disadvantage
	 */
	
	/**
	 * evaluates the comparative utility of given player vs the other player
	 * @param player player to evaluate for
	 * @return relative utility of board for given player
	 */
	public int evaluate(byte player){
		// few things to look out for
		// piece moves forward -> is able to win the game
		// pieces don't become blocked by opponent or environment -> doesn't put itself at a disadvantage
		// pieces are able to put enemy at a disadvantage -> is able to hinder opponent from winning the game to some extent
		// no two or combination of two evaluations should result in the same OR contradicting information
		
		int w1 = 4;
		int w2 = 2;
		int w3 = 3;
		int w4 = 8;
		int w5 = 9001;
		// all of these evaluations should be positive aspects for player
		return (w1 * relForward(player))
				+ (w2 * relBehind(player))
				+ (w3 * relFree(player)) 
				+ (w4 * relPieces(player))
				+ (w5 * hasWon(player)); 
	}
	
	/**
	 * relative cumulative distance forward - the same for opponent
	 * @param player player to evaluate for
	 * @return relative distance travelled forward
	 */
	private int relForward(byte player){
		
		int nVOff = this.board.length - 1; // how many pieces V should have if none are off
		int nHOff = this.board.length - 1; // how many pieces H should have if none are off
		int hForward = 0;
		int vForward = 0;
		for(int y = 0; y < this.board.length; y++){
			for(int x = 0; x < this.board.length; x++){
				// total forward for V player
				switch(this.tileAt(x, y)){
				case VERT:
					--nVOff;
					vForward += y;
					break;
				// total forward for H player
				case HORI:
					--nHOff;
					hForward += x;
					break;
				}
			}
		}
		// account for fact that having less pieces SHOULD be a positive factor
		// applies for entire evaluation
		// + 1 accounts for the fact that horizontal always has the first move advantage
		hForward += nHOff * this.board.length + 1;
		vForward += nVOff * this.board.length;
		return player == HORI ? hForward - vForward : vForward - hForward;
	}
	
	/** 
	 * Relative number of pieces you have "behind" the opponent in front of you
	 * @param player player to evaluate for
	 * @return relative number of pieces in a positive state
	 */
	private int relBehind(byte player){
		// relative pieces with forward and left free for V, forward and right for H
		// don't want to get out of a bind just to get blocked again immediately after
		//
		// min just moved
		//     + + +
		//     + H +
		//     + V +
		//
		// max's turn to move
		// available moves
		//   1       2
		// + + +   + + +
		// + H + > + H +
		// V + +   + + V
		// we want to +1 to get to state 1, -1 for state 2
		// if both occur simultaneously, evaluates to 0
		// if(board[x+1][y+1] == H && board[x][y+1] == +)
		
		int nH = 0; // sum of case 1 and case 2 for H
		int nV = 0; // sum of case 1 and case 2 for V
		
		for(int y = 0; y < this.board.length; y++){
			for(int x = 0; x < this.board.length; x++){
				switch(this.tileAt(x, y)){
				case VERT:
					if(y+1 >= this.board.length){
						nV++;
					}
					else if(this.tileAt(x, y+1) == FREE){
						// left edge of board, nothing can stop you
						if(x-1 >= 0 && this.tileAt(x-1, y+1) == HORI){
							nV--;
						}
						// right edge of board, nothing to get behind
						if(x+1 < this.board.length && this.tileAt(x+1, y+1) == HORI){
							nV++;
						}
					}
					break;
				case HORI:
					if(x+1 >= this.board.length){
						nH++;
					}
					else if(this.tileAt(x+1, y) == FREE){
						
						// bottom edge of board, nothing can stop you
						if(y-1 >= 0 && this.tileAt(x+1, y-1) == VERT){
							nH--;
						}
						// top edge of board, nothing to get behind
						if(y + 1 < this.board.length && this.tileAt(x+1, y+1) == VERT){
							nH++;
						}
					}
					break;
				}
			}
		}
		return player == HORI ? nH - nV : nV - nH;
	}
	
	/**
	 * How many pieces you have free relative to your opponent
	 * Higher is better, means you have more space to move
	 * @param player player to evaluate for
	 * @return number of pieces with forward free - same for opponent
	 */
	private int relFree(byte player){
		int vFree = 0; // vertical pieces free
		int hFree = 0; // horizontal pieces free
		for(int y = 0; y < this.board.length; y++){
			for(int x = 0; x < this.board.length; x++){
				switch(this.tileAt(x, y)){
				case HORI:
					// H piece is on edge of board   H piece is in normal regions of board
					if(x + 1 == this.board.length || x + 1 < this.board.length && this.tileAt(x + 1, y) == FREE){
						hFree++;
					}
					break;
				case VERT:
					if(y + 1 == this.board.length || y + 1 < this.board.length && this.tileAt(x, y+1) == FREE){
						vFree++;
					}
					break;
				}
			}
		}
		
		return player == HORI ? hFree - vFree : vFree - hFree;
	}
	
	/**
	 * How many pieces the opponent has relative to the player
	 * Higher is better, more opponent pieces relative to yours
	 * @param player player to evaluate for
	 * @return n opponent pieces - n player's pieces
	 */
	private int relPieces(byte player){
		int hp = 0;
		int vp = 0;
		// count for each H, dist = dim(board) - curr(x)
		// count for each V, dist = dim(board) - curr(y)
		for(int y = 0; y < this.board.length; y++){
			for(int x = 0; x < this.board.length; x++){
				if(this.tileAt(x, y) == HORI){
					++hp;
				}
				else if(this.tileAt(x, y) == VERT){
					++vp;
				}
				
			}
		}
		// opponent pieces - my pieces 
		return player == VERT ? hp - vp : vp - hp;
	}
	
	/**
	 * Determines if given player has won
	 * @param player player to evaluate for
	 * @return -1 if lost, 1 if won. Adjust significance with weight
	 */
	private int hasWon(byte player){
		int hp = 0;
		int vp = 0;
		for(int y = 0; y < this.board.length; y++){
			for(int x = 0; x < this.board.length; x++){
				if(this.tileAt(x, y) == HORI){
					++hp;
				}
				else if(this.tileAt(x, y) == VERT){
					++vp;
				}
			}
		}
		if(hp == 0){
			// horizontal won
			return player == HORI ? 1 : -1;
		}
		else if(vp == 0){
			// vertical won
			return player == VERT ? 1 : -1;
		}
		else{
			// nobody won
			return 0;
		}
	}
	
	/**
	 * Entire list of available moves to a particular player
	 * Scans board twice, once for vertical moves, once for right and left moves, in that order
	 * @param player player to evaluate for
	 * @return array of moves available for that player
	 */
	public Move[] movesAvailable(byte player){
		
		ArrayList<Move> moves = new ArrayList<Move>();
		// scans array 2 times, once for forward, once for both sideways
		// when picking a move later, ties are broken by pieces which came in first
		// ideally would move forward first then sideways
		
		for(int y = 0; y < this.board.length; y++){
			for(int x = 0; x < this.board.length; x++){
				if(player == this.tileAt(x, y)){
					if(this.validMove(x, y, MOPS.forward(player))){
						moves.add(new Move(x,y,MOPS.forward(player)));
					}
				}
			}
		}
		for(int y = 0; y < this.board.length; y++){
			for(int x = 0; x < this.board.length; x++){
				if(player == this.tileAt(x, y)){
					if(this.validMove(x, y, MOPS.right(player))){
						moves.add(new Move(x,y,MOPS.right(player)));
					}
					if(this.validMove(x, y, MOPS.left(player))){
						moves.add(new Move(x,y,MOPS.left(player)));
					}
				}
			}
		}
		return moves.toArray(new Move[moves.size()]);
	}	

	/**
	 * Checks if a particular state of the game is finished or not
	 * @return true if a game is finished
	 */
	public boolean hasFinished(){
		int hp = 0;
		int vp = 0;
		for(int y = 0; y < this.board.length; y++){
			for(int x = 0; x < this.board.length; x++){
				if(this.tileAt(x, y) == HORI){
					++hp;
				}
				else if(this.tileAt(x, y) == VERT){
					++vp;
				}
			}
		}
		return hp == 0 || vp == 0;
	}
	
	public byte[][] getTiles(){
		return this.board;
	}
	
	public int getLen(){
		return this.board.length;
	}
	
	public int tileAt(int x, int y){
		return this.board[x][y];
	}
	
	public char charAt(int x, int y){
		return BLOCKS[this.board[x][y]];
	}
	
	public String toString(){
		String str = "";
		
		for(int y = this.board.length - 1; y >= 0; --y){
			
			for(int x = 0; x < this.board.length; ++x){
				str += this.charAt(x, y);
				if(x != this.board.length-1){
					str += (" ");
				}
			}
			str += "\n";
		}
		
		return str;
	}
}
