//CODE BY Kenneth Lee
//AND Abriel Hernandez

package pieces;

import chess.ChessBoard;

/**
 * creating a Bishop piece that would be allowed to move in 
 * the diagonal areas respective to its current position
 * @author Kenneth Lee
 * @author Abriel Hernandez
 *
 */
public class Bishop extends Piece{
    
    /**
     * initialization of the Bishop piece that 
     * tells its the starting position of the piece
     * and the color that we care creating
     * 
     * @param file the file location of the Bishop on the board
     * @param rank the rank location of the Bishop on the board
     * @param color the color of the Rook
     */
    public Bishop (int file, int rank, String color) {
        super(file, rank, color);
    }


    public void printPiece() {
        if (super.getColor().equalsIgnoreCase("white")) {
            System.out.print("wB");
        }
        else if(super.getColor().equalsIgnoreCase("black")) {
            System.out.print("bB");
        }
    }
    

    public Piece[][] movePiece(ChessBoard chessboard, int newFile, int newRank) {
    	
    	int prevFile = this.getFile();
        int prevRank = this.getRank();
		Piece[][] Board = chessboard.getBoard();
        Piece[][] newBoard = Board;
		Piece targetPiece = null;

		if(Board[newRank][newFile] != null) {
			targetPiece = Board[newRank][newFile];
		}
    	
    	//not moving diagonally
    	if(Math.abs(newFile - prevFile) != Math.abs(newRank - prevRank)) {
    		chessboard.illegalMoveMessage();
    		return Board;
    	}
		else {// is moving diagonally
    		
    		if((newFile-prevFile) > 0) { //moving to the right
    			if((newRank - prevRank)> 0) {//moving up
    				for(int i = 1; i < Math.abs(newRank - prevRank); i++ ) {
    					if(Board[prevRank+i][prevFile+i] != null) {
    						//something is blocking way
    						chessboard.illegalMoveMessage();
    			    		return Board;
    					}
    				}
    				newBoard = super.finalizeMove(newBoard, this, prevFile, prevRank, newFile, newRank);
					if(super.ownKingChecked(chessboard, newBoard)) {
						//move put own king in check, so illegal
						Board = super.revertMove(newBoard, this, targetPiece, prevFile, prevRank, newFile, newRank);
						return Board;
					}
    			}else {//moving down
    				for(int i = 1; i < Math.abs(newRank - prevRank); i++ ) {
    					if(Board[prevRank-i][prevFile+i] != null) {
    						//somthing is blocking way
    						chessboard.illegalMoveMessage();
    			    		return Board;
    					}
    				}
    				newBoard = super.finalizeMove(newBoard, this, prevFile, prevRank, newFile, newRank);
					if(super.ownKingChecked(chessboard, newBoard)) {
						//move put own king in check, so illegal
						Board = super.revertMove(newBoard, this, targetPiece, prevFile, prevRank, newFile, newRank);
						return Board;
					}
    			}		
    		}
			else {//newfile - prevfile is negative so moving left
    			if((newRank-prevRank)>0) { //moving up
    				for(int i = 1; i < Math.abs(newRank - prevRank); i++ ) {
    					if(Board[prevRank+i][prevFile-i] != null) {
    						//somthing is blocking way
    						chessboard.illegalMoveMessage();
    			    		return Board;
    					}
    				}
    				newBoard = super.finalizeMove(newBoard, this, prevFile, prevRank, newFile, newRank);
					if(super.ownKingChecked(chessboard, newBoard)) {
						//move put own king in check, so illegal
						Board = super.revertMove(newBoard, this, targetPiece, prevFile, prevRank, newFile, newRank);
						return Board;
					}
    			}
				else {//moving down
    				for(int i = 1; i < Math.abs(newRank - prevRank); i++ ) {
    					if(Board[prevRank-i][prevFile-i] != null) {
    						//somthing is blocking way
    						chessboard.illegalMoveMessage();
    			    		return Board;
    					}
    				}
    				newBoard = super.finalizeMove(newBoard, this, prevFile, prevRank, newFile, newRank);
					if(super.ownKingChecked(chessboard, newBoard)) {
						//move put own king in check, so illegal
						Board = super.revertMove(newBoard, this, targetPiece, prevFile, prevRank, newFile, newRank);
						return Board;
					}			
    			}		
    		}	
    	}
    	
    	return newBoard;
    }//movePiece closing

	
	public boolean canAttack(ChessBoard chessboard, int newFile, int newRank) {
		int prevFile = this.getFile();
        int prevRank = this.getRank();
		Piece[][] Board = chessboard.getBoard();
    	
    	//not moving diagonally
    	if(Math.abs(newFile - prevFile) != Math.abs(newRank - prevRank)) {
    		return false;	
    	}
		else {// is moving diagonally
    		
    		if((newFile-prevFile) > 0) { //moving to the right
    			if((newRank - prevRank)> 0) {//moving up
    				for(int i = 1; i < Math.abs(newRank - prevRank); i++ ) {
    					if(Board[prevRank+i][prevFile+i] != null) {
    						//something is blocking way
    						return false;
    					}
    				}
    				return true;
    			}else {//moving down
    				for(int i = 1; i < Math.abs(newRank - prevRank); i++ ) {
    					if(Board[prevRank-i][prevFile+i] != null) {
    						//somthing is blocking way
    						return false;
    					}
    				}
    				return true;
    			}		
    		}
			else {//newfile - prevfile is negative so moving left
    			if((newRank-prevRank)>0) { //moving up
    				for(int i = 1; i < Math.abs(newRank - prevRank); i++ ) {
    					if(Board[prevRank+i][prevFile-i] != null) {
    						//somthing is blocking way
    						return false;
    					}
    				}
    				return true;
    				
    			}else {//moving down
    				for(int i = 1; i < Math.abs(newRank - prevRank); i++ ) {
    					if(Board[prevRank-i][prevFile-i] != null) {
    						//somthing is blocking way
    						return false;
    					}
    				}
    				return true;		
    			}		
    		}	
    	}
	}
    

	public String pieceDetails() {
		return this.getColor() + " bishop";
	}
    
}