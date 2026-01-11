package org.firstinspires.ftc.teamcode.opModes.tests.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.subsystems.vision.limelight.LimelightProcessor;
import org.firstinspires.ftc.teamcode.opModes.OpModeGroups;
import org.firstinspires.ftc.teamcode.season.Intake;
import org.firstinspires.ftc.teamcode.season.Launcher;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.utils.components.Subsystem;
import org.firstinspires.ftc.teamcode.utils.Team;

@Autonomous(name = "Subsystem Test", group = OpModeGroups.TEST)
class Auto_SubsystemTest : LinearOpMode(), Subsystem {
  private var tagProcessor: LimelightProcessor? = null;
  private var intake: Intake? = null;
  private var launcher: Launcher? = null;

  @Throws(InterruptedException::class)
  override fun runOpMode() {
    intake = Intake(this);
    launcher = Launcher(this, tagProcessor);
    val pathFollower = Constants.createFollower(hardwareMap);

    // Configure state
    tagProcessor = LimelightProcessor(this, if (gamepad1.a) Team.RED else Team.BLUE);

    // Wait for Op mode to start and cancel startup if stopped
    waitForStart();
    if(isStopRequested) return;

    // Run main loop
    while (opModeIsActive() && !isStopRequested) {
      tagProcessor!!.findArtifactPattern();

      getTelemetryData();
    }
  }

  override fun getTelemetryData() {
    tagProcessor!!.getTelemetryData();
    intake!!.getTelemetryData();
    launcher!!.getTelemetryData();
    telemetry.update();
  }
}
