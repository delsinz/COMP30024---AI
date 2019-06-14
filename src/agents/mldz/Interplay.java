package agents.mldz;

import aiproj.slider.SliderPlayer;
import board.Board;
import java.util.Random;

import aiproj.slider.Move;

/** base class with some foundation functions to help with generating moves
 * e.g. listing all available moves and counting number of available moves etc.
 */
public class Interplay implements SliderPlayer{
	
	protected Board board;
	protected byte me;
	protected byte op;
	
	@Override
	public void init(int dimension, String board, char player) {
		// TODO get board representation
		this.board = new Board(Board.convertBoard(dimension, board));
		
		if(player == 'H'){
			this.me = Board.HORI;
			this.op = Board.VERT;
		}
		else{
			this.me = Board.VERT;
			this.op = Board.HORI;
		}
		
	}

	@Override
	public void update(Move move) {
		// update based on opponent's move;
		if(move != null){
			this.board.movePiece(move.i, move.j, move.d);
		}
	}

	@Override
	// Handles accepting a new move and updating board state.
	// Saves each player from having to have update(next)
	public Move move() {
		
		Move next = nextMove();
		
		update(next);
		
		return next;
	}
	
	public Move nextMove(){
		Random r = new Random();
		// base movement, random
		Move[] m = this.board.movesAvailable(this.me);
		
		return m.length > 0 ? m[r.nextInt(m.length)] : null;
	}
	
	// TODO method to log move
	public void logMove(Move m){
		
	}
	
	/*
	// takes a player char as it could be used to check opponent's moves as well
	protected Move[] movesAvailable(byte player){
		
		ArrayList<Move> moves = new ArrayList<Move>();
		// scans array 2 times, once for forward, once for both sideways
		// when picking a move later, ties are broken by pieces which came in first
		// ideally would move forward first then sideways
		
		for(int y = 0; y < this.board.getLen(); y++){
			for(int x = 0; x < this.board.getLen(); x++){
				if(player == this.board.tileAt(x, y)){
					if(this.board.validMove(x, y, MOPS.forward(player))){
						moves.add(new Move(x,y,MOPS.forward(player)));
					}
				}
			}
		}
		for(int y = 0; y < this.board.getLen(); y++){
			for(int x = 0; x < this.board.getLen(); x++){
				if(player == this.board.tileAt(x, y)){
					if(this.board.validMove(x, y, MOPS.right(player))){
						moves.add(new Move(x,y,MOPS.right(player)));
					}
					if(this.board.validMove(x, y, MOPS.left(player))){
						moves.add(new Move(x,y,MOPS.left(player)));
					}
				}
			}
		}
		return moves.toArray(new Move[moves.size()]);
	}
	*/
	protected boolean isMe(int x, int y){
		return this.board.tileAt(x, y) == this.me;
	}	
}
