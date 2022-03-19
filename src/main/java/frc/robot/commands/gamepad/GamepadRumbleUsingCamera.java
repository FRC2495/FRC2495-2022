// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package frc.robot.commands.gamepad;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.Robot;
import frc.robot.ControllerBase;

/**
 *
 */
public class GamepadRumbleUsingCamera extends Command {

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
	static final double DISTANCE_MIN = 100-6;
	static final double DISTANCE_MAX = 100+24+6;

	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

	// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
	public GamepadRumbleUsingCamera() {
	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
		setRunWhenDisabled(true); // allows running of command when robot is disabled		
		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
		//requires(Robot.oi.getGamepad());
		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		System.out.println("GamepadRumbleUsingCamera: initialize");
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		if (Robot.camera != null && Robot.camera.getNumberOfTargets() > 0 && 
			Math.abs(Robot.camera.getAngleToTurnToCompositeTarget()) < 5 &&
			Robot.camera.getFilteredDistance() > DISTANCE_MIN &&
			Robot.camera.getFilteredDistance() < DISTANCE_MAX)
			{
			ControllerBase.rumble(true, Robot.oi.getGamepad());
		} else { // no camera or no target, so quiet
			ControllerBase.rumble(false, Robot.oi.getGamepad());
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return false; // we are never finished
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		System.out.println("GamepadRumbleUsingCamera: end");
		ControllerBase.rumble(false, Robot.oi.getGamepad());
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		System.out.println("GamepadRumbleUsingCamera: interrupted");
		end();
	}
}
