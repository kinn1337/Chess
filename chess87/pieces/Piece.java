
//CODE BY Kenneth Lee
//AND 
package pieces;

import chess.ChessBoard;

/**
 * creates a abstract class pieces where we can create subclass that share the same methods but all have
 *their own implementation of each method
 * @author Kenneth Lee
 * @author Abriel Hernandez
 *
 *
 */
public abstract class Piece {
    private int file;
    private int rank;
    private String pieceColor;

    /**
     * initializes a pieces with the locaiton and color the pieces is at
     * @param col the file location the piece should start at
     * @param row the rank location the piece should start at
     * @param color the color this pieces should be
     */
    public Piece(int col, int row, String color) {
        this.pieceColor = color;
        this.file = col;
        this.rank = row;
    }

    /**
     * prints the color and what pieces it is for the board being displaed to the player
     */
    public abstract void printPiece();
    /**
     * when called this method creates and gives a string with the color and the type of pieces it is
     * @return string that has the color and the type Piece it is
     */
    public abstract String pieceDetails();
    /**
     * allows to move the piece to a new FIle and rank the user wants on the board
     * accourding to how that perticular piece is allowed to move
     * @param chessboard the currecnt board the player is seeing
     * @param file the new file the user wants to piece to move to 
     * @param rank the new Rank the user wants to piece to move to 
     * @return the new board with the new location of the piece and if a move is not allowed nothing is returned and an illegle move message is shown
     */
    public abstract Piece[][] movePiece(ChessBoard chessboard, int file, int rank);
    /**
     * checks if the piece can attack the position that is given to the method
     * @param chessboard the board the user is currently looking at
     * @param file the new file to check if this piece can attack
     * @param rank the rank to check if this piece can attack
     * @return  a boolean where if its true it mean that this piece can attack the location given to the method
     */
    public abstract boolean canAttack(ChessBoard chessboard, int file, int rank);

    /**
     * sets the pieces position to the rank and file given to the method
     * @param newFile sets pieces file to the newFile given to the method
     * @param newRank sets pieces rank to the newRank given to the method
     */
    public void setFileRank(int newFile, int newRank) {
        this.file = newFile;
        this.rank = newRank;
    }

    /**
     * method to check if the new board after a pieces movement causes its own king to be in check
     * @param chessboard the chessboard the is currectly being used
     * @param theoreticalBoard the new version of the board that we want to check will cause a check
     * @return a true boolean if a movement causes its own king to be in check
     */
    public boolean ownKingChecked(ChessBoard chessboard, Piece[][] theoreticalBoard) {
        if(chessboard.checkCheck(theoreticalBoard, this.pieceColor)) {
            chessboard.illegalMoveMessage();
            return true;
        }

        return false;
    }

    /**
     * method meant to move the pieces on the board and sets its own positions varibales to match the board
     * @param newBoard the board with the new placement of pieces after they were moved
     * @param piece the piece that was just moved
     * @param prevFile the previous file of the piece that was moved
     * @param prevRank the previous rank of the piece that was moved
     * @param newFile the new file of the piece that was moved
     * @param newRank the new rank of the piece that was moved
     * @return a new board with the finalized placement after a piece was moved
     */
    public Piece[][] finalizeMove(Piece[][] newBoard, Piece piece, int prevFile, int prevRank, int newFile, int newRank) {
        newBoard[newRank][newFile] = piece;
        newBoard[prevRank][prevFile] = null;
        setFileRank(newFile, newRank); //have to fix this, either revert positions, or figure out way of pass by value instead of reference

        return newBoard;
    }

    /**
     * meant to undo the setting of position that filalizeMove did
     * @param oldBoard the previous board that was made
     * @param movedPiece the piece that was previously moved
     * @param targetPiece the piece what was originally attacked so that it can be placed back to the board
     * @param prevFile the previous file location of the piece that was moved 
     * @param prevRank the previous rank location of the piece that was moved
     * @param newFile the newfile locaiotn to move a piece to 
     * @param newRank th new rank locaiton to move a piece to  
     * @return the board that undoes the previous move 
     */
    public Piece[][] revertMove(Piece[][] oldBoard, Piece movedPiece, Piece targetPiece, int prevFile, int prevRank, int newFile, int newRank) {
        oldBoard[movedPiece.getRank()][movedPiece.getFile()] = null;
        oldBoard[prevRank][prevFile] = movedPiece;
        movedPiece.setFileRank(prevFile, prevRank);

        if(targetPiece != null) {
            oldBoard[targetPiece.getRank()][targetPiece.getFile()] = targetPiece;
        }

        return oldBoard;
    }

    /**
     * gives the rank of the pieces the method is being called for 
     * @return the rank of the piece
     */
    public int getRank() {
        return this.rank;
    }

    /**
     * gives the file of the pieces the method is being called for 
     * @return the file of the piece
     */
    public int getFile(){
        return this.file;
    }

    /**
     * gives the color the method is being called for 
     * @return the color of the piece
     */
    public String getColor() {
        return this.pieceColor;
    }
}