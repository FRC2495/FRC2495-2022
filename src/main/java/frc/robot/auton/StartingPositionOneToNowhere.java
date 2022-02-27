package frc.robot.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

//import frc.robot.commands.*;
import frc.robot.commands.drivetrain.*;
//import frc.robot.auton.AutonConstants;


public class StartingPositionOneToNowhere extends CommandGroup {
	/**
	 * Add your docs here.
	 */
    final int TURN_DIRECTION = +1;
        //Left is equal to -1
        //Right is equal to +1

	public StartingPositionOneToNowhere() {

    	// Add Commands here:
		// e.g. addSequential(new Command1());
		// addSequential(new Command2());
		// these will run in order.

		// To run multiple commands at the same time,
		// use addParallel()
		// e.g. addParallel(new Command1());
		// addSequential(new Command2());
		// Command1 and Command2 will run in parallel.

		// A command group will require all of the subsystems that each member
		// would require.
		// e.g. if Command1 requires chassis, and Command2 requires arm,
		// a CommandGroup containing them would require both the chassis and the
		// arm.    

		addSequential(new DrivetrainMoveDistanceWithStallDetection(-AutonConstants.DISTANCE_FROM_STARTING_POINT_ONE_TO_OUTSIDE_TARMAC));
		// Moving from starting point 1 to outside tarmac
  }

}