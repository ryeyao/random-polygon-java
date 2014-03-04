import shape.ExtendedPolygon;
import shape.ExtendedPolygonBuilder;
import shape.RectangleContainer;

import java.awt.*;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Rye
 * Date: 3/4/14
 * Time: 2:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class RandomPolygonGen {

    private RectangleContainer container = new RectangleContainer();

    public ExtendedPolygon randPolygon() {
        Random rand = new Random();
        int edgeNum = rand.nextInt() % 3;
        ExtendedPolygonBuilder pgBuilder = new ExtendedPolygonBuilder();

        return pgBuilder.buildPolygon(edgeNum);
    }
}
