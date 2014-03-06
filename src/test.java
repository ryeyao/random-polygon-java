import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Rye
 * Date: 3/6/14
 * Time: 11:14 AM
 */
public class test extends JComponent{
    public void paint(Graphics g) {
        Polygon p = new Polygon();
        p.addPoint(81, 49);
        p.addPoint(117, 209);
        p.addPoint(52, 242);
        p.addPoint(143, 142);
        g.drawPolygon(p);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("Random Polygon");
//        frame.setLayout(new BorderLayout());
        frame.setSize(500 + 200, 500 + 200);
        frame.setBackground(Color.black);
        frame.setLocation(500, 150);

        frame.getContentPane().add(new test());
        frame.setVisible(true);
    }
}
