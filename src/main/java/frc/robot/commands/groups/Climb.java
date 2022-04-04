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

		// 2. use gamepad to extended rear arms
		// addSequential(new RearArmsExtend());

		// 2.5 use gamepad to set rear elbows midway
		// addSequential(new RearElbowsMidwayWithStallDetection());

		// 3. drive back against bar

		// 4. extend front elbow
		//addParallel(new FrontElbowsOpen());

		// 5. extend front arms
		//addSequential(new FrontArmsExtendWithStallDetection());

		// 4. & 5.
		addSequential(new FrontOpenExtend());

		//addSequential(new WaitCommand(2));

		// 6. retract rear arms
		addSequential(new RearArmsRetractWithStallDetection());

	    //addSequential(new WaitCommand(2));

		// 6.5. retract rear elbow
		addSequential(new RearElbowsCloseWithStallDetection());

		//addSequential(new WaitCommand(2));

		// 7. close front elbow 
		addSequential(new FrontElbowsMoveWithStallDetection(3137));

		//addSequential(new WaitCommand(2));

		// 8. retract front arms 
		addSequential(new FrontArmsRetractWithStallDetection());

		//addSequential(new WaitCommand(2));

		// we are now hanging from the second bar


		// next, we attempt to climb to the third bar
		
		// 1. extend rear arms
		addSequential(new RearArmsExtendWithStallDetection());

		//addSequential(new WaitCommand(2));

		// 2. close front elbows
		addSequential(new FrontElbowsMoveWithStallDetection(0));

		//addSequential(new WaitCommand(2));

		// 3. close rear elbows
		addSequential(new RearElbowsMoveWithStallDetection(0));

		//addSequential(new WaitCommand(2));


		// 4. retract rear arms
		addSequential(new RearArmsRetractWithStallDetection());

		//addSequential(new WaitCommand(2));

		// 5. extend front arms
		addSequential(new FrontArmsExtendWithStallDetection());

		//(new WaitCommand(2));

		// 6. open front elbows
		addSequential(new FrontElbowsOpenWithStallDetection());

		//addSequential(new WaitCommand(2));

		// 7. retract front arms 
		addSequential(new FrontArmsRetractWithStallDetection());

		//addSequential(new WaitCommand(2));

		// 8. extend rear arms
		addSequential(new RearArmsExtendWithStallDetection());

		//addSequential(new WaitCommand(2));

		// 9. open rear elbows
		addSequential(new RearElbowsOpenWithStallDetection());

		//addSequential(new WaitCommand(2));

		// 5. open front elbows
		//addSequential(new FrontElbowsOpen());

		// 6. retract front arms
		//addSequential(new FrontArmsRetractWithStallDetection());

		// 6. close front elbows
		//addSequential(new FrontElbowsCloseWithStallDetection());

		// we are now hanging from the third bar
		
	}
}