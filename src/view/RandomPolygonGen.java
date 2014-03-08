package view;

import shape.ExtendedPolygon;
import shape.ExtendedPolygonBuilder;
import shape.RectangleContainer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;

/**
 * Created with IntelliJ IDEA.
 * User: Rye
 * Date: 3/4/14
 * Time: 2:16 PM
 */
public class RandomPolygonGen extends JComponent implements Runnable{

    public static int CONTAINER_WIDTH = 500;
    public static int CONTAINER_HEIGHT = 500;

    private RectangleContainer container;
    private int maxEdgeNum;
    private int minRadius;
    private int maxRadius;
    private int stepX;
    private int stepY;
    private double minCoverageRatio;
    private int iterCount;
    private int expandTry;
    private int expandStep;
    private int successCount;
    private int failureCount;

    private boolean interrupted = false;

    public RandomPolygonGen() {
        super();
        this.init();
    }

    private void init() {
        container = new RectangleContainer(0, 0, CONTAINER_WIDTH, CONTAINER_HEIGHT);
        maxEdgeNum = 5;
        minRadius = 30;
        maxRadius = 80;
        stepX = -1;
        stepY = 1;
        minCoverageRatio = 0.50;
        iterCount = container.width * container.height;
        expandTry = 50;
        expandStep = 1;
        successCount = 0;
        failureCount = 0;

    }

    private void cleanup() {
        container = null;
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

    private void awesomelyFillTheRest() throws InterruptedException {
        // TODO(Rye): 1.   Randomly run points, pick those that are not in any of the polygons in container
        //              2.    For each point, change it into random boxes with a small unit bound.
        //              2.1.  Then increase there bounds by a small random step independently,
        //              3.    Go through the small boxes in 2.1, throw out those intersect with exist polygons
        //              3.1   For each of the rest boxes, randomly run polygons within
        //              3.2   Put the generated polygons into container
        //              4     Repeat for reasonable times

        System.out.println("Awesomely fill the rest.");

        Random rand = new Random(Double.doubleToLongBits(Math.random()));
        for(int i = 0; i < iterCount; i++) {
            Rectangle box = new Rectangle();
//            box.x = i % container.width;
//            box.y = i / container.height;
            box.x = rand.nextInt(container.width - 1) + 1;
            box.y = rand.nextInt(container.height - 1) + 1;
            box.width = 30;
            box.height = 30;
            if(interrupted) {
                return;
            }

            ExtendedPolygon polygon = null;
            boolean success;
            for(int j = 0; j < expandTry * expandStep; j += expandStep) {
//                System.out.println("Try " + j + " times");
                if(interrupted) {
                    return;
                }
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

                this.repaint();

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

    @Override
    public void run(){

//        this.setVisible(true);

        interrupted = false;
        long beginTime = System.currentTimeMillis();

        while(true) {
            if(interrupted) {
                return;
            }
            this.repaint();
            boolean result;
            ExtendedPolygon polygon = RandomPolygonGen.randPolygon(container, maxEdgeNum, minRadius, maxRadius);
            result = container.safePut(polygon);
            if(!result) {
                for(int i = 0; i < 50; i++) {
                    if(interrupted) {
                        return;
                    }
                    this.repaint();
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
                try {
                    awesomelyFillTheRest();
                    if(interrupted) {
                        return;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        long timeUsedMillis = System.currentTimeMillis() - beginTime;
        System.out.format("%ds used\n", timeUsedMillis/1000);
        System.out.format("Coverage Ratio: %.2f%%\n", container.getCoverageRatio() * 100);
    }

    public double getMinCoverageRatio() {
        return minCoverageRatio;
    }

    public void setMinCoverageRatio(double minCoverageRatio) {
        this.minCoverageRatio = minCoverageRatio;
    }

    public int getStepY() {
        return stepY;
    }

    public void setStepY(int stepY) {
        this.stepY = stepY;
    }

    public int getStepX() {
        return stepX;
    }

    public void setStepX(int stepX) {
        this.stepX = stepX;
    }

    public int getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }

    public int getMinRadius() {
        return minRadius;
    }

    public void setMinRadius(int minRadius) {
        this.minRadius = minRadius;
    }

    public int getMaxEdgeNum() {
        return maxEdgeNum;
    }

    public void setMaxEdgeNum(int maxEdgeNum) {
        this.maxEdgeNum = maxEdgeNum;
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    public void setInterrupted(boolean interrupted) {
        this.interrupted = interrupted;
    }


}
