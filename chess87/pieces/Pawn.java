//CODE BY Kenneth Lee
//AND 

package pieces;

import chess.ChessBoard;

/**
 * pawn pieces that can move forward 1 square and 2 squares forward if its the pawn first move
 *  and diagonal 1 square if an enemy is diagonal to it
 * it is allowed to do a move called enpassan where it can move diagonal toward the opposit side
 * if the piece to its side is of the opposit color and its previous move was a jump 2 squares forward
 * @author Kenneth Lee
 * @author Abriel Hernandez
 *
 */
public class Pawn extends Piece {
    
	private boolean firstmove = true;// if its a first move we can move 2 spaces
	private String promotionChoice = "Q";
	private int first_jump = -1;

    /**
     * creates the Pawn piece for the board
     * 
     * @param file the new File location the user wants
     * @param rank the new rank location the user wants for the pawn
     * @param color the color of the pawn
     */
    public Pawn (int file, int rank, String color) {
        super(file, rank, color);
    }

    public void printPiece() {
        if (super.getColor().equalsIgnoreCase("white")) {
            System.out.print("wp");
        }
        else if(super.getColor().equalsIgnoreCase("black")) {
            System.out.print("bp");
        }
    }

    /**
     *
     */
    public Piece[][] movePiece(ChessBoard chessboard, int newFile, int newRank) {
    	int prevFile = this.getFile();
        int prevRank = this.getRank();
        Piece[][] Board = chessboard.getBoard();
        Piece[][] newBoard = Board;
		Piece targetPiece = null;

		if(Board[newRank][newFile] != null) {
			targetPiece = Board[newRank][newFile];
		}

        int maxDist = 1; //max distance the pawn can move
        if(this.firstmove) {
        	maxDist = 2;
        }
        
		//enemy on target square, since chessboard checks for opposite team
        if(chessboard.getPieceAtSquare(newFile, newRank) != null) {
			if (this.getColor().equals("white")) {
				//white pawn, so rank has to increase by 1
				if (newRank - prevRank == 1 && Math.abs(newFile-prevFile) == 1) {
					//have to check if rank == 7 (8 on the board)
					//if so, then that means promote to Queen since no arg was provided
					if(newRank == 7) {
						Piece promotedPiece = getPromotedPiece(newFile, newRank);
						newBoard = super.finalizeMove(newBoard, promotedPiece, prevFile, prevRank, newFile, newRank);
						if(super.ownKingChecked(chessboard, newBoard)) {
							//move put own king in check, so illegal
							Board = super.revertMove(newBoard, this, targetPiece, prevFile, prevRank, newFile, newRank);
							return Board;
						}
						return newBoard;
					}
					else {
						newBoard = super.finalizeMove(newBoard, this, prevFile, prevRank, newFile, newRank);
						if(super.ownKingChecked(chessboard, newBoard)) {
							//move put own king in check, so illegal
							Board = super.revertMove(newBoard, this, targetPiece, prevFile, prevRank, newFile, newRank);
							return Board;
						}

						if(this.firstmove == true) {
							this.firstmove = false;
						}

            			return newBoard;
					}
				}
				else {
					chessboard.illegalMoveMessage();
					return Board;
				}
			}
			else if (this.getColor().equals("black")) {
				//black pawn, so rank has to decrease by 1
				if (prevRank - newRank == 1 && Math.abs(newFile-prevFile) == 1) {
					//have to check if rank == 0 (1 on the board)
					//if so, then that means promote to Queen since no arg was provided
					if(newRank == 0) {
						Piece promotedPiece = getPromotedPiece(newFile, newRank);
						newBoard = super.finalizeMove(newBoard, promotedPiece, prevFile, prevRank, newFile, newRank);
						if(super.ownKingChecked(chessboard, newBoard)) {
							//move put own king in check, so illegal
							Board = super.revertMove(newBoard, this, targetPiece, prevFile, prevRank, newFile, newRank);
							return Board;
						}
						return newBoard;
					}
					else {
						newBoard = super.finalizeMove(newBoard, this, prevFile, prevRank, newFile, newRank);
						if(super.ownKingChecked(chessboard, newBoard)) {
							//move put own king in check, so illegal
							Board = super.revertMove(newBoard, this, targetPiece, prevFile, prevRank, newFile, newRank);
							return Board;
						}

						if(this.firstmove == true) {
							this.firstmove = false;
						}

            			return newBoard;
					}
				}
				else {
					chessboard.illegalMoveMessage();
					return Board;
				}
			}
        }
        //if enpassant 
		if(Math.abs(newFile-prevFile) ==1 && Math.abs(newRank-prevRank) ==1) { //checks if the file changes by 1
			if(this.getColor().equals("white")) {
				if(Board[this.getRank()][newFile] != null && !Board[this.getRank()][newFile].getColor().equals("white")) {
					if(Board[this.getRank()][newFile] instanceof Pawn && this.getRank() == 4 ) {
						if( chessboard.round_count-1 == ((Pawn)Board[this.getRank()][newFile]).first_jump ) {
							targetPiece = newBoard[this.getRank()][newFile];
							newBoard[this.getRank()][newFile] = null;//delets the enpassant piece
							newBoard = super.finalizeMove(newBoard, this, prevFile, prevRank, newFile, newRank);
							if(super.ownKingChecked(chessboard, newBoard)) {
								//move put own king in check, so illegal
								Board = super.revertMove(newBoard, this, targetPiece, prevFile, prevRank, newFile, newRank);
								return Board;
							}

							return newBoard;
						}
						else {
							chessboard.illegalMoveMessage();
							return Board;
						}
					}
					else {
						chessboard.illegalMoveMessage();
						return Board;
					}
				}
				else {
					chessboard.illegalMoveMessage();
					return Board;
				}
			}
			else {// black
				if(Board[this.getRank()][newFile] != null && !Board[this.getRank()][newFile].getColor().equals("black")) {
					if(Board[this.getRank()][newFile] instanceof Pawn && this.getRank() == 3 ) {
						if( chessboard.round_count-1 == ((Pawn)Board[this.getRank()][newFile]).first_jump ) {
							targetPiece = newBoard[this.getRank()][newFile];
							newBoard[this.getRank()][newFile] = null;//delets the enpassant piece
							newBoard = super.finalizeMove(newBoard, this, prevFile, prevRank, newFile, newRank);
							if(super.ownKingChecked(chessboard, newBoard)) {
								//move put own king in check, so illegal
								Board = super.revertMove(newBoard, this, targetPiece, prevFile, prevRank, newFile, newRank);
								return Board;
							}
							
							return newBoard;
						}
						else {
							chessboard.illegalMoveMessage();
							return Board;
						}
					}
					else {
						chessboard.illegalMoveMessage();
						return Board;
					}
				}
				else {
					chessboard.illegalMoveMessage();
					return Board;
				}
			}
		}

		//no enemy on target square, so standard pawn move
    	if(newFile != prevFile) { //file can't change
        	chessboard.illegalMoveMessage();
            return Board;
        }
		else {
        	
        	if((this.getColor().equals("white") && (newRank > (prevRank+maxDist) || newRank < prevRank))
				|| (this.getColor().equals("black") && (newRank < (prevRank-maxDist) || newRank > prevRank))) {
				//makes sure that pawns can only move 1 or 2 squares in their allowed direction
        		chessboard.illegalMoveMessage();
                return Board;
        	}
			else if((this.firstmove == true) &&
					((this.getColor() == "white" && chessboard.getPieceAtSquare(prevFile, prevRank+1) != null)
					|| (this.getColor() == "black" && chessboard.getPieceAtSquare(prevFile, prevRank-1) != null))) {
						//check for first move, if so, then check for collision 1 square ahead
						//since we already made sure the next move is 1/2 spots forward
						//and if code reaches here, then there was no piece on the square
						//if pawn is moving up 1, it will pass right through this if statement
						//if pawn is moving up 2, have to check inbetween the squares for collision
						chessboard.illegalMoveMessage();
						return Board;
        	}
			else {
				//check for promotion
				if(newRank == 7 && this.getColor().equals("white")) {
					Piece promotedPiece = getPromotedPiece(newFile, newRank);
					newBoard = super.finalizeMove(newBoard, promotedPiece, prevFile, prevRank, newFile, newRank);
					if(super.ownKingChecked(chessboard, newBoard)) {
						//move put own king in check, so illegal
						Board = super.revertMove(newBoard, this, targetPiece, prevFile, prevRank, newFile, newRank);
						Board[newRank][newFile] = null;
						return Board;
					}
				}
				else if(newRank == 0 && this.getColor().equals("black")) {
					Piece promotedPiece = getPromotedPiece(newFile, newRank);
					newBoard = super.finalizeMove(newBoard, promotedPiece, prevFile, prevRank, newFile, newRank);
					if(super.ownKingChecked(chessboard, newBoard)) {
						//move put own king in check, so illegal
						Board = super.revertMove(newBoard, this, targetPiece, prevFile, prevRank, newFile, newRank);
						Board[newRank][newFile] = null;
						return Board;
					}
				}
				else {
					newBoard = super.finalizeMove(newBoard, this, prevFile, prevRank, newFile, newRank);
					if(super.ownKingChecked(chessboard, newBoard)) {
						//move put own king in check, so illegal
						Board = super.revertMove(newBoard, this, targetPiece, prevFile, prevRank, newFile, newRank);
						return Board;
					}

					if(this.firstmove == true) {
						this.firstmove = false;

						if(Math.abs(newRank - prevRank) == 2) {
							first_jump = chessboard.round_count; //has to move forward two squares, otherwise ignore
						}
					}

				}
			}
        			
        }

    	return newBoard;
    }


