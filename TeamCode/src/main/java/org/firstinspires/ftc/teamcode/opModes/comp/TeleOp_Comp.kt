package org.firstinspires.ftc.teamcode.opModes.comp

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.opModes.OpModeGroups
import org.firstinspires.ftc.teamcode.season.Intake
import org.firstinspires.ftc.teamcode.season.LauncherBasic
import org.firstinspires.ftc.teamcode.season.LightController
import org.firstinspires.ftc.teamcode.season.LightMode
import org.firstinspires.ftc.teamcode.season.Magazine
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.PPMecanumDrive
import org.firstinspires.ftc.teamcode.utils.components.OpModeBase

@TeleOp(name = "Competition Complete", group = OpModeGroups.COMPETITION)
class TeleOp_Comp : OpMode(), OpModeBase {
  private lateinit var mecanumDrive: PPMecanumDrive;
  private lateinit var lightController: LightController;
  private lateinit var intake: Intake;
  private lateinit var magazine: Magazine;
  private lateinit var launcher: LauncherBasic;

  override fun init() {
    mecanumDrive = PPMecanumDrive(this);
    lightController = LightController(this);
    intake = Intake(this);
    magazine = Magazine(this);
    launcher = LauncherBasic(this, lightController);
  }

  override fun start() {
    mecanumDrive.start();
  }

  override fun loop() {
    mecanumDrive.update();
    intake.update();
    magazine.update();
    launcher.update();

    updateTelemetryData();
  }

  override fun updateTelemetryData() {
    mecanumDrive.getTelemetryData();
    intake.getTelemetryData();
    magazine.getTelemetryData();
    launcher.getTelemetryData();
    telemetry.update();
  }
}
