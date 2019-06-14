package agents.mldz.heuristic;

import agents.mldz.Interplay;
import aiproj.slider.Move;
import board.Board;

public class Heuristic extends Interplay{
	
	@Override
	public Move nextMove(){
		Move best = null;
		float score = Integer.MIN_VALUE;
		float tempScore = Integer.MIN_VALUE;
		Board nb;
		
		//System.out.println(this.me + " Boards after possible moves");
		for(Move m : this.board.movesAvailable(this.me)){
			nb = new Board(this.board.getTiles(), m);
			tempScore = nb.evaluate(this.me);
			if(tempScore > score){
				score = tempScore;
				best = m;
			}
			//Board nb = new Board(this.board.getTiles(), m);
			//System.out.println(tempScore + "\n" + nb.toString());
			
		}
		System.out.println(Board.BLOCKS[this.me] + " " + score + "\n");
		return best;
	}
}
