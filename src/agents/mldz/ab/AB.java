package agents.mldz.ab;

import agents.mldz.Interplay;
import aiproj.slider.Move;
import board.Board;

public class AB extends Interplay{
	//private long total = 0;
	
	public Move nextMove(){
		Move next = abDecision();
		//System.out.println("ab total evals: " + total);
		return next;
	}
	
	private Move abDecision(){
		int folds = 7;
		if(this.board.getLen() <= 5){
			folds = 10;
		}
		else if(this.board.getLen() <= 6){
			folds = 8;
		}
		else if(this.board.getLen() <= 7){
			folds = 7;
		}
		Move best = null;
		int highScore = Integer.MIN_VALUE;
		int tempScore = Integer.MIN_VALUE;
		Board nb;
		for(Move m : this.board.movesAvailable(this.me)){
			nb = new Board(this.board.getTiles(), m);
			tempScore = abVal(nb, Integer.MIN_VALUE, Integer.MAX_VALUE, this.me, folds);
			
			if(tempScore >= highScore){
				highScore = tempScore;
				best = m;
			}
		}
		//System.out.println(Board.BLOCKS[this.me] + " " + highScore);
		return best;
	}
	
	// TODO verify correctness
	private int abVal(Board b, int alpha, int beta, byte prevPlayer, int folds){
		//total++;
		Move[] moves;
		if(prevPlayer == this.op){
			moves = b.movesAvailable(this.me);
		}
		else{
			moves = b.movesAvailable(this.op);
		}
		// checking if no moves are available
		// Terminal states
		if(b.hasFinished() || folds == 0 || moves.length == 0){
			return b.evaluate(this.me);
		}
		
		// max's turn to move -> my player
		else if(prevPlayer == this.op){
			Board nb;
			for(Move m : moves){
				nb = new Board(b.getTiles(), m);
				alpha = Integer.max(alpha, abVal(nb, alpha, beta, this.me, folds - 1));
				if(alpha >= beta){
					return beta;
				}
			}
			return alpha;
		}
		
		// min's turn
		else{
			Board nb;
			for(Move m : moves){
				nb = new Board(b.getTiles(), m);
				beta = Integer.min(beta, abVal(nb, alpha, beta, this.op, folds - 1));
				if(beta <= alpha){
					return alpha;
				}
				
			}
			return beta;
		}
	}
	/*
	private int abMax(Board b, int alpha, int beta, int folds){
		total++;
		Move[] moves = b.movesAvailable(this.me);
		if(folds == 0 || b.hasFinished() || moves.length == 0){
			return b.evaluate(this.me);
		}
		Board nb;
		// evaluate my scores
		for(Move m : moves){
			nb = new Board(b.getTiles(), m);
			
			alpha = Integer.max(alpha, abMin(nb, alpha, beta, folds - 1));
			if(alpha >= beta){
				return beta;
			}
		}
		return alpha;
	}
	
	private int abMin(Board b, int alpha, int beta, int folds){
		total++;
		Move[] moves = b.movesAvailable(this.op);
		if(folds == 0 || b.hasFinished() || moves.length == 0){
			return b.evaluate(this.me);
		}
		Board nb;
		// evaluate my score for opponent's moves
		for(Move m : moves){
			nb = new Board(b.getTiles(), m);
			
			beta = Integer.min(beta, abMax(nb, alpha, beta,folds - 1));
			if(beta <= alpha){
				return alpha;
			}
		}
		return beta;
	}
	*/
}
