package org.firstinspires.ftc.teamcode.opModes.tests.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.subsystems.vision.limelight.LimelightProcessor;
import org.firstinspires.ftc.teamcode.opModes.OpModeGroups;
import org.firstinspires.ftc.teamcode.season.Intake;
import org.firstinspires.ftc.teamcode.season.Launcher;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.utils.Team;
import org.firstinspires.ftc.teamcode.utils.components.OpModeBase;

@Autonomous(name = "Subsystem Test", group = OpModeGroups.TEST)
class Auto_SubsystemTest : LinearOpMode(), OpModeBase {
  private lateinit var tagProcessor: LimelightProcessor;
  private lateinit var intake: Intake;
  private lateinit var launcher: Launcher;

  @Throws(InterruptedException::class)
  override fun runOpMode() {
    // Configure state
    tagProcessor = LimelightProcessor(this, if (gamepad1.a) Team.RED else Team.BLUE);
    intake = Intake(this);
    launcher = Launcher(this, tagProcessor);
    val pathFollower = Constants.createFollower(hardwareMap);


    // Wait for Op mode to start and cancel startup if stopped
    waitForStart();
    if(isStopRequested) return;

    // Run main loop
    while (opModeIsActive() && !isStopRequested) {
      tagProcessor.findArtifactPattern();

      updateTelemetryData();
    }
  }

  override fun updateTelemetryData() {
    tagProcessor.getTelemetryData();
    intake.getTelemetryData();
    launcher.getTelemetryData();
    telemetry.update();
  }
}
