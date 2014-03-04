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

    public static RectangleContainer container = new RectangleContainer(500, 500);
    public static ExtendedPolygon randPolygon() {
        Random rand = new Random();
        int edgeNum = rand.nextInt() % 3;
        ExtendedPolygonBuilder pgBuilder = new ExtendedPolygonBuilder(container);

        return pgBuilder.buildPolygon(0);
    }

    public void paint(Graphics g) {

        for (Polygon p : container.getPolygonsInside()) {
            g.drawPolygon(p);
        }
    }

    public static void main(String[] args) {
        ExtendedPolygon polygon = RandomPolygonGen.randPolygon();
        JFrame frm = new JFrame();
        frm.setTitle("Random Polygon");
        frm.setLayout(new BorderLayout());
        frm.setSize(500, 500);
        frm.setBackground(Color.black);
        int count = 0;
//        for (int i = 0; i < 1000000; i++) {
        while(true) {
            boolean result = false;
            result = container.safePut(RandomPolygonGen.randPolygon());
            if (!result) {
                System.out.println("Put again " + count++);
                continue;
            }
            if ((double)container.getBlankArea() / (double)container.getArea() < 0.3 ) {
                break;
            }
        }
        frm.getContentPane().add(new RandomPolygonGen());
        frm.setVisible(true);

//        Graphics g = frm.getGraphics();
//        Polygon p = RandomPolygonGen.randPolygon();
//        g.setColor(Color.white);
//        g.drawPolygon(p);
//        g.fillRect(100, 50, 70, 55);


//        frm.paint(g);
    }
}
