package game;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * RemoveMove as a mouseListener to remove tokens
 * <p>
 * Created by Rachit Bhatia
 *
 * @author Rachit Bhatia
 * Modified by: Shoumil, Tan Jun Yu
 */

public class RemoveMove extends MouseAdapter implements TokenRemoval {

    /**
     * token to be removed
     */
    private Token tokenInstance;

    /**
     * Constructor
     * @param tokenInstance token to remove
     */
    public RemoveMove(Token tokenInstance) {
        this.tokenInstance = tokenInstance;
    }

    /**
     * Resets the remaining tokens and board to their original appearance and removed the selected token from the game
     * @param cursor the mouse pointer
     */
    @Override
    public void mouseClicked(MouseEvent cursor) {
        performRemoval(tokenInstance);
        Game game = Game.getInstance();
        game.incrementTurn(); //turn increment only after token has been removed
    }


}



