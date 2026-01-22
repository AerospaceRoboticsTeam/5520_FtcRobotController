package org.firstinspires.ftc.teamcode.opModes.tests.teleOp;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.opModes.OpModeGroups;

import com.qualcomm.hardware.limelightvision.LLResult;

@TeleOp(name="Limelight Test", group=OpModeGroups.TEST)
public class TeleOp_LimelightTest extends OpMode {
	private Limelight3A limelight;
	private IMU imu;

	@Override
	public void init() {
		limelight = hardwareMap.get(Limelight3A.class, "limelight");
		limelight.pipelineSwitch(0);//Set to constant
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
		YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
		limelight.updateRobotOrientation(orientation.getYaw());
		LLResult llResult = limelight.getLatestResult();

		if(llResult != null && llResult.isValid()) {
			Pose3D pose = llResult.getBotpose_MT2();
			telemetry.addData("Pose X", llResult.getTx());
			telemetry.addData("Pose Y", llResult.getTy());
			telemetry.addData("Pose A", llResult.getTa());
			//telemetry.addData("ID", llResult.getTargetID());
		}
		else {
			telemetry.addData("Limelight", "No valid target");
		}
	}
}
