/**
 * @author Kenneth Lee
 * @author Abriel Hernandez
 */
//CODE BY Kenneth Lee
//AND  Abriel Hernandez
package chess;

import java.util.List;
import java.util.ArrayList;

import pieces.*;

/**
 * creates a chessboard object that contains all the pieces of the board 
 * @author Kenneth Lee
 * @author Abriel Hernandez
 */
public class ChessBoard {
    private Piece[][] Board;
    private King wKingRef, bKingRef;
    private String currentTurn;
    private boolean illegalMove;
    public Piece lastMove;
    public boolean lastRound;
    public boolean inProgress;
    public boolean checkMated;
    public int round_count = 0;

    /**
     * initialization of the chesssBoard where a board is created with
     * 8 rs and 8 cs
     * and each board has a boolean illegalMove which indicates if the preious move was illegal
     * inprogress which tells if the game has ended or is still going
     * lassMovedPiece which keeps track of the last valid moved piece 
     * lastRound
     * checkMated which indiacted if a checkmate has be caused and tells if the game has ended
     */
    public ChessBoard() {
        this.Board = new Piece[8][8];
        this.illegalMove = false;
        this.inProgress = true;
        this.lastMove = null;
        this.lastRound = false;
        this.checkMated = false;
    }

    //method fills in an empty board to create a board for a new chess match
    /**
     * when called creates and sets all the pieces of a chessboard 
     * to their appropriate starting positions and assigns them all their colors
     * 
     */
    public void newGame() {
        this.currentTurn = "white";

        //set white pieces and set black pieces and set up board
        this.Board[0][0] = new Rook(0, 0, "white");
        this.Board[0][1] = new Knight(1, 0, "white");
        this.Board[0][2] = new Bishop(2, 0, "white");
        this.Board[0][3] = new Queen(3, 0, "white");
        this.Board[0][4] = new King(4, 0, "white");
        this.Board[0][5] = new Bishop(5, 0, "white");
        this.Board[0][6] = new Knight(6, 0, "white");
        this.Board[0][7] = new Rook(7, 0, "white");
        for(int c = 0; c < 8; c++) {
            this.Board[1][c] = new Pawn(c, 1, "white");
        }
        this.wKingRef = (King)this.Board[0][4];

        this.Board[7][0] = new Rook(0, 7, "black");
        this.Board[7][1] = new Knight(1, 7, "black");
        this.Board[7][2] = new Bishop(2, 7, "black");
        this.Board[7][3] = new Queen(3, 7, "black");
        this.Board[7][4] = new King(4, 7, "black");
        this.Board[7][5] = new Bishop(5, 7, "black");
        this.Board[7][6] = new Knight(6, 7, "black");
        this.Board[7][7] = new Rook(7, 7, "black");
        for(int c = 0; c < 8; c++) {
            this.Board[6][c] = new Pawn(c, 6, "black");
        }
        this.bKingRef = (King)this.Board[7][4];

        for(int r = 2; r < 6; r++) {
            for(int col = 0; col < 8; col++) {
                this.Board[r][col] = null;
            }
        }
    }

    //moving piece
    /**
     * @param oldFRank the string with the positions of the pieces the user wants to move
     * @param newFRank the string with the new location of the pieces the user is moving
     */
    public void move(String oldFRank, String newFRank) {
        String[] oldFR = convertFileRank(oldFRank).split(",");
        String[] newFR = convertFileRank(newFRank).split(",");

        int oldF, oldR, newF, newR;
        newF = Integer.parseInt(newFR[0]);
        newR = Integer.parseInt(newFR[1]);
        oldF = Integer.parseInt(oldFR[0]);
        oldR = Integer.parseInt(oldFR[1]);

        if(this.illegalMove == true) {
            this.illegalMove = false; 
        }

        if(illegalMoveCheck(oldF, oldR, newF, newR) == true) {
            illegalMoveMessage();
            return;
        }

        //castling?
        if((oldFRank.equals("e1") && newFRank.equals("g1")) || (oldFRank.equals("e1") && newFRank.equals("c1")) ||
            (oldFRank.equals("e8") && newFRank.equals("g8")) || (oldFRank.equals("e8") && newFRank.equals("c8"))) {
            if(this.Board[oldR][oldF] instanceof King) {
                castle(oldF, oldR, newF, newR);
                setlastMove(newF, newR);
                turnController();
                return;
            }
        }

        if(lastRound == true) {
            Piece[][] checkBoard = this.Board;
            checkBoard = checkBoard[oldR][oldF].movePiece(this, newF, newR);
            
            if(this.illegalMove == true) {
                return; 
            }
            else {
                if(this.currentTurn.equals("black")) {
                    bKingRef.inCheck = false;
                }
                else if(this.currentTurn.equals("white")) {
                    wKingRef.inCheck = false;
                }
            }

            this.Board = checkBoard;
        }
        else { 
            this.Board = this.Board[oldR][oldF].movePiece(this, newF, newR);
        }
        
        setlastMove(newF, newR);
        turnController();
    }

    //for draw?, and promotion
    /**
     * @param oldFRank the positions of a piece the user wants to move
     * @param newFRank the new position of the pieces the user wants a piece to move to
     * @param third_arg the argument that tells the method if the user is requesting a draw, 
     */
    public void move(String oldFRank, String newFRank, String third_arg) {
        if(third_arg.equals("draw?")) {
            move(oldFRank, newFRank);
        }
        else if(third_arg.equals("Q") || third_arg.equals("R") || third_arg.equals("N") || third_arg.equals("B")){ 
            String[] oldFR = convertFileRank(oldFRank).split(",");
            String[] newFR = convertFileRank(newFRank).split(",");

            int oldF, oldR, newF, newR;
            newF = Integer.parseInt(newFR[0]);
            newR = Integer.parseInt(newFR[1]);
            oldF = Integer.parseInt(oldFR[0]);
            oldR = Integer.parseInt(oldFR[1]);
            if(this.illegalMove == true) {
                this.illegalMove = false; 
            }

            if(illegalMoveCheck(oldF, oldR, newF, newR) == true) {
                illegalMoveMessage();
                return;
            }

            if(this.Board[oldR][oldF] instanceof Pawn) {
                this.Board = ((Pawn)this.Board[oldR][oldF]).movePiece(this, newF, newR, third_arg);
            }
            else {
                illegalMoveMessage();
            }

            setlastMove(newF, newR);
            turnController();
        }
    }

