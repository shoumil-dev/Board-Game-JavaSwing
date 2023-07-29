package game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


/**
 * A JPanel representing the GameBoard that has all the intersection points
 * <p>
 * Created  by Shoumil
 *
 * @author Shoumil, Tan Jun Yu
 * Modified by: Rachit Bhatia
 */
public class GameBoard extends JPanel {

    /**
     * An arrayList containing all the intersection points on the GameBoard
     */
    private ArrayList<IntersectionPoint> intersectionPointsList;

    /**
     * Singleton instance of GameBoard
     */
    private static GameBoard instance ;

    /**
     * Constructor
     */
    private GameBoard(){
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        setLayout(null);

        // RGB values for color of GameBoard UI
        int redComponentId = 110;
        int greenComponentId = 65;
        int blueComponentId = 40;

        setBackground(new Color(redComponentId, greenComponentId, blueComponentId).brighter()); // Set background color of GameBoard
        intersectionPointsList = new ArrayList<IntersectionPoint>(); // Initialize a list of Intersection Points on the GameBoard
    }

    /**
     * Access GameBoard singleton instance publicly
     * @return GameBoard instance
     */
    public static GameBoard getInstance(){
        if (instance == null) {
            instance = new GameBoard();
        }

        return instance;
    }

    /**
     * Resets the game-board instance
     */
    public static void voidInstance() {
        instance = null;
    }


    /**
     * Set the dimensions of GameBoard according to the screen size of user
     * @param screenDimension dimensions of the user's screen
     */
    public void setDimensionsOfBoard(Dimension screenDimension){

        double boardWidthAdjustment = 2.4;
        double boardHeightAdjustment = 1.35;
        int boardWidth = (int) (screenDimension.width/boardWidthAdjustment);
        int boardHeight = (int) (screenDimension.height/boardHeightAdjustment);
        int boardX = (screenDimension.width - boardWidth) / 2;
        int boardY = (screenDimension.height - boardHeight) / 2;

        this.setBounds(boardX, boardY, boardWidth, boardHeight);
    }

    /**
     * Adding all the intersection points into their respective positions on the GameBoard
     * @param boardWidth width of GameBoard
     * @param boardHeight height of GameBoard
     */
    public void addIntersections (int boardWidth, int boardHeight){

        int adjustmentOneOverEightOfBoard = 8;
        int adjustmentHalfOfBoard = 2;
        int adjustmentOneQuarterOfBoard = 4;
        /*
        The intersection points have to be repeated because of the unique
        coordinates for each point on the board. Thus, they have to be added manually.
        */

        int[][] points = {
            // Add 8 points to the outer square
            {boardWidth/adjustmentOneOverEightOfBoard , boardHeight/adjustmentOneOverEightOfBoard},
            {boardWidth/ adjustmentHalfOfBoard, boardHeight/ adjustmentOneOverEightOfBoard},
            {boardWidth-boardWidth/ adjustmentOneOverEightOfBoard, boardHeight/ adjustmentOneOverEightOfBoard},
            {boardWidth-boardWidth/ adjustmentOneOverEightOfBoard, boardHeight/ adjustmentHalfOfBoard},
            {boardWidth-boardWidth/ adjustmentOneOverEightOfBoard, boardHeight-boardHeight/ adjustmentOneOverEightOfBoard},
            {boardWidth/ adjustmentHalfOfBoard, boardHeight-boardHeight/ adjustmentOneOverEightOfBoard},
            {boardWidth/ adjustmentOneOverEightOfBoard, boardHeight-boardHeight/ adjustmentOneOverEightOfBoard},
            {boardWidth/ adjustmentOneOverEightOfBoard, boardHeight/ adjustmentHalfOfBoard},

            // Add 8 points to the middle square
            {boardWidth/ adjustmentOneQuarterOfBoard, boardHeight/ adjustmentOneQuarterOfBoard},
            {boardWidth/ adjustmentHalfOfBoard, boardHeight/ adjustmentOneQuarterOfBoard},
            {boardWidth-boardWidth/ adjustmentOneQuarterOfBoard, boardHeight/ adjustmentOneQuarterOfBoard},
            {boardWidth-boardWidth/ adjustmentOneQuarterOfBoard, boardHeight/ adjustmentHalfOfBoard},
            {boardWidth-boardWidth/ adjustmentOneQuarterOfBoard, boardHeight-boardHeight/ adjustmentOneQuarterOfBoard},
            {boardWidth/ adjustmentHalfOfBoard, boardHeight-boardHeight/ adjustmentOneQuarterOfBoard},
            {boardWidth/ adjustmentOneQuarterOfBoard, boardHeight-boardHeight/ adjustmentOneQuarterOfBoard},
            {boardWidth/ adjustmentOneQuarterOfBoard, boardHeight/ adjustmentHalfOfBoard},

            // Add 8 points to the inner square
            {boardWidth/ adjustmentHalfOfBoard - boardWidth/ adjustmentOneOverEightOfBoard, boardHeight/ adjustmentHalfOfBoard - boardHeight/ adjustmentOneOverEightOfBoard},
            {boardWidth/ adjustmentHalfOfBoard, boardHeight/ adjustmentHalfOfBoard - boardHeight/ adjustmentOneOverEightOfBoard},
            {boardWidth/ adjustmentHalfOfBoard + boardWidth/ adjustmentOneOverEightOfBoard, boardHeight/ adjustmentHalfOfBoard - boardHeight/ adjustmentOneOverEightOfBoard},
            {boardWidth/ adjustmentHalfOfBoard + boardWidth/ adjustmentOneOverEightOfBoard, boardHeight/ adjustmentHalfOfBoard},
            {boardWidth/ adjustmentHalfOfBoard + boardWidth/ adjustmentOneOverEightOfBoard, boardHeight/ adjustmentHalfOfBoard + boardHeight/ adjustmentOneOverEightOfBoard},
            {boardWidth/ adjustmentHalfOfBoard, boardHeight/ adjustmentHalfOfBoard + boardHeight/ adjustmentOneOverEightOfBoard},
            {boardWidth/ adjustmentHalfOfBoard - boardWidth/ adjustmentOneOverEightOfBoard, boardHeight/ adjustmentHalfOfBoard + boardHeight/ adjustmentOneOverEightOfBoard},
            {boardWidth/ adjustmentHalfOfBoard - boardWidth/ adjustmentOneOverEightOfBoard, boardHeight/ adjustmentHalfOfBoard}
        };

        // Add the intersection points as components to the GameBoard Panel
        for (int[] point : points) {
            IntersectionPoint intersectionPoint = new IntersectionPoint(point[0], point[1]);
            intersectionPointsList.add(intersectionPoint);
            this.add(intersectionPoint);
        }

    }

