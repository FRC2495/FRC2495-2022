package frc.robot.interfaces;

import edu.wpi.first.wpilibj.Joystick;

public interface IShooter {
	
	public void shoot();
	
	public void stop();
		
	// NOTE THAT THIS METHOD WILL IMPACT BOTH OPEN AND CLOSED LOOP MODES
	public void setNominalAndPeakOutputs(double peakOutput);
	
	public boolean isShooting();

	// for debug purpose only
	public void joystickControl(Joystick joystick);
}










