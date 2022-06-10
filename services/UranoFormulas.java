package ur4n0.services;

import robocode.AdvancedRobot;
import java.awt.geom.Point2D;

public class UranoFormulas {

// angulo absoluto do robo inimigo
public static double absoluteBearing(Point2D.Double source, Point2D.Double target) {
        // math.atan2 converte a cordenada resultande da subtracao de
        // cordenada atual do inimigo - local do ur4n0
        // em cordenadas polares*, com base no ponto de referencia sendo
        // o cantinho esquerdo inferior da tela
        // cordenadas polares*: https://pt.wikipedia.org/wiki/Coordenadas_polares
        return Math.atan2(target.x - source.x, target.y - source.y);
}

// usando trigonometria para calcular a cordenada do inimigo
public static Point2D.Double calcEnemyLoc(Point2D.Double sourceLocation, double angle, double length) {
        return new Point2D.Double(sourceLocation.x + Math.sin(angle) * length,
                                  sourceLocation.y + Math.cos(angle) * length);
}

//limite entre um valor, um maximo e um minimo
public static double limit(double min, double value, double max) {
        //daria para fazer essa funcao
        // com if seria if(value > max ) { return max } else if(value < min){ return min } else { value }
        // com if ternario seria ( value > max ) ? max : (value < min) ? min : value
        // mas para motivos de entendimento e facilidade, usei Math.max e Math.min
        // mentira so usei metodos da math pq curto essa classe do java
        return Math.max(min, Math.min(value, max));
}

// usando fisica do robocode para calcular velocidade da bala
public static double bulletVelocity(double power) {
        return 20.0 - (3.0*power);
}

// calcular o signo do robo inimigo
public static int sign(double velocity){
        // se velocidade > 0 entao inimigo ta indo pra frente
        // frente = 1 ; re = -1
        return (velocity > 0) ? 1 : -1;
}

// tambem usando fisica do robocode:
// angulo que o robo pode fazer para escapar de um tiro, com base na sua velocidade atual
public static double maxEscapeAngle(double velocity) {
        return Math.asin(8.0/velocity);
}

// usados pela arma aaaaaaaaaaaa

public static double sqr(double x) {
        return x * x;
}

public static double sinD(double ang) {
        return(Math.sin(Math.toRadians(ang)));
}

public static double cosD(double ang) {
        return(Math.cos(Math.toRadians(ang)));
}

public static double tanD(double ang) {
        return(Math.tan(Math.toRadians(ang)));
}

public static double angleTo(double x1, double y1, double x2, double y2) {
        return(Math.toDegrees(Math.PI/2 - Math.atan2(y2 - y1, x2 - x1)));
}

public static double angleToRadians(double x1, double y1, double x2, double y2) {
        return(Math.PI/2 - Math.atan2(y2 - y1, x2 - x1));
}

public static double angleTo(AdvancedRobot bot, double x, double y) {
        return(Math.toDegrees(Math.PI/2 - Math.atan2(y - bot.getY(), x - bot.getX())));
}

public static double angleToRadians(AdvancedRobot bot, double x, double y) {
        return(Math.PI/2 - Math.atan2(y - bot.getY(), x - bot.getX()));
}

public static double distanceTo(double x1, double y1, double x2, double y2) {
        return(Math.sqrt(sqr(x2 - x1) + sqr(y2 - y1)));
}

public static double distanceTo(AdvancedRobot bot, double x2, double y2) {
        return(Math.sqrt(sqr(x2 - bot.getX()) + sqr(y2 - bot.getY())));
}

public static double normalizeBearing(double ang) {
        ang = ang % 360;
        if (ang > 180) ang -= 360;
        if (ang < -180) ang += 360;
        return ang;
}

public static double normalizeBearingRadians(double ang) {
        ang = ang % Math.PI * 2;
        if (ang > Math.PI) ang -= Math.PI * 2;
        if (ang < -Math.PI) ang += Math.PI * 2;
        return ang;
}

public static void setTurnToAngle(AdvancedRobot bot, double ang) {
        bot.setTurnRight(normalizeBearing(ang - bot.getHeading()));
}

public static void setTurnToAngleRadians(AdvancedRobot bot, double ang) {
        bot.setTurnLeftRadians(normalizeBearingRadians(bot.getHeadingRadians() - ang));
}

public static double setTurnToAngle90(AdvancedRobot bot, double ang) {
        double ang1 = normalizeBearing(bot.getHeading() - ang);
        if (ang1 > 90) ang1 -= 180;
        if (ang1 < -90) ang1 += 180;
        bot.setTurnLeft(ang1);
        return (bot.getHeading() < 90 || bot.getHeading() > 270 ? 1 : -1);
}

public static void setTurnGunToAngle(AdvancedRobot bot, double ang) {
        bot.setTurnGunLeft(normalizeBearing(bot.getGunHeading() - ang));
}

public static void setTurnRadarToAngle(AdvancedRobot bot, double ang) {
        bot.setTurnRadarLeft(normalizeBearing(bot.getRadarHeading() - ang));
}

}
