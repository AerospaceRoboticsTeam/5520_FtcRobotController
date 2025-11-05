package org.firstinspires.ftc.teamcode.libs.decodeLibs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.constants.ArtifactNumRef;

/** Controller for the robot's launcher. */
public class Launcher {
	private final LinearOpMode bot;
	private final DcMotor launcherMotorLeft;
	private final DcMotor launcherMotorRight;

	private LauncherStatus state;
	private final ArtifactNumRef artifactNum = new ArtifactNumRef(0);

	public Launcher(LinearOpMode opMode) {
		bot = opMode;
		launcherMotorLeft = bot.hardwareMap.get(DcMotor.class, "launcherMotorLeft");
		launcherMotorLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		launcherMotorRight = bot.hardwareMap.get(DcMotor.class, "launcherMotorRight");
		launcherMotorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		state = LauncherStatus.IDLE;
	}

	public void launch(double motorPowerLeft, double motorPowerRight) {
		launcherMotorLeft.setPower(motorPowerLeft);
		launcherMotorRight.setPower(motorPowerRight);
		state = LauncherStatus.LAUNCHING;
	}

	public void stopLaunch() {
		launcherMotorRight.setPower(0);
		launcherMotorLeft.setPower(0);
		state = LauncherStatus.IDLE;
	}

	public double[] linearVelocityToPower(float leftMotorVelocity, float rightMotorVelocity) {
		final float leftRPS = leftMotorVelocity / LauncherConstants.GECKO_WHEEL_CIRCUMFERENCE;
		final float rightRPS = rightMotorVelocity / LauncherConstants.GECKO_WHEEL_CIRCUMFERENCE;

		final float leftPower = leftRPS / LauncherConstants.MAX_RPS;
		final float rightPower = rightRPS / LauncherConstants.MAX_RPS;

		return new double[]{leftPower, rightPower};
	}

	public void getTelemetryData() {
		bot.telemetry.addData("Launcher status: ", state);
	}
}

enum LauncherStatus {
	IDLE,
	LAUNCHING,
	READY
}

final class LauncherConstants {
	private LauncherConstants() {}

	/** The radius of the Gecko wheels used in the launcher in meters. */
	public static final float GECKO_WHEEL_RADIUS = (96f / 2f) / 1000f;
	/** The circumference of the Gecko wheels used in the launcher in meters. */
	public static final float GECKO_WHEEL_CIRCUMFERENCE = 2 * (float) Math.PI * GECKO_WHEEL_RADIUS;
	/** The max <u>Revolutions Per Second</u> of the launcher motors. */
	public static final float MAX_RPS = 6000f / 60f;
	/** The maximum possible linear velocity of the launcher. */
	public static final float MAX_LINEAR_VELOCITY = GECKO_WHEEL_CIRCUMFERENCE * MAX_RPS;
}
