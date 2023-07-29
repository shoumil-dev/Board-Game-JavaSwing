package game;


import java.util.ArrayList;
import java.util.Random;

/**
 * Computer Player that randomly performs an action depending on the state of move it is currently in
 * <p>
 * Created  by Tan Jun Yu
 * @author  Tan Jun Yu , Rachit Bhatia
 * Modified by: -
 */

public class ComputerPlayer extends Player implements NeighbourPositionFinder, TokenRemoval{

    /**
     * Constructor
     */
    public ComputerPlayer(){
        super();
        this.isComputer = true;

    }

    /**
     * Set the current turn of the game to Computer so that Computer can perform a random move
     * @param bool boolean true if it is Computer's turn , False otherwise.
     */
    @Override
    public void setPlayerTurn(boolean bool) {
        if (bool){ // If current turn is ComputerPlayer turn
            Token tokenUsed ; // random token used
            IntersectionPoint intersectionPointUsed ; // random intersection point used

            produceDelay(2000);  //delay of 2 secs before a move is made to give a better playing experience

            // If Computer currentStateOfMove is sliding
            if (this.getCurrentStateOfMove() == CurrentStateofMove.SLIDING){
                tokenUsed =  generateRandomSlidingMove();
            }
            else {// If Computer currentStateOfMove is flying or placing
                tokenUsed = generateRandomPlacingFlyingMove();
            }

            // Get the new location of the token
            intersectionPointUsed = tokenUsed.getIntersectionPoint();
            int newLocationX = intersectionPointUsed.getX() + (intersectionPointUsed.getWidth() / 2) - (tokenUsed.getWidth() / 2);
            int newLocationY = intersectionPointUsed.getY() + (intersectionPointUsed.getHeight() / 2) - (tokenUsed.getHeight() / 2);
            tokenUsed.setLocation(newLocationX, newLocationY); // Set the token location to the new location found

            MainWindow.getInstance().repaint();

            //mill checking and handling operations
            MillChecker millChecker = MillChecker.getInstance();
            millChecker.checkIfTokenInMill(tokenUsed); //checking removal of mills after every move
            boolean millFormed = millChecker.checkMill(tokenUsed.getIntersectionPoint(),false);
            if (millFormed){
               generateRandomRemoveMove();
            }

            Game.getInstance().incrementTurn(); // Increment turn so that turn is switched back to HumanPlayer

        }
    }

    /**
     * Generates a random integer between 0 to maxInt.
     * @return an random integer
     */
    public int generateRandomNumber(int maxInt) {
        return new Random().nextInt(maxInt);
    }

    /**
     * Produce delay before Computer makes a move
     * @param delay seconds of delay in ms
     */
    private void produceDelay(int delay){
        try {
            PerformThread.sleep(delay);  //thread sleeps for mentioned time delay
        }
        catch(InterruptedException newException) {
            PerformThread.currentThread().interrupt();  //raising interrupt for all thread usages to know about this time delay
        }
    }

    /**
     * Generate a random sliding move
     * @return the token that is randomly selected for sliding
     */
    private Token generateRandomSlidingMove(){
        int randomTokenIndex = generateRandomNumber(this.getNumberOfTokens());
        Token tokenUsed = this.getTokenList().get(randomTokenIndex);

        boolean validSlidingMoveFound = false;

        while (!validSlidingMoveFound){ // while valid sliding move is not found

            // look for an empty neighbour intersection point of the random token selected
            IntersectionPoint intersectionPoint = tokenUsed.getIntersectionPoint();
            ArrayList<IntersectionPoint> neighbours = findNeighbouringIntersections(intersectionPoint);
            for (IntersectionPoint neighbour : neighbours){
                if (!neighbour.hasToken() && !validSlidingMoveFound){
                    intersectionPoint.removeToken();
                    tokenUsed.removeFromIntersectionPoint();
                    tokenUsed.addTokenToIntersectionPoint(neighbour);
                    validSlidingMoveFound = true;
                }
            }

            // if current random token selected has no valid sliding move, proceed to look for another random token
            if(!validSlidingMoveFound){
                randomTokenIndex = generateRandomNumber(this.getNumberOfTokens());
                tokenUsed = this.getTokenList().get(randomTokenIndex);
            }
        }

        return tokenUsed;
    }

