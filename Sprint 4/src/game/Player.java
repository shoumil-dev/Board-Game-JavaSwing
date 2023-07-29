package game;

import java.util.ArrayList;
/**
 * Player
 * <p>
 * Created by Tan Jun Yu
 *
 * @author Tan Jun Yu
 * Modified by: Rachit Bhatia
 */

public class Player {

    /**
     * All the tokens that the Player has
     */
    private ArrayList<Token> tokenList ;

    /**
     * Player's current state of move
     */
    private CurrentStateofMove currentStateofMove;

    /**
     * boolean value indicating whether the current player is a cpu bot player
     */
    protected boolean isComputer;

    /**
     * Constructor
     */
    public Player(){
        tokenList = new ArrayList<Token>();
        currentStateofMove = CurrentStateofMove.PLACING;
        this.isComputer = false;
    }

    /**
     * Set player turn to true that allows all of its tokens to be usable
     * @param bool
     */
    public void setPlayerTurn(boolean bool) {

        boolean tokenCanBeUsed = false;
        if (bool){
            tokenCanBeUsed = true;
        }
        for(Token token : getTokenList()){
            token.setCanBeUsed(tokenCanBeUsed); // set all the tokens to be usable
        }
    }


    /**
     * Getter for currentStateOfMove
     * @return currentStateOfMove
     */
    public CurrentStateofMove getCurrentStateOfMove() {
        return currentStateofMove;
    }

    /**
     * Check the state of the game the Player is in and update the stateOfMove accordingly
     */
    public void updateStateOfMove(){

        // If current state of move is flying and all the tokens are already placed
        if (currentStateofMove == CurrentStateofMove.PLACING && areAllTokensPlaced()){

            // Change the state of move to SLIDING
            currentStateofMove = CurrentStateofMove.SLIDING;
            for (Token token : tokenList){
                token.changeListener(new SlidingMove(token,token.getX(),token.getY()),false);
            }

        }
        if (currentStateofMove == CurrentStateofMove.SLIDING && tokenList.size() == 3) { // if current state of move is sliding, and player has 3 tokens left


            // Change the state of move to FLYING
            currentStateofMove = CurrentStateofMove.FLYING;

            for (Token token : tokenList){
                token.changeListener(new FlyingMove(token,token.getX(),token.getY()),false);
            }

        }

    }

    /**
     * Getter for tokenList
     * @return all tokens of the player
     */
    public ArrayList<Token> getTokenList() {
        return tokenList;
    }


    /**
     * Add token instance to player arrayList of tokens
     * @param tokenInstance token to be added
     */
    public void addToken(Token tokenInstance){
        this.tokenList.add(tokenInstance);
        tokenInstance.addPlayer(this);
    }

    /**
     * Get the number of tokens the player has
     * @return
     */
    public int getNumberOfTokens(){
        return this.tokenList.size();
    }


    /**
     * Check if all the tokens of the player is already placed on the board so that can be used as indicator whether or not to switch to SLIDING
     * @return True if all tokens are placed. False otherwise
     */
    public boolean areAllTokensPlaced(){
        boolean allPlaced = true;

        for (Token token : tokenList){
            if (!token.isTokenPlaced()){
                allPlaced = false;
                break;
            }
        }
        return allPlaced;
    }

    /**
     * getter for player type
     */
    public boolean typeIsComputer(){
        return this.isComputer;
    }



}
