package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * An abstract class Move that has the implementations of the mouseEvents listened by Tokens
 * <p>
 * Created by Rachit Bhatia
 *
 * @author Tan Jun Yu
 * Modified by: Rachit Bhatia, Shoumil
 */

public abstract class Move extends MouseAdapter{

    /**
     * Token instance
     */
    private Token tokenInstance;

    /**
     * x coordinate of Token
     */
    private int xCoordinate;

    /**
     * y coordinate of Token
     */
    private int yCoordinate;

    /**
     * offset x value to set the location of Token so that the Token is always kept at the same position of it being pressed while being dragged
     */
    private int offSetX;

    /**
     * offset y value to set the location of Token so that the Token is always kept at the same position of it being pressed while being dragged
     */
    private int offSetY;

    /**
     * Constructor
     * @param tokenInstance the Token listening to the mouseEvents of Move
     * @param xCord x coordinate of the Token
     * @param yCord y coordinate of the Token
     */
    public Move(Token tokenInstance, int xCord, int yCord) {
        this.tokenInstance = tokenInstance;
        xCoordinate = xCord;
        yCoordinate = yCord;
    }


    /**
     * Actions performed when the mouse is released
     * @param cursor the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent cursor) {

        GameBoard gameBoard = GameBoard.getInstance();
        // Find neighbouring intersection points to the currIntersectionPoint and set them as valid intersection point
        ArrayList<IntersectionPoint> intersectionPoints = gameBoard.getIntersectionPoints();
        for (IntersectionPoint intersectionPoint : intersectionPoints){
            intersectionPoint.setPointSelected(false);  //set point selection to false once mouse is released
            intersectionPoint.repaint();  //repainting all intersection points with black border once mouse is released
        }

        if(tokenInstance.canBeUsed()){
            // All the intersectionPoints in the panel

            boolean foundIntersectionPoint = false;

            // The coordinate system of the component(Intersection Point)
            Point pointToUse;

            // Look through all the intersectionPoints to find if the token is released at any intersection points
            for (IntersectionPoint intersectionPoint : intersectionPoints) {

                // If the token instance is not yet placed on the board
                if (!tokenInstance.getParent().equals(gameBoard)){
                    // Change the coordinate system of the intersection point from the GameBoard Panel to the Main Panel which the Token is in
                    pointToUse = SwingUtilities.convertPoint(gameBoard,intersectionPoint.getX(),intersectionPoint.getY(),intersectionPoint.getParent().getParent());
                } else { // If the token is already on the board
                    // Use the coordinate system of the GameBoard that both the Token and Intersection Point are in
                    pointToUse = intersectionPoint.getLocation();
                }

                // If token is released at a valid intersection point , then set it as a new location of the token
                if (tokenInstance.getBounds().contains(pointToUse.getX(), pointToUse.getY(), intersectionPoint.getWidth(), intersectionPoint.getHeight())
                        && intersectionPoint.isMoveValid()) {

                    int newLocationX = intersectionPoint.getX() + (intersectionPoint.getWidth() / 2) - (tokenInstance.getWidth() / 2);
                    int newLocationY = intersectionPoint.getY() + (intersectionPoint.getHeight() / 2) - (tokenInstance.getHeight() / 2);

                    // Remove this particular token from the Main Panel
                    tokenInstance.getParent().remove(tokenInstance);

                    // Add the token into the GameBoard panel
                    gameBoard.add(tokenInstance);

                    // If this token previously belongs to another intersection point
                    if (tokenInstance.getIntersectionPoint() != null){
                        IntersectionPoint previousIntersectionPoint = tokenInstance.getIntersectionPoint();
                        // Remove the token from that Intersection Point
                        tokenInstance.removeFromIntersectionPoint();
                        previousIntersectionPoint.removeToken();
                    }

                    // Add the token to intersection point as an attribute
                    tokenInstance.addTokenToIntersectionPoint(intersectionPoint);
                    tokenInstance.setIsTokenPlaced(true);


                    MillChecker millChecker = MillChecker.getInstance();

                    // if token was part of a mill , remove it as it has been moved and is no longer part of a mill
                    millChecker.checkIfTokenInMill(tokenInstance);

                    // Checking for Mills and changing player state
                    boolean millFormed = millChecker.checkMill(intersectionPoint,true);


                    //setting the order of display on the game board: token appears above intersection point
                    gameBoard.setComponentZOrder(tokenInstance, 0);
                    gameBoard.setComponentZOrder(intersectionPoint, gameBoard.getComponentCount()-1); //last element

                    // If current player move is placing , remove the mouseListener
                    if (tokenInstance.getPlayer().getCurrentStateOfMove() == CurrentStateofMove.PLACING){
                        tokenInstance.changeListener(null,false);
                    }

                    // Set the token coordinate at the new location relative to the GameBoard's coordinate system
                    tokenInstance.setLocation(newLocationX, newLocationY);
                    xCoordinate = newLocationX;
                    yCoordinate = newLocationY;
                    foundIntersectionPoint = true;

                    if (!millFormed) { // If mill is not formed
                        Game.getInstance().incrementTurn(); // increment turn if mill is not found
                    }



                    gameBoard.repaint();
                    gameBoard.resetAllIntersectionPoints();
                    break;
                }
            }

            // if token is dragged to a non valid intersection point
            if (!foundIntersectionPoint) {
                // set it back to its original position
                tokenInstance.setLocation(xCoordinate, yCoordinate);
                gameBoard.resetAllIntersectionPoints();
            }
        }

    }

    /**
     * Actions performed when mouse being dragged
     * @param cursor the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent cursor) {

        if (tokenInstance.canBeUsed()){
            // Get the location of the cursor relative to the MainPanel coordinate system
            Point location = SwingUtilities.convertPoint(tokenInstance,cursor.getPoint(), GameBoard.getInstance().getParent());

            // If token is not yet placed on board
            if (!tokenInstance.isTokenPlaced()){
                // set the location of the token using coordinates relative to the MainPanel coordinate system
                tokenInstance.setLocation(location.x - this.offSetX,location.y - this.offSetY);
            }else{ // if token is already placed on the board

                // set the location of the token using coordinates relative to the GameBoard coordinate system
                Point newLocation = SwingUtilities.convertPoint(tokenInstance,cursor.getPoint(), tokenInstance.getParent());
                if (tokenInstance.getParent().getBounds().contains(location)){
                    tokenInstance.setLocation(newLocation.x - this.offSetX,newLocation.y - this.offSetY);
                }
            }
        }

    }

    /**
     * Getter for token instance
     * @return the Token instance
     */
    public Token getTokenInstance() {
        return tokenInstance;
    }

    /**
     * Setter for offset values
     * @param offSetX new offsetX value
     * @param offSetY new offsetY value
     */
    public void setOffSets(int offSetX,int offSetY) {
        this.offSetX = offSetX;
        this.offSetY = offSetY;
    }



}
