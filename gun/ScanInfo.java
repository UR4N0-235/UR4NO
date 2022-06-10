package ur4n0.gun;

public class ScanInfo {
public double x = 0, y = 0, d = 0;
public long t = 0;
public double v = 0;
public double acc = 0;
public double atm = 0;
public double dtm = 0;
public double dtwf = 0;
public double dtwb = 0;
public double runTime = 0;
public double lastRunTime = 0;
public double myGunHeat = 0;
public boolean fired = false;
public ScanInfo previous;
public ScanInfo next;

public ScanInfo (double x1, double y1, double d1, double v1, long t1) {
        x = x1;
        y = y1;
        d = d1;
        v = v1;
        t = t1;
}
}
