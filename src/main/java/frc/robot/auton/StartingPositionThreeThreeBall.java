package frc.robot.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
//import frc.robot.commands.*;
import frc.robot.commands.drivetrain.*;
import frc.robot.commands.feeder.FeederTimedFeed;
import frc.robot.commands.grasper.GrasperTimedGrasp;
//import frc.robot.auton.AutonConstants;
import frc.robot.commands.hinge.HingeTimedMoveDown;
import frc.robot.commands.hinge.HingeTimedMoveUp;
import frc.robot.commands.shooter.ShooterShootUsingCamera;
import frc.robot.commands.shooter.ShooterTimedShootHigh;
import frc.robot.commands.shooter.ShooterTimedShootUsingCamera;


public class StartingPositionThreeThreeBall extends CommandGroup {
	/**
	 * Add your docs here.
	 */
    final int TURN_DIRECTION = +1;
        //Left is equal to -1
        //Right is equal to +1

	public StartingPositionThreeThreeBall() {

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

		addSequential(new ShooterTimedShootHigh(1));
		// Starts shooter for 1 sec

		addParallel(new ShooterTimedShootUsingCamera(15));
		// Starts magic shoot

		addParallel(new HingeTimedMoveDown(5));
		// Moves hinge down for 5 secs max

		addParallel(new GrasperTimedGrasp(14));
		addSequential(new DrivetrainMoveDistanceWithStallDetection(+AutonConstants.DISTANCE_FROM_STARTING_POINT_TWO_TO_CARGO_PICKUP));
		// Moves from starting point three to cargo pickup area

		addSequential(new DrivetrainTurnAngleFromCameraUsingPidController());
		// Turns to hub

		addParallel(new FeederTimedFeed(5));
		// Feeds (i.e. shoots) for max 5 secs

		addSequential(new HingeTimedMoveUp(5));
		// Moves hinge up max 5 secs

		addSequential(new WaitCommand(2));

		addSequential(new DrivetrainMoveDistanceWithStallDetection(-AutonConstants.DISTANCE_FROM_CARGO_PICKUP_TO_SHOOTING_ZONE));
		// Move from picking up cargo to shooting zone

		addSequential(new DrivetrainTurnAngleUsingPidControllerWithStallDetection(-70));
		// Turns 70 degrees left to face third cargo

		addSequential(new DrivetrainMoveDistanceWithStallDetection(+AutonConstants.DISTANCE_FROM_STARTING_POINT_THREE_TO_THIRD_CARGO));
		// Moves from shooting zone to third cargo

		addSequential(new WaitCommand(2));
		// Waits to pick up cargo

		addSequential(new DrivetrainMoveDistanceWithStallDetection(-AutonConstants.DISTANCE_FROM_STARTING_POINT_THREE_TO_THIRD_CARGO));
		// Moves from third cargo to tarmac

		addSequential(new DrivetrainTurnAngleUsingPidControllerWithStallDetection(70));
		// Turns 70 degrees right to face hub 

		addSequential(new DrivetrainTurnAngleFromCameraUsingPidController());
		// Turns to face shooter to hub

		addSequential(new FeederTimedFeed(3));
		// Feeds (i.e. shoots) max 3 secs




		
  }

}