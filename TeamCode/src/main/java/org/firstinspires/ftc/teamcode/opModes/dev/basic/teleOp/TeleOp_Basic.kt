package org.firstinspires.ftc.teamcode.opModes.dev.basic.teleOp

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.opModes.OpModeGroups
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.RudimentaryMecanumDrive
import org.firstinspires.ftc.teamcode.utils.components.OpModeBase

@TeleOp(name = "Basic Mecanum", group = OpModeGroups.BASIC)
class TeleOp_Basic : LinearOpMode(), OpModeBase {
  private lateinit var mecanumDrive: RudimentaryMecanumDrive;

  @Throws(InterruptedException::class)
  override fun runOpMode() {
    // Initialize mechanisms
    mecanumDrive = RudimentaryMecanumDrive(this);

    // Wait for Op mode to start and cancel startup if stopped
    waitForStart()
    if(isStopRequested) return;

    // Run main loop
    while(opModeIsActive() && !isStopRequested) {
      // Updates the drivetrain with the game controller's current values once every loop
      mecanumDrive.update();

      // Activate the drivetrain's boost if the left trigger is pressed down
      if(gamepad1.left_trigger >= 0.25) mecanumDrive.boost = 1.0;
      else mecanumDrive.boost = 0.5;

      updateTelemetryData();
    }
  }

  override fun updateTelemetryData() {
    // Get new telemetry data and push it to the driver station
    mecanumDrive.getTelemetryData();
    telemetry.update();
  }
}
