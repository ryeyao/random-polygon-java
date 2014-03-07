package util;

import java.awt.*;
import java.util.Comparator;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Rye
 * Date: 3/7/14
 * Time: 11:08 AM
 */
public class VectorComparator implements Comparator<Point> {

    private static final Logger logger = Logger.getLogger(VectorComparator.class.getName());
    private Point referp;

    public void setReferPoint(Point referp) {
        this.referp = referp;
    }
    private double getMagnitude(Point vector) {
        return Math.sqrt(Math.pow(vector.x, 2) + Math.pow(vector.y, 2));
    }

    private double dotProduct(Point vec1, Point vec2) {
        return vec1.x * vec2.x + vec1.y * vec2.y;
    }

    private double getAngleCos(Point vec1, Point vec2) {
        return dotProduct(vec1, vec2)/(getMagnitude(vec1) * getMagnitude(vec2));
    }

    @Override
    public int compare(Point point, Point point2) {
        Point vec1 = new Point();
        Point vec2 = new Point();

        Point axis = new Point();
        Point axisVertex = new Point();

        axis.setLocation(-referp.x, 0);
        axisVertex.setLocation(0, referp.y);

        vec1.setLocation(point.x - referp.x, point.y - referp.y);
        vec2.setLocation(point2.x - referp.x, point2.y - referp.y);

        double cos1 = getAngleCos(axis, vec1);
        double cos2 = getAngleCos(axis, vec2);

        boolean on_right_av1 = onTheRightSide(axis, vec1);
        boolean on_right_av2 = onTheRightSide(axis, vec2);
        boolean on_left_av1 = onTheLeftSide(axis, vec1);
        boolean on_left_av2 = onTheLeftSide(axis, vec2);
        boolean on_same_av1 = onTheSameLine(axis, vec1);
        boolean on_same_av2 = onTheSameLine(axis, vec2);
        boolean on_right_v1v2 = onTheRightSide(vec1, vec2);
        boolean on_same_v1v2 = onTheSameLine(vec1, vec2);
        double dotpro_av1 = dotProduct(axis, vec1);
        double dotpro_av2 = dotProduct(axis, vec2);
        double magpro_av1 = getMagnitude(axis) * getMagnitude(vec1);
        double magpro_av2 = getMagnitude(axis) * getMagnitude(vec2);

        if(on_right_av1 && on_right_av2) {
            if(on_right_v1v2) {
                return -1;
            } else {
                return 1;
            }
        } else if(on_left_av1 && on_left_av2) {
            if(on_right_v1v2) {
                return -1;
            } else {
                return 1;
            }
        } else if(on_right_av1 && on_left_av2) {
            return -1;
        } else if(on_left_av1 && on_right_av2) {
            return 1;
        } else if(on_same_v1v2) {
            // on_same_av1 && on_same_av2
            if(dotpro_av1 == magpro_av1) {
                return -1;
            } else {
                return 1;
            }
        } else if(on_same_av1 && on_left_av2) {
            return -1;
        } else if(on_same_av1 && on_right_av2) {
            if(dotpro_av1 == magpro_av1) {
                return -1;
            } else {
                return 1;
            }
        } else if(on_same_av2 && on_left_av1) {
            return 1;
        } else {
//                } else if(on_same_av2 && on_right_av1) {
            if(dotpro_av2 == magpro_av2) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    // In a colockwise circle, same to all.
    private boolean onTheRightSide(Point vector, Point vector2) {
        return vector2.y * vector.x - vector2.x * vector.y < 0;
    }

    private boolean onTheLeftSide(Point vector, Point vector2) {
        return vector2.y * vector.x - vector2.x * vector.y > 0;
    }

    private boolean onTheSameLine(Point vector, Point vector2) {
        return vector2.y * vector.x - vector2.x * vector.y == 0;
    }
}
