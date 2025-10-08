package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mecanumdrive.MecanumDrive;

@TeleOp(name = "Dev TeleOp", group = "TeleOp")
public class TeleOp_Dev extends LinearOpMode {
  private MecanumDrive mecanumDrive;
  private int artifactNum = 0;

  @Override
  public void runOpMode() throws InterruptedException {
    // Initialize mechanisms
    mecanumDrive = new MecanumDrive(this);

    // Wait for Op mode to start and cancel startup if stopped
    if(isStopRequested()) return;

    // Run main loop
    while(opModeIsActive() && !isStopRequested()) {
      // Updates the drivetrain with the game controller's current values once every loop
      mecanumDrive.drive();

      // Activate the drivetrain's boost if the left trigger is pressed down
      // NOTE: May want to change from != 0 to >= 0.5 to prevent accidental presses
      if(gamepad1.left_trigger != 0) {
        mecanumDrive.setBoost(1);
      }
      else {
        mecanumDrive.setBoost(0.5);
      }

      /* TODO: Add input handlers for controlling other hardware */

      // Get new telemetry data and push it to the driver station
      mecanumDrive.getTelemetryData();
      telemetry.update();
    }
  }
}
