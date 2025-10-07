package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Dev Auto", group = "Auto")
public class Auto_Dev extends LinearOpMode {
  @Override
  public void runOpMode() throws InterruptedException {
    // Wait for Op mode to start and cancel startup if stopped
    waitForStart();
    if(isStopRequested()) return;

    // Run main loop
    while(opModeIsActive() && !isStopRequested()) {}
  }
}
