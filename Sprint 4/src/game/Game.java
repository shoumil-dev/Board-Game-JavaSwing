package game;


import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;


/**
 * Game engine that takes care of switching turns between Player 1 and Player 2 and checking the state of the game to update the moves available for each player
 * <p>
 * Created  by Tan Jun Yu
 *
 * @author Tan Jun Yu
 * Modified by: Rachit Bhatia
 */
public class Game implements NeighbourPositionFinder{

    /**
     * Player 1
     */
    private Player player1;

    /**
     * Player 2
     */
    private Player player2;

    /**
     * Singleton instance
     */
    private static Game instance = null;

    /**
     * Current turn of the game
     */
    private int turn;

    /**
     * Constructor
     */
    private Game(){
        this.turn = 0 ;
    }

    /**
     * Access Game singleton instance publicly
     * @return Game singleton instance
     */
    public static Game getInstance(){
        if (instance == null) {
            instance = new Game();
        }

        return instance;
    }

    /**
     * Resets the game instance
     */
    public static void voidInstance() {
        instance = null;
    }


    /**
     * Game loop that keeps the game running
     */
    public void run() {

        String endMessage = "Congratulations "; //message to be displayed at end of game

        //borders acting as player turn identifier
        Border playerIdentifier = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.orange.brighter().brighter(), Color.orange.darker());
        Border identifierThickness = BorderFactory.createEmptyBorder(1, 1, 1, 1);

        MainWindow mainWindow = MainWindow.getInstance();

        boolean player1NoValidMove = false;
        boolean player2NoValidMove = false;
        // While both players still have 3 or more tokens, keep the game running
        while (player1.getNumberOfTokens() >= 3 && player2.getNumberOfTokens() >= 3 && !player1NoValidMove && !player2NoValidMove) {


            // update state of move if needed
            player1.updateStateOfMove();

            // Display the state of move player 1 is currently in
            mainWindow.getPlayerStateOfMoveLabel1().setText(player1.getCurrentStateOfMove().toString());

            if (player1.getCurrentStateOfMove() == CurrentStateofMove.SLIDING) {
                player1NoValidMove = checkIfPlayerHasNoValidSlidingMove(player1);
            }

            // Clear mill formed label every turn
            MainWindow.getInstance().getMillLabel().setText("");
            // While a valid move is not yet made by the player 1
            while (this.turn % 2 == 0 && player1.getNumberOfTokens() >= 3 && !player1NoValidMove && !player2NoValidMove) {
                //setting player label border to show which player's turn it is
                mainWindow.getPlayerLabel2().setBorder(null);
                mainWindow.getPlayerLabel1().setBorder(BorderFactory.createCompoundBorder(identifierThickness, playerIdentifier));
                player1.setPlayerTurn(true);
            }

            // After player 1 turn is finished
            player1.setPlayerTurn(false);


            // update state of move if needed
            player2.updateStateOfMove();

            // Display the state of move player 2 is currently in
            mainWindow.getPlayerStateOfMoveLabel2().setText(player2.getCurrentStateOfMove().toString());

            if (player2.getCurrentStateOfMove() == CurrentStateofMove.SLIDING) {
                player2NoValidMove = checkIfPlayerHasNoValidSlidingMove(player2);
            }


            // Clear mill formed label every turn
            MainWindow.getInstance().getMillLabel().setText("");

            // While a valid move is not yet made by the player 2
            while (this.turn % 2 == 1 && player2.getNumberOfTokens() >= 3 && !player1NoValidMove && !player2NoValidMove) {
                //setting player label border to show which player's turn it is
                mainWindow.getPlayerLabel1().setBorder(null);
                mainWindow.getPlayerLabel2().setBorder(BorderFactory.createCompoundBorder(identifierThickness, playerIdentifier));
                player2.setPlayerTurn(true);
            }

            // After player 2 turn is finished
            player2.setPlayerTurn(false);

        }

        // If player 1 has less than 3 tokens , player 2 or Computer wins the game
        if (player1.getTokenList().size() < 3 || player1NoValidMove) {

            if (Game.getInstance().getPlayer2().typeIsComputer()) {
                endMessage = "Computer wins the game!";
            } else {
                endMessage += "Player 2!\nYou win the game!";
            }
        }
        // If player 2 has less than 3 tokens , player 1 wins the game
        else if (player2.getTokenList().size() < 3 || player2NoValidMove) {
            if (Game.getInstance().getPlayer2().typeIsComputer()) {
                endMessage = "Congratulations! You win the game!";
            } else {
                endMessage += "Player 1!\nYou win the game!";
            }
        }

        if (player1.getTokenList().size() < 3 || player1NoValidMove || player2.getTokenList().size() < 3 || player2NoValidMove) {

            String[] options = {"Back to Home", "Quit Application"};
            var selectedOption = JOptionPane.showOptionDialog(null, endMessage, "Game Over", 0, 3, null, options, options[0]);

            //option to return to home page
            if (selectedOption == 0) {
                //resetting the entire state of the game by resetting every component
                voidInstance(); //reset game instance to null
                mainWindow.getContentPane().removeAll();
                MainWindow.voidInstance(); //void instance of main window to enable creation of new instance
                mainWindow.dispose();
                mainWindow = MainWindow.getInstance();
                mainWindow.getContentPane().revalidate();
                mainWindow.getContentPane().repaint();
                mainWindow.setupHomePageWindow();  //set a new game page
            }

            //option to quit the application
            if (selectedOption == 1) {
                System.exit(0);
            }
        }
    }

    /**
     * Check if a Player still has a valid sliding move
     * @param player player which is being checked if there is a valid sliding move
     * @return True if there is no valid sliding move can be made. False otherwise
     */
    public boolean checkIfPlayerHasNoValidSlidingMove(Player player){
        ArrayList<Token> tokens = player.getTokenList();

        boolean noValidMoves = true;
        for (Token token : tokens){  // Go through all the tokens
            if (token.isTokenPlaced()){ // If the token is placed
                IntersectionPoint intersectionPoint = token.getIntersectionPoint();

                // Check if the token has a valid empty adjacent intersection point to slide to
                for ( IntersectionPoint neighbour : findNeighbouringIntersections(intersectionPoint)){
                    if (!neighbour.hasToken()){
                        noValidMoves = false; // There is valid sliding move
                        break;
                    }
                }

                if (!noValidMoves){ // If there is one empty adjacent intersection point found , break and return false
                    break;
                }

            }
        }

        return noValidMoves;
    }

    /**
     * Getter for player 1
     * @return Player 1
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * Getter for player 2
     * @return Player 2
     */
    public Player getPlayer2() {
        return player2;
    }


    /**
     * Increment game turn to allow switching of player's turns
     */
    public void incrementTurn() {
        this.turn = turn + 1;
    }

    /**
     * Set the players to play in the game ( HumanPlayer vs HumanPlayer or HumanPlayer vs ComputerPlayer
     * @param player1
     * @param player2
     */
    public void setPlayers(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
    }
}
