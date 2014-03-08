package shape;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Rye
 * Date: 3/4/14
 * Time: 2:47 PM
 */
public class ExtendedPolygon extends Polygon {

    private Vector<Point> norms = null;
    private double area = 0;
    private double radius = 0; // radius of the polygons' circumcircle if it exists
    private Point circleCenter;
    private short quadrant = -1;

    public void setQuadrant(int quadrant) {
        this.quadrant = (short)quadrant;
    }

    public int getQuadrant() {
        return quadrant;
    }

    private int[] getMinMaxProjs(Point axis) {
        int minProj = this.xpoints[0] * axis.x + this.ypoints[0] * axis.y;
        int maxProj = minProj;

        for (int i = 1; i < this.npoints; i++) {
            int proj = this.xpoints[i] * axis.x + this.ypoints[i] * axis.y;

            if (minProj > proj) {
                minProj = proj;
            }
            if (maxProj < proj) {
                maxProj = proj;
            }
        }

        int[] result = new int[2];
        result[0] = minProj;
        result[1] = maxProj;

        return result;
    }

    @Override
    public void translate(int deltX, int deltY) {
        super.translate(deltX, deltY);
        this.circleCenter.translate(deltX, deltY);
    }

    public boolean intersects(ExtendedPolygon polygon) {

        // Use box first
        if(!this.intersects(polygon.getBounds2D())) {
            return false;
        }

        // Check each of this polygon's norms
        for (int i = 0; i < this.npoints; i++) {
            int[] resultPoly_1 = this.getMinMaxProjs(this.getNorms().get(i));
            int[] resultPoly_2 = polygon.getMinMaxProjs(this.getNorms().get(i));

            if (resultPoly_1[1] < resultPoly_2[0] || resultPoly_2[1] < resultPoly_1[0]) {
                // Not intersected
                return false;
            }
        }

        // Check each of other polygon's norms
        for (int i = 0; i < polygon.npoints; i++) {
            int[] resultPoly_1 = this.getMinMaxProjs(polygon.getNorms().get(i));
            int[] resultPoly_2 = polygon.getMinMaxProjs(polygon.getNorms().get(i));

            if (resultPoly_1[1] < resultPoly_2[0] || resultPoly_2[1] < resultPoly_1[0]) {
                // Not intersected
                return false;
            }
        }

        return true;
    }

    private double calculateArea() {

        double area1 = 0, area2 = 0;
        for (int i = 0; i < this.npoints - 1; i++) {
            area1 += this.xpoints[i] * this.ypoints[i + 1];
        }
        area1 += this.xpoints[this.npoints - 1] * this.ypoints[0];

        for (int i = 0; i < this.npoints - 1; i++) {
            area2 += this.ypoints[i] * this.xpoints[i + 1];
        }
        area2 += this.ypoints[this.npoints - 1] * this.xpoints[0];

        area = (area1 - area2) * 0.5;

        if(area > 0) {
            return area;
        } else {
            area = -area;
            return area;
        }
    }

    public double getArea() {
        if (area != 0) {
            return area;
        }
        area = this.calculateArea();
        return area;
    }

    public Vector<Point> getNorms() {
        if (this.norms != null) {
            return this.norms;
        }

        this.norms = new Vector<Point>();

        int i = 0;
        for (; i < this.npoints - 1; i++) {
            Point norm = new Point(this.ypoints[i] - this.ypoints[i + 1], this.xpoints[i + 1] - this.xpoints[i]);
            this.norms.add(norm);
        }
        this.norms.add(new Point(this.ypoints[i] - this.ypoints[0], this.xpoints[0] - this.xpoints[i]));

        return this.norms;
    }

    public double getRadius() {
        return this.radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setCircleCenter(Point center) {
        this.circleCenter = center;
    }

    public Point getCircleCenter() {
        return this.circleCenter;
    }

    /**
     * @author sunlike
     * 获取多边形的顶点坐标
     * @return 多边形的顶点坐标
     */
    public Vector<Point> getVertexPoints()
    {
        Vector<Point> points = new Vector<Point>();

        for(int i = 0 ; i < this.npoints; ++i)
        {
            points.add(new Point(this.xpoints[i],this.ypoints[i]));
        }
        return points;
    }
}
