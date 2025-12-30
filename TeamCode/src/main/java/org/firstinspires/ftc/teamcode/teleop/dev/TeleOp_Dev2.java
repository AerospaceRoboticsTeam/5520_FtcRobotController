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

@TeleOp(name = "Dev TeleOp 2", group = OpModeGroups.dev)
public class TeleOp_Dev2 extends LinearOpMode {
	private MecanumDrive mecanumDrive;
	//private TagProcessor tagProcessor;
	private Intake intake;
	private LauncherBasic launcher;

	private MagMotor magMotor;

	private static double launchPower = 0.0;
	private static double launchPowerBalance = 0.0;

	private final ArtifactNumRef artifactNum = new ArtifactNumRef(0);

	@SuppressLint("SuspiciousIndentation")
	@Override
	public void runOpMode() throws InterruptedException {
		// Initialize mechanisms
		mecanumDrive = new MecanumDrive(this);
		//tagProcessor = new TagProcessor(this, gamepad1.a ? Team.RED : Team.BLUE);
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
			magMotor.go();

			// Activate the drivetrain's boost if the left trigger is pressed down
			if ((gamepad1.left_trigger >= 0.25)) mecanumDrive.setBoost(1);
			else mecanumDrive.setBoost(0.5);

			// Switch intake direction or turn it off
			if(gamepad1.left_bumper) intake.intakeIn();
			else if(gamepad1.right_bumper) intake.intakeOut();
			else if(gamepad1.triangle) intake.intakeStop();

			if (gamepad1.dpad_up) {
				launchPower += 0.1;
			} else if (gamepad1.dpad_down) {
				launchPower -= 0.1;
			}

			if (gamepad1.dpad_right) {
				launchPowerBalance += 0.1;
			} else if (gamepad1.dpad_left) {
				launchPowerBalance -= 0.1;
			}

			if (gamepad1.right_trigger >= 0.25) {
				launcher.activateMotors(launchPower + launchPowerBalance, launchPower - launchPowerBalance);
			} else if (gamepad1.left_trigger >= 0.25) {
				launcher.stopMotors();
			}

			/* TODO: Add input handlers for controlling other hardware */

			// Get new telemetry data and push it to the driver station
			mecanumDrive.getTelemetryData();
			intake.getTelemetryData();
			launcher.getTelemetryData();
			telemetry.update();
		}
	}
}
