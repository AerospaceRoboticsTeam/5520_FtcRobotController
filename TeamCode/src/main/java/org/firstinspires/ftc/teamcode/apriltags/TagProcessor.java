package org.firstinspires.ftc.teamcode.apriltags;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import org.firstinspires.ftc.teamcode.constants.LimelightPipelines;
import org.firstinspires.ftc.teamcode.constants.Team;

/** The April tag processor for the Decode season. */
public class TagProcessor {
	private final LinearOpMode bot;
	private final IMU imu;
	private final Limelight3A limelight;

	/** Stores the found artifact pattern. If one wasn't found, the value will be 0. */
	private final Team team;
	public int artifactPattern = 0;

	public TagProcessor(LinearOpMode opMode, Team team) {
		bot = opMode;
		this.team = team;

		// Initialize and set up IMU
		RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.LEFT;
		RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection.UP;
		RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);  // TODO: Change to match actual orientation
		imu = bot.hardwareMap.get(IMU.class, "imu");
		imu.initialize(new IMU.Parameters(orientationOnRobot));

		// Initialize Limelight
		limelight = bot.hardwareMap.get(Limelight3A.class, "limelight");
		limelight.setPollRateHz(100); // This makes the Limelight computer refresh 100 times per
		limelight.pipelineSwitch(LimelightPipelines.APRILTAG_PIPELINE);
	}

	/** Tells the Limelight computer to start. */
	public void startLimelight() { limelight.start(); }

	/** Update the Limelight computer. */
	public void update() {
		YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
		limelight.updateRobotOrientation(orientation.getYaw(AngleUnit.DEGREES));
	}

	/** Finds the artifact pattern from the obelisk using the camera's output. */
	public void getArtifactPattern() {
		// If the artifact pattern has not been found, attempt to get it
		if(artifactPattern == 0) {
			LLResult result = limelight.getLatestResult();
			if(result != null && result.isValid() && result.getPipelineType().equals("TODO: Get Apriltag type")) {}
		}
	}

	/**
	 * @return An object containing position vectors to the middle of the goal tag relative to the
	 * robot's current position if the goal tag is in sight, otherwise null.
	 */
	public void getVectorsToGoalTag() {

	}

	public void getTelemetryData() {
		bot.telemetry.addData("Team: ", team);
		bot.telemetry.addData("Artifact pattern ID: ", artifactPattern);
		bot.telemetry.addData("Artifact pattern: ",
			artifactPattern == TagIDs.GGP ? "GGP" : artifactPattern == TagIDs.PGP ? "PGP" : artifactPattern == TagIDs.PPG ? "PPG" : "Unknown"
		);
	}
}
