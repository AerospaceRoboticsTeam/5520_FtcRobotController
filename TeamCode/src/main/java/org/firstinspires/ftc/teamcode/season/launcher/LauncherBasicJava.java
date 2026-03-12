package org.firstinspires.ftc.teamcode.season.launcher;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.season.LightController;
import org.firstinspires.ftc.teamcode.season.LightMode;
import org.firstinspires.ftc.teamcode.utils.components.Subsystem;

@Configurable
public class LauncherBasicJava implements Subsystem {
	private static final double SPIN_SENSITIVITY = 0.2;
	private static final double SHORT_DISTANCE_POWER = 0.325;
	private static final double LONG_DISTANCE_POWER = 0.425;

	private final OpMode opMode;
	private final LightController lightController;

	private final DcMotorEx leftMotor;
	private final DcMotorEx rightMotor;
	private final Gamepad gamepad;
	private boolean leftTriggerWasPressed = false;

	private double basePowerValue = SHORT_DISTANCE_POWER;
	private double leftMotorPower = 0.0;
	private double rightMotorPower = 0.0;
	private boolean leftMotorActive = false;
	private boolean rightMotorActive = false;
	private LauncherBasicJavaStatus launcherStatus = LauncherBasicJavaStatus.SHORT;

	public LauncherBasicJava(OpMode opMode, LightController lightController) {
		this.opMode = opMode;
		this.lightController = lightController;

		leftMotor = opMode.hardwareMap.get(
			DcMotorEx.class,
			"leftLauncherMotor"
		);
		rightMotor = opMode.hardwareMap.get(
			DcMotorEx.class,
			"rightLauncherMotor"
		);
		gamepad = opMode.gamepad2;

		for(DcMotorEx motor : new DcMotorEx[]{leftMotor, rightMotor}) {
			motor.setZeroPowerBehavior(ZeroPowerBehavior.FLOAT);
			motor.setMode(RunMode.STOP_AND_RESET_ENCODER);
			motor.setMode(RunMode.RUN_USING_ENCODER);
		}

		leftMotor.setDirection(Direction.REVERSE);
		rightMotor.setDirection(Direction.FORWARD);
		lightController.setMode(LightMode.ORANGE);
	}

	@Override
	public void update() {
		// Toggle between SHORT and LONG distance powers
		if(gamepad.squareWasPressed()) {
			switch(launcherStatus) {
				case SHORT: {
					lightController.setMode(LightMode.AQUA);
					basePowerValue = LONG_DISTANCE_POWER;
					launcherStatus = LauncherBasicJavaStatus.LONG;
					break;
				}
				case LONG: {
					lightController.setMode(LightMode.ORANGE);
					basePowerValue = SHORT_DISTANCE_POWER;
					launcherStatus = LauncherBasicJavaStatus.SHORT;
				}
			}
		}

		if(!leftTriggerWasPressed && gamepad.left_trigger >= 0.1) {
			leftTriggerWasPressed = true;
			leftMotorActive = !leftMotorActive;
			rightMotorActive = !rightMotorActive;
		}
		else leftTriggerWasPressed = false;

		double leftPower;
		double rightPower;

		if(!leftMotorActive) leftPower = 0.0;
		else if(!gamepad.triangle) leftPower = clampPowerVal(
			basePowerValue + gamepad.left_stick_y * (1.0 - basePowerValue) * SPIN_SENSITIVITY
		);
		else leftPower = -0.25;

		if(!rightMotorActive) rightPower = 0.0;
		else if(!gamepad.triangle) rightPower = clampPowerVal(
			basePowerValue + gamepad.right_stick_y * (1.0 - basePowerValue) * SPIN_SENSITIVITY
		);
		else rightPower = -0.25;

		setPower(leftPower, rightPower);
	}

	public void setPower(Double leftPower, Double rightPower) {
		leftMotorPower = leftPower;
		rightMotorPower = rightPower;

		leftMotor.setPower(leftPower);
		rightMotor.setPower(rightPower);
	}

	public void stop() {
		setPower(0.0, 0.0);
	}

	public void launch() {
		setPower(0.5, 0.5);
	}

	private Double clampPowerVal(Double power) {
		if(power > 1.0) return 1.0;
		return Math.max(power, 0.0);
	}

	@Override
	public void getTelemetryData() {
		opMode.telemetry.addData(
			"Left Launcher Motor Power",
			leftMotorPower
		);
		opMode.telemetry.addData(
			"Right Launcher Motor Power",
			rightMotorPower
		);
		opMode.telemetry.addData("Left Launcher Motor Is Active", leftMotorActive);
		opMode.telemetry.addData("Right Launcher Motor Is Active", rightMotorActive);
		opMode.telemetry.addData("Launcher Status", launcherStatus);
	}
}

enum LauncherBasicJavaStatus {
	SHORT,
	LONG
}
