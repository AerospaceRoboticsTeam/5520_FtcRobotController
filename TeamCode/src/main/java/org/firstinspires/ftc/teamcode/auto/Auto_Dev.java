package org.firstinspires.ftc.teamcode.auto;

import org.firstinspires.ftc.teamcode.constants.ArtifactNumRef;
import org.firstinspires.ftc.teamcode.constants.Team;
import org.firstinspires.ftc.teamcode.libs.decodeLibs.Intake;
import org.firstinspires.ftc.teamcode.libs.decodeLibs.Launcher;
import org.firstinspires.ftc.teamcode.apriltags.TagProcessor;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Dev Auto", group = "Auto")
public class Auto_Dev extends LinearOpMode {
	private TagProcessor tagProcessor;
	private Intake intake;
	private Launcher launcher;

	private final ArtifactNumRef artifactNum = new ArtifactNumRef(0);

	@Override
  public void runOpMode() throws InterruptedException {
		intake = new Intake(this);
		launcher = new Launcher(this, tagProcessor);

		// Configure state
		tagProcessor = new TagProcessor(this, gamepad1.a ? Team.RED : Team.BLUE);

		// Wait for Op mode to start and cancel startup if stopped
    waitForStart();
    if(isStopRequested()) return;

    // Run main loop
    while(opModeIsActive() && !isStopRequested()) {
			tagProcessor.getArtifactPattern();

			// Get new telemetry data and push it to the driver station
			tagProcessor.getTelemetryData();
			intake.getTelemetryData();
			launcher.getTelemetryData();
			telemetry.update();
		}
  }
}
