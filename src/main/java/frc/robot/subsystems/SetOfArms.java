/**
 * 
 */
package frc.robot.subsystems;

//import java.util.Timer;
//import java.util.TimerTask;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;

import frc.robot.interfaces.*;
//import frc.robot.Ports;
import frc.robot.Robot;


/**
 * The {@code SetOfArms} class contains fields and methods pertaining to the function of the set of arms.
 */
public class SetOfArms extends Subsystem implements ISetOfArms {

	
	// general settings
	public static final int LENGTH_OF_TRAVEL_TICKS_FRONT = -450000; // TODO adjust as needed (halve for Talon FX)
	public static final int LENGTH_OF_TRAVEL_TICKS_REAR = 600000; // TODO adjust as needed (halve for Talon FX)

	static final double MAX_PCT_OUTPUT = 1.0;
	static final int WAIT_MS = 1000;
	static final int TIMEOUT_MS = 5000;

	static final int TALON_TIMEOUT_MS = 20;

	// move settings
	static final int PRIMARY_PID_LOOP = 0;
	
	static final int SLOT_0 = 0;
	
	static final double REDUCED_PCT_OUTPUT = 0.5;
	
	static final double MOVE_PROPORTIONAL_GAIN = 0.6; // 1.2 for SRX // TODO switch to 0.6 if required if switching to Talon FX (as encoder resolution is halved)
	static final double MOVE_INTEGRAL_GAIN = 0.0;
	static final double MOVE_DERIVATIVE_GAIN = 0.0;
	
	static final int TALON_TICK_THRESH = 128; //256
	static final double TICK_THRESH = 512;
	public static final double TICK_PER_100MS_THRESH = 64; // about a tenth of a rotation per second 
	
	private final static int MOVE_ON_TARGET_MINIMUM_COUNT= 20; // number of times/iterations we need to be on target to really be on target

	private final static int MOVE_STALLED_MINIMUM_COUNT = MOVE_ON_TARGET_MINIMUM_COUNT * 2 + 30; // number of times/iterations we need to be stalled to really be stalled

	WPI_TalonSRX arm; 
	//BaseMotorController arm_follower;
	
	boolean isMoving;
	boolean isExtending;
	boolean isReallyStalled;

	double tac;

	private int onTargetCount; // counter indicating how many times/iterations we were on target 
	private int stalledCount; // counter indicating how many times/iterations we were stalled
	
	Robot robot;

