package ur4n0.graph;

import ur4n0.UR4NO;

import java.awt.Color;

import robocode.AdvancedRobot;
/**
 * UranoColor - a class for URANO robocode
 */
public class UranoSkins extends AdvancedRobot {

private double _randColor = Math.random() * 100;   // numero double entre 0.0 e 1.0
private UR4NO _robot;

public UranoSkins(UR4NO robot) {
        _robot = robot;
}

public void choiceColor(){


        if(_randColor > 50) {
                if(_randColor < 75) {
                        setSagittariusA();

                }else{
                        setDarkRed();
                }

        }else{
                if(_randColor < 25) {
                        setToxic();
                }else{
                        setRedGuard();
                }
        }
}

public void setRedGuard(){
        Color bloodRed = new Color(120, 0, 0);
        Color gold = new Color(240, 235, 170);
        _robot.setColors(bloodRed, gold, bloodRed);
}

public void setToxic(){
        Color greenUranColor = new Color(255, 255, 170);
        _robot.setColors(Color.black, Color.black, greenUranColor);
}

public void setDarkRed(){
        Color head = new Color(0,0,0);
        Color gun = new Color(168, 50, 54);
        Color body = new Color(157, 228, 237);
        _robot.setColors(head, gun, body);
}

public void setSagittariusA(){
        Color head = new Color(0,0,0);
        Color gun = new Color(0,0,0);
        Color body = new Color(120, 0, 0);
        _robot.setColors(body, gun, head);
}

}
