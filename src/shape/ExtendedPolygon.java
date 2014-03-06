package shape;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: Rye
 * Date: 3/4/14
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExtendedPolygon extends Polygon {

    private Vector<Point> norms = null;
    private double area = 0;
    private double radius = 0; // radius of the polygons' circumcircle if it exists

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

    public boolean intersects(ExtendedPolygon polygon) {

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

    public double getArea() {
        if (area != 0) {
            return area;
        }

        double area1 = 0, area2 = 0;
        for (int i = 0; i < this.npoints - 1; i++) {
            area1 += this.xpoints[i] * this.ypoints[i + 1];
        }
        area1 += this.xpoints[this.npoints - 1] * this.ypoints[0];

        for (int i = 0; i < this.npoints - 1; i++) {
            area2 += this.ypoints[i] * this.xpoints[i + 1];
        }
        area2 += this.ypoints[this.npoints - 1] * this.xpoints[0];
        area = (area1 - area2) / 2.0;

        return Math.abs(area);
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
}
