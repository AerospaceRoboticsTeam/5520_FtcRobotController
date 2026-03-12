package org.firstinspires.ftc.teamcode.opModes.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.opModes.OpModeGroups;
import org.firstinspires.ftc.teamcode.season.Intake;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.RudimentaryRobotCentricDrivetrain;
import org.firstinspires.ftc.teamcode.utils.components.OpModeBase;

@TeleOp(name = "Intake Test", group = OpModeGroups.TEST)
public class TeleOp_IntakeTest extends LinearOpMode implements OpModeBase {
	private RudimentaryRobotCentricDrivetrain mecanumDrive;
	private Intake intake;

	@Override
	public void runOpMode() throws InterruptedException {
		// Initialize mechanisms
		mecanumDrive = new RudimentaryRobotCentricDrivetrain(this);
		intake = new Intake(this);

		// Wait for Op mode to start and cancel startup if stopped
		waitForStart();
		if(isStopRequested()) return;

		// Run main loop
		while(opModeIsActive() && !isStopRequested()) {
			// Updates the drivetrain with the game controller's current values once every loop
			mecanumDrive.update();

			// Activate the drivetrain's boost if the left trigger is pressed down
			if(gamepad1.left_trigger >= 0.25) mecanumDrive.setBoost(1);
			else mecanumDrive.setBoost(0.5);

			// Switch intake direction or turn it off
			if(gamepad1.left_bumper) intake.intakeIn();
			else if(gamepad1.right_bumper) intake.intakeOut();
			else if(gamepad1.y) intake.intakeStop();

			updateTelemetryData();
		}
	}

	@Override
	public void updateTelemetryData() {
		mecanumDrive.getTelemetryData();
		intake.getTelemetryData();
		telemetry.update();
	}
}
