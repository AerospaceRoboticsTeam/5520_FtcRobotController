package org.firstinspires.ftc.teamcode.subsystems.vision.limelight

import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D
import org.firstinspires.ftc.teamcode.subsystems.vision.TagIDs
import org.firstinspires.ftc.teamcode.utils.components.Subsystem
import kotlin.math.hypot

class LimelightProcessor(private val opMode: OpMode, private val imu: IMU) : Subsystem {
  private val limelight = opMode.hardwareMap.get(
    Limelight3A::class.java, "limelight"
  );

  private var pipeline = LimelightPipelines.MOTIF;
  private var artifactPattern = TagIDs.NOT_FOUND;

  private var latestResult: LLResult? = null;
  private var isLatestResultValid = false;

  init {
    limelight.setPollRateHz(100);
    limelight.pipelineSwitch(LimelightPipelines.MOTIF);
  }

  fun start() {
    limelight.start();
  }

  override fun update() {
    limelight.updateRobotOrientation(imu.robotYawPitchRollAngles.getYaw(AngleUnit.DEGREES));
    latestResult = limelight.latestResult;
  }

  fun setPipeline(index: Int) {
    limelight.pipelineSwitch(index);
  }

  fun checkValidity() {
    isLatestResultValid = latestResult != null && latestResult!!.isValid();
  }

  fun getBotPose(): Pose3D? {
    if(isLatestResultValid) return latestResult!!.botpose_MT2;
    return null;
  }

  fun getFiducialResults(): CalculatedFiducialResult? {
    if(!isLatestResultValid) return null;

    val fiducials = latestResult!!.fiducialResults;
    opMode.telemetry.addData("Fiducial Count", fiducials.size);
    if(fiducials.isEmpty()) return null;

    for(fiducial in fiducials) {
      val id = fiducial.fiducialId;

      val txDeg = fiducial.targetXDegrees;
      val tyDeg = fiducial.targetYDegrees;

      val poseTarget = fiducial.robotPoseTargetSpace;
      val poseTargetPos = poseTarget.position;
      val distance = if(poseTarget != null && poseTargetPos != null) {
        hypot(poseTargetPos.x, poseTargetPos.y);
      } else -1.0;

      opMode.telemetry.addData(
        "Fiducial $id", "Tx = %.2f, Ty = %.2f, Dist = %.2f", txDeg, tyDeg, distance
      );

      return CalculatedFiducialResult(
        id, poseTargetPos.x, poseTargetPos.y, txDeg, tyDeg, distance
      );
    }

    return null;
  }

  override fun getTelemetryData() {
    opMode.telemetry.addData("Limelight Pipeline State", when(pipeline) {
      LimelightPipelines.MOTIF -> "MOTIF";
      LimelightPipelines.BLUE_GOAL -> "BLUE GOAL";
      LimelightPipelines.RED_GOAL -> "RED GOAL";
      else -> "Unknown";
    });
    opMode.telemetry.addData("Artifact Pattern", when(artifactPattern) {
      TagIDs.NOT_FOUND -> "NOT FOUND";
      TagIDs.GPP -> "GGP";
      TagIDs.PGP -> "PGP";
      TagIDs.PPG -> "PPG";
      else -> "Unknown";
    });
    opMode.telemetry.addData("Is Latest Result Valid", isLatestResultValid);
  }
}

data class CalculatedFiducialResult(
  val id: Int,
  val tx: Double,
  val ty: Double,
  /** How far left or right the target is (degrees). */
  val txDeg: Double,
  /** How far up or down the target is (degrees). */
  val tyDeg: Double,
  /** -1 if target position data was null. */
  val distance: Double
);
