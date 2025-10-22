package org.firstinspires.ftc.teamcode.apriltags;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.constants.Team;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

/** The April tag processor for the Decode season. */
public class TagProcessor {
	private final LinearOpMode bot;
	public final AprilTagProcessor tagProcessor;
	public final VisionPortal visionPortal;

	/** Stores the found artifact pattern. If one wasn't found, the value will be 0. */
	private Team team;
	public int artifactPattern = 0;

	public TagProcessor(LinearOpMode opMode, Team team) {
		bot = opMode;
		this.team = team;

		tagProcessor = new AprilTagProcessor.Builder()
			.setDrawAxes(true)
			.setDrawCubeProjection(true)
			.setDrawTagID(true)
			.setDrawTagOutline(true)
			.build();

		visionPortal = new VisionPortal.Builder()
			.addProcessor(tagProcessor)
			.setCamera(bot.hardwareMap.get(WebcamName.class, "Webcam 1"))
			.setCameraResolution(new Size(640, 480))
			.build();
	}

	/** Finds the artifact pattern from the obelisk using the camera's output. */
	public void getArtifactPattern() {
		if(!tagProcessor.getDetections().isEmpty() && artifactPattern == 0) {
			for(AprilTagDetection tag : tagProcessor.getDetections()) {
				if(tag.id == TagIDs.GGP || tag.id == TagIDs.PGP || tag.id == TagIDs.PPG) {
					artifactPattern = tag.id;
				}
			}
		}
	}

	/** @return An object containing position vectors to the middle of the goal tag relative to the robot's current position, otherwise null. */
	public AprilTagPoseFtc getVectorsToGoalTag() {
		if(tagProcessor.getDetections().isEmpty()) return null;

		AprilTagPoseFtc tagPose = null;

		for(AprilTagDetection tag : tagProcessor.getDetections()) {
			if(team == Team.BLUE && tagPose == null && tag.id == TagIDs.BLUE_TAG) tagPose = tag.ftcPose;
			else if(team == Team.RED && tagPose == null && tag.id == TagIDs.RED_TAG) tagPose = tag.ftcPose;
		}

		return tagPose;
	}
}
