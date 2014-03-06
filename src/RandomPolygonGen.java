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
 */
public class RandomPolygonGen extends JComponent {

    public static int CONTAINER_WIDTH = 500;
    public static int CONTAINER_HEIGHT = 500;

    public static RectangleContainer container = new RectangleContainer(CONTAINER_WIDTH, CONTAINER_HEIGHT);

    public static ExtendedPolygon randPolygon(int maxEdgeNum, int minRadius, int maxRadius) {
        Random rand = new Random(Double.doubleToLongBits(Math.random()));
        int edgeNum = 3 + rand.nextInt(maxEdgeNum - 2);

        ExtendedPolygonBuilder pgBuilder = new ExtendedPolygonBuilder(container);

        return pgBuilder.buildPolygon(edgeNum, minRadius, maxRadius);
    }

    @Override
    public void paint(Graphics g) {

        for (Polygon p : container.getPolygonsInside()) {
//            g.setColor(Color.BLUE);
//            g.fillPolygon(p);
            g.drawPolygon(p);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("Random Polygon");
//        frame.setLayout(new BorderLayout());
        frame.setSize(CONTAINER_WIDTH, CONTAINER_HEIGHT);
        frame.setBackground(Color.black);
        frame.setLocation(500, 150);

        int count = 0;
        int maxEdgeNum = 5;
        int minRadius = 10;
        int maxRadius = 50;
        double minCoverageRatio = 0.75;

        long beginTime = System.currentTimeMillis();
        while(true) {
            boolean result = false;
            result = container.safePut(RandomPolygonGen.randPolygon(maxEdgeNum, minRadius, maxRadius));
            if (!result) {
//                System.out.println("Put again " + count++);
                continue;
            }
            if (container.getCoverageRatio() > minCoverageRatio ) {
                break;
            }
        }
        long timeUsedMillis = System.currentTimeMillis() - beginTime;
        System.out.format("%ds used\n", timeUsedMillis/1000);
        System.out.format("Coverage Ratio: %.2f%%\n", container.getCoverageRatio() * 100);

        frame.getContentPane().add(new RandomPolygonGen());
        frame.setVisible(true);
    }
}
