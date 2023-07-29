package game;

/**
 * TokenRemoval action
 * <p>
 * Created  by Rachit Bhatia
 *
 * @author  Rachit Bhatia
 * Modified by: -
 */
public interface TokenRemoval {

    /**
     * Perform actions of removing a token
     * @param tokenInstance the token selected for removal
     */
    public default void performRemoval(Token tokenInstance){
        GameBoard gameBoard = GameBoard.getInstance();
        //resetting token appearance to remove pink highlight of border after selection has been made
        for (IntersectionPoint position : gameBoard.getIntersectionPoints()){
            Token curToken = position.getTokenInstance();
            if (curToken != null){
                curToken.toRemove = false ; //removal state false
                curToken.repaint();
            }
        }

        gameBoard.remove(tokenInstance);  //removing token from game board
        tokenInstance.getIntersectionPoint().removeToken();  //removing token from intersection point
        tokenInstance.getPlayer().getTokenList().remove(tokenInstance); //removing token from player's list


        Game game = Game.getInstance();
        // Remove all temporary listeners of the tokens after a selection has been made , so that the game returns to original state
        for( Token token: game.getPlayer1().getTokenList()){
            token.removeTemporaryListener();
        }
        for( Token token: game.getPlayer2().getTokenList()){
            token.removeTemporaryListener();
        }

        //updating the UI state of the board
        gameBoard.revalidate();
        gameBoard.repaint();

        //checking to remove mills when token removed
        MillChecker.getInstance().checkIfTokenInMill(tokenInstance);
    }
}
