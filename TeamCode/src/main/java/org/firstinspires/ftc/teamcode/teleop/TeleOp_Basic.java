package org.firstinspires.ftc.teamcode.teleop;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mecanumdrive.MecanumDrive;

@TeleOp(name = "Basic TeleOp", group = "TeleOp")
public class TeleOp_Basic extends LinearOpMode {
	private MecanumDrive mecanumDrive;

	@SuppressLint("SuspiciousIndentation")
	@Override
	public void runOpMode() throws InterruptedException {
		// Initialize mechanisms
		mecanumDrive = new MecanumDrive(this);

		// Wait for Op mode to start and cancel startup if stopped
		waitForStart();
		if(isStopRequested()) return;

		// Run main loop
		while(opModeIsActive() && !isStopRequested()) {
			// Updates the drivetrain with the game controller's current values once every loop
			mecanumDrive.drive();

			// Activate the drivetrain's boost if the left trigger is pressed down
			if ((gamepad1.left_trigger >= 0.25)) mecanumDrive.setBoost(1);
			else mecanumDrive.setBoost(0.5);

			// Get new telemetry data and push it to the driver station
			mecanumDrive.getTelemetryData();
			telemetry.update();
		}
	}
}
