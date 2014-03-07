import shape.ExtendedPolygon;
import shape.ExtendedPolygonBuilder;
import shape.RectangleContainer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * Created with IntelliJ IDEA.
 * User: Rye
 * Date: 3/4/14
 * Time: 2:16 PM
 */
public class RandomPolygonGen extends JComponent {

    public static int CONTAINER_WIDTH = 500;
    public static int CONTAINER_HEIGHT = 500;

    public static RectangleContainer container = new RectangleContainer(0, 0, CONTAINER_WIDTH, CONTAINER_HEIGHT);
    private JFrame frame;

    public RandomPolygonGen() {
        super();
        frame = new JFrame();
        frame.setTitle("Random Polygon");
        frame.setLayout(new BorderLayout());
        frame.setSize(CONTAINER_WIDTH + 17, CONTAINER_HEIGHT + 40);
        frame.setBackground(Color.black);
        frame.setLocation(500, 150);
        frame.getContentPane().add(this);
        frame.setVisible(true);
    }

    public static ExtendedPolygon randPolygonWithinBox(Rectangle box, int maxEdgeNum) {
        Random rand = new Random(Double.doubleToLongBits(Math.random()));
        int edgeNum = 3 + rand.nextInt(maxEdgeNum - 2);

        ExtendedPolygonBuilder pgBuilder = new ExtendedPolygonBuilder();

        return pgBuilder.buildPolygon(box, edgeNum);
    }

    public static ExtendedPolygon randPolygon(RectangleContainer box, int maxEdgeNum, int minRadius, int maxRadius) {
        Random rand = new Random(Double.doubleToLongBits(Math.random()));
        int edgeNum = 3 + rand.nextInt(maxEdgeNum - 2);

        ExtendedPolygonBuilder pgBuilder = new ExtendedPolygonBuilder(box);

        return pgBuilder.buildPolygon(edgeNum, minRadius, maxRadius);
    }

    @Override
    public void paint(Graphics g) {

        for(int i = 0; i < container.getPolygonsInside().length; i++) {
            ArrayList<ExtendedPolygon> l = container.getPolygonsInside()[i];
            for(int j = 0; j < l.size(); j++) {
                Polygon p = l.get(j);
                g.setColor(Color.BLUE);
                //            g.fillPolygon(p);
                g.drawPolygon(p);
            }
        }
        g.drawRect(container.x, container.y, CONTAINER_WIDTH, CONTAINER_HEIGHT);
    }

    public void awesomelyFillTheRest() throws InterruptedException {
        // TODO(Rye): 1.   Randomly generate points, pick those that are not in any of the polygons in container
        //              2.    For each point, change it into random boxes with a small unit bound.
        //              2.1.  Then increase there bounds by a small random step independently,
        //              3.    Go through the small boxes in 2.1, throw out those intersect with exist polygons
        //              3.1   For each of the rest boxes, randomly generate polygons within
        //              3.2   Put the generated polygons into container
        //              4     Repeat for reasonable times

        System.out.println("Awesomely fill the rest.");
        int iterCount = container.width * container.height;
        int expandTry = 50;
        int expandStep = 1;
        int successCount = 0;
        int failureCount = 0;

        Random rand = new Random(Double.doubleToLongBits(Math.random()));
        for(int i = 0; i < iterCount; i++) {
            Rectangle box = new Rectangle();
//            box.x = i % container.width;
//            box.y = i / container.height;
            box.x = rand.nextInt(container.width - 1) + 1;
            box.y = rand.nextInt(container.height - 1) + 1;
            box.width = 30;
            box.height = 30;

            ExtendedPolygon polygon = null;
            boolean success;
            for(int j = 0; j < expandTry * expandStep; j += expandStep) {
//                System.out.println("Try " + j + " times");
                box.width += j;
                box.height += j;

                ExtendedPolygon tmpPoly;
//                ExtendedPolygon tmpPoly = new ExtendedPolygon();
//                tmpPoly.addPoint(box.x, box.y);
//                tmpPoly.addPoint(box.x + box.width, box.y);
//                tmpPoly.addPoint(box.x + box.width, box.y + box.height);
//                tmpPoly.addPoint(box.x, box.y + box.height);
                tmpPoly = randPolygonWithinBox(box, 5);
                success = container.safePut(tmpPoly);

                frame.repaint();

                if(success) {
//                    System.out.println(j + " try ok.\n remove and retry.");
                    polygon = tmpPoly;
                    container.remove(tmpPoly);
                } else {
//                    System.out.println(j + " try fail.");
                    break;
                }
            }

            if(polygon != null) {
//                System.out.println("Success count: " + ++successCount);
                container.put(polygon);
            } else {
//                System.out.println("Failure count: " + ++failureCount);
            }
        }
    }

    public static void fillTheRest() {
        System.out.println("Fill the rest.");
        int boxEdgeLen = 4;
        int boxNum = (int)container.getArea() / (boxEdgeLen * boxEdgeLen);
        for(int i = 0; i < boxNum; i++) {
            int rectX = i % (container.width / boxEdgeLen) * boxEdgeLen;
            int rectY = (i / (container.width / boxEdgeLen)) * boxEdgeLen;
            RectangleContainer rect = new RectangleContainer(rectX, rectY, boxEdgeLen, boxEdgeLen);
            ExtendedPolygon poly = randPolygonWithinBox(rect, 5);
            boolean intersected = container.safePut(poly);
        }
    }

    public void run() throws InterruptedException {

        int count = 0;
        int maxEdgeNum = 5;
        int minRadius = 30;
        int maxRadius = 80;
        int stepX = -1;
        int stepY = 1;
        double minCoverageRatio = 0.60;
        boolean[] notOrgnized = {true, true, true, true, true, true, true, true};

        long beginTime = System.currentTimeMillis();
//        while(true) {
//            awesomelyFillTheRest();
//        }

        while(true) {
            frame.repaint();
            boolean result;
            ExtendedPolygon polygon = RandomPolygonGen.randPolygon(container, maxEdgeNum, minRadius, maxRadius);
            result = container.safePut(polygon);
            if(!result) {
                for(int i = 0; i < 50; i++) {
                    frame.repaint();
                    Random r = new Random(Double.doubleToLongBits(Math.random()));
                    int deltX = stepX + r.nextInt(3);
//                    int deltY = stepY * r.nextInt(2);
                    int deltY = stepY;
                    polygon.translate(deltX, deltY);
                    result = container.safePut(polygon);
                    if(result == true) {
                        break;
                    }
                }
            }
            if(container.getCoverageRatio() > minCoverageRatio ) {
//                fillTheRest();
                awesomelyFillTheRest();
                break;
            }
        }
        long timeUsedMillis = System.currentTimeMillis() - beginTime;
        System.out.format("%ds used\n", timeUsedMillis/1000);
        System.out.format("Coverage Ratio: %.2f%%\n", container.getCoverageRatio() * 100);

    }

    public static void main(String[] args) throws InterruptedException {
        RandomPolygonGen rpg = new RandomPolygonGen();
        rpg.run();
    }
}
