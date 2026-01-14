package org.firstinspires.ftc.teamcode.opModes.dev.complete.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.opModes.OpModeGroups
import org.firstinspires.ftc.teamcode.season.Intake
import org.firstinspires.ftc.teamcode.season.LauncherBasic
import org.firstinspires.ftc.teamcode.season.Magazine
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.RudimentaryMecanumDrive
import org.firstinspires.ftc.teamcode.utils.components.OpModeBase

@TeleOp(name = "Basic Complete", group = OpModeGroups.DEV_COMPLETE)
class TeleOp_BasicComplete : LinearOpMode(), OpModeBase {
  private lateinit var mecanumDrive: RudimentaryMecanumDrive;
  private lateinit var intake: Intake;
  private lateinit var magazine: Magazine;
  private lateinit var launcher: LauncherBasic;

  @Throws(InterruptedException::class)
  override fun runOpMode() {
    // Initialize mechanisms
    mecanumDrive = RudimentaryMecanumDrive(this);
    intake = Intake(this);
    magazine = Magazine(this);
    launcher = LauncherBasic(this);

    // Wait for Op mode to start and cancel startup if stopped
    waitForStart();
    if(isStopRequested) return;

    // Run main loop
    while(opModeIsActive() && !isStopRequested) {
      mecanumDrive.update();
      intake.update();
      magazine.update();
      launcher.update();


      /* TODO: Add input handlers for controlling other hardware */
    }
  }

  override fun updateTelemetryData() {
    mecanumDrive.getTelemetryData();
    intake.getTelemetryData();
    launcher.getTelemetryData();
    telemetry.update();
  }
}
