import javafx.scene.control.RadioMenuItem;
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

    public static ExtendedPolygon randPolygon() {
        Random rand = new Random();
        int edgeNum = rand.nextInt() % 3;
        ExtendedPolygonBuilder pgBuilder = new ExtendedPolygonBuilder();

        return pgBuilder.buildPolygon(edgeNum);
    }

    public static void main(String[] args) {
        ExtendedPolygon polygon = RandomPolygonGen.randPolygon();
    }
}
