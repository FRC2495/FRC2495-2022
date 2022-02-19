package frc.robot.interfaces;

import edu.wpi.first.wpilibj.Joystick;

public interface IFeeder {
	
	public void feed();
	
	public void stop();
		
	// NOTE THAT THIS METHOD WILL IMPACT BOTH OPEN AND CLOSED LOOP MODES
	public void setNominalAndPeakOutputs(double peakOutput);
	
	public boolean isFeeding();

	// for debug purpose only
	public void joystickControl(Joystick joystick);
}