	Side side;
	
	
	public SetOfArms(WPI_TalonSRX arm_in, /*BaseMotorController arm_follower_in,*/ Robot robot_in, boolean setInverted, Side side_in) {
		
		arm = arm_in;
		//arm_follower = arm_follower_in;
				
		robot = robot_in;

		side = side_in;

		arm.configFactoryDefault();
		//arm_follower.configFactoryDefault();
		
		// Mode of operation during Neutral output may be set by using the setNeutralMode() function.
		// As of right now, there are two options when setting the neutral mode of a motor controller,
		// brake and coast.
		arm.setNeutralMode(NeutralMode.Brake);
		//arm_follower.setNeutralMode(NeutralMode.Brake);
				
		// Sensor phase is the term used to explain sensor direction.
		// In order for limit switches and closed-loop features to function properly the sensor and motor has to be in-phase.
		// This means that the sensor position must move in a positive direction as the motor controller drives positive output.
		
		if (side == Side.FRONT) {
			arm.setSensorPhase(true); // false for SRX // TODO switch to true if required if switching to Talon FX
		} else {
			arm.setSensorPhase(true);
		}
		
		//Enable limit switches
		arm.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, TALON_TIMEOUT_MS);
		arm.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, TALON_TIMEOUT_MS);
		arm.overrideLimitSwitchesEnable(true);
	
		// Motor controller output direction can be set by calling the setInverted() function as seen below.
		// Note: Regardless of invert value, the LEDs will blink green when positive output is requested (by robot code or firmware closed loop).
		// Only the motor leads are inverted. This feature ensures that sensor phase and limit switches will properly match the LED pattern
		// (when LEDs are green => forward limit switch and soft limits are being checked).
		arm.setInverted(setInverted);  // TODO switch to false if required if switching to Talon FX
		//arm_follower.setInverted(setInverted);  // TODO comment out if switching to Talon FX
		
		// Both the Talon SRX and Victor SPX have a follower feature that allows the motor controllers to mimic another motor controller's output.
		// Users will still need to set the motor controller's direction, and neutral mode.
		// The method follow() allows users to create a motor controller follower of not only the same model, but also other models
		// , talon to talon, victor to victor, talon to victor, and victor to talon.
		//arm_follower.follow(arm);

		setPIDParameters();
		
		// set peak output to max in case if had been reduced previously
		setNominalAndPeakOutputs(MAX_PCT_OUTPUT);

	
		// Sensors for motor controllers provide feedback about the position, velocity, and acceleration
		// of the system using that motor controller.
		// Note: With Phoenix framework, position units are in the natural units of the sensor.
		// This ensures the best resolution possible when performing closed-loops in firmware.
		// CTRE Magnetic Encoder (relative/quadrature) =  4096 units per rotation		
		arm.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, PRIMARY_PID_LOOP, TALON_TIMEOUT_MS); // .CTRE_MagEncoder_Relative for SRX // TODO switch to FeedbackDevice.IntegratedSensor if switching to Talon FX
		
		// this will reset the encoder automatically when at or past the forward limit sensor
		arm.configSetParameter(ParamEnum.eClearPositionOnLimitF, 1, 0, 0, TALON_TIMEOUT_MS);
		arm.configSetParameter(ParamEnum.eClearPositionOnLimitR, 0, 0, 0, TALON_TIMEOUT_MS);
		
		isMoving = false;
		isExtending = false;
		isReallyStalled = false;
		stalledCount = 0;
	}
	
	@Override
	public void initDefaultCommand() {
		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

	@Override
	public void periodic() {
		// Put code here to be run every loop
	}

	// This method should be called to assess the progress of a move
	public boolean tripleCheckMove() {
		if (isMoving) {
			
			double error = arm.getClosedLoopError(PRIMARY_PID_LOOP);
			
			boolean isOnTarget = (Math.abs(error) < TICK_THRESH);
			
			if (isOnTarget) { // if we are on target in this iteration 
				onTargetCount++; // we increase the counter
			} else { // if we are not on target in this iteration
				if (onTargetCount > 0) { // even though we were on target at least once during a previous iteration
					onTargetCount = 0; // we reset the counter as we are not on target anymore
					System.out.println("Triple-check failed (arm moving).");
				} else {
					// we are definitely moving
				}
			}
			
			if (onTargetCount > MOVE_ON_TARGET_MINIMUM_COUNT) { // if we have met the minimum
				isMoving = false;
			}
			
			if (!isMoving) {
				System.out.println("You have reached the target (arm moving).");
				//arm.set(ControlMode.PercentOutput,0);
				if (isExtending)	{
					stop(); // adjust if needed
				} else {
					stop(); // adjust if needed
				}
			}
		}
		return isMoving; 
	}

	// return if drivetrain might be stalled
	public boolean tripleCheckIfStalled() {
		if (isMoving) {
			
			double velocity = getEncoderVelocity();
			
			boolean isStalled = (Math.abs(velocity) < TICK_PER_100MS_THRESH);
			
			if (isStalled) { // if we are stalled in this iteration 
				stalledCount++; // we increase the counter
			} else { // if we are not stalled in this iteration
				if (stalledCount > 0) { // even though we were stalled at least once during a previous iteration
					stalledCount = 0; // we reset the counter as we are not stalled anymore
					System.out.println("Triple-check failed (detecting stall).");
				} else {
					// we are definitely not stalled
					
					//System.out.println("moving velocity : " + velocity);
				}
			}
			
			if (isMoving && stalledCount > MOVE_STALLED_MINIMUM_COUNT) { // if we have met the minimum
				isReallyStalled = true;
			}
					
			if (isReallyStalled) {
				System.out.println("WARNING: Stall detected!");
				stop(); // WE STOP IF A STALL IS DETECTED				 
			}
		}
		
		return isReallyStalled;
	}

	public int getEncoderVelocity() {
		return (int) (arm.getSelectedSensorVelocity(PRIMARY_PID_LOOP));
	}
	
	public void extend() {
		
		//setPIDParameters();
		System.out.println("Extending");
		setNominalAndPeakOutputs(MAX_PCT_OUTPUT);

		if (side == Side.FRONT) {
			tac = +LENGTH_OF_TRAVEL_TICKS_FRONT;
		} else {
			tac = -LENGTH_OF_TRAVEL_TICKS_REAR;
		}
		
		arm.set(ControlMode.Position,tac);
		
		isMoving = true;
		isExtending = true;
		onTargetCount = 0;
		isReallyStalled = false;
		stalledCount = 0;
	}
	
	public void retract() {
		
		//setPIDParameters();
		System.out.println("Retracting");
		setNominalAndPeakOutputs(MAX_PCT_OUTPUT);

		tac = 0; // adjust as needed
		arm.set(ControlMode.Position,tac);
		
		isMoving = true;
		isExtending = false;
		onTargetCount = 0;
		isReallyStalled = false;
		stalledCount = 0;
	}

	public double getEncoderPosition() {
		return arm.getSelectedSensorPosition(PRIMARY_PID_LOOP);
	}
	
	public void stay() {	 		
		isMoving = false;		
		isExtending = false;
	}

	public synchronized void stop() {
		arm.set(ControlMode.PercentOutput, 0);
		
		isMoving = false;
		isExtending = false;
	}
	
	private void setPIDParameters() {		
		arm.configAllowableClosedloopError(SLOT_0, TALON_TICK_THRESH, TALON_TIMEOUT_MS);
		
		// P is the proportional gain. It modifies the closed-loop output by a proportion (the gain value)
		// of the closed-loop error.
		// P gain is specified in output unit per error unit.
		// When tuning P, it's useful to estimate your starting value.
		// If you want your mechanism to drive 50% output when the error is 4096 (one rotation when using CTRE Mag Encoder),
		// then the calculated Proportional Gain would be (0.50 X 1023) / 4096 = ~0.125.
		
		// I is the integral gain. It modifies the closed-loop output according to the integral error
		// (summation of the closed-loop error each iteration).
		// I gain is specified in output units per integrated error.
		// If your mechanism never quite reaches your target and using integral gain is viable,
		// start with 1/100th of the Proportional Gain.
		
		// D is the derivative gain. It modifies the closed-loop output according to the derivative error
		// (change in closed-loop error each iteration).
		// D gain is specified in output units per derivative error.
		// If your mechanism accelerates too abruptly, Derivative Gain can be used to smooth the motion.
		// Typically start with 10x to 100x of your current Proportional Gain.

		// Feed-Forward is typically used in velocity and motion profile/magic closed-loop modes.
		// F gain is multiplied directly by the set point passed into the programming API.
		// The result of this multiplication is in motor output units [-1023, 1023]. This allows the robot to feed-forward using the target set-point.
		// In order to calculate feed-forward, you will need to measure your motor's velocity at a specified percent output
		// (preferably an output close to the intended operating range).
		
		arm.config_kP(SLOT_0, MOVE_PROPORTIONAL_GAIN, TALON_TIMEOUT_MS);
		arm.config_kI(SLOT_0, MOVE_INTEGRAL_GAIN, TALON_TIMEOUT_MS);
		arm.config_kD(SLOT_0, MOVE_DERIVATIVE_GAIN, TALON_TIMEOUT_MS);
		arm.config_kF(SLOT_0, 0, TALON_TIMEOUT_MS);
	}
	
	// NOTE THAT THIS METHOD WILL IMPACT BOTH OPEN AND CLOSED LOOP MODES
	public void setNominalAndPeakOutputs(double peakOutput)
	{
		arm.configPeakOutputForward(peakOutput, TALON_TIMEOUT_MS);
		arm.configPeakOutputReverse(-peakOutput, TALON_TIMEOUT_MS);
		
		arm.configNominalOutputForward(0, TALON_TIMEOUT_MS);
		arm.configNominalOutputReverse(0, TALON_TIMEOUT_MS);
	}
	
	public synchronized boolean isMoving() {
		return isMoving;
	}

	public synchronized boolean isExtending() {
		return isExtending;
	}

	// return if stalled
	public boolean isStalled() {
		return isReallyStalled;
	}
	
	// for debug purpose only
	public void joystickControl(Joystick joystick)
	{
		if (!isMoving) // if we are already doing a move we don't take over
		{
			if (side == Side.FRONT) {
				arm.set(ControlMode.PercentOutput, -joystick.getY()); // adjust sign if desired
			} else {
				arm.set(ControlMode.PercentOutput, +joystick.getY()); // adjust sign if desired
			}
		}
	}

	public double getTarget() {
		return tac;
	}	

	public boolean getLimitSwitchState() {
		return arm.getSensorCollection().isFwdLimitSwitchClosed();
	}

	public boolean getReverseLimitSwitchState() {
		return arm.getSensorCollection().isRevLimitSwitchClosed();
	}

	// MAKE SURE THAT YOU ARE NOT IN A CLOSED LOOP CONTROL MODE BEFORE CALLING THIS METHOD.
	// OTHERWISE THIS IS EQUIVALENT TO MOVING TO THE DISTANCE TO THE CURRENT ZERO IN REVERSE! 
	public void resetEncoder() {
		arm.set(ControlMode.PercentOutput,0); // we stop AND MAKE SURE WE DO NOT MOVE WHEN SETTING POSITION
		arm.setSelectedSensorPosition(0, PRIMARY_PID_LOOP, TALON_TIMEOUT_MS); // we mark the virtual zero
	}


}
