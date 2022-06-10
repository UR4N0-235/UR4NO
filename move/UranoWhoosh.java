package ur4n0.move;

import ur4n0.UR4NO;
import ur4n0.enemy.EnemyWave;
import ur4n0.services.UranoFormulas;

import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;
// import robocode.AdvancedRobot;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList; // para colecionar as ondas

/**
 * UranoWhoosh - a class for URANO robocode
 */
public class UranoWhoosh {

public static int BINS = 47;
public static double _surfStats[] = new double[BINS];
public Point2D.Double _myLocation;
public Point2D.Double _enemyLocation;

public Point2D.Double _lastGoToPoint;
public double direction = 1;

public ArrayList _enemyWaves;
public ArrayList _surfDirections;
public ArrayList _surfAbsBearings;

public static double _oppEnergy = 100.0;

public static double WALL_STICK = 160;
public static Rectangle2D.Double _fieldRect
        = new java.awt.geom.Rectangle2D.Double(18, 18, 764, 564);

public UR4NO _robot;

public UranoWhoosh(UR4NO robot){
        this._robot = robot;
        _enemyWaves = new ArrayList();
        _surfDirections = new ArrayList();
        _surfAbsBearings = new ArrayList();

}

// metodo principal do movimento do ur4n0, todo o movimento vem depois da execucao do onScannedRobot
public void onScannedRobot(ScannedRobotEvent e) {
        _myLocation = new Point2D.Double(_robot.getX(), _robot.getY());

        double lateralVelocity = _robot.getVelocity()*Math.sin(e.getBearingRadians());
        double absBearing = e.getBearingRadians() + _robot.getHeadingRadians();

        // setTurnRadarRightRadians(Utils.normalRelativeAngle(absBearing - getRadarHeadingRadians()) * 2);

// uso de if ternario
// new Integer e new Double caiiiu, entao
// *.valueOf usado para converter valores e evitar chatisse do java
        _surfDirections.add(0, Integer.valueOf( (lateralVelocity >= 0) ? 1 : -1) );
        _surfAbsBearings.add(0, Double.valueOf(absBearing + Math.PI));


        double bulletPower = _oppEnergy - e.getEnergy();
        if (bulletPower < 3.01 && bulletPower > 0.09
            && _surfDirections.size() > 2) {
                EnemyWave ew = new EnemyWave();
                ew.fireTime = _robot.getTime() - 1;
                ew.bulletVelocity = UranoFormulas.bulletVelocity(bulletPower);
                ew.distanceTraveled = UranoFormulas.bulletVelocity(bulletPower);
                ew.direction = ((Integer)_surfDirections.get(2)).intValue();
                ew.directAngle = ((Double)_surfAbsBearings.get(2)).doubleValue();
                ew.fireLocation = (Point2D.Double)_enemyLocation.clone(); // last tick

                _enemyWaves.add(ew);
        }

        _oppEnergy = e.getEnergy();

        // atualiza as ondas depois da deteccao do inimigo, para que as ondas ultilizem o valor mais recente do inimigo
        _enemyLocation = UranoFormulas.calcEnemyLoc(_myLocation, absBearing, e.getDistance());

        updateWaves();
        doSurfing();
}

public void updateWaves() {
        for (int i = 0; i < _enemyWaves.size(); i++) {
                EnemyWave ew = (EnemyWave)_enemyWaves.get(i);

                ew.distanceTraveled = (_robot.getTime() - ew.fireTime) * ew.bulletVelocity;

                if (ew.distanceTraveled > _myLocation.distance(ew.fireLocation) + 50) {
                        _enemyWaves.remove(i);
                        i--;
                }
        }
}

public EnemyWave getClosestSurfableWave() {
        double closestDistance = 50000;
        EnemyWave surfWave = null;

        for (int x = 0; x < _enemyWaves.size(); x++) {
                EnemyWave ew = (EnemyWave)_enemyWaves.get(x);
                double distance = _myLocation.distance(ew.fireLocation) - ew.distanceTraveled;

                if (distance > ew.bulletVelocity && distance < closestDistance) {
                        surfWave = ew;
                        closestDistance = distance;
                }
        }

        return surfWave;
}

public static int getFactorIndex(EnemyWave ew, Point2D.Double targetLocation) {
        double offsetAngle = (UranoFormulas.absoluteBearing(ew.fireLocation, targetLocation) - ew.directAngle);
        double factor = Utils.normalRelativeAngle(offsetAngle) / UranoFormulas.maxEscapeAngle(ew.bulletVelocity) * ew.direction;

        return (int)UranoFormulas.limit(0, (factor * ((BINS - 1) / 2)) + ((BINS - 1) / 2), BINS - 1);
}

// se o robo for atingido por uma onda, ele atualiza os valores de perigo para determinado movimento
public void logHit(EnemyWave ew, Point2D.Double targetLocation) {
        int index = getFactorIndex(ew, targetLocation);

        for (int x = 0; x < BINS; x++) {
                _surfStats[x] += 1.0 / (Math.pow(index - x, 2) + 1);
        }
}

// chamada do evento onHitByBullet para verificar por qual onda que o robo foi atingido
public void onHitByBullet(HitByBulletEvent e) {
        if (!_enemyWaves.isEmpty()) { // primeiro verifica se existe pelomenos uma onda
                Point2D.Double hitBulletLocation = new Point2D.Double(e.getBullet().getX(), e.getBullet().getY());
                EnemyWave hitWave = null;

                // loop para descobrir qual onda que antingiu o robo
                for (int x = 0; x < _enemyWaves.size(); x++) {
                        EnemyWave ew = (EnemyWave)_enemyWaves.get(x);

                        // opa, encontrou a onda
                        if (Math.abs(ew.distanceTraveled - _myLocation.distance(ew.fireLocation)) < 50
                            && Math.abs(
                                    UranoFormulas.bulletVelocity(e.getBullet().getPower()) - ew.bulletVelocity
                                    ) < 0.001
                            ) { // as condicoes do if termina aq
                                hitWave = ew;
                                break;
                        }
                }

                // e possivel que nao seja possivel ver qual onda que atingiu o robo
                // entao verifica se a hitWave e valido antes de enviar a onda para o
                // logger
                if (hitWave != null) {
                        // roda o logHit para atualizar os perigos de determinado ponto
                        logHit(hitWave, hitBulletLocation);

                        // uma vez que a bala atingiu, o desvio da onda falhou, entao obviamente
                        // nao faz sentido manter aquela determinada onda na memoria!
                        _enemyWaves.remove(_enemyWaves.lastIndexOf(hitWave));
                }
        }
}

// funcao usada para calcular para onde ir para escapar da onda atual

// CREDIT: mini sized predictor from Apollon, by rozu
// http://robowiki.net?Apollon
public ArrayList predictPositions(EnemyWave surfWave, int direction) {
        Point2D.Double predictedPosition = (Point2D.Double)_myLocation.clone();
        double predictedVelocity = _robot.getVelocity();
        double predictedHeading = _robot.getHeadingRadians();
        double maxTurning, moveAngle, moveDir;
        ArrayList traveledPoints = new ArrayList();

        int counter = 0; // number of ticks in the future
        boolean intercepted = false;

        do {
                double distance = predictedPosition.distance(surfWave.fireLocation);
                double offset = Math.PI/2 - 1 + distance/400;

                moveAngle =
                        wallSmoothing(predictedPosition, UranoFormulas.absoluteBearing(surfWave.fireLocation,
                                                                                       predictedPosition) + (direction * (offset)), direction)
                        - predictedHeading;
                moveDir = 1;

                if(Math.cos(moveAngle) < 0) {
                        moveAngle += Math.PI;
                        moveDir = -1;
                }

                moveAngle = Utils.normalRelativeAngle(moveAngle);

                // maxTurning is built in like this, you can't turn more then this in one tick
                maxTurning = Math.PI/720d*(40d - 3d*Math.abs(predictedVelocity));
                predictedHeading = Utils.normalRelativeAngle(predictedHeading
                                                             + UranoFormulas.limit(-maxTurning, moveAngle, maxTurning));

                // this one is nice ;). if predictedVelocity and moveDir have
                // different signs you want to breack down
                // otherwise you want to accelerate (look at the factor "2")
                predictedVelocity += (predictedVelocity * moveDir < 0 ? 2*moveDir : moveDir);
                predictedVelocity = UranoFormulas.limit(-8, predictedVelocity, 8);

                // calculate the new predicted position
                predictedPosition = UranoFormulas.calcEnemyLoc(predictedPosition, predictedHeading, predictedVelocity);

                //add this point the our prediction
                traveledPoints.add(predictedPosition);

                counter++;

                if (predictedPosition.distance(surfWave.fireLocation) - 20 <
                    surfWave.distanceTraveled + (counter * surfWave.bulletVelocity)
                    //   + surfWave.bulletVelocity
                    ) {
                        intercepted = true;
                }
        } while(!intercepted && counter < 500);

        //we can't get the the last point, because we need to slow down
        if(traveledPoints.size() > 1)
                traveledPoints.remove(traveledPoints.size() - 1);

        return traveledPoints;
}

public double checkDanger(EnemyWave surfWave, Point2D.Double position) {
        int index = getFactorIndex(surfWave, position);
        double distance = position.distance(surfWave.fireLocation);
        return _surfStats[index]/distance;
}

public Point2D.Double getBestPoint(EnemyWave surfWave){
        if(surfWave.safePoints == null) {
                ArrayList forwardPoints = predictPositions(surfWave, 1);
                ArrayList reversePoints = predictPositions(surfWave, -1);
                int FminDangerIndex = 0;
                int RminDangerIndex = 0;
                double FminDanger = Double.POSITIVE_INFINITY;
                double RminDanger = Double.POSITIVE_INFINITY;
                for(int i = 0, k = forwardPoints.size(); i < k; i++) {
                        double thisDanger = checkDanger(surfWave, (Point2D.Double)(forwardPoints.get(i)));
                        if(thisDanger <= FminDanger) {
                                FminDangerIndex = i;
                                FminDanger = thisDanger;
                        }
                }
                for(int i = 0, k = reversePoints.size(); i < k; i++) {
                        double thisDanger = checkDanger(surfWave, (Point2D.Double)(reversePoints.get(i)));
                        if(thisDanger <= RminDanger) {
                                RminDangerIndex = i;
                                RminDanger = thisDanger;
                        }
                }
                ArrayList bestPoints;
                int minDangerIndex;

                if(FminDanger < RminDanger ) {
                        bestPoints = forwardPoints;
                        minDangerIndex = FminDangerIndex;
                }
                else {
                        bestPoints = reversePoints;
                        minDangerIndex = RminDangerIndex;
                }

                Point2D.Double bestPoint = (Point2D.Double)bestPoints.get(minDangerIndex);

                while(bestPoints.indexOf(bestPoint) != -1)
                        bestPoints.remove(bestPoints.size() - 1);
                bestPoints.add(bestPoint);

                surfWave.safePoints = bestPoints;

                //debugging - so that we should always be on top of the last point
                bestPoints.add(0,new Point2D.Double(_robot.getX(), _robot.getY()));

        }
        else
        if(surfWave.safePoints.size() > 1)
                surfWave.safePoints.remove(0);


        if(surfWave.safePoints.size() >= 1) {
                for(int i = 0,k=surfWave.safePoints.size(); i < k; i++) {
                        Point2D.Double goToPoint = (Point2D.Double)surfWave.safePoints.get(i);
                        if(goToPoint.distanceSq(_myLocation) > 20*20*1.1)
                                //if it's not 20 units away we won't reach max velocity
                                return goToPoint;
                }
                //if we don't find a point 20 units away, return the end point
                return (Point2D.Double)surfWave.safePoints.get(surfWave.safePoints.size() - 1);


        }

        return null;
}

// funcao pra fazer o movimento de "ondas"
public void doSurfing() {
        EnemyWave surfWave = getClosestSurfableWave();
        double distance = _enemyLocation.distance(_myLocation);
        if (surfWave == null || distance < 50) {
                //do 'away' movement  best distance of 400 - modified from RaikoNano
                double absBearing = UranoFormulas.absoluteBearing(_myLocation, _enemyLocation);
                double headingRadians = _robot.getHeadingRadians();
                double stick = 160;//Math.min(160,distance);
                double v2, offset = Math.PI/2 + 1 - distance/400;

                while(!_fieldRect.
                      contains(UranoFormulas.calcEnemyLoc(_myLocation,v2 = absBearing + direction*(offset -= 0.02), stick)
                               ));


                if( offset < Math.PI/3 )
                        direction = -direction;
                _robot.setAhead(50*Math.cos(v2 - headingRadians));
                _robot.setTurnRightRadians(Math.tan(v2 - headingRadians));

        }
        else
                goTo(getBestPoint(surfWave));
}

// alem das movimentacao, o robo se move para pontos considerados mais seguros
private void goTo(Point2D.Double destination) {
        if(destination == null) {
                if(_lastGoToPoint != null)
                        destination = _lastGoToPoint;
                else
                        return;
// tentativa de diminuir codigo
//                (_lastGoToPoint != null) ? destination = _lastGoToPoint : return;
        }

        _lastGoToPoint = destination;
        Point2D.Double location = new Point2D.Double(_robot.getX(), _robot.getY());
        double distance = location.distance(destination);
        double angle = Utils.normalRelativeAngle(UranoFormulas.absoluteBearing(location, destination) - _robot.getHeadingRadians());
        if (Math.abs(angle) > Math.PI/2) {
                distance = -distance;
                if (angle > 0) {
                        angle -= Math.PI;
                }
                else {
                        angle += Math.PI;
                }
        }

        _robot.setTurnRightRadians(angle*Math.signum(Math.abs((int)distance)));

        _robot.setAhead(distance);
}

// CREDIT: Iterative WallSmoothing by Kawigi
//   - return absolute angle to move at after account for WallSmoothing
// robowiki.net?WallSmoothing
public double wallSmoothing(Point2D.Double botLocation, double angle, int orientation) {
        while (!_fieldRect.contains(UranoFormulas.calcEnemyLoc(botLocation, angle, 160))) {
                angle += orientation*0.05;
        }
        return angle;
}

// usa uns calculos para saber pra onde virar, se vai pra frente ou pra tras, etc
public void setBackAsFront(UR4NO robot, double goAngle) {
        // transforma o angulo em um valor entre "180" e "-180" graus, mas no lugar de graus usa
        // radianos, pra facilitar calculo de circulos e essas coisas
        double angle = Utils.normalRelativeAngle(goAngle - _robot.getHeadingRadians());
        if (Math.abs(angle) > (Math.PI/2)) { // primeiro verifica se o angulo e maior que um angulo de "90°" as partes de baixo sao
                // obvias, pensa ae, quem tiver lendo esse codigo
                if (angle < 0) {
                        _robot.setTurnRightRadians(Math.PI + angle);
                }
                else {
                        _robot.setTurnLeftRadians(Math.PI - angle);
                }
                _robot.setBack(100);
        }
        else {
                if (angle < 0) {
                        _robot.setTurnLeftRadians(-1*angle);
                }
                else {
                        _robot.setTurnRightRadians(angle);
                }
                _robot.setAhead(100);
        }
}

}
