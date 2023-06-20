//CODE BY Kenneth Lee(KL877)
//AND Abriel Hernandez(AH1394)
package chess;

import java.util.Scanner;

/**
 * Regarding the chess class, it is designed to facilitate the game by creating a new board with the initial placement of all the pieces. 
 * Additionally, it allows the user to input moves and execute them on the board. 
 * The user also has the option to resign or request a draw.
 * @author Kenneth Lee
 * @author Abriel Hernandez
 *
 */
public class Chess {

    /**
     * Once the chess class is initiated, it generates a game board and prepares to receive user input.
     * Users can utilize this input to move any piece to a legal location or to resign the game by inputting "resign". 
     * Additionally, players can request a draw by stating "draw?" after making a move.
     * @param args the user inputs
     */
    public static void main(String[] args) {

        
        ChessBoard Board = new ChessBoard();
        Board.newGame();
        Board.printBoard();

        Scanner moveReader = new Scanner(System.in);
        String attemptedMove = "";

        while(Board.inProgress == true) {
            System.out.print(Board.getcurrentTurn().substring(0, 1).toUpperCase() + Board.getcurrentTurn().substring(1) + "'s move: ");
            attemptedMove = moveReader.nextLine();
            String[] moveTokenizer = attemptedMove.split(" ");
            if(moveTokenizer.length == 3) { 
                Board.move(moveTokenizer[0], moveTokenizer[1], moveTokenizer[2]);
                if(moveTokenizer[2].equals("draw?") && Board.lastMoveIllegalCheck() == false && Board.checkMated == false) {
                    System.out.println();
                    Board.printBoard();
                    if(Board.lastRound == true) {
                        System.out.println("Check");
                    }
                    System.out.print(Board.getcurrentTurn().substring(0, 1).toUpperCase() + Board.getcurrentTurn().substring(1) + "'s move: ");
                    attemptedMove = moveReader.nextLine(); 
                    Board.inProgress = false;
                    System.out.println("draw");
                }
                else if(Board.lastMoveIllegalCheck() == false && Board.checkMated == false){ 
                    System.out.println();
                    Board.printBoard();
                    if(Board.lastRound == true) {
                        System.out.println("Check");
                    }
                }
                else if(Board.checkMated == true) {
                    if(Board.getcurrentTurn().equalsIgnoreCase("white")){
                        System.out.println("Black wins");
                    }
                    else {
                        System.out.println("White wins");
                    }
                }
            }
            else if(moveTokenizer.length == 2) { 
                Board.move(moveTokenizer[0], moveTokenizer[1]);
                if(Board.lastMoveIllegalCheck() == false && Board.checkMated == false) {
                    System.out.println();
                    Board.printBoard();
                    if(Board.lastRound == true) {
                        System.out.println("Check");
                    }
                }
                else if(Board.checkMated == true) {
                    if(Board.getcurrentTurn().equalsIgnoreCase("white")){
                        System.out.println("Black wins");
                    }
                    else {
                        System.out.println("White wins");
                    }
                }
            }
            else if(moveTokenizer.length == 1) { 
                if(moveTokenizer[0].equalsIgnoreCase("resign")) {
                    if(Board.getcurrentTurn().equalsIgnoreCase("white")){
                        Board.inProgress = false;
                        System.out.println("Black wins");
                    }
                    else {
                        Board.inProgress = false;
                        System.out.println("White wins");
                    }
                }
            }
        }
        

        moveReader.close();
    }

}