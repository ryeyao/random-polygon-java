package shape;

import org.w3c.dom.css.Rect;
import util.VectorComparator;
import util.angle_helper;

import java.awt.*;
import java.awt.geom.Arc2D;
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

    public ExtendedPolygonBuilder() {
        super();
    }

    public ExtendedPolygonBuilder(RectangleContainer container) {
        this.container = container;
        logger.setLevel(Level.ALL);
    }

    public ExtendedPolygon buildPolygon(int edgeNum, int minRadius, int maxRadius) {
        return this.randAnyPolygon(container, edgeNum, minRadius, maxRadius);
    }

    public ExtendedPolygon buildPolygon(Rectangle box, int edgeNum) {
        return this.randAnyPolygonWithinBox(box, edgeNum);
    }

    public ExtendedPolygon buildPolygon(int edgeNum, int minRadius, int maxRadius,double minAngle,double maxAngle) {
        return this.randAnyPolygon(container, edgeNum, minRadius, maxRadius,minAngle,maxAngle);
    }

    public ExtendedPolygon buildPolygon(Rectangle box, int edgeNum,double minAngle,double maxAngle) {
        return this.randAnyPolygonWithinBox(box, edgeNum,minAngle,maxAngle);
    }

    private ExtendedPolygon randAnyPolygonWithinBox(Rectangle box, int edgeNum) {
        Random rand = new Random(Double.doubleToLongBits(Math.random()));

        Point center = new Point();
        center.x = box.x + box.width / 2;
        center.y = box.y + box.height / 2;
        int radius = box.width / 2;
        double minTriangleArea = 0.3 * Math.PI * radius * radius;

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
        logger.fine("new one\n");
        VectorComparator vComparator = new VectorComparator();
        vComparator.setReferPoint(center);
        Collections.sort(generatedPoints, vComparator);

        ExtendedPolygon polygon = new ExtendedPolygon();
        polygon.setCircleCenter(center);
        polygon.setRadius(radius);
//        logger.fine(String.println("Points:");
        for (Point p : generatedPoints) {
//            logger.fine(String.format("(%d, %d)\n", p.x, p.y);
            polygon.addPoint(p.x, p.y);
        }

        return polygon;
    }

    private ExtendedPolygon randAnyPolygon(RectangleContainer box, int edgeNum, int minRadius, int maxRadius) {
        Random rand = new Random(Double.doubleToLongBits(Math.random()));

        Point center = new Point();
        center.x = box.x + rand.nextInt(box.width);
        center.y = box.y + rand.nextInt(box.height);
        int radius = minRadius + rand.nextInt(maxRadius - minRadius + 1);
        double minTriangleArea = 0.05 * Math.PI * radius * radius;

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

        logger.fine("new one\n");
        VectorComparator vComparator = new VectorComparator();
        vComparator.setReferPoint(center);
        Collections.sort(generatedPoints, vComparator);


        ExtendedPolygon polygon = new ExtendedPolygon();
        polygon.setCircleCenter(center);
//        logger.fine(String.println("Points:");
        for (Point p : generatedPoints) {
//            logger.fine(String.format("(%d, %d)\n", p.x, p.y);
            polygon.addPoint(p.x, p.y);
        }

        return polygon;
    }

    private ExtendedPolygon randAnyPolygonWithinBox(Rectangle box, int edgeNum, double minAngle,double maxAngle) {
        Random rand = new Random(Double.doubleToLongBits(Math.random()));

        Point center = new Point();
        center.x = box.x + box.width / 2;
        center.y = box.y + box.height / 2;
        int radius = box.width / 2;
        double minTriangleArea = 0.3 * Math.PI * radius * radius;

        ArrayList<Point> generatedPoints = new ArrayList<Point>();


       do{
           ExtendedPolygon triangle = new ExtendedPolygon();
           generatedPoints.clear();
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
                } while (true);

                if (i == 2) {
                    // Limit the smallest area of triangle to 1/4 of the circle
                    if (triangle.getArea() < minTriangleArea) {
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

            logger.fine("new one\n");
            VectorComparator vComparator = new VectorComparator();
            vComparator.setReferPoint(center);
            Collections.sort(generatedPoints, vComparator);

            //whether the angle of each edge is under control
           ArrayList<Double> angles = angle_helper.getAngleFromPoints(generatedPoints);
           Boolean isAllOK = true;
           for(Double angle : angles)
           {
                if(angle < minAngle || angle > maxAngle)
                {
                    isAllOK = false;
                    break;
                }
           }

           if(isAllOK)
           {
               break;
           }

        }while(true);

        ExtendedPolygon polygon = new ExtendedPolygon();
        polygon.setCircleCenter(center);
        polygon.setRadius(radius);
//        logger.fine(String.println("Points:");
        for (Point p : generatedPoints) {
//            logger.fine(String.format("(%d, %d)\n", p.x, p.y);
            polygon.addPoint(p.x, p.y);
        }

        return polygon;
    }

    private ExtendedPolygon randAnyPolygon(RectangleContainer box, int edgeNum, int minRadius, int maxRadius,double minAngle,double maxAngle) {
        Random rand = new Random(Double.doubleToLongBits(Math.random()));

        Point center = new Point();
        center.x = box.x + rand.nextInt(box.width);
        center.y = box.y + rand.nextInt(box.height);
        int radius = minRadius + rand.nextInt(maxRadius - minRadius + 1);
        double minTriangleArea = 0.05 * Math.PI * radius * radius;

        ArrayList<Point> generatedPoints = new ArrayList<Point>();
        do {
            ExtendedPolygon triangle = new ExtendedPolygon();
            generatedPoints.clear();
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
                } while (true);

                if (i == 2) {
                    // Limit the smallest area of triangle to 1/4 of the circle
                    if (triangle.getArea() < minTriangleArea) {
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

            logger.fine("new one\n");
            VectorComparator vComparator = new VectorComparator();
            vComparator.setReferPoint(center);
            Collections.sort(generatedPoints, vComparator);

            ArrayList<Double> angles = angle_helper.getAngleFromPoints(generatedPoints);
            Boolean isAllOK = true;
            for(Double angle : angles)
            {
                if(angle < minAngle || angle > maxAngle)
                {
                    isAllOK = false;
                    break;
                }
            }
            if(isAllOK)
            {
                break;
            }
        }while (true);
        ExtendedPolygon polygon = new ExtendedPolygon();
        polygon.setCircleCenter(center);
//        logger.fine(String.println("Points:");
        for (Point p : generatedPoints) {
//            logger.fine(String.format("(%d, %d)\n", p.x, p.y);
            polygon.addPoint(p.x, p.y);
        }

        return polygon;
    }
}
