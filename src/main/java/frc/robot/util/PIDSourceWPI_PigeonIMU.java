package frc.robot.util;

import edu.wpi.first.wpilibj.*;

import com.ctre.phoenix.sensors.WPI_PigeonIMU;;

//import edu.wpi.first.wpilibj.PIDController;
//import edu.wpi.first.wpilibj.PIDSource;

/**
 * This utility class allows WPI_PigeonIMU to serve as a PIDSource (as PIDSource has been deprecated)
 */
public class PIDSourceWPI_PigeonIMU extends WPI_PigeonIMU implements PIDSource {

	private PIDSourceType m_pidSource = PIDSourceType.kDisplacement;

	public PIDSourceWPI_PigeonIMU(int port) {
		super(port);
	}

	/**
	 * Set which parameter of the gyro you are using as a process control variable. The Gyro class
	 * supports the rate and displacement parameters
	 *
	 * @param pidSource An enum to select the parameter.
	 */
	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
	  m_pidSource = pidSource;
	}
  
	@Override
	public PIDSourceType getPIDSourceType() {
	  return m_pidSource;
	}
  
	/**
	 * Get the output of the gyro for use with PIDControllers. May be the angle or rate depending on
	 * the set PIDSourceType
	 *
	 * @return the output according to the gyro
	 */
	@Override
	public double pidGet() {
	  switch (m_pidSource) {
		case kRate:
		  return getRate();
		case kDisplacement:
		  return getAngle();
		default:
		  return 0.0;
	  }
	}
}