	/**
	 * this check if a pawn is moving to the last Rank on the opposit side of the board
	 * and if it is then the pawn is upgraded to the pieces the user specified
	 * and if no upgrade is specified then the Pieces is upgraded to a Queen
	 * @param chessboard the current board the user is viewing
	 * @param newFile the new file the user wants the piece to be at
	 * @param newRank the rank the user wants the piece to move to
	 * @param promoteTo the specific type of Piece the user wants the pawn to be promoted to 
	 * @return a board with the pawn upgraded and in the new position the user wanted it to be at
	 */
	public Piece[][] movePiece(ChessBoard chessboard, int newFile, int newRank, String promoteTo) {
		//make sure pawn is moving to promotion spot, otherwise the extra arg is illegal
		if(!(newRank == 7 && this.getColor().equals("white")) && !(newRank == 0 && this.getColor().equals("black"))) {
			chessboard.illegalMoveMessage();
			return chessboard.getBoard();
		}

		this.promotionChoice = promoteTo;
		Piece[][] Board = chessboard.getBoard();
		Piece[][] newBoard = movePiece(chessboard, newFile, newRank);

		if(chessboard.lastMoveIllegalCheck()) {
			this.promotionChoice = "Q"; //reset to automatic queen
			return Board;
		}

		return newBoard;
	}

