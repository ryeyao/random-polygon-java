package shape;

import java.awt.*;
import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: Rye
 * Date: 3/4/14
 * Time: 2:45 PM
 * To change this template use File | Settings | File Templates.
 */

public class RectangleContainer extends Rectangle{

    private ArrayList<ExtendedPolygon> polygonsInside = new ArrayList<ExtendedPolygon>();
    private int blankArea;
    private int area;

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

        return true;
    }

    public ArrayList<ExtendedPolygon> getPolygonsInside() {
        return this.polygonsInside;
    }

    public int getBlankArea() {
        return this.blankArea;
    }

    public int getArea() {
        return this.area;
    }
}
