/**
 * @author Kenneth Lee
 * @author Abriel Hernandez
 * 
 */
//CODE BY Kenneth Lee
//AND Abriel Hernandez

package pieces;

import chess.ChessBoard;

/**
 * class that creates a Knight piece for a chessboard
 * that can move in a fashon of move-move-turn
 * @author Kenneth Lee
 * @author Abriel Hernandez
 *
 */
public class Knight extends Piece{
    
    /**
     * initialization of  a Knight piece for the chessboard
     * @param file the file location of the Knight on the board
     * @param rank the Rank location of the Knight on the board
     * @param color the color of the Knight
     */
    public Knight (int file, int rank, String color) {
        super(file, rank, color);
    }


    public void printPiece() {
        if (super.getColor().equalsIgnoreCase("white")) {
            System.out.print("wN");
        }
        else if(super.getColor().equalsIgnoreCase("black")) {
            System.out.print("bN");
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

        //knight does not need to check for collision, just needs to check for L shape
        //ChessBoard already checks for same team on existing square and bounds
        if(Math.abs(newFile - prevFile) == 1 && Math.abs(newRank - prevRank) == 2) { 
            //moved 1 square left or right, and up or down 2 squares
            newBoard = super.finalizeMove(newBoard, this, prevFile, prevRank, newFile, newRank);
            if(super.ownKingChecked(chessboard, newBoard)) {
                //move put own king in check, so illegal
                Board = super.revertMove(newBoard, this, targetPiece, prevFile, prevRank, newFile, newRank);
                return Board;
            }
        }
        else if(Math.abs(newFile - prevFile) == 2 && Math.abs(newRank - prevRank) == 1) {
            //moved 2 squares left or right, and up or down 1 square only
            newBoard = super.finalizeMove(newBoard, this, prevFile, prevRank, newFile, newRank);
            if(super.ownKingChecked(chessboard, newBoard)) {
                //move put own king in check, so illegal
                Board = super.revertMove(newBoard, this, targetPiece, prevFile, prevRank, newFile, newRank);
                return Board;
            }
        }
        else {
            chessboard.illegalMoveMessage();
            return Board;
        }

        return newBoard;
    }
    
    /**
     *checks if this knight can attacke the newFile and NewRank the given as paramaters
     *
     *@param chessboard the chessboard that the user currently sees
     *@param newFile the File location we are check if the knight can attack
     *@param newRank the Rank location we are check if the knight can attack
     *@return returns a boolean where its true if this knight can attack the given newfile and newRank the user wntss
     */
    public boolean canAttack(ChessBoard chessboard, int newFile, int newRank) {
    	int prevFile = this.getFile();
        int prevRank = this.getRank();

        //knight does not need to check for collision, just needs to check for L shape
        //ChessBoard already checks for same team on existing square and bounds
        if(Math.abs(newFile - prevFile) == 1 && Math.abs(newRank - prevRank) == 2) { 
            //moved 1 square left or right, and up or down 2 squares
            return true;
        }
        else if(Math.abs(newFile - prevFile) == 2 && Math.abs(newRank - prevRank) == 1) {
            //moved 2 squares left or right, and up or down 1 square only
            return true;
        }
        else {
            return false;
        }     
    }


    public String pieceDetails() {
		return this.getColor() + " knight";
	}

}

