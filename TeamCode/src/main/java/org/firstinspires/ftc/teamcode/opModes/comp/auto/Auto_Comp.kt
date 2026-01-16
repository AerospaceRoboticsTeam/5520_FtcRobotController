package org.firstinspires.ftc.teamcode.opModes.comp.auto

import com.pedropathing.follower.Follower
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.opModes.OpModeGroups
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.season.Intake
import org.firstinspires.ftc.teamcode.season.LightController
import org.firstinspires.ftc.teamcode.season.Magazine
import org.firstinspires.ftc.teamcode.season.launcher.LauncherBasic
import org.firstinspires.ftc.teamcode.utils.components.OpModeBase

@Autonomous(name = "Competition Auto", group = OpModeGroups.COMPETITION)
class Auto_Comp : OpMode(), OpModeBase {
  private lateinit var follower: Follower;
  private lateinit var lightController: LightController;
  private lateinit var intake: Intake;
  private lateinit var magazine: Magazine;
  private lateinit var launcher: LauncherBasic;

  override fun init() {
    follower = Constants.createFollower(hardwareMap);
    lightController = LightController(this);
    intake = Intake(this);
    magazine = Magazine(this);
    launcher = LauncherBasic(this, lightController);
  }

  override fun start() {
    follower.update();
  }

  override fun loop() {
    TODO("Not yet implemented")
  }

  override fun updateTelemetryData() {
    lightController.getTelemetryData();
    intake.getTelemetryData();
    magazine.getTelemetryData();
    launcher.getTelemetryData();
    telemetry.update();
  }
}
