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
import org.firstinspires.ftc.teamcode.mecanumdrive.MecanumDrive;

@TeleOp(name = "Dev TeleOp", group = OpModeGroups.TeleOp.dev)
public class TeleOp_Dev extends LinearOpMode {
  private MecanumDrive mecanumDrive;
	private TagProcessor tagProcessor;
	private Intake intake;
	private Launcher launcher;

  private final ArtifactNumRef artifactNum = new ArtifactNumRef(0);

	@SuppressLint("SuspiciousIndentation")
	@Override
  public void runOpMode() throws InterruptedException {
    // Initialize mechanisms
    mecanumDrive = new MecanumDrive(this);
		tagProcessor = new TagProcessor(this, gamepad1.a ? Team.RED : Team.BLUE);
		intake = new Intake(this);
		launcher = new Launcher(this, tagProcessor);

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

			// Switch intake direction or turn it off
			if(gamepad1.left_bumper) intake.intakeIn();
			else if(gamepad1.right_bumper) intake.intakeOut();
			else if(gamepad1.y) intake.intakeStop();

			// Activate the launcher if the right trigger is pressed down
			if(gamepad1.right_trigger >= 0.25) launcher.activateMotors(PowerConstants.LAUNCH_POWER, PowerConstants.LAUNCH_POWER); //Temporary
			else launcher.stopMotors();

      /* TODO: Add input handlers for controlling other hardware */

			// Get new telemetry data and push it to the driver station
			mecanumDrive.getTelemetryData();
			intake.getTelemetryData();
			launcher.getTelemetryData();
			telemetry.update();
		}
  }
}
