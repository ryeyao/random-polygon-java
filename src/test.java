import javax.swing.*;
import java.awt.*;
import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * Created with IntelliJ IDEA.
 * User: Rye
 * Date: 3/6/14
 * Time: 11:14 AM
 */
public class test extends JComponent{
    public static Polygon p = new Polygon();
    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillPolygon(p);
        g.drawPolygon(p);
    }

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame();
        frame.setTitle("Random Polygon");
//        frame.setLayout(new BorderLayout());
        frame.setSize(500, 500);
        frame.setBackground(Color.black);
        frame.setLocation(500, 150);

        frame.getContentPane().add(new test());
        frame.setVisible(true);
        for(int i = 0; i < 500 * 500; i++) {
            p = new Polygon();
            Random rand = new Random();
            int x = i % 500;
            int y = i / 500;
            int width;
            int height;
//            width = 5 + rand.nextInt(500);
//            height = 5 + rand.nextInt(500);
            width = 5;
            height = 5;
            for(int j = 0; j < 20; j++) {

                p.addPoint(x, y);
                p.addPoint(x + width + j, y);
                p.addPoint(x + width + j, y + height + j);
                p.addPoint(x, y + height + j);
                frame.repaint();
                sleep(10);
            }
        }
    }
}
