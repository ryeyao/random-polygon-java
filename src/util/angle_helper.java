package util;
import javax.swing.*;
import java.awt.*;
/**
 * Created by Sunlike on 2014/4/23.
 */
public class angle_helper {

    /**
     * calc the the angle of <BAC by using vector method
     * @param ptA
     * @param ptB
     * @param ptC
     * @return
     */
    public static double getAngle(Point ptA,Point ptB, Point ptC)
    {
        double angle = 0;
        // vector AB
        double Vab_x = ptB.x - ptA.x;
        double Vab_y = ptB.y - ptA.y;

        // vector AC
        double Vac_x = ptC.x - ptA.x;
        double Vac_y = ptC.y - ptA.y;

        double productValue = Vab_x*Vac_x + Vab_y*Vac_y;
        double Vab_value = Math.sqrt(Math.pow(Vab_x,2.0)+Math.pow(Vab_y,2.0));
        double Vac_value = Math.sqrt(Math.pow(Vac_x,2.0)+Math.pow(Vac_y,2.0));

        double cosValue  = productValue/(Vab_value*Vac_value);
        //casValue [-1,1]
        if(cosValue < -1 && cosValue > -2)
        {
            cosValue = -1;
        }
        else if(cosValue > 1 && cosValue < 2)
        {
            cosValue = 1;
        }

        angle = Math.acos(cosValue)* 180/Math.PI;
        return angle;
    }
}
