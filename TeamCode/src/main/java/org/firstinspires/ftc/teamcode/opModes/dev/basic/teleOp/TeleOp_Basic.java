package org.firstinspires.ftc.teamcode.opModes.dev.basic.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.opModes.OpModeGroups;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.RudimentaryMecanumDrive;

@TeleOp(name = "Basic Mecanum", group = OpModeGroups.BASIC)
public class TeleOp_Basic extends LinearOpMode {
	private RudimentaryMecanumDrive mecanumDrive;

	@Override
	public void runOpMode() throws InterruptedException {
		// Initialize mechanisms
		mecanumDrive = new RudimentaryMecanumDrive(this);

		// Wait for Op mode to start and cancel startup if stopped
		waitForStart();
		if(isStopRequested()) return;

		// Run main loop
		while(opModeIsActive() && !isStopRequested()) {
			// Updates the drivetrain with the game controller's current values once every loop
			mecanumDrive.drive();

			// Activate the drivetrain's boost if the left trigger is pressed down
			if(gamepad1.left_trigger >= 0.25) mecanumDrive.setBoost(1);
			else mecanumDrive.setBoost(0.5);

			// Get new telemetry data and push it to the driver station
			mecanumDrive.getTelemetryData();
			telemetry.update();
		}
	}
}
