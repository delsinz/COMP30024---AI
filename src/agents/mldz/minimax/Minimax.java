package agents.mldz.minimax;

import agents.mldz.Interplay;
import aiproj.slider.Move;
import board.Board;

public class Minimax extends Interplay{
	private long total = 0;
	
	public Move nextMove(){
		Move next = minimaxDecision();
		//System.out.println("MM total evals: " + total);
		return next;
	}
	
	private Move minimaxDecision(){
		
		Move best = null;
		int highScore = Integer.MIN_VALUE;
		int tempScore = Integer.MIN_VALUE;
		Board nb;
		for(Move m : this.board.movesAvailable(this.me)){
			
			nb = new Board(this.board.getTiles(), m);
			tempScore = minimaxVal(nb, this.me, 4);
			
			if(tempScore >= highScore){
				highScore = tempScore;
				best = m;
			}
			
			
		}
		System.out.println(Board.BLOCKS[this.me] + " " + highScore);
		return best;
	}
	
	private int minimaxVal(Board b, byte player, int folds){
		//total++;
		Move[] moves;
		if(player == this.op){
			moves = b.movesAvailable(this.me);
		}
		else{
			moves = b.movesAvailable(this.op);
		}
		// checking if no moves are available eliminates situations where
		// player picks redundant moves to keep opponent in a deadlock
		// Terminal states
		if(b.hasFinished() || folds == 0 || moves.length == 0){
			return b.evaluate(this.me);
		}
		// my turn to move, max
		else if(player == this.op){
			Board nb;
			// TODO how to deal with having no moves? what score to return?
			int score = Integer.MIN_VALUE;
			int tempScore = Integer.MIN_VALUE;
			
			for(Move m : moves){
				nb = new Board(b.getTiles(), m);
				
				tempScore = minimaxVal(nb, this.me, folds - 1);
				
				if(tempScore > score){
					score = tempScore;
				}
			}
			return score;
		}
		// opponent's turn to move, min
		else{
			Board nb;
			// TODO how to deal with having no moves? what score to return?
			int score = Integer.MAX_VALUE;
			int tempScore = Integer.MAX_VALUE;
			
			for(Move m : moves){
				nb = new Board(b.getTiles(), m);
				
				// evaluate board score for ME given opponent's moves
				tempScore = minimaxVal(nb, this.op, folds - 1);
				
				if(tempScore < score){
					score = tempScore;
				}
			}
			return score;
		}
	}
	
}
