package org.firstinspires.ftc.teamcode.libs.decodeLibs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.apriltags.TagProcessor;
import org.firstinspires.ftc.teamcode.constants.ArtifactNumRef;

/** Controller for the robot's launcher. */
public class Launcher {
	private final LinearOpMode bot;
	private final TagProcessor tagProcessor;
	private final DcMotor leftLauncherMotor;
	private final DcMotor rightLauncherMotor;

	private LauncherStatus state;
	private double leftLauncherMotorPower = 0;
	private double rightLauncherMotorPower = 0;
	private final ArtifactNumRef artifactNum = new ArtifactNumRef(0);

	/** The angle of the launcher from the ground in degrees. */
	private final double GRAVITATIONAL_ACCELERATION = 9.81;
	private final double LAUNCHER_ANGLE = 45;

	public Launcher(LinearOpMode opMode, TagProcessor tagProcessor) {
		bot = opMode;
		this.tagProcessor = tagProcessor;

		leftLauncherMotor = bot.hardwareMap.get(DcMotor.class, "launcherMotorLeft");
		leftLauncherMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		rightLauncherMotor = bot.hardwareMap.get(DcMotor.class, "launcherMotorRight");
		rightLauncherMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		state = LauncherStatus.IDLE;
	}

	public void update() {
		switch(state) {
			case IDLE: {
				final Pose3D cameraPos = tagProcessor.getVectorsToGoalTag();
				calculateLinearVelocities(cameraPos);

				break;
			}
		}
	}

	public void calculateLinearVelocities(Pose3D cameraPos) {
		final Position cameraPoint = cameraPos.getPosition();
		cameraPoint.toUnit(DistanceUnit.METER);

		// Goal point
		final double goalPosX = 0.0;
		final double goalPosY = 0.0;
		final double goalPosZ = 0.0;

		// Launch point
		final double launchPosX = cameraPoint.x;
		final double launchPosY = cameraPoint.y;
		final double launchPosZ = cameraPoint.z;

		// Calculate the distances on the X and Z axes
		final double deltaX = goalPosX - launchPosX;
		final double deltaZ = launchPosZ - goalPosZ;

		// Calculate the launcher angles
		final double launcherYaw = 0;

		// Get the numerator and denominator
		final double numerator = 0.5 * GRAVITATIONAL_ACCELERATION * Math.pow(deltaX, 2);
		final double denominator = Math.cos(launcherYaw);
	}

	/**
	 * Calculate launcher motor power values from linear velocities.
	 * @return A double array with 2 elements:
	 * <ol>
	 *   <li>The calculated left motor power value.</li>
	 *   <li>the calculated right motor power value.</li>
	 * </ol>
	 */
	public double[] linearVelocityToPower(float leftMotorVelocity, float rightMotorVelocity) {
		final float leftRPS = leftMotorVelocity / LauncherConstants.GECKO_WHEEL_CIRCUMFERENCE;
		final float rightRPS = rightMotorVelocity / LauncherConstants.GECKO_WHEEL_CIRCUMFERENCE;

		final float leftPower = leftRPS / LauncherConstants.MAX_RPS;
		final float rightPower = rightRPS / LauncherConstants.MAX_RPS;

		return new double[]{leftPower, rightPower};
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

	public void getTelemetryData() {
		bot.telemetry.addData("Launcher status: ", state);
		bot.telemetry.addData("Left Launcher Motor Power", leftLauncherMotorPower);
		bot.telemetry.addData("Right Launcher Motor Power", rightLauncherMotorPower);
	}
}

enum LauncherStatus {
	IDLE,
	LAUNCHING,
	START_UP,
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