    /**
     * @param oldF the file if the king the user wants to move
     * @param oldR the rank of the king the user wants to move
     * @param newF the file of the Rook the king wants to move 
     * @param newR the rank of the rook the king wants to move 
     */
    private void castle(int oldF, int oldR, int newF, int newR) {

        List<Piece> whitePieces = new ArrayList<Piece>(), blackPieces = new ArrayList<Piece>(); 
        for(int r = 0; r < 8; r++) {
            for(int col = 0; col < 8; col++) {
                if(this.Board[r][col] != null) {
                    if(this.Board[r][col].getColor().equals("white")) {
                        whitePieces.add(this.Board[r][col]);
                    }
                    else if(this.Board[r][col].getColor().equals("black")) {
                        blackPieces.add(this.Board[r][col]);
                    }
                }
            }
        }

        if(lastRound == true) {
            illegalMoveMessage();
            return;
        }
        if(this.currentTurn.equals("white") && oldF == 4 && oldR == 0) {
            if(newF == 6 && newR == 0) { 
                if(getPieceAtSquare(newF+1, newR) != null &&
                    getPieceAtSquare(newF+1, newR) instanceof Rook &&
                    getPieceAtSquare(newF+1, newR).getColor().equals("white")) {
                    for(int col = oldF+1; col < 7; col++) {
                        if(getPieceAtSquare(col, 0) != null) {
                            illegalMoveMessage();
                            return;
                        }

                        for(Piece bPiece : blackPieces) {
                            if(bPiece.canAttack(this, col, 0)) {
                                illegalMoveMessage();
                                return;
                            }
                        }
                    }
                    if(((King)this.Board[oldR][oldF]).firstMove == true && ((Rook)this.Board[newR][newF+1]).firstMove == true) {
                        this.Board[newR][newF] = this.Board[oldR][oldF];
                        this.Board[oldR][oldF] = null;
                        this.Board[newR][newF].setFileRank(newF, newR);
                        ((King)this.Board[newR][newF]).firstMove = false;

                        this.Board[newR][newF-1] = this.Board[oldR][7];
                        this.Board[oldR][7] = null;
                        this.Board[newR][newF-1].setFileRank(newF-1, newR);
                        ((Rook)this.Board[newR][newF-1]).firstMove = false;
                    }
                    else {
                        illegalMoveMessage();
                        return;
                    }
                }
                else {
                    illegalMoveMessage();
                    return;
                }
            }
            else if(newF == 2 && newR == 0) { 
                if(getPieceAtSquare(newF-2, newR) != null &&
                    getPieceAtSquare(newF-2, newR) instanceof Rook &&
                    getPieceAtSquare(newF-2, newR).getColor().equals("white")) {
                    for(int col = oldF-1; col > 0; col--) {
                        if(getPieceAtSquare(col, 0) != null) {
                            illegalMoveMessage();
                            return;
                        }
                        if(col > 1) {
                            for(Piece bPiece : blackPieces) {
                                if(bPiece.canAttack(this, col, 0)) {
                                    illegalMoveMessage();
                                    return;
                                }
                            }
                        }
                    }
                    if(((King)this.Board[oldR][oldF]).firstMove == true && ((Rook)this.Board[newR][newF-2]).firstMove == true) {
                        this.Board[newR][newF] = this.Board[oldR][oldF];
                        this.Board[oldR][oldF] = null;
                        this.Board[newR][newF].setFileRank(newF, newR);
                        ((King)this.Board[newR][newF]).firstMove = false;

                        this.Board[newR][newF+1] = this.Board[oldR][0];
                        this.Board[oldR][0] = null;
                        this.Board[newR][newF+1].setFileRank(newF+1, newR);
                        ((Rook)this.Board[newR][newF+1]).firstMove = false;
                    }
                    else {
                        illegalMoveMessage();
                        return;
                    }
                }
                else {
                    illegalMoveMessage();
                    return;
                }
            }
            else {
                illegalMoveMessage();
                return;
            }

        }
        else if(this.currentTurn.equals("black") && oldF == 4 && oldR == 7) {
            if(newF == 6 && newR == 7) { 
                if(getPieceAtSquare(newF+1, newR) != null &&
                    getPieceAtSquare(newF+1, newR) instanceof Rook &&
                    getPieceAtSquare(newF+1, newR).getColor().equals("black")) {
                    for(int col = oldF+1; col < 7; col++) {
                        if(getPieceAtSquare(col, 7) != null) {
                            illegalMoveMessage();
                            return;
                        }
                        for(Piece wPiece : whitePieces) {
                            if(wPiece.canAttack(this, col, 7)) {
                                illegalMoveMessage();
                                return;
                            }
                        }
                    }
                    if(((King)this.Board[oldR][oldF]).firstMove == true && ((Rook)this.Board[newR][newF+1]).firstMove == true) {
                        this.Board[newR][newF] = this.Board[oldR][oldF];
                        this.Board[oldR][oldF] = null;
                        this.Board[newR][newF].setFileRank(newF, newR);
                        ((King)this.Board[newR][newF]).firstMove = false;

                        this.Board[newR][newF-1] = this.Board[oldR][7];
                        this.Board[oldR][7] = null;
                        this.Board[newR][newF-1].setFileRank(newF-1, newR);
                        ((Rook)this.Board[newR][newF-1]).firstMove = false;
                    }
                    else {
                        illegalMoveMessage();
                        return;
                    }
                }
                else {
                    illegalMoveMessage();
                    return;
                }
            }
            else if(newF == 2 && newR == 7) { 
                if(getPieceAtSquare(newF-2, newR) != null &&
                    getPieceAtSquare(newF-2, newR) instanceof Rook &&
                    getPieceAtSquare(newF-2, newR).getColor().equals("black")) {
                    for(int col = oldF-1; col > 0; col--) {
                        if(getPieceAtSquare(col, 7) != null) {
                            illegalMoveMessage();
                            return;
                        }

                        if(col > 1) {
                            for(Piece wPiece : whitePieces) {
                                if(wPiece.canAttack(this, col, 7)) {
                                    illegalMoveMessage();
                                    return;
                                }
                            }
                        }
                    }
                    if(((King)this.Board[oldR][oldF]).firstMove == true && ((Rook)this.Board[newR][newF-2]).firstMove == true) {

                        this.Board[newR][newF] = this.Board[oldR][oldF];
                        this.Board[oldR][oldF] = null;
                        this.Board[newR][newF].setFileRank(newF, newR);
                        ((King)this.Board[newR][newF]).firstMove = false;

                        this.Board[newR][newF+1] = this.Board[oldR][0];
                        this.Board[oldR][0] = null;
                        this.Board[newR][newF+1].setFileRank(newF+1, newR);
                        ((Rook)this.Board[newR][newF+1]).firstMove = false;
                    }
                    else {
                        illegalMoveMessage();
                        return;
                    }
                }
                else {
                    illegalMoveMessage();
                    return;
                }
            }
            else {
                illegalMoveMessage(); 
                return;
            }
        }
        else {
            illegalMoveMessage(); 
            return;
        }

    }

    /**
     * @param FileRank the position of a piece 
     * @return int value of the File and rank given 
     */
    public String convertFileRank(String FileRank) {
        int file = FileRank.charAt(0)  % 97;
        int rank = Character.getNumericValue(FileRank.charAt(1)) - 1;

        return file + "," + rank;
    }

    /**
     * @param file the file of the piece on the position
     * @param rank the rank of the piece on the position
     * @return the type of piece that is on the position
     */
    public Piece getPieceAtSquare(int file, int rank) {
        if(this.Board[rank][file] == null) {
            return null;
        }

        return this.Board[rank][file];
    }

    /**
     * @param col the column to check if its in bound
     * @param r the row to check if its in bound
     * @return true if the given move is in bounds of the chessboard
     */
    public boolean inBounds(int col, int r) {

        if (col > 7 || col < 0 || r > 7 || r < 0) {
            return false;
        }

        return true;
    }

    /**
     * check if the location of the piece being moveed is legal
     * @param oldF the current file the piece is at
     * @param oldR the current rank the piece is at
     * @param newF the new file the piece is going to
     * @param newR the new rank the piece is going to
     * @return true if the move by a piece is not legal for any pieces on the board
     */
    public boolean illegalMoveCheck(int oldF, int oldR, int newF, int newR) {
        if (!inBounds(oldF, oldR) || !inBounds(newF, newR)) {
            return true;
        }
        
        if (getPieceAtSquare(oldF, oldR) == null) { 
            return true;
        }
        else { 
            if (!(this.currentTurn.equalsIgnoreCase(this.Board[oldR][oldF].getColor()))) {
                return true;
            }
        }


        if(oldF == newF && oldR == newR) {
            return true;
        }
        
        if (getPieceAtSquare(newF, newR) != null) {
            if (this.currentTurn.equalsIgnoreCase(this.Board[newR][newF].getColor())) {
                return true;
            }
        }

        return false;
    }
    public void illegalMoveMessage() {
        System.out.println("Illegal move, try again");
        this.illegalMove = true; 
    }

