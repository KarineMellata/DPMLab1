package ca.mcgill.ecse211.wallfollowing;

public interface UltrasonicController {

  public void processUSData(int distance);
  public boolean isTooClose = false;
  public int readUSDistance();
}
