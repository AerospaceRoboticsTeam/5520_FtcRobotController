package org.firstinspires.ftc.teamcode.teleop.dev;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.apriltags.TagProcessor;
import org.firstinspires.ftc.teamcode.constants.OpModeGroups;
import org.firstinspires.ftc.teamcode.constants.PowerConstants;
import org.firstinspires.ftc.teamcode.constants.Team;
import org.firstinspires.ftc.teamcode.libs.decodeLibs.Intake;
import org.firstinspires.ftc.teamcode.libs.decodeLibs.Launcher;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Configurable
@TeleOp(name = "PedroPathing With Complete Functionality", group = OpModeGroups.pedroPathing)
public class TeleOp_Complete extends OpMode {
	/** A manager for telemetry data inside of Panels. */
	private TelemetryManager telemetryManager;
	/** The PedroPathing object that allows movement of the robot via PedroPathing methods. */
	private Follower follower;
	private static final double BASE_MULTIPLIER = 0.5;
	private static final double BOOST_MULTIPLIER = 1.0;
	/**
	 * A multiplier to control the sensitivity of inputs to the drive train.
	 * Originally known as boost.
	 */
	private static double multiplier = BASE_MULTIPLIER;
	/** Determines how the robot moves. */
	private ControlState state;
	private boolean automatedDrive;
	private Pose previousRotationPos;

	private TagProcessor tagProcessor;
	private Intake intake;
	private Launcher launcher;

	@Override
	public void init() {
		tagProcessor = new TagProcessor(this, gamepad1.a ? Team.RED : Team.BLUE);
		intake = new Intake(this);
		launcher = new Launcher(this, tagProcessor);

		telemetryManager = null; // TODO: Initialize telemetry manager for panels
		follower = Constants.createFollower(hardwareMap);
		follower.update();

		state = ControlState.MANUAL;
		automatedDrive = false;
		previousRotationPos = null;
	}

	@Override
	public void start() {
		follower.startTeleOpDrive(true);
		tagProcessor.startLimelight();
	}

	@Override
	public void loop() {
		follower.update();

		switch(state) {
			case MANUAL: {
				// Switch PedroPathing to TeleOp mode
				if(automatedDrive) {
					automatedDrive = false;
					follower.startTeleOpDrive(true);
				}

				// Provide new input values to pedro pathing for TeleOp driving
				follower.setTeleOpDrive(
					-gamepad1.left_stick_y * multiplier,
					-gamepad1.left_stick_x * multiplier,
					-gamepad1.right_stick_x * multiplier,
					true
				);
				break;
			}

			case MAINTAIN_ANGLE_WITH_GOAL: {
				// Switch PedroPathing to path following mode
				if(!automatedDrive) {
					automatedDrive = true;
					follower.startTeleOpDrive();
				}

				// Get position data
				Pose3D cameraVectorsToGoal = tagProcessor.getVectorsToGoalTag();
				Pose3D vectorsToGoal = cameraVectorsToGoal != null ? cameraVectorsToGoal : null; // TODO: Use ODO to get vectors to goal
				YawPitchRollAngles orientation = vectorsToGoal.getOrientation();

				// Don't move the robot if it's already facing the goal
				if(orientation.getYaw() >= -0.1 && orientation.getYaw() <= 0.1) break;

				if(follower.isBusy() && follower.getCurrentPath() != null) {
					// If the robot is in the same position, do nothing
					if(
						follower.getPose().getX() >= previousRotationPos.getX() - 0.1 &&
						follower.getPose().getX() <= previousRotationPos.getX() + 0.1 &&
						follower.getPose().getY() >= previousRotationPos.getY() - 0.1 &&
						follower.getPose().getY() <= previousRotationPos.getY() + 0.1
					) break;

					// Stop the follower if the robot is no longer in the same position to recalculate heading
					follower.breakFollowing();
				}

				Pose currentPos = follower.getPose();
				previousRotationPos = currentPos;
				double targetHeading = currentPos.getHeading() - Math.toRadians(orientation.getYaw());
				Pose targetPos = new Pose(currentPos.getX(), currentPos.getY(), targetHeading);

				PathChain turnPath = follower.pathBuilder()
					.addPath(new BezierLine(currentPos, targetPos))
					.setLinearHeadingInterpolation(currentPos.getHeading(), targetHeading)
					.build();

				follower.followPath(turnPath);
				break;
			}
		}

		// Activate the drivetrain's boost if the left trigger is pressed down
		if (gamepad1.left_trigger >= 0.25)
			multiplier = BOOST_MULTIPLIER; // TODO: Determine if setting this based on how far the trigger is pressed would work better
		else multiplier = BASE_MULTIPLIER;

		// Switch between the MANUAL and MAINTAIN_ANGLE_WITH_GOAL states
		if(gamepad1.a) state = state == ControlState.MANUAL ? ControlState.MAINTAIN_ANGLE_WITH_GOAL : ControlState.MANUAL;

		/* TODO: Add input handlers for controlling other hardware */

		// Switch intake direction or turn it off
		if (gamepad1.left_bumper) intake.intakeIn();
		else if (gamepad1.right_bumper) intake.intakeOut();
		else if (gamepad1.y) intake.intakeStop();

		// Activate the launcher if the right trigger is pressed down
		if (gamepad1.right_trigger >= 0.25)
			launcher.activateMotors(PowerConstants.LAUNCH_POWER, PowerConstants.LAUNCH_POWER); //Temporary
		else launcher.stopMotors();

		getTelemetryData();
		intake.getTelemetryData();
		launcher.getTelemetryData();
		telemetry.update();
	}

	private void getTelemetryData() {
		telemetry.addData("Bot X pos", "%.2f", follower.getPose().getX());
		telemetry.addData("Bot Y pos", "%.2f", follower.getPose().getY());
		telemetry.addData(
			"Bot heading", "%.2f",
			Math.toDegrees(follower.getPose().getPose().getHeading())
		);
	}
}

enum ControlState {
	MANUAL,
	MAINTAIN_ANGLE_WITH_GOAL
}
