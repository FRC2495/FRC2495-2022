/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
//import frc.robot.commands.*;
import frc.robot.commands.arms.*;
import frc.robot.commands.elbows.*;


public class Climb extends CommandGroup {
	/**
	 * Add your docs here.
	 */
	public Climb() {

		// 1. drive forward into hangar

		// 2. use gamepad to extend arms and position at mid bar 
		
		addSequential (new FrontArmsRetractWithStallDetection());
		// 3. retract arm (now on mid bar)

		addSequential (new FrontElbowsOpenWithStallDetection());
		// 4. open elbow

		addSequential (new FrontArmsExtendWithStallDetection());
		// 5. extend arm

		addSequential (new FrontElbowsCloseWithStallDetection());
		// 6. close elbow (now touching high bar)

		addSequential (new FrontArmsRetractWithStallDetection());
		// 7. retract arm (now on high bar)

		addSequential (new FrontArmsExtendWithStallDetection());
		// 8. extend front arms

		addSequential (new FrontElbowsOpenWithStallDetection());
		// 9. close elbow (now touching traversal bar)

		addSequential (new FrontArmsRetractWithStallDetection());
		// 10. retract arm 
		
	}
}