package org.firstinspires.ftc.teamcode.subsystems.vision.limelight

import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.hardware.limelightvision.LLResultTypes
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles
import org.firstinspires.ftc.teamcode.subsystems.imu.IMUConstants
import org.firstinspires.ftc.teamcode.utils.components.Subsystem
import org.firstinspires.ftc.teamcode.utils.Team
import org.firstinspires.ftc.teamcode.subsystems.vision.TagIDs

/** The April tag processor for the Decode season. */
class LimelightProcessor(
  private val opMode: OpMode,
  private val team: Team
): Subsystem {
  private val imu: IMU = opMode.hardwareMap.get(
    IMU::class.java,
    IMUConstants.CONFIG_NAME
  );

  // Initialize Limelight
  private val limelight: Limelight3A = opMode.hardwareMap.get(
    Limelight3A::class.java,
    "limelight"
  );

  /** Stores the found artifact pattern. If one wasn't found, the value will be 0. */
  var artifactPattern = 0;

  init {
    limelight.setPollRateHz(100); // This makes the Limelight computer refresh 100 times per second
    limelight.pipelineSwitch(LimelightPipelines.APRILTAG_PIPELINE);
  }

  /** Tells the Limelight computer to start. */
  fun startLimelight() {
    limelight.start();
  }

  /** Update the Limelight computer. */
  fun update() {
    val orientation: YawPitchRollAngles = imu.robotYawPitchRollAngles;
    limelight.updateRobotOrientation(orientation.getYaw(AngleUnit.DEGREES));
  }

  /** Get the robot's starting position on the field via the April tags around the field. */
  fun localizeViaTags() {}

  /**
   * Checks whether the [LLResult] is a valid April Tag result.
   * @return `True` if the result is invalid, `false` otherwise.
   */
  private fun isValidAprilTagResult(result: LLResult?): Boolean {
    return result == null || !result.isValid() || (result.pipelineType != "tagType"); // TODO: Get Apriltag type
  }

  /**
   * Checks whether there are any April Tag results available.
   * @return `True` if there are no results, `false` otherwise.
   */
  private fun tagResultsExist(tagResults: MutableList<LLResultTypes.FiducialResult>?): Boolean {
    return tagResults == null || tagResults.isEmpty();
  }

  /** Finds the artifact pattern from the obelisk using the camera's output. */
  fun findArtifactPattern() {
    // If the artifact pattern has not been found, attempt to get it
    if(artifactPattern != 0) return;

    val result: LLResult? = limelight.latestResult;
    if(isValidAprilTagResult(result)) return;

    val tags: MutableList<LLResultTypes.FiducialResult>? = result!!.fiducialResults;
    if(tagResultsExist(tags)) return;

    // Check the IDs of the found April Tags, setting the artifact pattern if an ID matches a pattern ID
    for (tag in tags!!) {
      val tagID: Int = tag.fiducialId;
      if(tagID == TagIDs.GGP || tagID == TagIDs.PGP || tagID == TagIDs.PPG) artifactPattern = tagID;
    }
  }

  /**
   * @return A [Pose3D] containing vectors to the middle of the goal tag relative
   * to the robot's current position if the goal tag is in sight, otherwise null.
   */
  fun getVectorsToGoalTag(): Pose3D? {
    val result: LLResult? = limelight.latestResult;
    if(isValidAprilTagResult(result)) return null;

    val tags: MutableList<LLResultTypes.FiducialResult>? = result!!.fiducialResults;
    if(tagResultsExist(tags)) return null;

    for(tag in tags!!) {
      val tagID: Int = tag.fiducialId;
      if (team == Team.BLUE && tagID == TagIDs.BLUE_TAG) return tag.robotPoseTargetSpace;
      else if (team == Team.RED && tagID == TagIDs.RED_TAG) return tag.robotPoseTargetSpace;
    }

    return null;
  }

  override fun getTelemetryData() {
    opMode.telemetry.addData("Team", team);
    opMode.telemetry.addData("Artifact Pattern ID", artifactPattern);
    opMode.telemetry.addData(
      "Artifact Pattern",
      when(artifactPattern) {
        TagIDs.GGP -> "GGP";
        TagIDs.PGP -> "PGP";
        TagIDs.PPG -> "PPG";
        else -> "Unknown";
      }
    );
  }
}
