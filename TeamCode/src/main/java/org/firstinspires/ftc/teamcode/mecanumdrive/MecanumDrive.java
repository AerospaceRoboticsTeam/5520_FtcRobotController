package org.firstinspires.ftc.teamcode.mecanumdrive;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import org.firstinspires.ftc.teamcode.constants.DeviceConstants;

/** A mecanum drive train for driving the robot. */
public class MecanumDrive {
  /** Left joystick y value */
  private double y;
  /** Left joystick x value */
  private double x;
  /** Rotational value */
  private double rx;
  /** Rotational value of x */
  private double rotX;
  /** Rotational value of y */
  private double rotY;
  /** Direction bot is facing */
  private double botHeading;

  /** Left front motor power */
  private double leftFrontPower;
  /** Left back motor power */
  private double leftBackPower;
  /** Right front motor power */
  private double rightFrontPower;
  /** Right back motor power */
  private double rightBackPower;

  private static final double xSensitivity = 1.3;
  private static final double ySensitivity = 0.75;
  private static final double rxSensitivity = 0.75;
  private double boost = 0.5;

  /** Robot wheel motors */
  private final DcMotor frontLeft, frontRight, backLeft, backRight;
  /** Odometry Computer */
  private final IMU imu;
  /** Linear Op Mode instance */
  private final LinearOpMode bot;

  public MecanumDrive(LinearOpMode opMode)
  {
    // Get op mode instance and motors of robot from it
    bot = opMode;
    frontLeft = opMode.hardwareMap.dcMotor.get(DeviceConstants.DriveTrain.LEFT_FRONT_MOTOR);
    frontRight = opMode.hardwareMap.dcMotor.get(DeviceConstants.DriveTrain.RIGHT_FRONT_MOTOR);
    backLeft = opMode.hardwareMap.dcMotor.get(DeviceConstants.DriveTrain.LEFT_BACK_MOTOR);
    backRight = opMode.hardwareMap.dcMotor.get(DeviceConstants.DriveTrain.RIGHT_BACK_MOTOR);

    // Set motor directions (Reverse left motors so all motors rotate in the same direction)
    frontLeft.setDirection(DeviceConstants.DriveTrain.LFM_DIRECTION);
    backLeft.setDirection(DeviceConstants.DriveTrain.LBM_DIRECTION);
		frontRight.setDirection(DeviceConstants.DriveTrain.RFM_DIRECTION);
		frontLeft.setDirection(DeviceConstants.DriveTrain.LFM_DIRECTION);

    // Initialize GoBilda Pinpoint Computer
    imu = bot.hardwareMap.get(IMU.class, "imu");
    IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
      RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
      RevHubOrientationOnRobot.UsbFacingDirection.UP
    ));
    imu.initialize(parameters);
  }

  /** Calculate motor powers and make robot move */
  public void drive() {
    //-------------------------Gamepad 1 Controls/Drivetrain Movement-------------------------//

    // Get robot's heading from IMU (yaw angle)
    y = -(bot.gamepad1.left_stick_y) * ySensitivity;
    x = bot.gamepad1.left_stick_x * xSensitivity;
    rx = bot.gamepad1.right_stick_x * rxSensitivity;

    // Get the robot's heading (yaw) from the IMU
    botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

    // Translate to robot heading from field heading for motor values
    rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
    rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

    // Denominator is the largest motor power
    double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
    leftFrontPower = (rotY + rotX + rx) / denominator;
    leftBackPower = (rotY - rotX + rx) / denominator;
    rightFrontPower = (rotY - rotX - rx) / denominator;
    rightBackPower = (rotY + rotX - rx) / denominator;

    // Set motor power using calculated values
    frontLeft.setPower(leftFrontPower * boost);
    backLeft.setPower(leftBackPower * boost);
    frontRight.setPower(rightFrontPower * boost);
    backRight.setPower(rightBackPower * boost);
  }

  /** Set motor's boost power */
  public void setBoost(double x){
    boost = x;
  }

  /** Returns power of left front motor */
  public double getLeftFrontPower() {
    return leftFrontPower;
  }

  /** Returns power of left back motor */
  public double getLeftBackPower() {
    return leftBackPower;
  }

  /** Returns power of right front motor */
  public double getRightFrontPower() {
    return rightFrontPower;
  }

  /** Returns power to right back motor */
  public double getRightBackPower() {
    return rightBackPower;
  }

  /** Returns the robot's heading in radians */
  public double getBotHeading() {
    return botHeading;
  }

  /** Add the robot's telemetry data using the newly calculated values */
  @SuppressLint("DefaultLocale")
  public void getTelemetryData() {
    bot.telemetry.addData("Left Front: ", getLeftFrontPower());
    bot.telemetry.addData("Left Back: ", getLeftBackPower());
    bot.telemetry.addData("Right Front: ", getRightFrontPower());
    bot.telemetry.addData("Right Back: ", getRightBackPower());
    bot.telemetry.addData("Heading: ", String.format("%.2f", Math.toDegrees(getBotHeading())) + " deg");
  }
}
