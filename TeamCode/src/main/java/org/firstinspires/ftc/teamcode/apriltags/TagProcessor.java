package org.firstinspires.ftc.teamcode.apriltags;

import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import org.firstinspires.ftc.teamcode.constants.DeviceConstants;
import org.firstinspires.ftc.teamcode.constants.LimelightPipelines;
import org.firstinspires.ftc.teamcode.constants.Team;

import java.util.List;

/** The April tag processor for the Decode season. */
public class TagProcessor {
	private final LinearOpMode bot;
	private final IMU imu;
	private final Limelight3A limelight;

	private final Team team;
	/** Stores the found artifact pattern. If one wasn't found, the value will be 0. */
	public int artifactPattern = 0;

	public TagProcessor(LinearOpMode opMode, Team team) {
		bot = opMode;
		this.team = team;

		// Initialize and set up IMU
		imu = bot.hardwareMap.get(IMU.class, DeviceConstants.IMUDevice.NAME);
		imu.initialize(DeviceConstants.IMUDevice.getParameters());

		// Initialize Limelight
		limelight = bot.hardwareMap.get(Limelight3A.class, "limelight");
		limelight.setPollRateHz(100); // This makes the Limelight computer refresh 100 times per second
		limelight.pipelineSwitch(LimelightPipelines.APRILTAG_PIPELINE);
	}

	/** Tells the Limelight computer to start. */
	public void startLimelight() { limelight.start(); }

	/** Update the Limelight computer. */
	public void update() {
		YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
		limelight.updateRobotOrientation(orientation.getYaw(AngleUnit.DEGREES));
	}

	/** Checks whether the {@link LLResult} is a valid April Tag result. */
	private boolean isValidAprilTagResult(LLResult result) {
		return result == null || !result.isValid() || !result.getPipelineType().equals("tagType"); // TODO: Get Apriltag type
	}

	/** Checks whether there are any April Tag results available. */
	private boolean tagResultsExist(List<LLResultTypes.FiducialResult> tagResults) {
		return tagResults == null || tagResults.isEmpty();
	}

	/** Finds the artifact pattern from the obelisk using the camera's output. */
	public void getArtifactPattern() {
		// If the artifact pattern has not been found, attempt to get it
		if(artifactPattern != 0) return;

		LLResult result = limelight.getLatestResult();
		if(isValidAprilTagResult(result)) return;

		List<LLResultTypes.FiducialResult> tags = result.getFiducialResults();
		if(tagResultsExist(tags)) return;

		// Check the IDs of the found April Tags, setting the artifact pattern if an ID matches a pattern ID
		for(LLResultTypes.FiducialResult tag : tags) {
			int tagID = tag.getFiducialId();
			if(tagID == TagIDs.GGP || tagID == TagIDs.PGP || tagID == TagIDs.PPG) artifactPattern = tagID;
		}
	}

	/**
	 * @return A {@link Pose3D} containing vectors to the middle of the goal tag relative
	 * to the robot's current position if the goal tag is in sight, otherwise null.
	 */
	public Pose3D getVectorsToGoalTag() {
		LLResult result = limelight.getLatestResult();
		if(isValidAprilTagResult(result)) return null;

		List<LLResultTypes.FiducialResult> tags = result.getFiducialResults();
		if(tagResultsExist(tags)) return null;

		for(LLResultTypes.FiducialResult tag : tags) {
			int tagID = tag.getFiducialId();
			if(team == Team.BLUE && tagID == TagIDs.BLUE_TAG) return tag.getRobotPoseTargetSpace();
			else if(team == Team.RED && tagID == TagIDs.RED_TAG) return tag.getRobotPoseTargetSpace();
		}

		return null;
	}

	public void getTelemetryData() {
		bot.telemetry.addData("Team: ", team);
		bot.telemetry.addData("Artifact pattern ID: ", artifactPattern);
		bot.telemetry.addData("Artifact pattern: ",
			artifactPattern == TagIDs.GGP ? "GGP" : artifactPattern == TagIDs.PGP ? "PGP" : artifactPattern == TagIDs.PPG ? "PPG" : "Unknown"
		);
	}
}
