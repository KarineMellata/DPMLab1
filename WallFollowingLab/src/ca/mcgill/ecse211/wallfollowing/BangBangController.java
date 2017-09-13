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
    int DEADBAND = 2; //Standoff distance to wall
    int FWDSPEED = 200; //Error threshold
    int DELTASPD = 100; //Default rotational speed of wheels
    int WALLDIST = 30; // Bang-bang constant
    if(Math.abs(distError) <= DEADBAND) { //Within tolerance - Keep going.
    		WallFollowingLab.leftMotor.setSpeed(FWDSPEED); //0 bias
    		WallFollowingLab.rightMotor.setSpeed(FWDSPEED);
    		WallFollowingLab.leftMotor.forward();
    		WallFollowingLab.rightMotor.forward();
    }
    else if(distError > 0) { //Too close to the wall - Move away.
		WallFollowingLab.leftMotor.setSpeed(FWDSPEED); 
		WallFollowingLab.rightMotor.setSpeed(FWDSPEED - DELTASPD);
		WallFollowingLab.leftMotor.forward();
		WallFollowingLab.rightMotor.forward();
    }
    else if(distError < 0) { //Too far from the wall - Move closer.
		WallFollowingLab.leftMotor.setSpeed(FWDSPEED - DELTASPD);
		WallFollowingLab.rightMotor.setSpeed(FWDSPEED);
		WallFollowingLab.leftMotor.forward();
		WallFollowingLab.rightMotor.forward();
    }
    
    
  }

  @Override
  public int readUSDistance() {
    return this.distance;
  }
}
