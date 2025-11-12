package org.firstinspires.ftc.teamcode.teleop;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class TeleOp_PedroPathingBasic extends OpMode {
	private Follower follower;
	private double multiplier = 0.5;

	@Override
	public void init() {
		follower = Constants.createFollower(hardwareMap);
		follower.update();
	}

	@Override
	public void start() {
		follower.startTeleOpDrive();
	}

	@Override
	public void loop() {
		follower.update();

		follower.setTeleOpDrive(
			-gamepad1.left_stick_y * multiplier,
			-gamepad1.left_stick_x * multiplier,
			-gamepad1.right_stick_x * multiplier,
			true // Robot centric
		);

		// Activate the drivetrain's boost if the left trigger is pressed down
		if(gamepad1.left_trigger >= 0.25) multiplier = 1.0;
		else multiplier = 0.5;

		/* TODO: Add input handlers for controlling other hardware */
	}
}
