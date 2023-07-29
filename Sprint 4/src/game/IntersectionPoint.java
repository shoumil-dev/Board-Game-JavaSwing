package game;

import javax.swing.*;
import java.awt.*;


/**
 * A JComponent representing the Intersection Point
 * <p>
 * Created  by Tan Jun Yu
 *
 * @author Tan Jun Yu
 * Modified by: Shoumil, Rachit Bhatia
 */
public class IntersectionPoint extends JComponent {

    /**
     * The diameter of Intersection Point
     */
    private final int DIAMETER = 30;

    /**
     * The Token belongs to this intersection point
     */
    private Token tokenInstance ;

    /**
     * boolean value indicating whether the token on the intersection point has been selected for movement
     */
    private boolean pointSelected;

    /**
     * boolean value indicating whether a move is valid
     */
    private boolean isMoveValid ;


    /**
     * Constructor
     * @param x xCoordinate of the intersection point
     * @param y yCoordinate of the intersection point
     */
    public IntersectionPoint(int x,int y) {

        GameBoard.  getInstance().add(this);
        setBounds(x  -  ( DIAMETER /2 ),y-  ( DIAMETER /2 ), DIAMETER + 8, DIAMETER + 8 );

        tokenInstance = null;
        isMoveValid = false;

    }

    /**
     * Draw the UI of the Intersection Point
     * @param pointShape the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics pointShape) {
        pointShape.setColor(Color.ORANGE);
        pointShape.fillOval(3, 3, DIAMETER, DIAMETER);  //fill the position to be of orange colour

        Graphics2D pointShapeEnhance = (Graphics2D) pointShape;  //Graphics2D class used to change thickness of borders
        pointShapeEnhance.setStroke(new BasicStroke(4));   //border thickness set to 4
        pointShapeEnhance.setColor(Color.black);

        //highlight with green border if the token on the intersection point is selected
        if (pointSelected == true){
            pointShapeEnhance.setColor(Color.GREEN.darker());
        }

        pointShapeEnhance.drawOval(3, 3, DIAMETER, DIAMETER);  //outline border
    }


    /**
     * Adding a Token instance to the IntersectionPoint
     * @param token token to be added
     */
    public void addToken(Token token){
        this.tokenInstance = token;
    }

    /**
     * Remove a token from this intersection point
     */
    public void removeToken(){
        this.tokenInstance = null;
    }

    /**
     * Check if this intersection point has a token
     * @return boolean True if intersection point has a token . False otherwise
     */
    public boolean hasToken(){
        return this.tokenInstance != null;
    }

    /**
     * Getter for isMoveValid
     * @return True if move is valid . False otherwise
     */
    public boolean isMoveValid(){
        return this.isMoveValid;

    }

    /**
     * Setter of isMoveValid
     * @param bool True if move is vali d. False otherwise
     */
    public void setMoveValid(boolean bool){
        this.isMoveValid = bool;
    }

    /**
     * Set point as selected for highlighting
     * @param pointSelection
     */
    public void setPointSelected(boolean pointSelection){
        this.pointSelected = pointSelection;
    }

    /**
     * Getter for token that is placed on the intersection point
     * @return token that is on the intersection point
     */
    public Token getTokenInstance() {return this.tokenInstance;}

}