    /**
     * Generate random placing move or flying move
     * @return the token randomly selected for placing or flying
     */
    private Token generateRandomPlacingFlyingMove(){

        // Random token selected
        int randomTokenIndex = generateRandomNumber(this.getNumberOfTokens());
        Token tokenUsed = this.getTokenList().get(randomTokenIndex);

        // Random intersection point selected
        int randomIntersectionPointIndex = generateRandomNumber(GameBoard.getInstance().getIntersectionPoints().size());
        IntersectionPoint newIntersectionPoint = GameBoard.getInstance().getIntersectionPoints().get(randomIntersectionPointIndex);

        boolean emptyIntersectionPointFound = false;

        // if it is during placing move
        if (!this.areAllTokensPlaced()){
            while (tokenUsed.isTokenPlaced()){ // Ensure the random token selected is outside the board (not yet placed)
                randomTokenIndex = generateRandomNumber(this.getNumberOfTokens());
                tokenUsed = this.getTokenList().get(randomTokenIndex);
            }
        }

        // While an empty intersection point is not yet found
        while (!emptyIntersectionPointFound){

            if(!newIntersectionPoint.hasToken()){
                if(tokenUsed.getIntersectionPoint() != null){ // If token is already on the board ( happens during FLYING)
                    tokenUsed.getIntersectionPoint().removeToken();
                    tokenUsed.removeFromIntersectionPoint();
                } else { // If it does not belong to any intersection point , still outside the board ( happens during PLACING)

                    // Remove this particular token from the Main Panel
                    tokenUsed.getParent().remove(tokenUsed);
                    // Add the token into the GameBoard panel
                    GameBoard.getInstance().add(tokenUsed);

                    //setting the order of display on the game board: token appears above intersection point
                    GameBoard.getInstance().setComponentZOrder(tokenUsed, 0);
                    GameBoard.getInstance().setComponentZOrder(newIntersectionPoint, GameBoard.getInstance().getComponentCount()-1); //last element
                }
                // Add the token to intersection point as an attribute
                tokenUsed.addTokenToIntersectionPoint(newIntersectionPoint);
                tokenUsed.setIsTokenPlaced(true);
                emptyIntersectionPointFound = true;
            }
            if(!emptyIntersectionPointFound){ // If the random intersection point selected has a token (invalid) , look for another random empty intersection point
                randomIntersectionPointIndex = generateRandomNumber(GameBoard.getInstance().getIntersectionPoints().size());
                newIntersectionPoint = GameBoard.getInstance().getIntersectionPoints().get(randomIntersectionPointIndex);
            }
        }

        return tokenUsed;
    }


    /**
     * Generate a random remove move . Selects a random removable token of the player to be removed .
     */
    private void generateRandomRemoveMove(){
        Player otherPlayer = Game.getInstance().getPlayer1();
        int randomTokenIndex = generateRandomNumber(otherPlayer.getNumberOfTokens());
        Token tokenToBeRemoved = otherPlayer.getTokenList().get(randomTokenIndex); //random token generation

        //getting removable token which is not part of a mill and which is already on the game board
        while (!(!tokenToBeRemoved.inMill && tokenToBeRemoved.isTokenPlaced())) {
            randomTokenIndex = generateRandomNumber(otherPlayer.getNumberOfTokens());
            tokenToBeRemoved = otherPlayer.getTokenList().get(randomTokenIndex);
        }

        produceDelay(5000); //delay of 5 secs before a token is removed to let the player know about the game's state
        performRemoval(tokenToBeRemoved); //performing removal action on randomly selected token
    }
}