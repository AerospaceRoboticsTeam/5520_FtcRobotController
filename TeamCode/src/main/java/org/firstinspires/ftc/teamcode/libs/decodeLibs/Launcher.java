package org.firstinspires.ftc.teamcode.libs.decodeLibs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.apriltags.TagProcessor;
import org.firstinspires.ftc.teamcode.constants.ArtifactNumRef;

/** Controller for the robot's launcher. */
public class Launcher {
	private final LinearOpMode bot;
	private final TagProcessor tagProcessor;
	private final DcMotorEx leftLauncherMotor;
	private final DcMotorEx rightLauncherMotor;

	private LauncherStatus state;
	private double leftLauncherMotorPower = 0;
	private double rightLauncherMotorPower = 0;
	private final ArtifactNumRef artifactNum = new ArtifactNumRef(0);

	/** The angle of the launcher from the ground in degrees. */
	private final double GRAVITATIONAL_ACCELERATION = 9.81;
	private final double LAUNCHER_ANGLE = 45;

	private final double TICKS_PER_REV_6000 = 537.6;

	public Launcher(LinearOpMode opMode, TagProcessor tagProcessor) {
		bot = opMode;
		this.tagProcessor = tagProcessor;

		leftLauncherMotor = bot.hardwareMap.get(DcMotorEx.class, "launcherMotorLeft");
		leftLauncherMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);//Switched from BRAKE to FLOAT because braking isn't necessary
		rightLauncherMotor = bot.hardwareMap.get(DcMotorEx.class, "launcherMotorRight");
		rightLauncherMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

		leftLauncherMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
		rightLauncherMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

		state = LauncherStatus.IDLE;
	}

	public void update() {
		switch(state) {
			case START_UP:
			case IDLE: {
				final Pose3D cameraPos = tagProcessor.getVectorsToGoalTag();
				final double[] linearVelocities = calculateLinearVelocities(cameraPos);
				final double[] motorPowers = linearVelocityToPower(linearVelocities[0], linearVelocities[1]);
				activateMotors(motorPowers[0], motorPowers[1]);

				break;
			}
		}
	}

	public double[] calculateLinearVelocities(Pose3D cameraPos) {
		if(state == LauncherStatus.IDLE || state == LauncherStatus.START_UP) return null;

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
		final double launcherPitch = Math.toRadians(LAUNCHER_ANGLE);
		final double launcherYaw = Math.atan(deltaZ / deltaX);

		// Calculate the numerator and denominator of the linear velocity
		final double numerator = 0.5 * GRAVITATIONAL_ACCELERATION * Math.pow(deltaX, 2);
		final double denominator =
			Math.pow(Math.cos(launcherYaw), 2) * Math.pow(Math.cos(launcherPitch), 2) *
			(goalPosY - launchPosY) + deltaX * (Math.tan(launcherPitch) / Math.cos(launcherYaw));

		// Calculate the linear velocity
		final double linearVelocity = Math.sqrt(numerator / denominator);

		return new double[]{linearVelocity / 2, linearVelocity / 2};
	}

	/**
	 * Calculate launcher motor power values from linear velocities.
	 * @return A double array with 2 elements:
	 * <ol>
	 *   <li>The calculated left motor power value.</li>
	 *   <li>the calculated right motor power value.</li>
	 * </ol>
	 */
	public double[] linearVelocityToPower(double leftMotorVelocity, double rightMotorVelocity) {
		final double leftRPS = leftMotorVelocity / LauncherConstants.GECKO_WHEEL_CIRCUMFERENCE;
		final double rightRPS = rightMotorVelocity / LauncherConstants.GECKO_WHEEL_CIRCUMFERENCE;

		final double leftPower = leftRPS / LauncherConstants.MAX_RPS;//I am not sure if this is accurate
		final double rightPower = rightRPS / LauncherConstants.MAX_RPS;

		return new double[]{leftPower, rightPower};
	}

	//New method to set motor velocity based on encoder data
	public void setMotorVelocity(double leftMotorVelocityLinear, double rightMotorVelocityLinear){
		final double leftRPS = leftMotorVelocityLinear / LauncherConstants.GECKO_WHEEL_CIRCUMFERENCE;
		final double rightRPS = rightMotorVelocityLinear / LauncherConstants.GECKO_WHEEL_CIRCUMFERENCE;

		double targetTicksPerRevL = leftRPS*TICKS_PER_REV_6000;
		double targetTicksPerRevR = rightRPS*TICKS_PER_REV_6000;

		leftLauncherMotor.setVelocity(targetTicksPerRevL);
		rightLauncherMotor.setVelocity(targetTicksPerRevR);
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
