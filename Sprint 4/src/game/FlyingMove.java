package game;


/**
 * Class FlyingMove that has implementations of the MouseEvents
 * <p>
 * Created  by Rachit Bhatia
 *
 * @author Rachit Bhatia , Tan Jun Yu
 * Modified by: -
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class FlyingMove extends Move {

    /**
     * Constructor
     * @param tokenInstance the Token using FlyingMove as a mouseListener
     * @param xCord initial x coordinate of the Token
     * @param yCord initial y coordinate of the Token
     */
    public FlyingMove(Token tokenInstance, int xCord, int yCord) {
        super(tokenInstance, xCord, yCord);
    }

    /**
     * Look for empty intersection points and set them to valid positions to place the token
     * @param cursor the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent cursor) {

        if (super.getTokenInstance().canBeUsed()){
            // To find the offSet values so that the cursor can be at the same position of the token while being drag
            Point startPoint = SwingUtilities.convertPoint(super.getTokenInstance(), cursor.getPoint(), super.getTokenInstance().getParent());
            super.setOffSets(startPoint.x - super.getTokenInstance().getBounds().x , startPoint.y - super.getTokenInstance().getBounds().y);

            // Find valid moves
            ArrayList<IntersectionPoint> intersectionPoints = GameBoard.getInstance().getIntersectionPoints();
            for (IntersectionPoint intersectionPoint : intersectionPoints){
                if (!intersectionPoint.hasToken()){
                    intersectionPoint.setMoveValid(true);
                    intersectionPoint.setPointSelected(true);
                    intersectionPoint.repaint(); //repainting intersection point with green border when a token is selected
                }
            }
        }

    }

}
