package org.firstinspires.ftc.teamcode.auto.dev;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.constants.OpModeGroups;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Configurable
@TeleOp(name = "PedroPathing With Complete Functionality", group = OpModeGroups.pedroPathing)
public class Auto_Park extends OpMode {
	/** The PedroPathing object that allows movement of the robot via PedroPathing methods. */
	private Follower follower;
	private static final double BASE_MULTIPLIER = 0.5;
	private static final double BOOST_MULTIPLIER = 1.0;
	/**
	 * A multiplier to control the sensitivity of inputs to the drive train.
	 * Originally known as boost.
	 */
	private static double multiplier = BASE_MULTIPLIER;

	@Override
	public void init() {
		follower = Constants.createFollower(hardwareMap);
		follower.update();
	}

	@Override
	public void start() {
		follower.startTeleOpDrive(true);
	}

	@Override
	public void loop() {
		follower.update();

		// Provide new input values to pedro pathing for TeleOp driving
		follower.setTeleOpDrive(
			-gamepad1.left_stick_y * multiplier,
			-gamepad1.left_stick_x * multiplier,
			-gamepad1.right_stick_x * multiplier,
			false
		);

		// Activate the drivetrain's boost if the left trigger is pressed down
		if (gamepad1.left_trigger >= 0.25)
			multiplier = BOOST_MULTIPLIER; // TODO: Determine if setting this based on how far the trigger is pressed would work better
		else multiplier = BASE_MULTIPLIER;

		getTelemetryData();
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
