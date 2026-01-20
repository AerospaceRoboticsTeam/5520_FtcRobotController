package org.firstinspires.ftc.teamcode.subsystems.vision.limelight;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.opModes.OpModeGroups;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes.FiducialResult;
import java.util.List;
import java.util.Locale;

@TeleOp(name="Limelight Test", group=OpModeGroups.TEST)
public class LimelightTest2 extends OpMode {

	private Limelight3A limelight;
	private IMU imu;

	@Override
	public void init() {
		limelight = hardwareMap.get(Limelight3A.class, "limelight");
		limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
		limelight.start();
		limelight.pipelineSwitch(0);//Set to constant, pipeline 0 is set to april tag detection via web interface
		imu = hardwareMap.get(IMU.class, "imu");
		RevHubOrientationOnRobot revHubOrientationOnRobot = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD);
		imu.initialize(new IMU.Parameters(revHubOrientationOnRobot));
	}

	@Override
	public void start() {
		limelight.start();
	}

	@Override
	public void loop(){
		// First, tell Limelight which way your robot is facing
		double robotYaw;
		if (imu != null) {
			// Use IMU's YawPitchRollAngles API to get yaw in degrees
			YawPitchRollAngles ypr = imu.getRobotYawPitchRollAngles();
			robotYaw = ypr.getYaw(AngleUnit.DEGREES);
			limelight.updateRobotOrientation(robotYaw);
		} else {
			telemetry.addData("IMU", "Not initialized");
		}

		LLResult result = limelight.getLatestResult();
		telemetry.addData("Result object", result != null);
		telemetry.addData("Result valid", (result != null && result.isValid()));
		if (result != null && result.isValid()) {
			double tx = result.getTx(); // How far left or right the target is (degrees)
			double ty = result.getTy(); // How far up or down the target is (degrees)
			double ta = result.getTa(); // How big the target looks (0%-100% of the image)

			telemetry.addData("Target X", tx);
			telemetry.addData("Target Y", ty);
			telemetry.addData("Target Area", ta);
		} else {
			telemetry.addData("Limelight", "No Targets");
		}

		if (result != null && result.isValid()) {
			Pose3D botpose_mt2 = result.getBotpose_MT2();
			if (botpose_mt2 != null) {
				double x = botpose_mt2.getPosition().x;
				double y = botpose_mt2.getPosition().y;
				telemetry.addData("MT2 Location:", "(" + x + ", " + y + ")");
			}
		}

		// Safely handle fiducials (may be null or empty)
		List<FiducialResult> fiducials = (result != null) ? result.getFiducialResults() : null;
		int fidCount = (fiducials == null) ? 0 : fiducials.size();
		telemetry.addData("Fiducial count", fidCount);
		if (fidCount > 0) {
			for (FiducialResult fiducial : fiducials) {
				int id = fiducial.getFiducialId(); // The ID number of the fiducial

				double txDeg = Double.NaN;
				double tyDeg = Double.NaN;
				double distance = Double.NaN;
				try {
					txDeg = fiducial.getTargetXDegrees(); // Where it is (left-right)
					tyDeg = fiducial.getTargetYDegrees(); // Where it is (up-down)
				} catch (Exception e) {
					// Some SDK versions may not provide these; we'll ignore if unavailable
				}

				try {
					Pose3D poseTarget = fiducial.getRobotPoseTargetSpace();
					if (poseTarget != null && poseTarget.getPosition() != null) {
						double px = poseTarget.getPosition().x;
						double py = poseTarget.getPosition().y;
						// distance in the ground plane (x,y)
						distance = Math.hypot(px, py);
					}
				} catch (Exception e) {
					// ignore missing pose
				}

				telemetry.addData("Fiducial " + id, String.format(Locale.US, "tx=%.2f ty=%.2f dist=%.2f", txDeg, tyDeg, distance));
			}
		} else {
			telemetry.addData("Fiducials", "None");
		}
	}

}
