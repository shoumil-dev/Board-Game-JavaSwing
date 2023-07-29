package game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * MillChecker that helps to check if a mill is formed when a token is placed at a new position
 * <p>
 * Created  by Shoumil Guha
 *
 * @author Tan Jun Yu, Shoumil Guha
 * Modified by: Rachit Bhatia
 */
public class MillChecker implements NeighbourPositionFinder {

    /**
     * MillChecker singleton isntance
     */
    private static MillChecker instance;

    /**
     * All the tokens that currently are part of a mill
     */
    private ArrayList<ArrayList<Token>> millTokens = new ArrayList<>();

    /**
     * Gets the singular instance of the MillChecker.
     * @return the MillChecker singletoninstance
     */

    public static MillChecker getInstance() {
        if (instance == null) {
            instance = new MillChecker();
        }
        return instance;
    }

    /**
     * Check for vertical and horizontal mill
     *
     * @param intersectionPointInstance the intersection point in which a token is just placed on it
     * @param humanOrComputer true if HumanPlayer, false if ComputerPlayer
     * @return True when a mill is formed . Otherwise, False.
     */
    public boolean checkMill(IntersectionPoint intersectionPointInstance,boolean humanOrComputer) {

        boolean millFormed = false;

        ArrayList<Token> tokensInMillHorizontal = new ArrayList<Token>(); // Tokens in Horizontal Mill
        tokensInMillHorizontal.add(intersectionPointInstance.getTokenInstance()); // First add the token that is just placed on this intersection point
        int numberOfTokensAlignedHorizontal = 1; // Number of tokens aligned horizontally


        ArrayList<Token> tokensFoundHorizontal = checkHorizontalVertical(intersectionPointInstance,tokensInMillHorizontal,true); // Check for tokens aligned horizontally

        while (tokensFoundHorizontal.size() != 0){ // If no tokens aligned horizontally stop the loop

            if (tokensFoundHorizontal.size() == 2){ // If 2 other tokens found aligned with newly placed token
                numberOfTokensAlignedHorizontal += 2;
                break; // mill is formed
            } else { // If only one token is found aligned , proceed to check the third intersection point ( happens for the case where token is placed on the
                // corner most intersection point of a horizontal line)
                numberOfTokensAlignedHorizontal += 1;
                tokensFoundHorizontal = checkHorizontalVertical(tokensFoundHorizontal.get(0).getIntersectionPoint(),tokensInMillHorizontal,true);
            }
        }

        ArrayList<Token> tokensInMillVertical = new ArrayList<Token>(); // Tokens in Vertical Mill
        int numberOfTokensAlignedVertical = 1; // Number of tokens aligned vertically
        tokensInMillVertical.add(intersectionPointInstance.getTokenInstance()); // First add the token that is just placed on this intersection point

        ArrayList<Token> tokensFoundVertical = checkHorizontalVertical(intersectionPointInstance,tokensInMillVertical,false); // Check for tokens aligned vertically

        while (tokensFoundVertical.size() != 0){ // If no tokens aligned vertically stop the loop

            if (tokensFoundVertical.size() == 2){ // If 2 other tokens found aligned with newly placed token
                numberOfTokensAlignedVertical += 2;
                break; // mill is formed
            } else { // If only one token is found aligned , proceed to check the third intersection point ( happens for the case where token is placed on the
                // corner most intersection point of a vertical line)
                numberOfTokensAlignedVertical += 1;
                tokensFoundVertical = checkHorizontalVertical(tokensFoundVertical.get(0).getIntersectionPoint(),tokensInMillVertical,false);
            }
        }

        // if a horizontal mill is formed
        if (numberOfTokensAlignedHorizontal == 3){
            millFormed = true;
            millTokens.add(tokensInMillHorizontal);
        }

        // if a vertical mill is formed
        if (numberOfTokensAlignedVertical == 3){
            millFormed = true;
            millTokens.add(tokensInMillVertical);
        }

        // If a mill is formed ( does not matter horizontal or vertical or even both)
        if (millFormed){
            JLabel millFormedLabel = MainWindow.getInstance().getMillLabel();
            if (humanOrComputer){
                millFormedLabel.setText("Woohoo! Mill Formed! Removable Tokens highlighted in PINK");
            }
            else {
                millFormedLabel.setText("OH NO!! CPU formed a mill and will remove 1 of your tokens :(");
                millFormedLabel.setForeground(Color.RED.darker());
            }
            Player curPlayer = intersectionPointInstance.getTokenInstance().getPlayer();
            changeToRemoveState(curPlayer);
        }

        // Paint the borders of the tokens of the mill formed to blue
        if (tokensInMillHorizontal.size() == 3){
            for (Token millToken : tokensInMillHorizontal){
                millToken.inMill = true;
                millToken.repaint();
            }
        }
        if (tokensInMillVertical.size() == 3){
            for (Token millToken : tokensInMillVertical){
                millToken.inMill = true;
                millToken.repaint();
            }
        }



        return millFormed;
    }

    /**
     * Check the horizontal neighbour(s) of an intersection point to see if there is another token of same player next to it
     * @param bool True if Checking for horizontal alignment , False if checking for vertical alignment
     * @param intersectionPoint intersection point to which the neighbours are being checked
     * @param tokensInMill current tokens that are already found aligned ( to avoid adding the same token found again to the mill)
     * @return all the tokens found aligned ( either 1 token or 2 tokens ( middle intersection point) )
     */

