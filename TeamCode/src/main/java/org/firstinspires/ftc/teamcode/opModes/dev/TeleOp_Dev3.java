package org.firstinspires.ftc.teamcode.opModes.dev;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.opModes.OpModeGroups;
import org.firstinspires.ftc.teamcode.season.LauncherBasic;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.RudimentaryMecanumDrive;
import org.firstinspires.ftc.teamcode.season.MagMotor;
import org.firstinspires.ftc.teamcode.season.Intake;

@TeleOp(name = "Dev 3", group = OpModeGroups.DEV)
public class TeleOp_Dev3 extends LinearOpMode {
	private RudimentaryMecanumDrive mecanumDrive;
	private LauncherBasic launcher;
	private MagMotor magMotor;
	private Intake intake;

	private static final double spinSensitivity = 1;

	@Override
	public void runOpMode() throws InterruptedException {
		// Initialize mechanisms
		mecanumDrive = new RudimentaryMecanumDrive(this);
		intake = new Intake(this);
		launcher = new LauncherBasic(this);
		magMotor = new MagMotor(this);

		// Wait for Op mode to start and cancel startup if stopped
		waitForStart();
		if(isStopRequested()) return;

		// Run main loop
		while(opModeIsActive() && !isStopRequested()) {
			// Updates the drivetrain with the game controller's current values once every loop
			mecanumDrive.drive();
			magMotor.run();

			// Activate the drivetrain's boost if the left trigger is pressed down
			if(gamepad1.left_trigger >= 0.25) mecanumDrive.setBoost(1);
			else mecanumDrive.setBoost(0.5);

			// Switch intake direction or turn it off
			if(gamepad1.left_bumper) intake.intakeIn();
			else if(gamepad1.right_bumper) intake.intakeOut();
			else if(gamepad1.triangle) intake.intakeStop();

			//Launcher Control with Gamepad2 Right Stick
			double joystickValR2Y = gamepad2.right_stick_y;
			double joystickValR2X = gamepad2.right_stick_x;

			double leftMotor = joystickValR2Y - (joystickValR2X * spinSensitivity);
			double rightMotor = joystickValR2Y + (joystickValR2X * spinSensitivity);

			leftMotor = checkLauncherPowerVals(leftMotor);
			rightMotor = checkLauncherPowerVals(rightMotor);

			launcher.setMotorsPower(-leftMotor, rightMotor);

			/* TODO: Add input handlers for controlling other hardware */

			// Get new telemetry data and push it to the driver station
			mecanumDrive.getTelemetryData();
			intake.getTelemetryData();
			launcher.getTelemetryData();
			telemetry.update();
		}
	}
	private static double checkLauncherPowerVals(double value) {
		if(value > 1.0) return 1.0;
		return Math.max(value, 0.0);
	}
}
