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

		// 1. drive into hangar

		// 2. use gamepad to extended rear arms
		// addSequential(new RearArmsExtend());

		// 3. drive back against bar

		// 4. extend front elbow
		addSequential(new FrontElbowsOpen());

		// 5. extend front arms
		addSequential(new FrontArmsExtend());

		// 6. retract rear arms (partially?)
		addSequential(new RearArmsRetract());

		// 7. retract front elbow (partially?)
		addSequential(new FrontElbowsClose());


		/*addSequential(new RearArmsRetract());

		// move onto the bar

		addSequential(new FrontElbowsOpen());

		addSequential(new FrontArmsExtend());

		addSequential(new FrontElbowsOpen());

		addSequential(new FrontArmsExtend());

		addSequential(new FrontElbowsClose());

		addSequential(new RearArmsExtend());

		addSequential(new RearElbowsClose());

		addSequential(new RearArmsRetract());

		addSequential(new RearArmsExtend());

		addSequential(new FrontArmsExtend());

		addSequential(new FrontElbowsClose());

		addSequential(new FrontArmsRetract());

		addSequential(new FrontElbowsOpen());

		addSequential(new RearArmsExtend());

		addSequential(new FrontArmsExtend());

		addSequential(new RearElbowsOpen());

		addSequential(new FrontArmsRetract());

		addSequential(new FrontElbowsClose());

		addSequential(new RearArmsExtend());*/

		// todo: finish implementation
		
	}
}