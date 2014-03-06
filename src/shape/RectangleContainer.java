package shape;

import java.awt.*;
import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: Rye
 * Date: 3/4/14
 * Time: 2:45 PM
 */

public class RectangleContainer extends Rectangle{

    private ArrayList<ExtendedPolygon> polygonsInside = new ArrayList<ExtendedPolygon>();
    private double blankArea;
    private double area;

    public RectangleContainer(int width, int length) {
        super(width, length);
        this.blankArea = width * length;
        this.area = width * length;
    }

    public boolean contains(ExtendedPolygon polygon) {
        for (int i = 0; i < polygon.npoints; i++) {
            if (!this.contains(polygon.xpoints[i], polygon.ypoints[i])) {
                 return false;
            }
        }
        return true;
    }

    public boolean safePut(ExtendedPolygon polygon) {
        if (!this.contains(polygon)) {
            return false;
        }

        for (ExtendedPolygon pg : polygonsInside) {
            if (polygon.intersects(pg)) {
                return false;
            }
        }

        this.polygonsInside.add(polygon);
        this.blankArea -= polygon.getArea();
        double a = polygon.getArea();
        System.out.format("%d: %d-edges %.2f%% +%.2f pix^2\n", this.polygonsInside.size(), polygon.npoints, getCoverageRatio() * 100, polygon.getArea());
//        System.out.format("Circle:(%d, %d)\n", polygon.getCircleCenter().x, polygon.getCircleCenter().y);
//        System.out.println("Points:");
//        for(int j = 0; j < polygon.npoints; j++) {
//            System.out.format("(%d, %d)\n", polygon.xpoints[j], polygon.ypoints[j]);
//        }

        return true;
    }

    public ArrayList<ExtendedPolygon> getPolygonsInside() {
        return this.polygonsInside;
    }

    public double getBlankArea() {
        return this.blankArea;
    }

    public double getArea() {
        return this.area;
    }

    public double getCoverageRatio() {
        return 1 - (this.getBlankArea() / this.getArea());
    }
}
