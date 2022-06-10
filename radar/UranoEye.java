package ur4n0.radar;

import ur4n0.UR4NO;

import robocode.ScannedRobotEvent;

import robocode.util.Utils;

//  creditos aos robos
//  {
// 		Diamond - by Voidios
// 		DrussGT - by Skilgannon
//  } :: grandes inspiracoes para essa classe

public class UranoEye {
// variaveis de ambiente
private double _enemyAbsoluteBearing;
private boolean _1v1Mode;  // ativar lock mode quando identificar 1v1
private boolean _locked = false;
// variaves de controle
private UR4NO _robot;  // importa meu robo

public UranoEye(UR4NO imUrano){
								_robot = imUrano;
}

// chamar funcao toda vez que um round inicia
public void initNewRound(){
								_1v1Mode = false;
								_locked = false;
}

public void onScannedRobot(ScannedRobotEvent e){
								_enemyAbsoluteBearing = _robot.getHeadingRadians() + e.getBearingRadians();
								_locked = true;
}

// funcao para executar chamadas ao eye
public void execute(){
								if (_robot.getOthers() == 1 && !_1v1Mode) { // se apenas um inimigo e lockmode == false
																_1v1Mode = true;
								}
								directRadar(); // direciona o radar
}

// direcionar o radar em busca de alvos
public void directRadar(){
								if(_locked) {
																_robot.setTurnRadarRightRadians(Utils.normalRelativeAngle(_enemyAbsoluteBearing - _robot.getRadarHeadingRadians()) * 2);
								}else{
																_robot.setTurnRadarRightRadians(45);
								}

}

}
