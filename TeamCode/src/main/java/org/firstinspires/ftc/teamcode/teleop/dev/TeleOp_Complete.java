package org.firstinspires.ftc.teamcode.teleop.dev;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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
	}

	@Override
	public void start() {
		follower.startTeleOpDrive();
		tagProcessor.startLimelight();
	}

	@Override
	public void loop() {
		follower.update();

		// Provide new input values to pedro pathing for TeleOp driving
		follower.setTeleOpDrive(
			-gamepad1.left_stick_y * multiplier,
			-gamepad1.left_stick_x * multiplier,
			-gamepad1.right_stick_x * multiplier,
			true
		);

		// Activate the drivetrain's boost if the left trigger is pressed down
		if(gamepad1.left_trigger >= 0.25) multiplier = BOOST_MULTIPLIER; // TODO: Determine if setting this based on how far the trigger is pressed would work better
		else multiplier = BASE_MULTIPLIER;

		/* TODO: Add input handlers for controlling other hardware */

		// Switch intake direction or turn it off
		if(gamepad1.left_bumper) intake.intakeIn();
		else if(gamepad1.right_bumper) intake.intakeOut();
		else if(gamepad1.y) intake.intakeStop();

		// Activate the launcher if the right trigger is pressed down
		if(gamepad1.right_trigger >= 0.25) launcher.activateMotors(PowerConstants.LAUNCH_POWER, PowerConstants.LAUNCH_POWER); //Temporary
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
