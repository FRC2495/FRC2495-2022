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
 * The {@code SetOfElbows} class contains fields and methods pertaining to the function of the set of elbows.
 */
public class SetOfElbows extends Subsystem implements ISetOfElbows {

	
	// general settings
	public static final int LENGTH_OF_TRAVEL_TICKS_FRONT = 9000; // adjust as needed
	public static final int LENGTH_OF_TRAVEL_TICKS_REAR = 7000; // adjust as needed

	public static final int LENGTH_OF_TRAVEL_TICKS_MIDWAY = 4600; // adjust as needed

	static final double MAX_PCT_OUTPUT = 1.0;
	static final int WAIT_MS = 1000;
	static final int TIMEOUT_MS = 5000;

	static final int TALON_TIMEOUT_MS = 20;

	// move settings
	static final int PRIMARY_PID_LOOP = 0;
	
	static final int SLOT_0 = 0;
	
	static final double REDUCED_PCT_OUTPUT = 0.5;
	
	static final double MOVE_PROPORTIONAL_GAIN = 0.6;
	static final double MOVE_INTEGRAL_GAIN = 0.0;
	static final double MOVE_DERIVATIVE_GAIN = 0.0;
	
	static final int TALON_TICK_THRESH = 64;//128;
	static final double TICK_THRESH = 128;	
	
	private final static int MOVE_ON_TARGET_MINIMUM_COUNT= 10; // number of times/iterations we need to be on target to really be on target


	WPI_TalonSRX elbow; 
	BaseMotorController elbow_follower;
	
	boolean isMoving;
	boolean isOpening;

	double tac;

	private int onTargetCount; // counter indicating how many times/iterations we were on target 
	
	Robot robot;

	Side side;
	
	
	public SetOfElbows(WPI_TalonSRX elbow_in, BaseMotorController elbow_follower_in, Robot robot_in, Side side_in) {
		
		elbow = elbow_in;
		elbow_follower = elbow_follower_in;
				
		robot = robot_in;

		side = side_in;

		elbow.configFactoryDefault();
		elbow_follower.configFactoryDefault();
		
		// Mode of operation during Neutral output may be set by using the setNeutralMode() function.
		// As of right now, there are two options when setting the neutral mode of a motor controller,
		// brake and coast.
		elbow.setNeutralMode(NeutralMode.Brake);
		elbow_follower.setNeutralMode(NeutralMode.Brake);
				
		// Sensor phase is the term used to explain sensor direction.
		// In order for limit switches and closed-loop features to function properly the sensor and motor has to be in-phase.
		// This means that the sensor position must move in a positive direction as the motor controller drives positive output.
		elbow.setSensorPhase(true);

		//Enable limit switches
		elbow.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, TALON_TIMEOUT_MS);
		elbow.overrideLimitSwitchesEnable(true);
	
		// Motor controller output direction can be set by calling the setInverted() function as seen below.
		// Note: Regardless of invert value, the LEDs will blink green when positive output is requested (by robot code or firmware closed loop).
		// Only the motor leads are inverted. This feature ensures that sensor phase and limit switches will properly match the LED pattern
		// (when LEDs are green => forward limit switch and soft limits are being checked).
		elbow.setInverted(false);
		elbow_follower.setInverted(false);
		
		// Both the Talon SRX and Victor SPX have a follower feature that allows the motor controllers to mimic another motor controller's output.
		// Users will still need to set the motor controller's direction, and neutral mode.
		// The method follow() allows users to create a motor controller follower of not only the same model, but also other models
		// , talon to talon, victor to victor, talon to victor, and victor to talon.
		elbow_follower.follow(elbow);

		setPIDParameters();
		
		// set peak output to max in case if had been reduced previously
		setNominalAndPeakOutputs(MAX_PCT_OUTPUT);
	
