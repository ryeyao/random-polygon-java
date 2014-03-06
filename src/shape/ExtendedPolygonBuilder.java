package shape;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Rye
 * Date: 3/4/14
 * Time: 3:15 PM
 */
public class ExtendedPolygonBuilder {

    private RectangleContainer container;
    private static final Logger logger = Logger.getLogger(ExtendedPolygon.class.getName());

    public ExtendedPolygonBuilder(RectangleContainer container) {
        this.container = container;
        logger.setLevel(Level.ALL);
    }

    public ExtendedPolygon buildPolygon(int edgeNum, int minRadius, int maxRadius) {
        return this.randAnyPolygon(edgeNum, minRadius, maxRadius);
    }

    private ExtendedPolygon randAnyPolygon(int edgeNum, int minRadius, int maxRadius) {
        Random rand = new Random(Double.doubleToLongBits(Math.random()));

        Point center = new Point();
        center.x = rand.nextInt(container.width - 1) + 1;
        center.y = rand.nextInt(container.height - 1) + 1;
        int radius = minRadius + rand.nextInt(maxRadius - minRadius + 1);
        double minTriangleArea = 0.2 * Math.PI * radius * radius;

        ArrayList<Point> generatedPoints = new ArrayList<Point>();

        ExtendedPolygon triangle = new ExtendedPolygon();
        for (int i = 0; i < edgeNum; i++) {
            Point p = new Point();
            do {
                // TODO(Rye): use general formula to be able to limit x & y
                double param_t = rand.nextDouble() * 2 * Math.PI;
                p.setLocation(radius * Math.cos(param_t) + center.x, radius * Math.sin(param_t) + center.y);
                if (!generatedPoints.contains(p)) {
                    generatedPoints.add(p);
                    triangle.addPoint(p.x, p.y);
                    break;
                }
            } while(true);

            if(i == 2) {
                // Limit the smallest area of triangle to 1/4 of the circle
                if(triangle.getArea() < minTriangleArea) {
                    i = 0;
                    generatedPoints.remove(generatedPoints.size() - 1);
                    generatedPoints.remove(generatedPoints.size() - 1);
                    triangle = new ExtendedPolygon();
                    Point tmp = generatedPoints.get(generatedPoints.size() - 1);
                    triangle.addPoint(tmp.x, tmp.y);
                    continue;
                }
            }
        }

        final Point c = center;

        logger.fine("new one\n");
        Collections.sort(generatedPoints, new Comparator<Point>() {
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

                axis.setLocation(-c.x, 0);
                axisVertex.setLocation(0, c.y);

                vec1.setLocation(point.x - c.x, point.y - c.y);
                vec2.setLocation(point2.x - c.x, point2.y - c.y);

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
                        logger.fine(String.format("1.vec1=(%d, %d), vec2=(%d, %d)\n", vec1.x, vec1.y, vec2.x, vec2.y));
                        return -1;
                    } else {
                        logger.fine(String.format("2.vec1=(%d, %d), vec2=(%d, %d)\n", vec1.x, vec1.y, vec2.x, vec2.y));
                        return 1;
                    }
                } else if(on_left_av1 && on_left_av2) {
                    if(on_right_v1v2) {
                        logger.fine(String.format("3.vec1=(%d, %d), vec2=(%d, %d)\n", vec1.x, vec1.y, vec2.x, vec2.y));
                        return -1;
                    } else {
                        logger.fine(String.format("4.vec1=(%d, %d), vec2=(%d, %d)\n", vec1.x, vec1.y, vec2.x, vec2.y));
                        return 1;
                    }
                } else if(on_right_av1 && on_left_av2) {
                    logger.fine(String.format("5.vec1=(%d, %d), vec2=(%d, %d)\n", vec1.x, vec1.y, vec2.x, vec2.y));
                    return -1;
                } else if(on_left_av1 && on_right_av2) {
                    logger.fine(String.format("6.vec1=(%d, %d), vec2=(%d, %d)\n", vec1.x, vec1.y, vec2.x, vec2.y));
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
        });

        ExtendedPolygon polygon = new ExtendedPolygon();
        polygon.setCircleCenter(center);
//        logger.fine(String.println("Points:");
        for (Point p : generatedPoints) {
//            logger.fine(String.format("(%d, %d)\n", p.x, p.y);
            polygon.addPoint(p.x, p.y);
        }

        return polygon;
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
