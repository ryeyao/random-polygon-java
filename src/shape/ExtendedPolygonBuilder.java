package shape;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Rye
 * Date: 3/4/14
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExtendedPolygonBuilder {

    private RectangleContainer container;


    public ExtendedPolygonBuilder(RectangleContainer container) {
        this.container = container;
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
                axis.setLocation(-c.x, 0);

                vec1.setLocation(point.x - c.x, point.y - c.y);
                vec2.setLocation(point2.x - c.x, point2.y - c.y);

                double cos1 = getAngleCos(axis, vec1);
                double cos2 = getAngleCos(axis, vec2);

                if(onTheRightSide(axis, vec1) && onTheRightSide(axis, vec2)) {
                    if(onTheRightSide(vec1, vec2)) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else if(onTheLeftSide(axis, vec1) && onTheLeftSide(axis, vec2)) {
                    if(onTheRightSide(vec1, vec2)) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else if(onTheRightSide(axis, vec1) && onTheLeftSide(axis, vec2)) {
                    return -1;
                } else if(onTheLeftSide(axis, vec1) && onTheRightSide(axis, vec2)) {
                    return 1;
                } else {

                    if(onTheSameLine(axis, vec1)) {
                        double magProduct = axis.distance(c) * point.distance(c);
                        if (dotProduct(axis, vec1) == magProduct) {
                            return -1;
                        } else {
                            if(onTheLeftSide(axis, vec2)) {
                                return -1;
                            } else {
                                return 1;
                            }
                        }
//                    } else if(onTheSameLine(axis, vec2)) {
                    } else {
                        double magProduct = axis.distance(c) * point2.distance(c);
                        if (dotProduct(axis, vec2) == magProduct) {
                            return 1;
                        } else {
                            if(onTheLeftSide(axis, vec1)) {
                                return -1;
                            } else {
                                return 1;
                            }
                        }
                    }
                }
            }
        });

        ExtendedPolygon polygon = new ExtendedPolygon();
        for (Point p : generatedPoints) {
            polygon.addPoint(p.x, p.y);
        }

        return polygon;
    }

    private boolean onTheRightSide(Point vector, Point vector2) {
        return vector2.y * vector.x - vector2.x * vector.y > 0;
    }

    private boolean onTheLeftSide(Point vector, Point vector2) {
        return vector2.y * vector.x - vector2.x * vector.y < 0;
    }

    private boolean onTheSameLine(Point vector, Point vector2) {
        return vector2.y * vector.x - vector2.x * vector.y == 0;
    }

}
