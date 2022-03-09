/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.groups;

import edu.wpi.first.wpilibj.command.CommandGroup;

//import frc.robot.commands.*;
import frc.robot.commands.arms.*;
import frc.robot.commands.elbows.*;


public class Climb extends CommandGroup {
	/**
	 * Add your docs here.
	 */
	public Climb() {

		// 1. drive forward into hangar

		// 2. use gamepad to extended rear arms
		// addSequential(new RearArmsExtend());

		// 2.5 use gamepad to set rear elbows midway
		// addSequential(new RearElbowsMidwayWithStallDetection());

		// 3. drive back against bar

		// 4. extend front elbow
		addSequential(new FrontElbowsOpen());

		// 5. extend front arms
		addSequential(new FrontArmsExtendWithStallDetection());

		// 6. retract rear arms
		addSequential(new RearArmsRetractWithStallDetection());

		// 6.5. retract rear elbow
		addSequential(new RearElbowsCloseWithStallDetection());

		// 7. retract front elbow
		addSequential(new FrontElbowsCloseWithStallDetection());

		// we are now hanging from the second bar

		/*

		// next, we attempt to climb to the third bar
		
		// 1. extend rear arms
		addSequential(new RearArmsExtendWithStallDetection());

		// 2. retract front arms
		addSequential(new FrontArmsRetractWithStallDetection());

		// 3. rear elbows midway
		addSequential(new RearElbowsMidwayWithStallDetection());

		// 4. rear arms retract
		addSequential(new RearArmsRetractWithStallDetection());

		todo finish

		*/

		// we are now hanging from the third bar
		
	}
}