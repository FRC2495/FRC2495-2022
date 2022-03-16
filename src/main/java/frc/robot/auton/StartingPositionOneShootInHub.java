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
import frc.robot.commands.feeder.FeederStop;
//import frc.robot.auton.AutonConstants;
import frc.robot.commands.feeder.FeederTimedFeed;


public class StartingPositionOneShootInHub extends CommandGroup {
	/**
	 * Add your docs here.
	 */
	static final int TURN_DIRECTION = +1;
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

		addSequential(new ShooterTimedShootHigh(1));
		// Runs Shooter (i.e. spinning) for one sec
		
		addParallel(new ShooterTimedShootHigh(14));
		// Starts Shooter (i.e. spinning) - will stop after 15 secs or explicit stop, whichever comes first

		addParallel(new GrasperTimedGrasp(15));
		// Strarts Grasper (i.e. grasping) - will stop after 15 seconds or explicit stop, whichever comes first
	
		addSequential(new DrivetrainMoveDistanceWithStallDetection(+AutonConstants.DISTANCE_FROM_STARTING_POINT_ONE_TO_HIGH_SHOOTING_ZONE));
		// Moving from starting point 1 to high shooting zone

		addParallel(new FeederTimedFeed(15));
		// Feeds (i.e. shoots) - will take 15 secs

		//addSequential(new DrivetrainMoveDistanceWithStallDetection(+AutonConstants.DISTANCE_FROM_CARGO_PICKUP_TO_SHOOTING_ZONE));
		// Moving from shooting zone to cargo pickup

		addSequential(new HingeTimedMoveDown(7));
		// Moves hinge down (wait for it to go down max 7 secs)

		addSequential(new DrivetrainMoveDistanceWithStallDetection(-AutonConstants.DISTANCE_FROM_CARGO_PICKUP_TO_SHOOTING_ZONE));
		// Moving from cargo pickup to shooting zone (adjust constant if needed)

		addSequential(new HingeMoveUp());
		// Moves hinge up

		addSequential(new FeederTimedFeed(5));
		// Feeder feeds - will take 5 secs (to stall from stopping motors)

		//addSequential(new DrivetrainMoveDistanceWithStallDetection(+AutonConstants.DISTANCE_FROM_SHOOTING_ZONE_TO_CARGO_PICKUP));
		// Attempts to pickup cargo

		//addSequential(new HingeTimedMoveUp(2));
		// Moves hinge up

		//addParallel(new HingeTimedMoveDown(5));
		// Moves hinge down for 5 seconds	

		//addSequential(new FeederTimedFeed(12));
		// Feeds cargo - will take 12 secs

		//addSequential(new DrivetrainMoveDistanceWithStallDetection(-AutonConstants.DISTANCE_FROM_CARGO_PICKUP_TO_SHOOTING_ZONE));
		// Moving from cargo pickup to shooting zone (adjust constant if needed)

		//addSequential(new HingeTimedMoveUp(2));
		// Moves hinge up for 2 seconds

		//addSequential(new FeederTimedFeed(2));
		// Feeds cargo - will take 2 secs

		addSequential(new GrasperStop());
		// Stops Grasper

		addSequential(new FeederStop());
		// Stops Feeder

		addSequential(new ShooterStop());
   		// Stops Shooter
	}
}