package org.firstinspires.ftc.teamcode.libs.decodeLibs;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.apriltags.TagProcessor;
import org.firstinspires.ftc.teamcode.constants.ArtifactNumRef;

/** Controller for the robot's launcher. */
public class LauncherBasic {
	private final OpMode bot;
	private final DcMotorEx leftLauncherMotor;
	private final DcMotorEx rightLauncherMotor;

	private LauncherStatus state;
	private double leftLauncherMotorPower = 0;
	private double rightLauncherMotorPower = 0;
	private final ArtifactNumRef artifactNum = new ArtifactNumRef(0);

	/** The angle of the launcher from the ground in degrees. */
	private final double GRAVITATIONAL_ACCELERATION = 9.81;
	private final double LAUNCHER_ANGLE = 45;

	public LauncherBasic(OpMode opMode) {
		bot = opMode;

		leftLauncherMotor = bot.hardwareMap.get(DcMotorEx.class, "launcherMotorLeft");
		rightLauncherMotor = bot.hardwareMap.get(DcMotorEx.class, "launcherMotorRight");

		leftLauncherMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
		rightLauncherMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

		leftLauncherMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		rightLauncherMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

		leftLauncherMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
		rightLauncherMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

		state = LauncherStatus.IDLE;
	}

	/**
	 * Calculate launcher motor power values from linear velocities.
	 * @return A double array with 2 elements:
	 * <ol>
	 *   <li>The calculated left motor power value.</li>
	 *   <li>the calculated right motor power value.</li>
	 * </ol>
	 */

	/** Set the motor's velocity using angular velocity rather than power. */
	public void setMotorVelocity(double leftMotorVelocityLinear, double rightMotorVelocityLinear){
		final double leftAngularVelocity = leftMotorVelocityLinear / LauncherConstants.GECKO_WHEEL_RADIUS;
		final double rightAngularVelocity = rightMotorVelocityLinear / LauncherConstants.GECKO_WHEEL_RADIUS;

		leftLauncherMotor.setVelocity(leftAngularVelocity, AngleUnit.RADIANS);
		rightLauncherMotor.setVelocity(rightAngularVelocity, AngleUnit.RADIANS);
	}

	public void activateMotors(double leftMotorPower, double rightMotorPower) {
		if(state == LauncherStatus.IDLE || state == LauncherStatus.START_UP) {
			leftLauncherMotorPower = leftMotorPower;
			rightLauncherMotorPower = rightMotorPower;

			leftLauncherMotor.setPower(leftMotorPower);
			rightLauncherMotor.setPower(rightMotorPower);

			state = LauncherStatus.START_UP;
		}
	}

	public void stopMotors() {
		rightLauncherMotor.setPower(0);
		leftLauncherMotor.setPower(0);
		state = LauncherStatus.IDLE;
	}

	public void launch() {
		if(
			state == LauncherStatus.START_UP &&
				leftLauncherMotor.getPower() > leftLauncherMotorPower - 0.1 &&
				leftLauncherMotor.getPower() < leftLauncherMotorPower + 0.1 &&
				rightLauncherMotor.getPower() > rightLauncherMotorPower - 0.1 &&
				rightLauncherMotor.getPower() < rightLauncherMotorPower + 0.1
		) {
			state = LauncherStatus.LAUNCHING;

			// TODO: ADD BOOT-KICKER CODE HERE
		}
	}

	public void getTelemetryData() {
		//bot.telemetry.addData("Launcher status: ", state);
		bot.telemetry.addData("Left Launcher Motor Power: ", leftLauncherMotorPower);
		bot.telemetry.addData("Right Launcher Motor Power: ", rightLauncherMotorPower);
	}
}


