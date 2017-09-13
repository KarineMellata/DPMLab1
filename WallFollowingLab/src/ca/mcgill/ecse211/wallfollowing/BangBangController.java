package ca.mcgill.ecse211.wallfollowing;

import lejos.hardware.motor.*;
import java.math.*;

public class BangBangController implements UltrasonicController {

  private final int bandCenter;
  private final int bandwidth;
  private final int motorLow;
  private final int motorHigh;
  private int distance;

  public BangBangController(int bandCenter, int bandwidth, int motorLow, int motorHigh) {
    // Default Constructor
    this.bandCenter = bandCenter;
    this.bandwidth = bandwidth;
    this.motorLow = motorLow;
    this.motorHigh = motorHigh;
    WallFollowingLab.leftMotor.setSpeed(motorHigh); // Start robot moving forward
    WallFollowingLab.rightMotor.setSpeed(motorHigh);
    WallFollowingLab.leftMotor.forward();
    WallFollowingLab.rightMotor.forward();
  }

  @Override
  public void processUSData(int distance) {
    this.distance = distance;
    // TODO: process a movement based on the us distance passed in (BANG-BANG style)
    int distError = bandCenter - distance; //Error = reference control value - measured distance from the wall
//    int DEADBAND = 2; //Error threshold
//    int FWDSPEED = 200; //Default rotational speed of wheels
//    int DELTASPD = 100; // Bang-bang constant
//    int WALLDIST = 30; 
    if(Math.abs(distError) <= bandwidth) { //Within tolerance - Keep going.
    		WallFollowingLab.leftMotor.setSpeed(motorHigh); //0 bias
    		WallFollowingLab.rightMotor.setSpeed(motorHigh);
    		WallFollowingLab.leftMotor.forward();
    		WallFollowingLab.rightMotor.forward();
    }
    else if(distError > 0) { //Too close to the wall - Move away.
		WallFollowingLab.leftMotor.setSpeed(motorHigh); 
		WallFollowingLab.rightMotor.setSpeed(motorLow);
		WallFollowingLab.leftMotor.forward();
		WallFollowingLab.rightMotor.forward();
    }
    else if(distError < 0) { //Too far from the wall - Move closer.
		WallFollowingLab.leftMotor.setSpeed(motorLow);
		WallFollowingLab.rightMotor.setSpeed(motorHigh);
		WallFollowingLab.leftMotor.forward();
		WallFollowingLab.rightMotor.forward();
    }
    
  }

  @Override
  public int readUSDistance() {
    return this.distance;
  }
}
