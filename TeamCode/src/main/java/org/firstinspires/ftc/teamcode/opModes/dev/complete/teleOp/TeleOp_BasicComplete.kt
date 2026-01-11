package org.firstinspires.ftc.teamcode.opModes.dev.complete.teleOp;

import android.annotation.SuppressLint;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.season.Intake;
import org.firstinspires.ftc.teamcode.season.Launcher;
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.RudimentaryMecanumDrive;
import org.firstinspires.ftc.teamcode.utils.Team;
import org.firstinspires.ftc.teamcode.utils.components.OpModeBase;
import org.firstinspires.ftc.teamcode.opModes.OpModeGroups;
import org.firstinspires.ftc.teamcode.subsystems.vision.limelight.LimelightProcessor;

@TeleOp(name = "Basic Complete", group = OpModeGroups.DEV_COMPLETE)
class TeleOp_BasicComplete : LinearOpMode(), OpModeBase {
  private var mecanumDrive: RudimentaryMecanumDrive? = null;
  private var tagProcessor: LimelightProcessor? = null;
  private var intake: Intake? = null;
  private var launcher: Launcher? = null;

  @SuppressLint("SuspiciousIndentation")
  @Throws(InterruptedException::class)
  override fun runOpMode() {
    // Initialize mechanisms
    mecanumDrive = RudimentaryMecanumDrive(this);
    tagProcessor = LimelightProcessor(
      this, if (gamepad1.a) Team.RED else Team.BLUE
    );
    intake = Intake(this);
    launcher = Launcher(this, tagProcessor);

    // Wait for Op mode to start and cancel startup if stopped
    waitForStart();
    if(isStopRequested) return;

    // Run main loop
    while(opModeIsActive() && !isStopRequested) {
      // Updates the drivetrain with the game controller's current values once every loop
      mecanumDrive!!.drive();

      // Activate the drivetrain's boost if the left trigger is pressed down
      mecanumDrive!!.setBoost(
        if(gamepad1.left_trigger >= 0.25) 1.0 else 0.5
      );

      // Switch intake direction or turn it off
      if(gamepad1.left_bumper) intake!!.intakeIn();
      else if(gamepad1.right_bumper) intake!!.intakeOut();
      else if(gamepad1.y) intake!!.intakeStop();

      // Activate the launcher if the right trigger is pressed down
      if(gamepad1.right_trigger >= 0.25) launcher!!.activateMotors(
        gamepad1.right_trigger.toDouble(),
        gamepad1.right_trigger.toDouble()
      );
      else launcher!!.stopMotors();

      /* TODO: Add input handlers for controlling other hardware */
    }
  }

  override fun updateTelemetryData() {
    mecanumDrive!!.getTelemetryData();
    intake!!.getTelemetryData();
    launcher!!.getTelemetryData();
    telemetry.update();
  }
}
