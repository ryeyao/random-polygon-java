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

        return true;
    }
}
