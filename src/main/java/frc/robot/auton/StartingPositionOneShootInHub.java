/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;

//import frc.robot.commands.*;
import frc.robot.commands.grasper.*;
import frc.robot.commands.shooter.*;
import frc.robot.commands.drivetrain.*;
import frc.robot.commands.hinge.*;
//import frc.robot.auton.AutonConstants;
import frc.robot.commands.feeder.FeederTimedFeed;


public class StartingPositionOneShootInHub extends CommandGroup {
	/**
	 * Add your docs here.
	 */
	final int TURN_DIRECTION = +1;
		//Left is equal to -1
		//Right is equal to +1

	public StartingPositionOneShootInHub() {

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

		addParallel(new ShooterTimedShootHigh(15));
		// Starts Shooter (i.e. spinning) - will stop after 15 secs or explicit stop, whichever comes first
	
		addSequential(new DrivetrainMoveDistanceWithStallDetection(-AutonConstants.DISTANCE_FROM_STARTING_POINT_ONE_TO_HIGH_SHOOTING_ZONE));
		// Moving from starting point 1 to high shooting zone

		addSequential(new FeederTimedFeed(2));
		// Feeds (i.e. shoots) - will take 2 secs

		addParallel(new HingeMoveDown());
		// Starts moving hinge down (does not wait for it to go down)

		addParallel(new GrasperTimedGrasp(5));
		// Starts Grasper - will stop after 5 secs or explicit stop, whichever comes first

		addSequential(new DrivetrainMoveDistanceWithStallDetection(-AutonConstants.DISTANCE_FROM_SHOOTING_ZONE_TO_CARGO_PICKUP));
		// Attempts to pickup cargo

		addSequential(new HingeMoveUp());
		// Moves hinge up

		addParallel(new GrasperTimedGrasp(5));
		// Starts Grasper to push cargo into the feeder and runs it for up to 5 secs - does not wait

		addParallel(new HingeMoveDown());
		// Moves hinge down (does not wait)

		addSequential(new DrivetrainMoveDistanceWithStallDetection(+AutonConstants.DISTANCE_FROM_SHOOTING_ZONE_TO_CARGO_PICKUP));
		// Moving from cargo pickup to shooting zone (adjust constant if needed)

		addSequential(new FeederTimedFeed(2));
		// Feeds cargo - will take 2 secs

		addSequential(new GrasperStop());
		// Stops Grasper

		addSequential(new ShooterStop());
   		// Stops Shooter
	}
}