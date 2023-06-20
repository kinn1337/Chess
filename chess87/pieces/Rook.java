//CODE BY Kenneth Lee
//AND Abriel Hernandez

package pieces;

import chess.ChessBoard;

/**
 * creates a Rook pieces for the chess board
 * @author Kenneth Lee
 * @author Abriel Hernandez
 *
 */
public class Rook extends Piece{
    public boolean firstMove = true;

    /**
     * initializes a Rook piece that can move in the up, down, left and right direction
     * @param file the file location of the rook on the board
     * @param rank the rank location of the rook on the board
     * @param color the color of the Rook
     */
    public Rook (int file, int rank, String color) {
        super(file, rank, color);
    }
   
    public void printPiece() {
        if (super.getColor().equalsIgnoreCase("white")) {
            System.out.print("wR");
        }
        else if(super.getColor().equalsIgnoreCase("black")) {
            System.out.print("bR");
        }
    }
    
    
    public Piece[][] movePiece(ChessBoard chessboard, int newFile, int newRank) {

        //rook can only move vertical or horizontal in one direction
        int prevFile = this.getFile();
        int prevRank = this.getRank();
        Piece[][] Board = chessboard.getBoard();
        Piece[][] newBoard = Board;
        Piece targetPiece = null;

		if(Board[newRank][newFile] != null) {
			targetPiece = Board[newRank][newFile];
		}

        //rank or column should stay the same, other should change
        //then check if the piece collides with any existing piece along the path
        //if we go through a piece, that is illegal
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
                if(this.firstMove == true) {
                    this.firstMove = false;
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
                if(this.firstMove == true) {
                    this.firstMove = false;
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
                if(this.firstMove == true) {
                    this.firstMove = false;
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
                if(this.firstMove == true) {
                    this.firstMove = false;
                }
            }
        }
        else {
            //rook moved in both directions in one move, which is illegal
            chessboard.illegalMoveMessage();
            return Board;
        }

        return newBoard;
    }
    
    public boolean canAttack(ChessBoard chessboard, int newFile, int newRank) {
    	
    	//rook can only move vertical or horizontal in one direction
        int prevFile = this.getFile();
        int prevRank = this.getRank();
        Piece[][] Board = chessboard.getBoard();

        //rank or column should stay the same, other should change
        //then check if the piece collides with any existing piece along the path
        //if we go through a piece, that is illegal
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
        else {
            //rook moved in both directions in one move, which is illegal
            return false;
        }	
    }

    
    public String pieceDetails() {
		return this.getColor() + " rook";
	}

}