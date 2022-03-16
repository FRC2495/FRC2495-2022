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


public class FrontOpenExtend extends CommandGroup {
	/**
	 * Add your docs here.
	 */
	public FrontOpenExtend() {

		// 4. extend front elbow
		addParallel(new FrontElbowsOpen());

		// 5. extend front arms
		addSequential(new FrontArmsExtendWithStallDetection());
	}
}