package org.firstinspires.ftc.teamcode.teleop;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mecanumdrive.MecanumDrive;

@TeleOp(name = "Basic Chassis TeleOp", group = "TeleOp")
public class TeleOp_Basic_Chassis extends LinearOpMode {
	private MecanumDrive mecanumDrive;
	//private Intake intake;
	//private Launcher launcher;

	//private final ArtifactNumRef artifactNum = new ArtifactNumRef(0);

	@SuppressLint("SuspiciousIndentation")
	@Override
	public void runOpMode() throws InterruptedException {
		// Initialize mechanisms
		mecanumDrive = new MecanumDrive(this);
		//intake = new Intake(this);
		//launcher = new Launcher(this);

		// Wait for Op mode to start and cancel startup if stopped
		if(isStopRequested()) return;

		// Run main loop
		while(opModeIsActive() && !isStopRequested()) {
			// Updates the drivetrain with the game controller's current values once every loop
			mecanumDrive.drive();

			// Activate the drivetrain's boost if the left trigger is pressed down
			if ((gamepad1.left_trigger >= 0.25)) mecanumDrive.setBoost(1);
			else mecanumDrive.setBoost(0.5);

			// Switch intake direction or turn it off
			/*
			if(gamepad1.left_bumper) intake.intakeOut();
			else if(gamepad1.right_bumper) intake.intakeOut();
			else if(gamepad1.y) intake.intakeStop();

			// Activate the launcher if the right trigger is pressed down
			if(gamepad1.right_trigger >= 0.25) launcher.launch(PowerConstants.LAUNCH_POWER, PowerConstants.LAUNCH_POWER); //Temporary
			else launcher.stopLaunch();
			*/
			/* TODO: Add input handlers for controlling other hardware */

			// Get new telemetry data and push it to the driver station
			mecanumDrive.getTelemetryData();
			//launcher.getTelemetryData();
			telemetry.update();
		}
	}
}
