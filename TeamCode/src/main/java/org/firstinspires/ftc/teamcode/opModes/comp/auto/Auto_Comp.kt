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
  private lateinit var distLight: LightController;
  private lateinit var aimLight: LightController;
  private lateinit var intake: Intake;
  private lateinit var magazine: Magazine;
  private lateinit var launcher: LauncherBasic;

  private var startTime = 0L;

  override fun init() {
    follower = Constants.createFollower(hardwareMap);
    distLight = LightController(this, "distanceLight");
    aimLight = LightController(this, "aimLight");
    intake = Intake(this);
    magazine = Magazine(this);
    launcher = LauncherBasic(this, distLight);

    follower.setStartingPose(Pose(0.0, 0.0, 0.0));
  }

  override fun start() {
    startTime=System.currentTimeMillis();
    val forwardPath = Path(BezierLine(
      Pose(0.0, 0.0, 0.0),
      Pose(39.37, 0.0, 0.0)
    ));

    follower.followPath(forwardPath);
  }

  fun launchSequence(){
    var launchSeqStart = System.currentTimeMillis();
    launcher.setPower(0.325, 0.325);
    //Add Sleep
    magazine.moveUpLower();
    magazine.moveUpUpper();
    while (true){
      var currentTime = System.currentTimeMillis();
      var deltaT = currentTime-launchSeqStart;
      if (deltaT>1000){
        break;
      } else {
        continue;
      }
    }
    //Add Sleep
    //Stop Everything
    magazine.stop();
    launcher.stop();
  }
  override fun loop() {
    follower.update();

    if (!follower.isBusy()){
      launchSequence();
    }
    updateTelemetryData();
  }

  override fun updateTelemetryData() {
    val targetPos = follower.currentPath.endPose();
    val targetPoseCords = "(${targetPos.x}, ${targetPos.y}, ${Math.toDegrees(targetPos.heading)})";

    telemetry.addData("X Pos", follower.pose.x);
    telemetry.addData("Y Pos", follower.pose.y);
    telemetry.addData("Heading", Math.toDegrees(follower.pose.heading));
    telemetry.addData("Target Pos", targetPoseCords);
    distLight.getTelemetryData();
    aimLight.getTelemetryData();
    intake.getTelemetryData();
    magazine.getTelemetryData();
    launcher.getTelemetryData();
    telemetry.update();
  }
}
