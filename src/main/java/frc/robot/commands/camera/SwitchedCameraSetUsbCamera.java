// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package frc.robot.commands.camera;

import edu.wpi.first.wpilibj.command.InstantCommand;

//import frc.robot.Robot;
import frc.robot.sensors.SwitchedCamera;

/**
 *
 */
public class SwitchedCameraSetUsbCamera extends InstantCommand {

	private int camera;

	public SwitchedCameraSetUsbCamera(int camera_in) {
		setRunWhenDisabled(true); // allows running of command when robot is disabled

		camera = camera_in;
	}

	// Called once when this command runs
	@Override
	protected void initialize() {
		System.out.println("SwitchedCameraSetUsbCamera: initialize");
		SwitchedCamera.setUsbCamera(camera);
	}

}
