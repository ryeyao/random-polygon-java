package shape;

import java.awt.*;
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

    public ExtendedPolygon buildPolygon(int edgeNum) {
        switch (edgeNum) {
            case 0:
                return this.randTriangle();
            case 1:
                return this.randQuadrangle();
            case 2:
                return this.randPentangon();
            default:
                return this.randTriangle();
        }
    }
    private ExtendedPolygon randTriangle() {
        ExtendedPolygon polygon = new ExtendedPolygon();
//        polygon.npoints = 3;
//        polygon.xpoints = new int[3];
//        polygon.ypoints = new int[3];

        Point A = new Point();
        Point B = new Point();
        Point C = new Point();

        Random rand = new Random();

        // First determine any one of the three edges by randomly generate two vertexs' 2d-coordination
        A.x = rand.nextInt(container.width - 1) + 1;
        A.y = rand.nextInt(container.height - 1) + 1;

        do {
            B.x = rand.nextInt(container.width - 1) + 1;
            B.y = rand.nextInt(container.height - 1) + 1;
            if (B.x != A.x || B.y != A.y) {
                break;
            }
        } while (true);

        // Control the type of triangle by control the angle correspond with the certain edge.
        // As any triangle has a circumcircle, the last vertex should vary on the edge of the
        // circle, therefore we can control this circle instead.

        // Assume we need acute triangle
//        double AB_mag = A.distance(B);
//        int r = rand.nextInt() % (int)Math.ceil(AB_mag) + 1;
        do {
            C.x = rand.nextInt(container.width - 1) + 1;
            C.y = rand.nextInt(container.height - 1) + 1;
            double dotProduct = (B.x - A.x) * (C.x - A.x) + (B.y - A.y) * (C.y - A.y);
            double magProduct = B.distance(A) * C.distance(A);

            if (dotProduct != -magProduct && dotProduct != magProduct) {
                break;
            }
        } while (true);

        polygon.addPoint(A.x, A.y);
        polygon.addPoint(B.x, B.y);
        polygon.addPoint(C.x, C.y);

        return polygon;
    }

    private ExtendedPolygon randQuadrangle() {
        ExtendedPolygon polygon = new ExtendedPolygon();
//        polygon.npoints = 4;
//        polygon.xpoints = new int[4];
//        polygon.ypoints = new int[4];

        return polygon;
    }

    private ExtendedPolygon randPentangon() {
        ExtendedPolygon polygon = new ExtendedPolygon();
//        polygon.npoints = 5;
//        polygon.xpoints = new int[5];
//        polygon.ypoints = new int[5];

        return polygon;
    }

}
