//CODE BY Kenneth Lee
//AND 

package pieces;

import chess.ChessBoard;

/**
 * meant to create a Queen that can move in the diagonal and up,down, left and right directions
 * @author Kenneth Lee
 * @author Abriel Hernandez
 *
 */
public class Queen extends Piece {
    
    /**
     * initializes the Queen pieces with its starting positons on the board and its color
     * @param file the file the queen starts at
     * @param rank the rank the queen starts at
     * @param color the color of this perticular queen
     */
    public Queen (int file, int rank, String color) {
        super(file, rank, color);
    }

    public void printPiece() {
        if (super.getColor().equalsIgnoreCase("white")) {
            System.out.print("wQ");
        }
        else if(super.getColor().equalsIgnoreCase("black")) {
            System.out.print("bQ");
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

        if (prevFile == newFile) {
            //column stayed the same
            if(newRank > prevRank) {
                for (int i = prevRank+1; i < newRank; i++) {
                    if(Board[i][prevFile] != null) {
                        //there is a piece in the way, so illegal move
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
            else {
                for (int i = prevRank-1; i > newRank; i--) {
                    if(Board[i][prevFile] != null) {
                        //there is a piece in the way, so illegal move
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
        else if (prevRank == newRank) {
            //row stayed the same
            if(newFile > prevFile) {
                for (int i = prevFile+1; i < newFile; i++) {
                    if(Board[prevRank][i] != null) {
                        //there is a piece in the way, so illegal move
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
            else {
                for (int i = prevFile-1; i > newFile; i--) {
                    if(Board[prevRank][i] != null) {
                        //there is a piece in the way, so illegal move
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
        else if (Math.abs(newFile-prevFile) == Math.abs(newRank-prevRank)) {
            //moving diagonally
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
    				
    			}else {//moving down
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
        else {
            //not moving diagonally or along file/rank, so illegal
            chessboard.illegalMoveMessage();
            return Board;            
        }

        return newBoard;
    }

    public boolean canAttack(ChessBoard chessboard, int newFile, int newRank) {
        int prevFile = this.getFile();
        int prevRank = this.getRank();
		Piece[][] Board = chessboard.getBoard();

        if (prevFile == newFile) {
            //column stayed the same
            if(newRank > prevRank) {
                for (int i = prevRank+1; i < newRank; i++) {
                    if(Board[i][prevFile] != null) {
                        //there is a piece in the way, so illegal move
                        return false;
                    }
                }
                
                return true;
            }
            else {
                for (int i = prevRank-1; i > newRank; i--) {
                    if(Board[i][prevFile] != null) {
                        //there is a piece in the way, so illegal move
                        return false;
                    }
                }
                
                return true;
            }
        }
        else if (prevRank == newRank) {
            //row stayed the same
            if(newFile > prevFile) {
                for (int i = prevFile+1; i < newFile; i++) {
                    if(Board[prevRank][i] != null) {
                        //there is a piece in the way, so illegal move
                        return false;
                    }
                }
                
                return true;
            }
            else {
                for (int i = prevFile-1; i > newFile; i--) {
                    if(Board[prevRank][i] != null) {
                        //there is a piece in the way, so illegal move
                        return false;
                    }
                }
                
                return true;
            }
        }
        else if (Math.abs(newFile-prevFile) == Math.abs(newRank-prevRank)) {
            //moving diagonally
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
        else {
            //not moving diagonally or along file/rank, so illegal
            return false;         
        }
    }


    public String pieceDetails() {
		return this.getColor() + " queen";
	}

}