    /**
     * @param targetC the color to check if it is in checkmate
     * @return if true the given color is in checkmate
     */
    public boolean checkCheckmate(String targetC) {
        List<Piece> whitePieces = new ArrayList<Piece>(), blackPieces = new ArrayList<Piece>(); 
        Piece whiteKing = null, blackKing = null;
        for(int r = 0; r < 8; r++) {
            for(int col = 0; col < 8; col++) {
                if(this.Board[r][col] != null) {
                    if(this.Board[r][col].getColor().equals("black")) {
                        blackPieces.add(this.Board[r][col]);
                        if(this.Board[r][col] instanceof King) {
                            blackKing = this.Board[r][col]; 
                        }
                    }
                    else if(this.Board[r][col].getColor().equals("white")) {
                        whitePieces.add(this.Board[r][col]);
                        if(this.Board[r][col] instanceof King) {
                            whiteKing = this.Board[r][col]; 
                        }
                    }
                }
            }
        }

        List<int[]> nearkingPos = new ArrayList<int[]>(); 
        List<int[]> unsafeSquares = new ArrayList<int[]>();
        int[] kingPos = new int[2];

        if(targetC.equals("white")) {

            nearkingPos = FindSquaresnearKing(whiteKing);
            kingPos[0] = whiteKing.getRank();
            kingPos[1] = whiteKing.getFile();

            for(int[] adjSquare : nearkingPos) {
                for(Piece bPiece : blackPieces) {
                    if(bPiece.canAttack(this, adjSquare[1], adjSquare[0])) {
                        unsafeSquares.add(adjSquare);
                        break;
                    }
                }
            }

            if(unsafeSquares.size() < nearkingPos.size()) {
                return false; 
            }
            else { 
                Piece attackingPiece = null;

                for(Piece bPiece : blackPieces) {
                    if(bPiece.canAttack(this, kingPos[1], kingPos[0])) {
                        attackingPiece = bPiece;
                        break;
                    }
                }

                for(Piece wPiece : whitePieces) {
                    boolean kingSafe = false;
                    int preRank = wPiece.getRank();
                    int preFile = wPiece.getFile();
                    if(wPiece.canAttack(this, attackingPiece.getFile(), attackingPiece.getRank())) {
                        kingSafe = true;

                        wPiece.finalizeMove(this.Board, wPiece, preFile, preRank, attackingPiece.getFile(), attackingPiece.getRank());
                        for(Piece bPiece : blackPieces) {
                            if(bPiece == attackingPiece) {
                                continue;
                            }

                            if(bPiece.canAttack(this, whiteKing.getFile(), whiteKing.getRank())) {
                                kingSafe = false;
                                break;
                            }
                        }
                        wPiece.revertMove(this.Board, wPiece, attackingPiece, preFile, preRank, attackingPiece.getFile(), attackingPiece.getRank());

                        if(kingSafe == true) {
                            return false;
                        }
                    }
                }
                
                if(attackingPiece.getFile() == whiteKing.getFile()) { 
                    if(attackingPiece.getRank() > whiteKing.getRank()) { 
                        for(int r = whiteKing.getRank()+1; r < attackingPiece.getRank(); r++) {
                            for(Piece wPiece : whitePieces) {
                                boolean kingSafe = false;
                                int preRank = wPiece.getRank();
                                int preFile = wPiece.getFile();

                                if(wPiece == whiteKing) {
                                    continue;
                                }

                                if(wPiece instanceof Pawn) {
                                    if(wPiece.canAttack(this, whiteKing.getFile(), r) == true || ((Pawn)wPiece).canMoveTo(this, whiteKing.getFile(), r) == true) {
                                        kingSafe = true;
                                        
                                        Piece targetPiece = null;
                                        targetPiece = this.Board[r][whiteKing.getFile()];
                                        wPiece.finalizeMove(this.Board, wPiece, preFile, preRank, whiteKing.getFile(), r);
                                        for(Piece bPiece : blackPieces) {
                                            if(bPiece == attackingPiece) {
                                                continue;
                                            }
                
                                            if(bPiece.canAttack(this, whiteKing.getFile(), whiteKing.getRank())) {
                                                kingSafe = false;
                                                break;
                                            }
                                        }
                                        wPiece.revertMove(this.Board, wPiece, targetPiece, preFile, preRank, whiteKing.getFile(), r);

                                        if(kingSafe == true) {
                                            return false;
                                        }
                                    }
                                }
                                else if (wPiece.canAttack(this, whiteKing.getFile(), r) == true){
                                    kingSafe = true;
                                        
                                    Piece targetPiece = null;
                                    targetPiece = this.Board[r][whiteKing.getFile()];
                                    wPiece.finalizeMove(this.Board, wPiece, preFile, preRank, whiteKing.getFile(), r);
                                    for(Piece bPiece : blackPieces) {
                                        if(bPiece == attackingPiece) {
                                            continue;
                                        }
            
                                        if(bPiece.canAttack(this, whiteKing.getFile(), whiteKing.getRank())) {
                                            kingSafe = false;
                                            break;
                                        }
                                    }
                                    wPiece.revertMove(this.Board, wPiece, targetPiece, preFile, preRank, whiteKing.getFile(), r);

                                    if(kingSafe == true) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    else if(attackingPiece.getRank() < whiteKing.getRank()) { 
                        for(int r = whiteKing.getRank()-1; r > attackingPiece.getRank(); r--) {
                            for(Piece wPiece : whitePieces) {
                                boolean kingSafe = false;
                                int preRank = wPiece.getRank();
                                int preFile = wPiece.getFile();

                                if(wPiece == whiteKing) {
                                    continue;
                                }

                                if(wPiece instanceof Pawn) {
                                    if(wPiece.canAttack(this, whiteKing.getFile(), r) == true || ((Pawn)wPiece).canMoveTo(this, whiteKing.getFile(), r) == true) {
                                        kingSafe = true;
                                        
                                        Piece targetPiece = null;
                                        targetPiece = this.Board[r][whiteKing.getFile()];
                                        wPiece.finalizeMove(this.Board, wPiece, preFile, preRank, whiteKing.getFile(), r);
                                        for(Piece bPiece : blackPieces) {
                                            if(bPiece == attackingPiece) {
                                                continue;
                                            }
                
                                            if(bPiece.canAttack(this, whiteKing.getFile(), whiteKing.getRank())) {
                                                kingSafe = false;
                                                break;
                                            }
                                        }
                                        wPiece.revertMove(this.Board, wPiece, targetPiece, preFile, preRank, whiteKing.getFile(), r);

                                        if(kingSafe == true) {
                                            return false;
                                        }
                                    }
                                }
                                else if (wPiece.canAttack(this, whiteKing.getFile(), r) == true){
                                    kingSafe = true;
                                        
                                    Piece targetPiece = null;
                                    targetPiece = this.Board[r][whiteKing.getFile()];
                                    wPiece.finalizeMove(this.Board, wPiece, preFile, preRank, whiteKing.getFile(), r);
                                    for(Piece bPiece : blackPieces) {
                                        if(bPiece == attackingPiece) {
                                            continue;
                                        }
            
                                        if(bPiece.canAttack(this, whiteKing.getFile(), whiteKing.getRank())) {
                                            kingSafe = false;
                                            break;
                                        }
                                    }
                                    wPiece.revertMove(this.Board, wPiece, targetPiece, preFile, preRank, whiteKing.getFile(), r);

                                    if(kingSafe == true) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
                else if(attackingPiece.getRank() == whiteKing.getRank()) { 
                    if(attackingPiece.getFile() > whiteKing.getFile()) { 
                        for(int col = whiteKing.getFile()+1; col < attackingPiece.getFile(); col++) {
                            for(Piece wPiece : whitePieces) {
                                boolean kingSafe = false;
                                int preRank = wPiece.getRank();
                                int preFile = wPiece.getFile();

                                if(wPiece == whiteKing) {
                                    continue;
                                }

                                if(wPiece instanceof Pawn) {
                                    if(wPiece.canAttack(this, col, whiteKing.getRank()) == true || ((Pawn)wPiece).canMoveTo(this, col, whiteKing.getRank()) == true) {
                                        kingSafe = true;
                                        
                                        Piece targetPiece = null;
                                        targetPiece = this.Board[whiteKing.getRank()][col];
                                        wPiece.finalizeMove(this.Board, wPiece, preFile, preRank, col, whiteKing.getRank());
                                        for(Piece bPiece : blackPieces) {
                                            if(bPiece == attackingPiece) {
                                                continue;
                                            }
                
                                            if(bPiece.canAttack(this, whiteKing.getFile(), whiteKing.getRank())) {
                                                kingSafe = false;
                                                break;
                                            }
                                        }
                                        wPiece.revertMove(this.Board, wPiece, targetPiece, preFile, preRank, col, whiteKing.getRank());

                                        if(kingSafe == true) {
                                            return false;
                                        }
                                    }
                                }
                                else if (wPiece.canAttack(this, col, whiteKing.getRank()) == true){
                                    kingSafe = true;
                                        
                                    Piece targetPiece = null;
                                    targetPiece = this.Board[whiteKing.getRank()][col];
                                    wPiece.finalizeMove(this.Board, wPiece, preFile, preRank, col, whiteKing.getRank());
                                    for(Piece bPiece : blackPieces) {
                                        if(bPiece == attackingPiece) {
                                            continue;
                                        }
            
                                        if(bPiece.canAttack(this, whiteKing.getFile(), whiteKing.getRank())) {
                                            kingSafe = false;
                                            break;
                                        }
                                    }
                                    wPiece.revertMove(this.Board, wPiece, targetPiece, preFile, preRank, col, whiteKing.getRank());

                                    if(kingSafe == true) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    else if(attackingPiece.getFile() < whiteKing.getFile()) { 
                        for(int col = whiteKing.getFile()-1; col > attackingPiece.getFile(); col--) {
                            for(Piece wPiece : whitePieces) {
                                boolean kingSafe = false;
                                int preRank = wPiece.getRank();
                                int preFile = wPiece.getFile();

                                if(wPiece == whiteKing) {
                                    continue;
                                }

                                if(wPiece instanceof Pawn) {
                                    if(wPiece.canAttack(this, col, whiteKing.getRank()) == true || ((Pawn)wPiece).canMoveTo(this, col, whiteKing.getRank()) == true) {
                                        kingSafe = true;
                                        
                                        Piece targetPiece = null;
                                        targetPiece = this.Board[whiteKing.getRank()][col];
                                        wPiece.finalizeMove(this.Board, wPiece, preFile, preRank, col, whiteKing.getRank());
                                        for(Piece bPiece : blackPieces) {
                                            if(bPiece == attackingPiece) {
                                                continue;
                                            }
                
                                            if(bPiece.canAttack(this, whiteKing.getFile(), whiteKing.getRank())) {
                                                kingSafe = false;
                                                break;
                                            }
                                        }
                                        wPiece.revertMove(this.Board, wPiece, targetPiece, preFile, preRank, col, whiteKing.getRank());

                                        if(kingSafe == true) {
                                            return false;
                                        }
                                    }
                                }
                                else if (wPiece.canAttack(this, col, whiteKing.getRank()) == true){
                                    kingSafe = true;
                                        
                                    Piece targetPiece = null;
                                    targetPiece = this.Board[whiteKing.getRank()][col];
                                    wPiece.finalizeMove(this.Board, wPiece, preFile, preRank, col, whiteKing.getRank());
                                    for(Piece bPiece : blackPieces) {
                                        if(bPiece == attackingPiece) {
                                            continue;
                                        }
            
                                        if(bPiece.canAttack(this, whiteKing.getFile(), whiteKing.getRank())) {
                                            kingSafe = false;
                                            break;
                                        }
                                    }
                                    wPiece.revertMove(this.Board, wPiece, targetPiece, preFile, preRank, col, whiteKing.getRank());

                                    if(kingSafe == true) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
                else if(Math.abs(attackingPiece.getFile()-whiteKing.getFile()) == Math.abs(attackingPiece.getRank()-whiteKing.getRank())) {
                    if(attackingPiece.getFile() > whiteKing.getFile() && attackingPiece.getRank() > whiteKing.getRank()) {
                        for(int i = 1; i < Math.abs(attackingPiece.getFile()-whiteKing.getFile()); i++) {
                            int targetR = whiteKing.getRank() + i;
                            int targetF = whiteKing.getFile() + i;
                            for(Piece wPiece : whitePieces) {
                                boolean kingSafe = false;
                                int preRank = wPiece.getRank();
                                int preFile = wPiece.getFile();

                                if(wPiece == whiteKing) {
                                    continue;
                                }

                                if(wPiece instanceof Pawn) {
                                    if(wPiece.canAttack(this, targetF, targetR) == true || ((Pawn)wPiece).canMoveTo(this, targetF, targetR) == true) {
                                        kingSafe = true;
                                        
                                        Piece targetPiece = null;
                                        targetPiece = this.Board[targetR][targetF];
                                        wPiece.finalizeMove(this.Board, wPiece, preFile, preRank, targetF, targetR);
                                        for(Piece bPiece : blackPieces) {
                                            if(bPiece == attackingPiece) {
                                                continue;
                                            }
                
                                            if(bPiece.canAttack(this, whiteKing.getFile(), whiteKing.getRank())) {
                                                kingSafe = false;
                                                break;
                                            }
                                        }
                                        wPiece.revertMove(this.Board, wPiece, targetPiece, preFile, preRank, targetF, targetR);

                                        if(kingSafe == true) {
                                            return false;
                                        }
                                    }
                                }
                                else if (wPiece.canAttack(this, targetF, targetR) == true){
                                    kingSafe = true;
                                        
                                    Piece targetPiece = null;
                                    targetPiece = this.Board[targetR][targetF];
                                    wPiece.finalizeMove(this.Board, wPiece, preFile, preRank, targetF, targetR);
                                    for(Piece bPiece : blackPieces) {
                                        if(bPiece == attackingPiece) {
                                            continue;
                                        }
            
                                        if(bPiece.canAttack(this, whiteKing.getFile(), whiteKing.getRank())) {
                                            kingSafe = false;
                                            break;
                                        }
                                    }
                                    wPiece.revertMove(this.Board, wPiece, targetPiece, preFile, preRank, targetF, targetR);

                                    if(kingSafe == true) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    else if(attackingPiece.getFile() > whiteKing.getFile() && attackingPiece.getRank() < whiteKing.getRank()) {
                        for(int i = 1; i < Math.abs(attackingPiece.getFile()-whiteKing.getFile()); i++) {
                            int targetR = whiteKing.getRank() - i;
                            int targetF = whiteKing.getFile() + i;
                            for(Piece wPiece : whitePieces) {
                                boolean kingSafe = false;
                                int preRank = wPiece.getRank();
                                int preFile = wPiece.getFile();

                                if(wPiece == whiteKing) {
                                    continue;
                                }

                                if(wPiece instanceof Pawn) {
                                    if(wPiece.canAttack(this, targetF, targetR) == true || ((Pawn)wPiece).canMoveTo(this, targetF, targetR) == true) {
                                        kingSafe = true;
                                        
                                        Piece targetPiece = null;
                                        targetPiece = this.Board[targetR][targetF];
                                        wPiece.finalizeMove(this.Board, wPiece, preFile, preRank, targetF, targetR);
                                        for(Piece bPiece : blackPieces) {
                                            if(bPiece == attackingPiece) {
                                                continue;
                                            }
                
                                            if(bPiece.canAttack(this, whiteKing.getFile(), whiteKing.getRank())) {
                                                kingSafe = false;
                                                break;
                                            }
                                        }
                                        wPiece.revertMove(this.Board, wPiece, targetPiece, preFile, preRank, targetF, targetR);

                                        if(kingSafe == true) {
                                            return false;
                                        }
                                    }
                                }
                                else if (wPiece.canAttack(this, targetF, targetR) == true){
                                    kingSafe = true;
                                        
                                    Piece targetPiece = null;
                                    targetPiece = this.Board[targetR][targetF];
                                    wPiece.finalizeMove(this.Board, wPiece, preFile, preRank, targetF, targetR);
                                    for(Piece bPiece : blackPieces) {
                                        if(bPiece == attackingPiece) {
                                            continue;
                                        }
            
                                        if(bPiece.canAttack(this, whiteKing.getFile(), whiteKing.getRank())) {
                                            kingSafe = false;
                                            break;
                                        }
                                    }
                                    wPiece.revertMove(this.Board, wPiece, targetPiece, preFile, preRank, targetF, targetR);

                                    if(kingSafe == true) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    else if(attackingPiece.getFile() < whiteKing.getFile() && attackingPiece.getRank() > whiteKing.getRank()){
                        for(int i = 1; i < Math.abs(attackingPiece.getFile()-whiteKing.getFile()); i++) {
                            int targetR = whiteKing.getRank() + i;
                            int targetF = whiteKing.getFile() - i;
                            for(Piece wPiece : whitePieces) {
                                boolean kingSafe = false;
                                int preRank = wPiece.getRank();
                                int preFile = wPiece.getFile();

                                if(wPiece == whiteKing) {
                                    continue;
                                }

                                if(wPiece instanceof Pawn) {
                                    if(wPiece.canAttack(this, targetF, targetR) == true || ((Pawn)wPiece).canMoveTo(this, targetF, targetR) == true) {
                                        kingSafe = true;
                                        
                                        Piece targetPiece = null;
                                        targetPiece = this.Board[targetR][targetF];
                                        wPiece.finalizeMove(this.Board, wPiece, preFile, preRank, targetF, targetR);
                                        for(Piece bPiece : blackPieces) {
                                            if(bPiece == attackingPiece) {
                                                continue;
                                            }
                
                                            if(bPiece.canAttack(this, whiteKing.getFile(), whiteKing.getRank())) {
                                                kingSafe = false;
                                                break;
                                            }
                                        }
                                        wPiece.revertMove(this.Board, wPiece, targetPiece, preFile, preRank, targetF, targetR);

                                        if(kingSafe == true) {
                                            return false;
                                        }
                                    }
                                }
                                else if (wPiece.canAttack(this, targetF, targetR) == true){
                                    kingSafe = true;
                                        
                                    Piece targetPiece = null;
                                    targetPiece = this.Board[targetR][targetF];
                                    wPiece.finalizeMove(this.Board, wPiece, preFile, preRank, targetF, targetR);
                                    for(Piece bPiece : blackPieces) {
                                        if(bPiece == attackingPiece) {
                                            continue;
                                        }
            
                                        if(bPiece.canAttack(this, whiteKing.getFile(), whiteKing.getRank())) {
                                            kingSafe = false;
                                            break;
                                        }
                                    }
                                    wPiece.revertMove(this.Board, wPiece, targetPiece, preFile, preRank, targetF, targetR);

                                    if(kingSafe == true) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    else if(attackingPiece.getFile() < whiteKing.getFile() && attackingPiece.getRank() < whiteKing.getRank()) {
                        for(int i = 1; i < Math.abs(attackingPiece.getFile()-whiteKing.getFile()); i++) {
                            int targetR = whiteKing.getRank() - i;
                            int targetF = whiteKing.getFile() - i;
                            for(Piece wPiece : whitePieces) {
                                boolean kingSafe = false;
                                int preRank = wPiece.getRank();
                                int preFile = wPiece.getFile();

                                if(wPiece == whiteKing) {
                                    continue;
                                }

                                if(wPiece instanceof Pawn) {
                                    if(wPiece.canAttack(this, targetF, targetR) == true || ((Pawn)wPiece).canMoveTo(this, targetF, targetR) == true) {
                                        kingSafe = true;
                                        
                                        Piece targetPiece = null;
                                        targetPiece = this.Board[targetR][targetF];
                                        wPiece.finalizeMove(this.Board, wPiece, preFile, preRank, targetF, targetR);
                                        for(Piece bPiece : blackPieces) {
                                            if(bPiece == attackingPiece) {
                                                continue;
                                            }
                
                                            if(bPiece.canAttack(this, whiteKing.getFile(), whiteKing.getRank())) {
                                                kingSafe = false;
                                                break;
                                            }
                                        }
                                        wPiece.revertMove(this.Board, wPiece, targetPiece, preFile, preRank, targetF, targetR);

                                        if(kingSafe == true) {
                                            return false;
                                        }
                                    }
                                }
                                else if (wPiece.canAttack(this, targetF, targetR) == true){
                                    kingSafe = true;
                                        
                                    Piece targetPiece = null;
                                    targetPiece = this.Board[targetR][targetF];
                                    wPiece.finalizeMove(this.Board, wPiece, preFile, preRank, targetF, targetR);
                                    for(Piece bPiece : blackPieces) {
                                        if(bPiece == attackingPiece) {
                                            continue;
                                        }
            
                                        if(bPiece.canAttack(this, whiteKing.getFile(), whiteKing.getRank())) {
                                            kingSafe = false;
                                            break;
                                        }
                                    }
                                    wPiece.revertMove(this.Board, wPiece, targetPiece, preFile, preRank, targetF, targetR);

                                    if(kingSafe == true) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else if(targetC.equals("black")) {
            nearkingPos = FindSquaresnearKing(blackKing);
            kingPos[0] = blackKing.getRank();
            kingPos[1] = blackKing.getFile();

            for(int[] adjSquare : nearkingPos) {
                for(Piece wPiece : whitePieces) {
                    if(wPiece.canAttack(this, adjSquare[1], adjSquare[0])) {
                        unsafeSquares.add(adjSquare);
                        break;
                    }
                }
            }

            if(unsafeSquares.size() < nearkingPos.size()) {
                return false; 
            }
            else {
                Piece attackingPiece = null;

                for(Piece wPiece : whitePieces) {
                    if(wPiece.canAttack(this, kingPos[1], kingPos[0])) {
                        attackingPiece = wPiece;
                        break;
                    }
                }

                for(Piece bPiece : blackPieces) {
                    boolean kingSafe = false;
                    int preRank = bPiece.getRank();
                    int preFile = bPiece.getFile();
                    if(bPiece.canAttack(this, attackingPiece.getFile(), attackingPiece.getRank())) {
                        kingSafe = true;

                        bPiece.finalizeMove(this.Board, bPiece, preFile, preRank, attackingPiece.getFile(), attackingPiece.getRank());
                        for(Piece wPiece : whitePieces) {
                            if(wPiece == attackingPiece) {
                                continue;
                            }

                            if(wPiece.canAttack(this, blackKing.getFile(), blackKing.getRank())) {
                                kingSafe = false;
                                break;
                            }
                        }
                        bPiece.revertMove(this.Board, bPiece, attackingPiece, preFile, preRank, attackingPiece.getFile(), attackingPiece.getRank());

                        if(kingSafe == true) {
                            return false;
                        }
                    }
                }
                
                if(attackingPiece.getFile() == blackKing.getFile()) { 
                    if(attackingPiece.getRank() > blackKing.getRank()) { 
                        for(int r = blackKing.getRank()+1; r < attackingPiece.getRank(); r++) {
                            for(Piece bPiece : blackPieces) {
                                boolean kingSafe = false;
                                int preRank = bPiece.getRank();
                                int preFile = bPiece.getFile();

                                if(bPiece == blackKing) {
                                    continue;
                                }

                                if(bPiece instanceof Pawn) {
                                    if(bPiece.canAttack(this, blackKing.getFile(), r) == true || ((Pawn)bPiece).canMoveTo(this, blackKing.getFile(), r) == true) {
                                        kingSafe = true;
                                        
                                        Piece targetPiece = null;
                                        targetPiece = this.Board[r][blackKing.getFile()];
                                        bPiece.finalizeMove(this.Board, bPiece, preFile, preRank, blackKing.getFile(), r);
                                        for(Piece wPiece : whitePieces) {
                                            if(wPiece == attackingPiece) {
                                                continue;
                                            }
                
                                            if(wPiece.canAttack(this, blackKing.getFile(), blackKing.getRank())) {
                                                kingSafe = false;
                                                break;
                                            }
                                        }
                                        bPiece.revertMove(this.Board, bPiece, targetPiece, preFile, preRank, blackKing.getFile(), r);

                                        if(kingSafe == true) {
                                            return false;
                                        }
                                    }
                                }
                                else if (bPiece.canAttack(this, blackKing.getFile(), r) == true){
                                    kingSafe = true;
                                        
                                    Piece targetPiece = null;
                                    targetPiece = this.Board[r][blackKing.getFile()];
                                    bPiece.finalizeMove(this.Board, bPiece, preFile, preRank, blackKing.getFile(), r);
                                    for(Piece wPiece : whitePieces) {
                                        if(wPiece == attackingPiece) {
                                            continue;
                                        }
            
                                        if(wPiece.canAttack(this, blackKing.getFile(), blackKing.getRank())) {
                                            kingSafe = false;
                                            break;
                                        }
                                    }
                                    bPiece.revertMove(this.Board, bPiece, targetPiece, preFile, preRank, blackKing.getFile(), r);

                                    if(kingSafe == true) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    else if(attackingPiece.getRank() < blackKing.getRank()) { //attacker coming from bottom
                        for(int r = blackKing.getRank()-1; r > attackingPiece.getRank(); r--) {
                            for(Piece bPiece : blackPieces) {
                                boolean kingSafe = false;
                                int preRank = bPiece.getRank();
                                int preFile = bPiece.getFile();

                                if(bPiece == blackKing) {
                                    continue;
                                }

                                if(bPiece instanceof Pawn) {
                                    if(bPiece.canAttack(this, blackKing.getFile(), r) == true || ((Pawn)bPiece).canMoveTo(this, blackKing.getFile(), r) == true) {
                                        kingSafe = true;
                                        
                                        Piece targetPiece = null;
                                        targetPiece = this.Board[r][blackKing.getFile()];
                                        bPiece.finalizeMove(this.Board, bPiece, preFile, preRank, blackKing.getFile(), r);
                                        for(Piece wPiece : whitePieces) {
                                            if(wPiece == attackingPiece) {
                                                continue;
                                            }
                
                                            if(wPiece.canAttack(this, blackKing.getFile(), blackKing.getRank())) {
                                                kingSafe = false;
                                                break;
                                            }
                                        }
                                        bPiece.revertMove(this.Board, bPiece, targetPiece, preFile, preRank, blackKing.getFile(), r);

                                        if(kingSafe == true) {
                                            return false;
                                        }
                                    }
                                }
                                else if (bPiece.canAttack(this, blackKing.getFile(), r) == true){
                                    kingSafe = true;
                                        
                                    Piece targetPiece = null;
                                    targetPiece = this.Board[r][blackKing.getFile()];
                                    bPiece.finalizeMove(this.Board, bPiece, preFile, preRank, blackKing.getFile(), r);
                                    for(Piece wPiece : whitePieces) {
                                        if(wPiece == attackingPiece) {
                                            continue;
                                        }
            
                                        if(wPiece.canAttack(this, blackKing.getFile(), blackKing.getRank())) {
                                            kingSafe = false;
                                            break;
                                        }
                                    }
                                    bPiece.revertMove(this.Board, bPiece, targetPiece, preFile, preRank, blackKing.getFile(), r);

                                    if(kingSafe == true) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
                else if(attackingPiece.getRank() == blackKing.getRank()) { //attacking horizontally
                    if(attackingPiece.getFile() > blackKing.getFile()) { //attacker coming from right
                        for(int col = blackKing.getFile()+1; col < attackingPiece.getFile(); col++) {
                            for(Piece bPiece : blackPieces) {
                                boolean kingSafe = false;
                                int preRank = bPiece.getRank();
                                int preFile = bPiece.getFile();

                                if(bPiece == blackKing) {
                                    continue;
                                }

                                if(bPiece instanceof Pawn) {
                                    if(bPiece.canAttack(this, col, blackKing.getRank()) == true || ((Pawn)bPiece).canMoveTo(this, col, blackKing.getRank()) == true) {
                                        kingSafe = true;
                                        
                                        Piece targetPiece = null;
                                        targetPiece = this.Board[blackKing.getRank()][col];
                                        bPiece.finalizeMove(this.Board, bPiece, preFile, preRank, col, blackKing.getRank());
                                        for(Piece wPiece : whitePieces) {
                                            if(wPiece == attackingPiece) {
                                                continue;
                                            }
                
                                            if(wPiece.canAttack(this, blackKing.getFile(), blackKing.getRank())) {
                                                kingSafe = false;
                                                break;
                                            }
                                        }
                                        bPiece.revertMove(this.Board, bPiece, targetPiece, preFile, preRank, col, blackKing.getRank());

                                        if(kingSafe == true) {
                                            return false;
                                        }
                                    }
                                }
                                else if (bPiece.canAttack(this, col, blackKing.getRank()) == true){
                                    kingSafe = true;
                                        
                                    Piece targetPiece = null;
                                    targetPiece = this.Board[blackKing.getRank()][col];
                                    bPiece.finalizeMove(this.Board, bPiece, preFile, preRank, col, blackKing.getRank());
                                    for(Piece wPiece : whitePieces) {
                                        if(wPiece == attackingPiece) {
                                            continue;
                                        }
            
                                        if(wPiece.canAttack(this, blackKing.getFile(), blackKing.getRank())) {
                                            kingSafe = false;
                                            break;
                                        }
                                    }
                                    bPiece.revertMove(this.Board, bPiece, targetPiece, preFile, preRank, col, blackKing.getRank());

                                    if(kingSafe == true) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    else if(attackingPiece.getFile() < blackKing.getFile()) { //attacker coming from left
                        for(int col = blackKing.getFile()-1; col > attackingPiece.getFile(); col--) {
                            for(Piece bPiece : blackPieces) {
                                boolean kingSafe = false;
                                int preRank = bPiece.getRank();
                                int preFile = bPiece.getFile();

                                if(bPiece == blackKing) {
                                    continue;
                                }

                                if(bPiece instanceof Pawn) {
                                    if(bPiece.canAttack(this, col, blackKing.getRank()) == true || ((Pawn)bPiece).canMoveTo(this, col, blackKing.getRank()) == true) {
                                        kingSafe = true;
                                        
                                        Piece targetPiece = null;
                                        targetPiece = this.Board[blackKing.getRank()][col];
                                        bPiece.finalizeMove(this.Board, bPiece, preFile, preRank, col, blackKing.getRank());
                                        for(Piece wPiece : whitePieces) {
                                            if(wPiece == attackingPiece) {
                                                continue;
                                            }
                
                                            if(wPiece.canAttack(this, blackKing.getFile(), blackKing.getRank())) {
                                                kingSafe = false;
                                                break;
                                            }
                                        }
                                        bPiece.revertMove(this.Board, bPiece, targetPiece, preFile, preRank, col, blackKing.getRank());

                                        if(kingSafe == true) {
                                            return false;
                                        }
                                    }
                                }
                                else if (bPiece.canAttack(this, col, blackKing.getRank()) == true){
                                    kingSafe = true;
                                        
                                    Piece targetPiece = null;
                                    targetPiece = this.Board[blackKing.getRank()][col];
                                    bPiece.finalizeMove(this.Board, bPiece, preFile, preRank, col, blackKing.getRank());
                                    for(Piece wPiece : whitePieces) {
                                        if(wPiece == attackingPiece) {
                                            continue;
                                        }
            
                                        if(wPiece.canAttack(this, blackKing.getFile(), blackKing.getRank())) {
                                            kingSafe = false;
                                            break;
                                        }
                                    }
                                    bPiece.revertMove(this.Board, bPiece, targetPiece, preFile, preRank, col, blackKing.getRank());

                                    if(kingSafe == true) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
                else if(Math.abs(attackingPiece.getFile()-blackKing.getFile()) == Math.abs(attackingPiece.getRank()-blackKing.getRank())) {
                    if(attackingPiece.getFile() > blackKing.getFile() && attackingPiece.getRank() > blackKing.getRank()) {
                        for(int i = 1; i < Math.abs(attackingPiece.getFile()-blackKing.getFile()); i++) {
                            int targetR = blackKing.getRank() + i;
                            int targetF = blackKing.getFile() + i;
                            for(Piece bPiece : blackPieces) {
                                boolean kingSafe = false;
                                int preRank = bPiece.getRank();
                                int preFile = bPiece.getFile();

                                if(bPiece == blackKing) {
                                    continue;
                                }

                                if(bPiece instanceof Pawn) {
                                    if(bPiece.canAttack(this, targetF, targetR) == true || ((Pawn)bPiece).canMoveTo(this, targetF, targetR) == true) {
                                        kingSafe = true;
                                        
                                        Piece targetPiece = null;
                                        targetPiece = this.Board[targetR][targetF];
                                        bPiece.finalizeMove(this.Board, bPiece, preFile, preRank, targetF, targetR);
                                        for(Piece wPiece : whitePieces) {
                                            if(wPiece == attackingPiece) {
                                                continue;
                                            }
                
                                            if(wPiece.canAttack(this, blackKing.getFile(), blackKing.getRank())) {
                                                kingSafe = false;
                                                break;
                                            }
                                        }
                                        bPiece.revertMove(this.Board, bPiece, targetPiece, preFile, preRank, targetF, targetR);

                                        if(kingSafe == true) {
                                            return false;
                                        }
                                    }
                                }
                                else if (bPiece.canAttack(this, targetF, targetR) == true){
                                    kingSafe = true;
                                        
                                    Piece targetPiece = null;
                                    targetPiece = this.Board[targetR][targetF];
                                    bPiece.finalizeMove(this.Board, bPiece, preFile, preRank, targetF, targetR);
                                    for(Piece wPiece : whitePieces) {
                                        if(wPiece == attackingPiece) {
                                            continue;
                                        }
            
                                        if(wPiece.canAttack(this, blackKing.getFile(), blackKing.getRank())) {
                                            kingSafe = false;
                                            break;
                                        }
                                    }
                                    bPiece.revertMove(this.Board, bPiece, targetPiece, preFile, preRank, targetF, targetR);

                                    if(kingSafe == true) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    else if(attackingPiece.getFile() > blackKing.getFile() && attackingPiece.getRank() < blackKing.getRank()) {
                        for(int i = 1; i < Math.abs(attackingPiece.getFile()-blackKing.getFile()); i++) {
                            int targetR = blackKing.getRank() - i;
                            int targetF = blackKing.getFile() + i;
                            for(Piece bPiece : blackPieces) {
                                boolean kingSafe = false;
                                int preRank = bPiece.getRank();
                                int preFile = bPiece.getFile();

                                if(bPiece == blackKing) {
                                    continue;
                                }

                                if(bPiece instanceof Pawn) {
                                    if(bPiece.canAttack(this, targetF, targetR) == true || ((Pawn)bPiece).canMoveTo(this, targetF, targetR) == true) {
                                        kingSafe = true;
                                        
                                        Piece targetPiece = null;
                                        targetPiece = this.Board[targetR][targetF];
                                        bPiece.finalizeMove(this.Board, bPiece, preFile, preRank, targetF, targetR);
                                        for(Piece wPiece : whitePieces) {
                                            if(wPiece == attackingPiece) {
                                                continue;
                                            }
                
                                            if(wPiece.canAttack(this, blackKing.getFile(), blackKing.getRank())) {
                                                kingSafe = false;
                                                break;
                                            }
                                        }
                                        bPiece.revertMove(this.Board, bPiece, targetPiece, preFile, preRank, targetF, targetR);

                                        if(kingSafe == true) {
                                            return false;
                                        }
                                    }
                                }
                                else if (bPiece.canAttack(this, targetF, targetR) == true){
                                    kingSafe = true;
                                        
                                    Piece targetPiece = null;
                                    targetPiece = this.Board[targetR][targetF];
                                    bPiece.finalizeMove(this.Board, bPiece, preFile, preRank, targetF, targetR);
                                    for(Piece wPiece : whitePieces) {
                                        if(wPiece == attackingPiece) {
                                            continue;
                                        }
            
                                        if(wPiece.canAttack(this, blackKing.getFile(), blackKing.getRank())) {
                                            kingSafe = false;
                                            break;
                                        }
                                    }
                                    bPiece.revertMove(this.Board, bPiece, targetPiece, preFile, preRank, targetF, targetR);

                                    if(kingSafe == true) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    else if(attackingPiece.getFile() < blackKing.getFile() && attackingPiece.getRank() > blackKing.getRank()){
                        for(int i = 1; i < Math.abs(attackingPiece.getFile()-blackKing.getFile()); i++) {
                            int targetR = blackKing.getRank() + i;
                            int targetF = blackKing.getFile() - i;
                            for(Piece bPiece : blackPieces) {
                                boolean kingSafe = false;
                                int preRank = bPiece.getRank();
                                int preFile = bPiece.getFile();

                                if(bPiece == blackKing) {
                                    continue;
                                }

                                if(bPiece instanceof Pawn) {
                                    if(bPiece.canAttack(this, targetF, targetR) == true || ((Pawn)bPiece).canMoveTo(this, targetF, targetR) == true) {
                                        kingSafe = true;
                                        
                                        Piece targetPiece = null;
                                        targetPiece = this.Board[targetR][targetF];
                                        bPiece.finalizeMove(this.Board, bPiece, preFile, preRank, targetF, targetR);
                                        for(Piece wPiece : whitePieces) {
                                            if(wPiece == attackingPiece) {
                                                continue;
                                            }
                
                                            if(wPiece.canAttack(this, blackKing.getFile(), blackKing.getRank())) {
                                                kingSafe = false;
                                                break;
                                            }
                                        }
                                        bPiece.revertMove(this.Board, bPiece, targetPiece, preFile, preRank, targetF, targetR);

                                        if(kingSafe == true) {
                                            return false;
                                        }
                                    }
                                }
                                else if (bPiece.canAttack(this, targetF, targetR) == true){
                                    kingSafe = true;
                                        
                                    Piece targetPiece = null;
                                    targetPiece = this.Board[targetR][targetF];
                                    bPiece.finalizeMove(this.Board, bPiece, preFile, preRank, targetF, targetR);
                                    for(Piece wPiece : whitePieces) {
                                        if(wPiece == attackingPiece) {
                                            continue;
                                        }
            
                                        if(wPiece.canAttack(this, blackKing.getFile(), blackKing.getRank())) {
                                            kingSafe = false;
                                            break;
                                        }
                                    }
                                    bPiece.revertMove(this.Board, bPiece, targetPiece, preFile, preRank, targetF, targetR);

                                    if(kingSafe == true) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                    else if(attackingPiece.getFile() < blackKing.getFile() && attackingPiece.getRank() < blackKing.getRank()) {
                        for(int i = 1; i < Math.abs(attackingPiece.getFile()-blackKing.getFile()); i++) {
                            int targetR = blackKing.getRank() - i;
                            int targetF = blackKing.getFile() - i;
                            for(Piece bPiece : blackPieces) {
                                boolean kingSafe = false;
                                int preRank = bPiece.getRank();
                                int preFile = bPiece.getFile();

                                if(bPiece == blackKing) {
                                    continue;
                                }

                                if(bPiece instanceof Pawn) {
                                    if(bPiece.canAttack(this, targetF, targetR) == true || ((Pawn)bPiece).canMoveTo(this, targetF, targetR) == true) {
                                        kingSafe = true;
                                        
                                        Piece targetPiece = null;
                                        targetPiece = this.Board[targetR][targetF];
                                        bPiece.finalizeMove(this.Board, bPiece, preFile, preRank, targetF, targetR);
                                        for(Piece wPiece : whitePieces) {
                                            if(wPiece == attackingPiece) {
                                                continue;
                                            }
                
                                            if(wPiece.canAttack(this, blackKing.getFile(), blackKing.getRank())) {
                                                kingSafe = false;
                                                break;
                                            }
                                        }
                                        bPiece.revertMove(this.Board, bPiece, targetPiece, preFile, preRank, targetF, targetR);

                                        if(kingSafe == true) {
                                            return false;
                                        }
                                    }
                                }
                                else if (bPiece.canAttack(this, targetF, targetR) == true){
                                    kingSafe = true;
                                        
                                    Piece targetPiece = null;
                                    targetPiece = this.Board[targetR][targetF];
                                    bPiece.finalizeMove(this.Board, bPiece, preFile, preRank, targetF, targetR);
                                    for(Piece wPiece : whitePieces) {
                                        if(wPiece == attackingPiece) {
                                            continue;
                                        }
            
                                        if(wPiece.canAttack(this, blackKing.getFile(), blackKing.getRank())) {
                                            kingSafe = false;
                                            break;
                                        }
                                    }
                                    bPiece.revertMove(this.Board, bPiece, targetPiece, preFile, preRank, targetF, targetR);

                                    if(kingSafe == true) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * @param checkBoard check if it would put us in check
     * @param targetC the color we are checking to see is in check
     * @return if true means the given color would be in check
     */
    public boolean checkCheck(Piece[][] checkBoard, String targetC) {
        List<Piece> whitePieces = new ArrayList<Piece>(), blackPieces = new ArrayList<Piece>(); //init arrays to hold pieces of each color
        Piece whiteKing = null, blackKing = null;
        for(int r = 0; r < 8; r++) {
            for(int col = 0; col < 8; col++) {
                if(checkBoard[r][col] != null) {
                    if(checkBoard[r][col].getColor().equals("black")) {
                        blackPieces.add(checkBoard[r][col]);
                        if(checkBoard[r][col] instanceof King) {
                            blackKing = checkBoard[r][col]; 
                        }
                    }
                    else if(checkBoard[r][col].getColor().equals("white")) {
                        whitePieces.add(checkBoard[r][col]);
                        if(checkBoard[r][col] instanceof King) {
                            whiteKing = checkBoard[r][col]; 
                        }
                    }
                    
                }
            }
        }
        if(targetC.equals("white")) {
            for(Piece potentialThreat : blackPieces) {
                if(potentialThreat.canAttack(this, whiteKing.getFile(), whiteKing.getRank())) {
                    return true;
                }
            }
        }
        else if(targetC.equals("black")) {
            for(Piece potentialThreat : whitePieces) {
                if(potentialThreat.canAttack(this, blackKing.getFile(), blackKing.getRank())) {
                    return true;
                }
            }
        }
        

        return false;
    }

    /**
     * given a king Piece this creates a list that contains the locations
     * of all the squares in the immediate vacinity of the king so at to check if all
     * these location will cause a check
     * @param king the king piece who;s immediate vacinity to look for all squares
     * @return a list of the all the squares within the immediate vacinity of the king
     */
    public List<int[]> FindSquaresnearKing(Piece king) {
        //to get the squares near the king
        List<int[]> result = new ArrayList<int[]>();
        
        for(int r = king.getRank()-1; r <= king.getRank()+1; r++) {
            for(int col = king.getFile()-1; col <= king.getFile()+1; col++) {
                if(r == king.getRank() && col == king.getFile()) {
                    continue;
                }
                else if(r >= 0 && r <= 7) {
                    if(col >= 0 && col <= 7) {
                        if(this.Board[r][col] != null && this.Board[r][col].getColor().equals(king.getColor())) {
                            continue;
                        }
                        int[] RankFile = new int[2];
                        RankFile[0] = r;
                        RankFile[1] = col;
                        result.add(RankFile);
                    }
                }
            }
        }

        return result;
    }


    public void turnController() {
        if(this.illegalMove == true) {
            return;
        }
        if(this.currentTurn.equalsIgnoreCase("white")){
            this.currentTurn = "black";
        }
        else {
            this.currentTurn = "white";
        }

        //check if the person is in check
        if(checkCheck(this.Board, this.currentTurn)) {
            if(checkCheckmate(this.currentTurn) == true) {
                System.out.println("Checkmate");
                this.checkMated = true;
                this.inProgress = false;
            }
            
            this.lastRound = true;
            if(this.currentTurn.equals("white")) {
                wKingRef.inCheck = true;
            }
            else if(this.currentTurn.equals("black")) {
                bKingRef.inCheck = true;
            }
        }
        else {
            this.lastRound = false;
        }

        round_count++;
    }

    /**
     * @param newF the new file location of the last moved piece
     * @param newR the new rank location of the last moved piece
     */
    public void setlastMove(int newF, int newR) {
        if(this.illegalMove == true) {
            return;
        }

        this.lastMove = this.Board[newR][newF];
    }

    //displays the board in terminal
    /**
     * prints out the board with the type and color
     * prints "##" if a given position is a black square and empty
     */
    public void printBoard() {
        for(int r = 7; r >= 0; r--) {
            for(int c = 0; c <= 7; c++) {
                if(this.Board[r][c] != null) {
                    this.Board[r][c].printPiece();
                }
                else {
                    if (r % 2 == 0) {
                        if (c % 2 == 0) {
                            System.out.print("##");
                        }
                        else if (c % 2 == 1) {
                            System.out.print("  ");
                        }
                    }
                    else if (r % 2 == 1) {
                        if (c % 2 == 0) {
                            System.out.print("  ");
                        }
                        else if (c % 2 == 1) {
                            System.out.print("##");
                        }
                    }
                }
                System.out.print(" ");
            }
            System.out.println(r+1);
        }
        System.out.println(" a  b  c  d  e  f  g  h");
        System.out.println();
    }

    /**
     * @return the the chessboards Board is returned
     */
    public Piece[][] getBoard() {
        return this.Board;
    }

    /**
     * @return the color of the team who turn it is to move
     */
    public String getcurrentTurn() {
        return this.currentTurn;
    }

    /**
     * @return a boolean indicating if the last move was legal
     */
    public boolean lastMoveIllegalCheck() {
        return this.illegalMove;
    }
}