	public boolean canAttack(ChessBoard chessboard, int newFile, int newRank) { //for check
		int prevFile = this.getFile();
        int prevRank = this.getRank();
        
		//enemy on target square, since chessboard checks for opposite team
        if(chessboard.getPieceAtSquare(newFile, newRank) != null) {
			if (this.getColor().equals("white")) {
				//white pawn, so rank has to increase by 1
				if (newRank - prevRank == 1 && Math.abs(newFile-prevFile) == 1) {
					return true;
				}
				else {
					return false;
				}
			}
			else if (this.getColor().equals("black")) {
				//black pawn, so rank has to decrease by 1
				if (prevRank - newRank == 1 && Math.abs(newFile-prevFile) == 1) {
					return true;
				}
				else {
					return false;
				}
			}
        }

		return false;
	}

	/**
	 * check if the the pawn can move the specified location given to the method
	 * and returns true if its possible to move to the new location
	 * @param chessboard the current chess board the user is looking at
	 * @param newFile the file to see if the pawn can move to 
	 * @param newRank the rank to check if this pawn can move to 
	 * @return a true statement if the pawn can move to the new file and rank given to the method
	 * 
	 */
	public boolean canMoveTo(ChessBoard chessboard, int newFile, int newRank) {
		int prevFile = this.getFile();
        int prevRank = this.getRank();

        int maxDist = 1; //max distance the pawn can move
        if(this.firstmove) {
        	maxDist = 2;
        }
		
		if(newFile != prevFile) { //file can't change
        	return false;
        }
		else {
        	if((this.getColor().equals("white") && (newRank > (prevRank+maxDist) || newRank < prevRank))
				|| (this.getColor().equals("black") && (newRank < (prevRank-maxDist) || newRank > prevRank))) {
				//makes sure that pawns can only move 1 or 2 squares in their allowed direction
        		return false;
        	}
			else if((this.firstmove == true) &&
					((this.getColor() == "white" && chessboard.getPieceAtSquare(prevFile, prevRank+1) != null)
					|| (this.getColor() == "black" && chessboard.getPieceAtSquare(prevFile, prevRank-1) != null))) {
						//check for first move, if so, then check for collision 1 square ahead
						//since we already made sure the next move is 1/2 spots forward
						//and if code reaches here, then there was no piece on the square
						//if pawn is moving up 1, it will pass right through this if statement
						//if pawn is moving up 2, have to check inbetween the squares for collision
						return false;
        	}
			else {
				return true;
			}
        			
        }
	}

	//for ease of promoting a pawn
	/**
	 * given the file and rank, this pawn is promoted to what the user specifies and placed on the new rank and file
	 * 
	 * @param newFile the file location the pawn moved to
	 * @param newRank the rank location the pawn move to 
	 * @return a newly initilized piece at the new location the pawn is supposed to be at
	 */
	private Piece getPromotedPiece(int newFile, int newRank) {
		//reset promotionChoice to Queen
		if(this.promotionChoice.equals("R")) {
			this.promotionChoice = "Q";
			return new Rook(newFile, newRank, this.getColor());
		}
		else if(this.promotionChoice.equals("N")) {
			this.promotionChoice = "Q";
			return new Knight(newFile, newRank, this.getColor());
		}
		else if(this.promotionChoice.equals("B")) {
			this.promotionChoice = "Q";
			return new Bishop(newFile, newRank, this.getColor());
		}

		return new Queen(newFile, newRank, this.getColor());
	}

	public String pieceDetails() {
		return this.getColor() + " pawn";
	}

}