    private ArrayList<Token> checkHorizontalVertical(IntersectionPoint intersectionPoint ,ArrayList<Token> tokensInMill , boolean bool ){
        ArrayList<IntersectionPoint> neighbours = findNeighbouringIntersections(intersectionPoint); // Find neighbours of intersection point given as parameter
        ArrayList<Token> tokensFoundAligned = new ArrayList<Token>(); // All the tokens found aligned

        for (IntersectionPoint neighbour : neighbours){

            if (bool){ // If checking horizontal alignment
                if ( neighbour.getY() == intersectionPoint.getY() ){
                    // If horizontal neighbour has a token belongs to same player that is not yet been discovered
                    if (neighbour.hasToken() && (neighbour.getTokenInstance().getPlayer() == intersectionPoint.getTokenInstance().getPlayer()) &&
                            !tokensInMill.contains(neighbour.getTokenInstance())){
                        tokensFoundAligned.add(neighbour.getTokenInstance()); // add the token found to the output return
                        tokensInMill.add(neighbour.getTokenInstance()); // add the token found to be part of the mill
                    }
                }
            } else{ // If checking vertical alignment
                if ( neighbour.getX() == intersectionPoint.getX() ){
                    // If vertical neighbour has a token belongs to same player that is not yet been discovered
                    if (neighbour.hasToken() && (neighbour.getTokenInstance().getPlayer() == intersectionPoint.getTokenInstance().getPlayer()) &&
                            !tokensInMill.contains(neighbour.getTokenInstance())){
                        tokensFoundAligned.add(neighbour.getTokenInstance()); // add the token found to the output return
                        tokensInMill.add(neighbour.getTokenInstance()); // add the token found to be part of the mill
                    }
                }
            }

        }
        return tokensFoundAligned;
    }

    /**
     * Changes the state of all the tokens on the board and enables token of opponent player to be removed
     *
     * @param curPlayer current player who has formed a mill
     */
    public void changeToRemoveState(Player curPlayer){
        Player oppPlayer;

        Game game = Game.getInstance();
        //getting opponent
        if (curPlayer == game.getPlayer1()){
            oppPlayer = game.getPlayer2();
        }

        else {
            oppPlayer = game.getPlayer1();
        }

        int totalTokensOnBoard = 0; //count of tokens on the board for opponent player
        int totalTokensInMill = 0; //count of tokens in mills for opponent player

        //opponent's tokens are changed to removable state and highlighted red
        for (Token token : oppPlayer.getTokenList()){
            boolean tokenInMill = false; //boolean value to check if the token is part of a mill
            if (token.isTokenPlaced()){

                totalTokensOnBoard++;

                boolean tokenFound = false;
                //loop through all mills to check if token is part of mill
                for (ArrayList<Token> millFound : millTokens){
                    if (millFound.contains(token) && !tokenFound){
                        tokenInMill = true;
                        totalTokensInMill++;
                        tokenFound = true;
                    }
                }

                //token cannot be removed if it is part of a mill
                if (!tokenInMill){
                    token.toRemove = true;
                    token.repaint();
                    if (curPlayer.typeIsComputer()) {
                        token.changeListener(null, true);
                    }
                    else {
                        token.changeListener(new RemoveMove(token), true);
                    }
                }
            }
        }


        //tokens in mills can be removed if and only if there is no other token on the board not currently a part of a mill
        //tokens in mills equal total tokens on board means no other tokens are on board not currently part of a mill
        if ((totalTokensOnBoard == totalTokensInMill) || (totalTokensOnBoard == totalTokensInMill - 1) ){
            for (Token token : oppPlayer.getTokenList()){
                if (token.isTokenPlaced()) {
                    token.toRemove = true;
                    token.repaint();
                    token.changeListener(new RemoveMove(token), true);
                }
            }
        }

        //current player's tokens are set to non-movable state to force removal of opponent token
        for (Token token : curPlayer.getTokenList()){
            token.changeListener(null,true);
        }
    }

    /**
     * removing mills if a token in the mill has moved
     * @param curToken current token moved
     */
    public void checkIfTokenInMill(Token curToken){
        ArrayList<Integer> millIndices = new ArrayList<>();
        ArrayList<ArrayList<Token>> millsToRemove = new ArrayList<>();

        //iterating through all mills to check if token is in a mill
        for (ArrayList<Token> mill: millTokens){

            //if mill contains the token, the mill needs to be deleted since it is no longer a mill
            if (mill.contains(curToken)){
                millsToRemove.add(mill);    //mill in millTokens array
                for (Token token : mill){
                    token.inMill = false;
                    token.repaint();  //repainting to remove the blue highlight
                }
            }
        }

        //removing the mills which have been distorted
        for (ArrayList<Token> removableMill : millsToRemove){
            millTokens.remove(removableMill);
        }

        //iterating through the remaining mills to repaint any token which might have been a part of 2 mills
        for (ArrayList<Token> remainingMill: millTokens){
            for (Token token : remainingMill){
                token.inMill = true;
                token.repaint();  //resetting the blue highlight for the token which was a part of 2 mills and one of the mills was deleted
            }
        }
    }
}


