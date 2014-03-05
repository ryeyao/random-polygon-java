import javafx.scene.control.RadioMenuItem;
import shape.ExtendedPolygon;
import shape.ExtendedPolygonBuilder;
import shape.RectangleContainer;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Rye
 * Date: 3/4/14
 * Time: 2:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class RandomPolygonGen extends JComponent {

    public static int CONTAINER_WIDTH = 500;
    public static int CONTAINER_HEIGHT = 500;

    public static RectangleContainer container = new RectangleContainer(CONTAINER_WIDTH, CONTAINER_HEIGHT);

    public static ExtendedPolygon randPolygon(int maxEdgeNum, int minRadius, int maxRadius) {
        Random rand = new Random();
        int edgeNum = 3 + rand.nextInt() % (maxEdgeNum - 2);

        ExtendedPolygonBuilder pgBuilder = new ExtendedPolygonBuilder(container);

        return pgBuilder.buildPolygon(maxEdgeNum, minRadius, maxRadius);
    }

    @Override
    public void paint(Graphics g) {

        for (Polygon p : container.getPolygonsInside()) {
            g.drawPolygon(p);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("Random Polygon");
        frame.setLayout(new BorderLayout());
        frame.setSize(CONTAINER_WIDTH, CONTAINER_HEIGHT);
        frame.setBackground(Color.black);

        int count = 0;
        int maxEdgeNum = 5;
        int minRadius = 50;
        int maxRadius = 100;
        double minCoverageRatio = 0.9;

        long beginTime = System.currentTimeMillis();
        while(true) {
            boolean result = false;
            result = container.safePut(RandomPolygonGen.randPolygon(maxEdgeNum, minRadius, maxRadius));
            if (!result) {
                System.out.println("Put again " + count++);
                continue;
            }
            if ((double)container.getBlankArea() / (double)container.getArea() < 1.0 - minCoverageRatio ) {
                break;
            }
        }
        long timeUsedMillis = System.currentTimeMillis() - beginTime;
        System.out.format("%ds used\n", timeUsedMillis/1000);
        System.out.format("Coverage Ratio: %.2f%%\n", (1 - (double)container.getBlankArea() / (double)container.getArea()) * 100);

        frame.getContentPane().add(new RandomPolygonGen());
        frame.setVisible(true);
    }
}
