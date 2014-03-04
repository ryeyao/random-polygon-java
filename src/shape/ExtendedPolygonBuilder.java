package shape;

/**
 * Created with IntelliJ IDEA.
 * User: Rye
 * Date: 3/4/14
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExtendedPolygonBuilder {

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
        ExtendedPolygon triangle = new ExtendedPolygon();
        triangle.npoints = 3;

        return triangle;
    }

    private ExtendedPolygon randQuadrangle() {
        ExtendedPolygon quadrangle = new ExtendedPolygon();
        quadrangle.npoints = 4;

        return quadrangle;
    }

    private ExtendedPolygon randPentangon() {
        ExtendedPolygon pentangon = new ExtendedPolygon();
        pentangon.npoints = 5;

        return pentangon;
    }

}
