package ur4n0.enemy;

import java.awt.geom.Point2D;
import java.util.ArrayList; // for collection of waves

public class EnemyWave {
public Point2D.Double fireLocation;
public long fireTime;
public double bulletVelocity, directAngle, distanceTraveled;
public int direction;
public ArrayList safePoints;

public EnemyWave() {
}

}
