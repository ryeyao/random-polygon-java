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

    private ArrayList<ExtendedPolygon>[] polygonsInside = new ArrayList[5];
    private double blankArea;
    private double area;
    private int listSize = 0;

    public RectangleContainer(int x, int y, int width, int length) {
        super(width, length);
        super.setLocation(x, y);
        this.blankArea = width * length;
        this.area = width * length;
        for(int i = 0; i < 5; i++) {
            polygonsInside[i] = new ArrayList<ExtendedPolygon>();
        }
    }

    private int getQuadrant(ExtendedPolygon polygon) {
        return calculateQuadrant(polygon, this.x, this.y, this.width, this.height);
    }

    private int calculateQuadrant(ExtendedPolygon polygon, int x, int y, int width, int height) {
        Point center = new Point();
        center.x = x + width / 2;
        center.y = y + height / 2;

        RectangleContainer section1 = new RectangleContainer(x, y, width / 2, height / 2);
        RectangleContainer section2 = new RectangleContainer(center.x, y, width / 2, height / 2);
        RectangleContainer section3 = new RectangleContainer(x, center.y, width / 2, height / 2);
        RectangleContainer section4 = new RectangleContainer(center.x, center.y, width / 2, height / 2);

        if(section1.contains(polygon)) {
            return 1;
        } else if(section2.contains(polygon)) {
            return 2;
        } else if(section3.contains(polygon)) {
            return 3;
        } else if(section4.contains(polygon)) {
            return 4;
        } else {
            return 0;
        }
    }

    public boolean contains(ExtendedPolygon polygon) {
        for (int i = 0; i < polygon.npoints; i++) {
            if (!this.contains(polygon.xpoints[i], polygon.ypoints[i])) {
                 return false;
            }
        }
        return true;
    }

    public boolean remove(ExtendedPolygon p) {
        boolean res = this.polygonsInside[p.getQuadrant()].remove(p);
        if (res) {
            this.blankArea += p.getArea();
            this.listSize--;
        }
        return res;
    }

    public void put(ExtendedPolygon polygon) {
        this.polygonsInside[polygon.getQuadrant()].add(polygon);
        this.blankArea -= polygon.getArea();
        double a = polygon.getArea();
        for(ArrayList l : this.polygonsInside) {
            listSize += l.size();
        }
        System.out.format("%d: %d-edges %.2f%% +%.2f pix^2\n", listSize, polygon.npoints, getCoverageRatio() * 100, polygon.getArea());
    }

    public boolean safePut(ExtendedPolygon polygon) {
        if(!this.contains(polygon)) {
            return false;
        }

        for(int i = 4; i >= 0; i--) {
            if(polygonsInside[i].contains(polygon)) {
                for(ExtendedPolygon pg : polygonsInside[i]) {
                    if (polygon.intersects(pg)) {
                        return false;
                    }
                }
                polygon.setQuadrant((short)i);
                this.put(polygon);
                return true;
//                System.out.format("Circle:(%d, %d)\n", polygon.getCircleCenter().x, polygon.getCircleCenter().y);
//                System.out.println("Points:");
//                for(int j = 0; j < polygon.npoints; j++) {
//                    System.out.format("(%d, %d)\n", polygon.xpoints[j], polygon.ypoints[j]);
//                }
            } else if(i == 0) {
                break;
            }
        }

        for(ArrayList<ExtendedPolygon> l: this.polygonsInside) {
            for(ExtendedPolygon pg : l) {
                if (polygon.intersects(pg)) {
                   return false;
                }
            }
        }
        polygon.setQuadrant((short)0);
        this.put(polygon);

        return true;
    }

    public ArrayList<ExtendedPolygon>[] getPolygonsInside() {
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

    public int getListSize() {
        return listSize;
    }

}
