package org.firstinspires.ftc.teamcode.opModes.dev.complete.teleOp

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.opModes.OpModeGroups
import org.firstinspires.ftc.teamcode.season.Intake
import org.firstinspires.ftc.teamcode.season.LightController
import org.firstinspires.ftc.teamcode.season.Magazine
import org.firstinspires.ftc.teamcode.season.launcher.LauncherBasic
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.PPDrivetrain
import org.firstinspires.ftc.teamcode.utils.components.OpModeBase

@TeleOp(name = "Dev Complete", group = OpModeGroups.DEV_COMPLETE)
class TeleOp_Complete : OpMode(), OpModeBase {
  private lateinit var mecanumDrive: PPDrivetrain;
  private lateinit var distLight: LightController;
  private lateinit var aimLight: LightController;
  private lateinit var headlight: LightController;
  private lateinit var intake: Intake;
  private lateinit var magazine: Magazine;
  private lateinit var launcher: LauncherBasic;

  override fun init() {
    mecanumDrive = PPDrivetrain(this);
    distLight = LightController(this, "distanceLight");
    aimLight = LightController(this, "aimLight");
    headlight = LightController(this, "headlight");
    intake = Intake(this);
    magazine = Magazine(this);
    launcher = LauncherBasic(this, distLight);
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
    distLight.getTelemetryData();
    aimLight.getTelemetryData();
    intake.getTelemetryData();
    magazine.getTelemetryData();
    launcher.getTelemetryData();
    telemetry.update();
  }
}
