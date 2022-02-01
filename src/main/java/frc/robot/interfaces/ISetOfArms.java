package frc.robot.interfaces;


import edu.wpi.first.wpilibj.Joystick;

public interface ISetOfArms {
	
	public void extend();
	
	public void retract();
	
	public void extendAndStop();
	
	public void retractAndStop();
	
	public void stop();

	//public void open();

	//public void close();
	
	
	// NOTE THAT THIS METHOD WILL IMPACT BOTH OPEN AND CLOSED LOOP MODES
	public void setNominalAndPeakOutputs(double peakOutput);
	
	public boolean isExtending();
	
	public boolean isRetracting();

	// for debug purpose only
	public void joystickControl(Joystick joystick);
	
}