		// Sensors for motor controllers provide feedback about the position, velocity, and acceleration
		// of the system using that motor controller.
		// Note: With Phoenix framework, position units are in the natural units of the sensor.
		// This ensures the best resolution possible when performing closed-loops in firmware.
		// CTRE Magnetic Encoder (relative/quadrature) =  4096 units per rotation		
		elbow.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,	PRIMARY_PID_LOOP, TALON_TIMEOUT_MS);
		
		// this will reset the encoder automatically when at or past the forward limit sensor
		elbow.configSetParameter(ParamEnum.eClearPositionOnLimitF, 1, 0, 0, TALON_TIMEOUT_MS);
		
		isMoving = false;
		isOpening = false;
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
			
			double error = elbow.getClosedLoopError(PRIMARY_PID_LOOP);
			
			boolean isOnTarget = (Math.abs(error) < TICK_THRESH);
			
			if (isOnTarget) { // if we are on target in this iteration 
				onTargetCount++; // we increase the counter
			} else { // if we are not on target in this iteration
				if (onTargetCount > 0) { // even though we were on target at least once during a previous iteration
					onTargetCount = 0; // we reset the counter as we are not on target anymore
					System.out.println("Triple-check failed (elbow moving).");
				} else {
					// we are definitely moving
				}
			}
			
			if (onTargetCount > MOVE_ON_TARGET_MINIMUM_COUNT) { // if we have met the minimum
				isMoving = false;
			}
			
			if (!isMoving) {
				System.out.println("You have reached the target (elbow moving).");
				//elbow.set(ControlMode.PercentOutput,0);
				if (isOpening)	{
					stop(); // adjust if needed
				} else {
					stop(); // adjust if needed
				}
			}
		}
		return isMoving; 
	}
	
	public void open() {
		
		//setPIDParameters();
		System.out.println("Opening");
		setNominalAndPeakOutputs(MAX_PCT_OUTPUT);

		if (side == Side.FRONT) {
			tac = -LENGTH_OF_TRAVEL_TICKS_FRONT;
		} else {
			tac = -LENGTH_OF_TRAVEL_TICKS_REAR;
		}
		
		elbow.set(ControlMode.Position,tac);
		
		isMoving = true;
		isOpening = true;
		onTargetCount = 0;
	}

	public void midway() {
		
		//setPIDParameters();
		System.out.println("Opening");
		setNominalAndPeakOutputs(MAX_PCT_OUTPUT);

		if (side == Side.FRONT) {
			tac = -LENGTH_OF_TRAVEL_TICKS_MIDWAY;
		} else {
			tac = -LENGTH_OF_TRAVEL_TICKS_MIDWAY;
		}
		
		elbow.set(ControlMode.Position,tac);
		
		isMoving = true;
		isOpening = true;
		onTargetCount = 0;
	}
	
	public void close() {
		
		//setPIDParameters();
		System.out.println("Closing");
		setNominalAndPeakOutputs(MAX_PCT_OUTPUT);

		tac = 0; // adjust as needed
		elbow.set(ControlMode.Position,tac);
		
		isMoving = true;
		isOpening = false;
		onTargetCount = 0;
	}

	public double getEncoderPosition() {
		return elbow.getSelectedSensorPosition(PRIMARY_PID_LOOP);
	}
	
	public void stay() {	 		
		isMoving = false;		
		isOpening = false;
	}

	public synchronized void stop() {
		elbow.set(ControlMode.PercentOutput, 0);
		
		isMoving = false;
		isOpening = false;
	}
	
	private void setPIDParameters() {		
		elbow.configAllowableClosedloopError(SLOT_0, TALON_TICK_THRESH, TALON_TIMEOUT_MS);
		
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
		
		elbow.config_kP(SLOT_0, MOVE_PROPORTIONAL_GAIN, TALON_TIMEOUT_MS);
		elbow.config_kI(SLOT_0, MOVE_INTEGRAL_GAIN, TALON_TIMEOUT_MS);
		elbow.config_kD(SLOT_0, MOVE_DERIVATIVE_GAIN, TALON_TIMEOUT_MS);
		elbow.config_kF(SLOT_0, 0, TALON_TIMEOUT_MS);
	}
	
	// NOTE THAT THIS METHOD WILL IMPACT BOTH OPEN AND CLOSED LOOP MODES
	public void setNominalAndPeakOutputs(double peakOutput)
	{
		elbow.configPeakOutputForward(peakOutput, TALON_TIMEOUT_MS);
		elbow.configPeakOutputReverse(-peakOutput, TALON_TIMEOUT_MS);
		
		elbow.configNominalOutputForward(0, TALON_TIMEOUT_MS);
		elbow.configNominalOutputReverse(0, TALON_TIMEOUT_MS);
	}
	
	public synchronized boolean isMoving() {
		return isMoving;
	}

	public synchronized boolean isOpening() {
		return isOpening;
	}
	
	// for debug purpose only
	public void joystickControl(Joystick joystick)
	{
		if (!isMoving) // if we are already doing a move we don't take over
		{
			elbow.set(ControlMode.PercentOutput, joystick.getY());
		}
	}

	public double getTarget() {
		return tac;
	}	

	public boolean getLimitSwitchState() {
		return elbow.getSensorCollection().isFwdLimitSwitchClosed();
	}

	// MAKE SURE THAT YOU ARE NOT IN A CLOSED LOOP CONTROL MODE BEFORE CALLING THIS METHOD.
	// OTHERWISE THIS IS EQUIVALENT TO MOVING TO THE DISTANCE TO THE CURRENT ZERO IN REVERSE! 
	public void resetEncoder() {
		elbow.set(ControlMode.PercentOutput,0); // we stop AND MAKE SURE WE DO NOT MOVE WHEN SETTING POSITION
		elbow.setSelectedSensorPosition(0, PRIMARY_PID_LOOP, TALON_TIMEOUT_MS); // we mark the virtual zero
	}


}
