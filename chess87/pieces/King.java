//CODE BY Kenneth Lee
//AND Abriel Hernandez

package pieces;

import chess.ChessBoard;

/**
 * creates the King piece for the chessboard
 * it also impelments for the movement of the king piece
 * the kings can move 1 square in any direction as long as it doesnt cause it to be in
 * check 
 * @author Kenneth Lee
 * @author Abriel Hernandez
 *
 */
public class King extends Piece {
	public boolean firstMove = true;
	public boolean inCheck = false;
    
    /**
     * initialization of the king piece that
     * tells it what position the king is on
     * and its color
     * @param file the file location of the king
     * @param rank the rank location of the king is given
     * @param color the color of this king is assigned
     */
    public King (int file, int rank, String color) {
        super(file, rank, color);
    }

    public void printPiece() {
        if (super.getColor().equalsIgnoreCase("white")) {
            System.out.print("wK");
        }
        else if(super.getColor().equalsIgnoreCase("black")) {
            System.out.print("bK");
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
        
		/*
		since ChessBoard checks for same color, we don't have to worry about collision
		king can only move 1 square around it, so if it was an enemy piece, king can just eat it
		ONLY if moving to that square does not put the king in check.
		
		Also, have to check if the king only moved 1 square
		*/

		if((Math.abs(newFile-prevFile) == 1 || Math.abs(newFile-prevFile) == 0) &&
			(Math.abs(newRank-prevRank) == 1 || Math.abs(newRank-prevRank) == 0)) {

			newBoard = super.finalizeMove(newBoard, this, prevFile, prevRank, newFile, newRank);
			if(super.ownKingChecked(chessboard, newBoard)) {
				Board = super.revertMove(newBoard, this, targetPiece, prevFile, prevRank, newFile, newRank);
				return Board;
			}

			if(this.inCheck == true) {
				this.inCheck = false;
			}

			if(this.firstMove == true) {
				this.firstMove = false;
			}
		}
		else {
			chessboard.illegalMoveMessage();
			return Board;
		}

        
    	return newBoard;
    }

	public boolean canAttack(ChessBoard chessboard, int newFile, int newRank) {
		//for check, king can never check king, but can limit the other king's movement
		int prevFile = this.getFile();
        int prevRank = this.getRank();

		if((Math.abs(newFile-prevFile) == 1 || Math.abs(newFile-prevFile) == 0) &&
			(Math.abs(newRank-prevRank) == 1 || Math.abs(newRank-prevRank) == 0)) {
				return true;
		}
		else {
			return false;
		}
	}

	public String pieceDetails() {
		return this.getColor() + " king";
	}
    
}