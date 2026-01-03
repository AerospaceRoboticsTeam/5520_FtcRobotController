package org.firstinspires.ftc.teamcode.teleop.dev;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.apriltags.TagProcessor;
import org.firstinspires.ftc.teamcode.constants.ArtifactNumRef;
import org.firstinspires.ftc.teamcode.constants.OpModeGroups;
import org.firstinspires.ftc.teamcode.constants.PowerConstants;
import org.firstinspires.ftc.teamcode.constants.Team;
import org.firstinspires.ftc.teamcode.libs.decodeLibs.Intake;
import org.firstinspires.ftc.teamcode.libs.decodeLibs.Launcher;
import org.firstinspires.ftc.teamcode.libs.decodeLibs.LauncherBasic;
import org.firstinspires.ftc.teamcode.mecanumdrive.MecanumDrive;
import org.firstinspires.ftc.teamcode.libs.decodeLibs.MagMotor;
import org.firstinspires.ftc.teamcode.libs.decodeLibs.Intake2;

@TeleOp(name = "Dev TeleOp 3", group = OpModeGroups.dev)
public class TeleOp_Dev3 extends LinearOpMode {
	private MecanumDrive mecanumDrive;
	//private TagProcessor tagProcessor;
	private LauncherBasic launcher;

	private MagMotor magMotor;

	private Intake2 intake;

	private static double launchPower = 0.0;
	private static double launchPowerBalance = 0.0;

	private static final double spinSensitivity = 1;

	private final ArtifactNumRef artifactNum = new ArtifactNumRef(0);

	@SuppressLint("SuspiciousIndentation")
	@Override
	public void runOpMode() throws InterruptedException {
		// Initialize mechanisms
		mecanumDrive = new MecanumDrive(this);
		//tagProcessor = new TagProcessor(this, gamepad1.a ? Team.RED : Team.BLUE);
		intake = new Intake2(this);
		launcher = new LauncherBasic(this);
		magMotor = new MagMotor(this);

		// Wait for Op mode to start and cancel startup if stopped
		waitForStart();
		if(isStopRequested()) return;

		// Run main loop
		while(opModeIsActive() && !isStopRequested()) {
			// Updates the drivetrain with the game controller's current values once every loop
			mecanumDrive.drive();
			magMotor.go();

			// Activate the drivetrain's boost if the left trigger is pressed down
			if ((gamepad1.left_trigger >= 0.25)) mecanumDrive.setBoost(1);
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

			launcher.activateMotors(-leftMotor, rightMotor);

			/* TODO: Add input handlers for controlling other hardware */

			// Get new telemetry data and push it to the driver station
			mecanumDrive.getTelemetryData();
			intake.getTelemetryData();
			launcher.getTelemetryData();
			telemetry.update();
		}
	}
	private static double checkLauncherPowerVals(double value) {
		if (value > 1.0) return 1.0;
		if (value < 0.0) return 0.0;
		return value;
	}
}
