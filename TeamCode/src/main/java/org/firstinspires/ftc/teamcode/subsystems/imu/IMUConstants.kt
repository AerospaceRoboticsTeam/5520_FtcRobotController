package org.firstinspires.ftc.teamcode.subsystems.imu;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot.LogoFacingDirection;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot.UsbFacingDirection;
import com.qualcomm.robotcore.hardware.IMU;

object IMUConstants {
  const val CONFIG_NAME = "imu";

  @JvmField
  val LOGO_Direction = LogoFacingDirection.UP;
  @JvmField
  val USB_Direction = UsbFacingDirection.FORWARD;

  @JvmField
  val ORIENTATION = RevHubOrientationOnRobot(
    LOGO_Direction,
    USB_Direction
  );
  @JvmField
  val PARAMETERS: IMU.Parameters = IMU.Parameters(ORIENTATION);
}
