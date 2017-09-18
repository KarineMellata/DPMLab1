package ca.mcgill.ecse211.wallfollowing;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class PController implements UltrasonicController {

  /* Constants */
  private static final int motorSpeed = 200;
  private static final int motorSpeedSlow = 100;
  private static final int filterOut = 20;
  private static final int maxCorrection = 50;
  private static final int propConst = 10;
  private final int bandCenter;
  private final int bandWidth;
  private int distance;
  private int filterControl;
  
  public PController(int bandCenter, int bandwidth) {
    this.bandCenter = bandCenter;
    this.bandWidth = bandwidth;
    this.filterControl = 0;

    WallFollowingLab1.leftMotor.setSpeed(motorSpeed); // Initalize motor rolling forward
    WallFollowingLab1.rightMotor.setSpeed(motorSpeed);
    WallFollowingLab1.leftMotor.forward();
    WallFollowingLab1.rightMotor.forward();
  }
  
  public int calcProp(int diff) {
	  int correction;
	  if (diff < 0) {
		  diff = -diff;
	  }
	  correction = (int) (propConst * (double)diff);
	  if (correction >= motorSpeedSlow) {
		  correction = maxCorrection;
	  }
	  return correction;
  }

  @Override
  public void processUSData(int distance) {

    // rudimentary filter - toss out invalid samples corresponding to null
    // signal.
    // (n.b. this was not included in the Bang-bang controller, but easily
    // could have).
    //
    if (distance >= 255 && filterControl < filterOut) {
      // bad value, do not set the distance var, however do increment the
      // filter value
      filterControl++;
    } else if (distance >= 255) {
      // We have repeated large values, so there must actually be nothing
      // there: leave the distance alone
      this.distance = distance;
    } else {
      // distance went below 255: reset filter and leave
      // distance alone.
      filterControl = 0;
      this.distance = distance;
    }
    int distError = bandCenter - distance; //Error = reference control value - measured distance from the wall  
    int diff;
    if(Math.abs(distError) <= bandWidth) {
    		WallFollowingLab1.leftMotor.setSpeed(motorSpeed); //0 bias
		WallFollowingLab1.rightMotor.setSpeed(motorSpeed);
		WallFollowingLab1.leftMotor.forward();
		WallFollowingLab1.rightMotor.forward();
    }
    else if (distError > 0) {
    		diff = calcProp(distError);
    		if(distance < 18) {
        		WallFollowingLab1.leftMotor.setSpeed(motorSpeed); 
        		WallFollowingLab1.rightMotor.setSpeed(motorSpeed);
        		WallFollowingLab1.leftMotor.backward();
        		WallFollowingLab1.rightMotor.backward();
    		}
    		WallFollowingLab1.leftMotor.setSpeed(motorSpeed - diff); //0 bias
    		WallFollowingLab1.rightMotor.setSpeed(motorSpeed + diff);
    		WallFollowingLab1.leftMotor.backward();
    		WallFollowingLab1.rightMotor.forward();
    }
    
    else if (distError < 0) {
    		diff = calcProp(distError);
    		WallFollowingLab1.leftMotor.setSpeed(motorSpeed + diff); //0 bias
    		WallFollowingLab1.rightMotor.setSpeed(motorSpeed - diff);
    		WallFollowingLab1.leftMotor.forward();
    		WallFollowingLab1.rightMotor.forward();
    }
  }


  @Override
  public int readUSDistance() {
    return this.distance;
  }

}
