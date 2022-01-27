/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.auton;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.commands.*;
import frc.robot.auton.AutonConstants;


public class StartingPositionOneShootInHub extends CommandGroup {
	/**
	 * Add your docs here.
	 */
    final int TURN_DIRECTION = +1;
        //Left is equal to -1
        //Right is equal to +1

	public StartingPositionOneShootInHub() {

        addSequential(new GrasperGrasp());
    
        addSequential(new DrivetrainMoveDistanceWithStallDetection(-AutonConstants.DISTANCE_FROM_STARTING_POINT_ONE_TO_OUTSIDE_TARMAC));
        //Moving from starting point 1 to the drop zone

        addSequential(new GrasperStop());

        // todo: shoot
   
    }
}