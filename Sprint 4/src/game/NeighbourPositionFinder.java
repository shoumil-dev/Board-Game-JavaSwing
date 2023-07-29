package game;

import java.util.ArrayList;

/**
 * NeighbourPositionFinder helps to find adjacent intersection points of an intersection point instance
 * <p>
 * Created by Tan Jun Yu
 *
 * @author Shoumil
 * Modified by: Rachit Bhatia, Tan Jun Yu
 */
public interface NeighbourPositionFinder {


    /**
     * Find all the adjacent intersection points of an intersection point given as parameter
     * @param intersectionPointInstance intersection point in which its neigbouring intersection points will be returned
     * @return neighbouring intersection points of a particular intersection point in the form ArrayList
     */
    public default ArrayList<IntersectionPoint> findNeighbouringIntersections(IntersectionPoint intersectionPointInstance){


        int indexOfIntersectionPoint  = 0 ; // dummy value
        ArrayList<IntersectionPoint> intersectionPointsList = GameBoard.getInstance().getIntersectionPoints();

        // Look for the index of the intersectionPointInstance
        for (int i = 0 ; i <intersectionPointsList.size() ; i ++){
            if (intersectionPointsList.get(i) == intersectionPointInstance) {
                indexOfIntersectionPoint = i;
            }
        }

        ArrayList<IntersectionPoint> neighbours = new ArrayList<IntersectionPoint>();

        // if intersection point is at 7, 15, 23
        if (indexOfIntersectionPoint % 8 == 7) {
            neighbours.add(intersectionPointsList.get(indexOfIntersectionPoint - 1));
            neighbours.add(intersectionPointsList.get(indexOfIntersectionPoint - 7));
        }
        // if intersection point is at 0, 8, 16
        else if (indexOfIntersectionPoint % 8 == 0) {
            neighbours.add(intersectionPointsList.get(indexOfIntersectionPoint + 1));
            neighbours.add(intersectionPointsList.get(indexOfIntersectionPoint + 7));
        }
        // other intersection points
        else {
            neighbours.add(intersectionPointsList.get(indexOfIntersectionPoint + 1));
            neighbours.add(intersectionPointsList.get(indexOfIntersectionPoint - 1));
        }
        // middle intersection point, hence only odd intersections
        if (indexOfIntersectionPoint % 2 == 1) {

            if ( indexOfIntersectionPoint - 8 >= 0 ){
                neighbours.add(intersectionPointsList.get(indexOfIntersectionPoint - 8));
            }
            if ( indexOfIntersectionPoint + 8 <24 ){
                neighbours.add(intersectionPointsList.get(indexOfIntersectionPoint + 8));
            }
        }

        return neighbours;


    }
}
