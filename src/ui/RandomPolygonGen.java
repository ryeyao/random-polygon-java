package ui;

import shape.ExtendedPolygon;
import shape.ExtendedPolygonBuilder;
import shape.RectangleContainer;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * Created with IntelliJ IDEA.
 * User: Rye
 * Date: 3/4/14
 * Time: 2:16 PM
 */
public class RandomPolygonGen extends JComponent implements Runnable{

    public int containerWidth;
    public int containerHeight;

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

    public Object done = new Object();
    private long beginTime = 0;

    private boolean interrupted = false;

    public RandomPolygonGen() {
        super();
        this.init();
    }

    private void init() {
        maxEdgeNum = 5;
        minRadius = 30;
        maxRadius = 80;
        stepX = -1;
        stepY = 1;
        minCoverageRatio = 0.50;
        expandTry = 50;
        expandStep = 1;
        successCount = 0;
        failureCount = 0;
        containerWidth = 500;
        containerHeight = 500;
        iterCount = containerWidth * containerHeight;
        container = new RectangleContainer(0, 0, containerWidth, containerHeight);

    }

    private void cleanup() {
        System.out.println("Exiting...");
        container = new RectangleContainer(0, 0, containerWidth, containerHeight);
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
        g.drawRect(container.x, container.y, container.width, container.height);

        long timeUsed = 0;
        if(beginTime != 0) {
            timeUsed = System.currentTimeMillis() - beginTime;
        }
        String ratioText = String.format("Ratio: %.2f%%", container.getCoverageRatio() * 100, timeUsed);
        String timeText = String.format("Time: %dms", timeUsed);
        g.drawString(ratioText, container.width + 2, 15);
        g.drawString(timeText, container.width + 2, 30);
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
                cleanup();
                return;
            }

            ExtendedPolygon polygon = null;
            boolean success;
            for(int j = 0; j < expandTry * expandStep; j += expandStep) {
//                System.out.println("Try " + j + " times");
                if(interrupted) {
                    cleanup();
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
        beginTime = System.currentTimeMillis();

        while(true) {
            if(interrupted) {
                cleanup();
                break;
            }
            this.repaint();
            boolean result;
            ExtendedPolygon polygon = RandomPolygonGen.randPolygon(container, maxEdgeNum, minRadius, maxRadius);
            result = container.safePut(polygon);
            if(!result) {
                for(int i = 0; i < 50; i++) {
                    if(interrupted) {
                        cleanup();
                        break;
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
                        cleanup();
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        if(!interrupted) {
            synchronized (done) {
                done.notify();
            }
        }
        long timeUsedMillis = System.currentTimeMillis() - beginTime;
        System.out.format("%ds used\n", timeUsedMillis/1000);
        System.out.format("Coverage Ratio: %.2f%%\n", container.getCoverageRatio() * 100);
    }

    /**
     * @author sunlike
     * @param p
     * @return
     */
    private String getXmlContent(ExtendedPolygon p)
    {
        String strXmlContent = "  <Polygon>\n";
        strXmlContent+="    <Edges>"+p.npoints+"</Edges>\n";
        strXmlContent+="    <Points>\n";
        for(Point pt : p.getVertexPoints())
        {
            strXmlContent+="      <Point>\n";
            strXmlContent+="        <X>"+pt.getX()+"</X>\n";
            strXmlContent+="        <Y>"+pt.getY()+"</Y>\n";
            strXmlContent+="      </Point>\n";
        }

        strXmlContent+="    </Points>\n";
        strXmlContent+= "   </Polygon>\n";
        return strXmlContent;
    }


    /**
     * @author sunlike
     */
    public  void saveToXmlFormatFile()
    {
        ArrayList<ExtendedPolygon>[] listOfPolygon = container.getPolygonsInside();
        ArrayList<String> xmlContentArray = new ArrayList<String>();
        String strXmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        String strBeginXmlContent = "<TPolygons>\n";

        String strListBegin = " <Polygons>";
        xmlContentArray.add(strXmlHeader);
        xmlContentArray.add(strBeginXmlContent);
        xmlContentArray.add(strListBegin);

        // 添加容器到输出列表
        ExtendedPolygon outBounder = new ExtendedPolygon();
        outBounder.addPoint(container.x,container.y);
        outBounder.addPoint(container.x,container.height+container.y);
        outBounder.addPoint(container.width+container.x,container.height+container.y);
        outBounder.addPoint(container.width+container.x,container.y);

        String strXmlContent = getXmlContent(outBounder);
        xmlContentArray.add(strXmlContent);

        for(ArrayList<ExtendedPolygon>  l : listOfPolygon)
        {
            for(ExtendedPolygon p : l)
            {
                strXmlContent = getXmlContent(p);
                xmlContentArray.add(strXmlContent);
            }

        }
        String strListEnd = " </Polygons>\n";
        String strEndXmlContent = "</TPolygons>";
        xmlContentArray.add(strListEnd);
        xmlContentArray.add(strEndXmlContent);

        try
        {

            FileWriter fw  = new FileWriter("polygon.xml");
            for(String str : xmlContentArray)
            {
                System.out.print(str);
                fw.write(str);
            }
            fw.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();

        }
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

    public int getContainerWidth() {
        return containerWidth;
    }

    public void setContainerWidth(int containerWidth) {
        this.containerWidth = containerWidth;
    }

    public int getContainerHeight() {
        return containerHeight;
    }

    public void setContainerHeight(int containerHeight) {
        this.containerHeight = containerHeight;
    }

    public int getIterCount() {
        return iterCount;
    }

    public void setIterCount(int iterCount) {
        this.iterCount = iterCount;
    }

    public Object getDone() {
        return done;
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    public void setInterrupted(boolean interrupted) {
        this.interrupted = interrupted;
    }


}
