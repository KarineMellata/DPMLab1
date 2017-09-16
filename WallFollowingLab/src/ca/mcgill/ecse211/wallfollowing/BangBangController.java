package ca.mcgill.ecse211.wallfollowing;

import lejos.hardware.ev3.EV3;
import lejos.hardware.motor.*;
import java.math.*;

public class BangBangController implements UltrasonicController {

  private final int bandCenter;
  private final int bandwidth;
  private final int motorLow;
  private final int motorHigh;
  private int distance;
  public boolean isTooClose;

  public BangBangController(int bandCenter, int bandwidth, int motorLow, int motorHigh) {
    // Default Constructor
    this.bandCenter = bandCenter;
    this.bandwidth = bandwidth;
    this.motorLow = motorLow;
    this.motorHigh = motorHigh;
    WallFollowingLab1.leftMotor.setSpeed(motorHigh); // Start robot moving forward
    WallFollowingLab1.rightMotor.setSpeed(motorHigh);
    WallFollowingLab1.leftMotor.forward();
    WallFollowingLab1.rightMotor.forward();
  }

  @Override
  public void processUSData(int distance) {
    this.distance = distance;
    // TODO: process a movement based on the us distance passed in (BANG-BANG style)
    int distError = bandCenter - distance; //Error = reference control value - measured distance from the wall
    if(Math.abs(distError) <= bandwidth) { //Within tolerance - Keep going.
    		WallFollowingLab1.leftMotor.setSpeed(motorHigh); //0 bias
    		WallFollowingLab1.rightMotor.setSpeed(motorHigh);
    		WallFollowingLab1.leftMotor.forward();
    		WallFollowingLab1.rightMotor.forward();
    }
    else if(distError > 0) { //Too close to the wall - Move away.
			WallFollowingLab1.leftMotor.setSpeed(motorLow); 
			WallFollowingLab1.rightMotor.setSpeed(motorHigh + 50);
			WallFollowingLab1.leftMotor.backward();
			WallFollowingLab1.rightMotor.forward();
    }
    else if(distError < 0) { //Too far from the wall - Move closer.
		WallFollowingLab1.leftMotor.setSpeed(motorHigh + 50);
		WallFollowingLab1.rightMotor.setSpeed(motorLow );
		WallFollowingLab1.leftMotor.forward();
		WallFollowingLab1.rightMotor.forward();
    }
    
  }

  @Override
  public int readUSDistance() {
    return this.distance;
  }
}
