package agents.mldz.forwardfirst;

import aiproj.slider.Move;
import board.Board;
import utils.MOPS;

import agents.mldz.Interplay;


public class ForwardFirst extends Interplay {
	
	// init doesn't need change
	
	// update doesn't need change
	
	// updated move() 
	// finds the most preferred move in the order below
	// V: Up Left Right   (Forward Left Right)
	// H: Right Down Left (Forward Right Left)
	// reasoning is that it is preferred to go forward, then behind enemy lines, then in front of enemy lines
	@Override
	public Move nextMove(){
		int x, y;
		
		// don't put this against itself
		switch(this.me){
		case Board.HORI:
			// scan column by column top to bottom left to right
			// first pass forward
			for(x = 0; x < this.board.getLen(); x++){
				for(y = this.board.getLen() - 1; y >= 0; y--){
					// find available forward move
					if(this.isMe(x, y) && this.board.validMove(x, y, MOPS.forward(this.me))){
						return new Move(x,y,MOPS.forward(this.me));
					}
				}
			}
			
			// scan column by column bottom to top left to right
			// second pass right
			for(x = 0; x < this.board.getLen(); x++){
				for(y = 0; y < this.board.getLen(); y++){
					// find available right move
					if(this.isMe(x, y) && this.board.validMove(x, y, MOPS.right(this.me))){
						return new Move(x,y,MOPS.right(this.me));
					}
				}
			}
			
			// scan column by column top to bottom left to right
			// last pass left
			for(x = 0; x < this.board.getLen(); x++){
				for(y = this.board.getLen() - 1; y >= 0; y--){
					// find available left move
					if(this.isMe(x, y) && this.board.validMove(x, y, MOPS.left(this.me))){
						return new Move(x,y,MOPS.left(this.me));
					}
				}
			}
			break;
		case Board.VERT:
			// scan row by row right to left bottom to top
			// first pass forward
			for(y = 0; y < this.board.getLen(); y++){
				for(x = this.board.getLen() - 1; x >= 0; x--){
					// find available forward move
					if(this.isMe(x, y) && this.board.validMove(x, y, MOPS.forward(this.me))){
						return new Move(x,y,MOPS.forward(this.me));
					}
				}
			}
			
			// scan row by row left to right bottom to top
			// second pass left
			for(y = 0; y < this.board.getLen(); y++){
				for(x = 0; x < this.board.getLen(); x++){
					if(this.isMe(x, y) && this.board.validMove(x, y, MOPS.left(this.me))){
						return new Move(x,y,MOPS.left(this.me));
					}
				}
			}
			
			// scan row by row right to left bottom to top
			// last pass right
			for(y = 0; y < this.board.getLen(); y++){
				for(x = this.board.getLen() - 1; x >= 0; x--){
					// find available right move
					if(this.isMe(x, y) && this.board.validMove(x, y, MOPS.right(this.me))){
						return new Move(x,y,MOPS.right(this.me));
					}
				}
			}
			
			break;
		}
		
		return null;
	}
}
