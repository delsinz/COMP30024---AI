package utils;

/** Useful functions in accessing and manipulating the int board iBoard 
 * Entirely eliminated use of strings!
 * */

public class Row {
	// converts a list of ints in range 0-9 to a singular int
	public static int collapse(int... rowVals){
		int collapsed = 0;
		int factor = 1;
		for(int i = rowVals.length - 1; i >= 0; --i){
			collapsed += rowVals[i] * factor;
			factor *= 10;
		}
		return collapsed;
		
	}
	
	// adds number to end of row, only works for +ve ints
	public static int append(int row, int add){
		// much faster method
		// java doesn't display leading 0s of an int
		// values have to be > 0
		return row * n10(add) + add;
	}
	
	// adds number to front of row, only works for any +ve ints
	public static int prepend(int row, int add){
		// fast method
		return row <= 0 ? add : add * n10(row)  + row;
	}
	
	// function to retrieve a single digit of an int at specified index of board
	// (0,0) is bottom left corner, follows cartesian coords
	public static int getVal(int[] iBoard, int x, int y){
		int row = iBoard[y];
		
		int curr = iBoard.length; // uses property that a board is an nxn sized board to get n
		int d;
		int k = -1;
		
		// lists numbers from right to left,
		// meaning index increments high to low
		while(row > 0 && curr != x){
			d = row / 10;
			k = row - d * 10;
			row = d;
			--curr;
		}
		return k;
	}
	
	// faster now
	// only works to replace single digits
	// replaces value at (x,y) with rep 
	public static void replaceVal(int[] iBoard, int rep, int x, int y){
		int row = iBoard[y];
		int newRow = 0;
		// actually index currently at
		int curr = iBoard.length - 1; // uses property that a board is an nxn sized board to get n
		while (row > 0) {
			int d = row / 10;
			int k = row - d * 10;
			row = d;
			// k is the right hand most value
			// row is what's left after removing k
			
			// if not at ind, prepend k,
			// otherwise prepend rep
			// prepend row and return
			if(curr == x){
				newRow = prepend(newRow, rep);
				iBoard[y] = prepend(newRow, row);
				return;
				// no need for a return value as modifying an array is the equivalent of *(p+i) = n in c
			}
			newRow = prepend(newRow, k);
			--curr;
		}
	}
	
	// returns lowest power of 10 of an int
	// i.e. 2930 => 1000
	private static int n10(int row){
		int len = intLen(row);
		int tens = 1;
		for(int i = 0; i < len;++i){
			tens *= 10;
		}
		return tens;
		
	}
	
	// basically same as return (int) Math.floor(Math.log10(row)); 
	// but faster and simplified for this application 
	private static int intLen(int row){
		int len = 1;
		while(row > 9){
			row/=10;
			len++;
		}
		return len;
	}
}
