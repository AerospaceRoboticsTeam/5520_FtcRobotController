package org.firstinspires.ftc.teamcode.subsystems.vision.limelight;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

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

import org.firstinspires.ftc.teamcode.utils.Limelight_Behavior;


//Go to: http://192.168.43.1:5801/ to view from control hub
@TeleOp(name="Limelight Test 2", group=OpModeGroups.TEST)
public class LimelightProcessorJava{

	private Limelight3A limelight;

	private final OpMode bot;
	private IMU imu;

	//bot = iBot;

	private boolean IMUInitialized = false;

	private LLResult latestResult = null;
	private boolean latestResultValid = false;

	public LimelightProcessorJava(OpMode opMode) {

		bot = opMode;

		limelight = bot.hardwareMap.get(Limelight3A.class, "limelight");
		limelight.setPollRateHz(20); // This sets how often we ask Limelight for data (100 times per second)
		limelight.start();
		imu = bot.hardwareMap.get(IMU.class, "imu");
		RevHubOrientationOnRobot revHubOrientationOnRobot = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD);
		imu.initialize(new IMU.Parameters(revHubOrientationOnRobot));
	}
	/*
	@Override
	public void start() {
		limelight.start();
	}
	 */

	public void setPipeline(Limelight_Behavior TASK){
		if (TASK==Limelight_Behavior.MOTIF){
			limelight.pipelineSwitch(0);
		}
		else if (TASK==Limelight_Behavior.RED_GOAL){
			limelight.pipelineSwitch(2);
		}
		else if (TASK==Limelight_Behavior.BLUE_GOAL){
			limelight.pipelineSwitch(1);
		}
	}

	public void update(){
		double robotYaw;
		if (imu != null) {
			// Use IMU's YawPitchRollAngles API to get yaw in degrees
			YawPitchRollAngles ypr = imu.getRobotYawPitchRollAngles();
			robotYaw = ypr.getYaw(AngleUnit.DEGREES);
			limelight.updateRobotOrientation(robotYaw);
			this.IMUInitialized = true;
		} else {
			this.IMUInitialized = false;
		}

		LLResult result = limelight.getLatestResult();
		this.latestResult = result;
		//telemetry.addData("Result object", result != null);
		//telemetry.addData("Result valid", (result != null && result.isValid()));
	}

	public void checkValidity(){
		LLResult result = this.latestResult;
		if (result != null && result.isValid()){
			this.latestResultValid = true;
		} else {
			this.latestResultValid = false;
		}
	}

	public double[] getTargets() {
		LLResult result = this.latestResult;
		if (this.latestResultValid) {
			double tx = result.getTx(); // How far left or right the target is
			double ty = result.getTy(); // How far up or down the target is
			double ta = result.getTa(); // How big the target looks
			double[] returnArray = {tx, ty, ta};
			return returnArray;
		} else {
			return new double[0];
		}
	}

	public double[] getBotpose() {
		Pose3D botpose_mt2 = this.latestResult.getBotpose_MT2();
		if (this.latestResultValid && botpose_mt2 != null) {
			double x = botpose_mt2.getPosition().x;
			double y = botpose_mt2.getPosition().y;
			double z = botpose_mt2.getPosition().z;
			double[] returnArray = {x, y, z};
			return returnArray;
		} else {
			return new double[0];
		}
	}

	public double[] getFiducials() {
		List<FiducialResult> fiducials = (this.latestResult != null) ? this.latestResult.getFiducialResults() : null;
		int fidCount = (fiducials == null) ? 0 : fiducials.size();
		//telemetry.addData("Fiducial count", fidCount);
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

				//telemetry.addData("Fiducial " + id, String.format(Locale.US, "tx=%.2f ty=%.2f dist=%.2f", txDeg, tyDeg, distance));
				double[] returnArray = {id, txDeg, tyDeg, distance};
				return returnArray;
			}
		} else {
			//telemetry.addData("Fiducials", "None");
			return new double[0];
		}
		//telemetry.update();
		return new double[0];
	}
}