    /**
     * Drawing the UI of the GameBoard
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D lineShapeEnhance = (Graphics2D) g;
        lineShapeEnhance.setColor(Color.BLACK);
        lineShapeEnhance.setStroke(new BasicStroke(8));

        int intersectionPointCount = 24;
        int incrementPosition = 8;
        int incrementPositionSmall = 1;
        int firstID = 0;
        int startPoint;
        int endPoint;

        // Joining all outer points with lines except final two points
        int secondID = 1;
        int lastPointInSquareIndex = 7;
        for (int k = 0; k < intersectionPointCount; k += incrementPosition) {
            startPoint = k;
            endPoint = startPoint + lastPointInSquareIndex;
            drawLines(startPoint, endPoint, firstID, secondID, incrementPositionSmall, g);
        }

        // Joining final two points in each concentric square
        secondID = -7;
        startPoint = 7;
        endPoint = 24;
        drawLines(startPoint, endPoint, firstID , secondID, incrementPosition, g);

        //intersection points ID values for inner section of the board (not part of the square)
        secondID = 8;
        int innerLineCount = 4;

        // Joining inner lines that are not part of the square
        startPoint = 1;
        endPoint = 10;
        int incrementInnerLine = -2;

        for (int k = 0; k < innerLineCount; k++){
            drawLines(startPoint, endPoint, firstID, secondID, incrementPosition, g);

            if (firstID == 0) {
                firstID = 8;
                secondID = 16; }

            firstID += incrementInnerLine;
            secondID += incrementInnerLine;
        }
    }

    /**
     * Method for drawing the connecting lines on the board between given coordinates
     * Multiple parameters are needed to meet the reach each unique coordinate in a single loop
     * @return void
     */
    public void drawLines(int startPoint, int endPoint, int firstID, int secondID, int increment, Graphics g) {
        int adjustment = 19;

        // iterate through intersectionPointsList
        for (int i = startPoint; i < endPoint; i += increment) {
            IntersectionPoint point1 = intersectionPointsList.get(i + firstID);
            IntersectionPoint point2 = intersectionPointsList.get(i + secondID);
            // draw a line between point1 and point2
            g.drawLine(point1.getX() + adjustment, point1.getY() + adjustment, point2.getX() + adjustment, point2.getY() + adjustment);
        }
    }

    /**
     * Getter for the intersectionPointsList attribute
     * @return an ArrayList of Intersection Points
     */
    public ArrayList<IntersectionPoint> getIntersectionPoints(){
        return intersectionPointsList;
    }

    /**
     * Reset all the intersection points back to invalid move whenever a new token is picked up by players
     */
    public void resetAllIntersectionPoints(){
        for (IntersectionPoint intersectionPoint : intersectionPointsList){
            intersectionPoint.setMoveValid(false);
        }
    }
}
