package org.firstinspires.ftc.teamcode.opModes.comp.auto

import com.pedropathing.follower.Follower
import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.pedropathing.paths.Path
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

    follower.setStartingPose(Pose(0.0, 0.0, 0.0));
  }

  override fun start() {
    val forwardPath = Path(BezierLine(
      Pose(0.0, 0.0, 0.0),
      Pose(39.37, 0.0, 0.0)
    ));

    follower.followPath(forwardPath);
  }

  override fun loop() {
    follower.update();

    updateTelemetryData();
  }

  override fun updateTelemetryData() {
    val targetPos = follower.currentPath.endPose();
    val targetPoseCords = "(${targetPos.x}, ${targetPos.y}, ${Math.toDegrees(targetPos.heading)})";

    telemetry.addData("X Pos", follower.pose.x);
    telemetry.addData("Y Pos", follower.pose.y);
    telemetry.addData("Heading", Math.toDegrees(follower.pose.heading));
    telemetry.addData("Target Pos", targetPoseCords);
    lightController.getTelemetryData();
    intake.getTelemetryData();
    magazine.getTelemetryData();
    launcher.getTelemetryData();
    telemetry.update();
  }
}
