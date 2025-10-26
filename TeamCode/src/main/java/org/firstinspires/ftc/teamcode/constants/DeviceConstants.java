package org.firstinspires.ftc.teamcode.constants;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

public final class DeviceConstants {
	private DeviceConstants() {}

	public static final class DriveTrain {
		private DriveTrain() {}

		public static final String LEFT_FRONT_MOTOR = "leftFrontMotor";
		public static final String LEFT_BACK_MOTOR = "leftBackMotor";
		public static final String RIGHT_FRONT_MOTOR = "rightFrontMotor";
		public static final String RIGHT_BACK_MOTOR = "rightBackMotor";
		public static final DcMotor.Direction LFM_DIRECTION = DcMotor.Direction.REVERSE;
		public static final DcMotor.Direction LBM_DIRECTION = DcMotor.Direction.REVERSE;
		public static final DcMotor.Direction RFM_DIRECTION = DcMotor.Direction.FORWARD;
		public static final DcMotor.Direction RBM_DIRECTION = DcMotor.Direction.FORWARD;
	}

	public static final class IMUDevice {
		private IMUDevice() {}

		public static final String NAME = "imu";
		private static final RevHubOrientationOnRobot.LogoFacingDirection logoDirection =
			RevHubOrientationOnRobot.LogoFacingDirection.LEFT;
		private static final RevHubOrientationOnRobot.UsbFacingDirection usbDirection =
			RevHubOrientationOnRobot.UsbFacingDirection.UP;
		public static final RevHubOrientationOnRobot orientationOnRobot =
			new RevHubOrientationOnRobot(logoDirection, usbDirection); // TODO: Change to match actual orientation
		public static IMU.Parameters getParameters() { return new IMU.Parameters(orientationOnRobot); }
	}
}
