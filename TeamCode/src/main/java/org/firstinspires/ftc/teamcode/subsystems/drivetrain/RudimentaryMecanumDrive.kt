package org.firstinspires.ftc.teamcode.subsystems.drivetrain;

import android.annotation.SuppressLint;
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.*;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.subsystems.imu.IMUConstants
import org.firstinspires.ftc.teamcode.utils.components.Subsystem
import kotlin.math.*;

/** A mecanum drive train for driving the robot.  */
class RudimentaryMecanumDrive(
  /** Linear Op Mode instance. */
  private val opMode: OpMode
) : Subsystem {
  companion object {
    private const val X_SENSITIVITY = 1.0; // Originally: 1.3
    private const val Y_SENSITIVITY = 1.0; // Originally: 0.75
    private const val RX_SENSITIVITY = 1.0; // Originally: 0.75

    private const val BASE_BOOST = 0.5;
    private const val BOOST = 1.0;
  }

  /** Left joystick x value. */
  private var x = 0.0;
  /** Left joystick y value. */
  private var y = 0.0;
  /** Rotational value. */
  private var rx = 0.0;
  /** Returns the robot's heading in radians. */
  /** Direction bot is facing. */
  var botHeading = 0.0
    private set;

  /** Rotational value of x. */
  private var rotX = 0.0;
  /** Rotational value of y. */
  private var rotY = 0.0;

  /** Returns power of left front motor. */
  /** Left front motor power. */
  var leftFrontPower = 0.0
    private set;
  /** Returns power of left back motor. */
  /** Left back motor power. */
  var leftBackPower = 0.0
    private set;
  /** Returns power of right front motor. */
  /** Right front motor power. */
  var rightFrontPower = 0.0
    private set;
  /** Returns power to right back motor. */
  /** Right back motor power. */
  var rightBackPower = 0.0
    private set;
  var boost = BASE_BOOST;

  private val gamepad = opMode.gamepad1;
  private val leftFront: DcMotor = opMode.hardwareMap.dcMotor.get(
    DrivetrainConstants.LEFT_FRONT_MOTOR
  );
  private val leftBack: DcMotor = opMode.hardwareMap.dcMotor.get(
    DrivetrainConstants.LEFT_BACK_MOTOR
  );
  private val rightFront: DcMotor = opMode.hardwareMap.dcMotor.get(
    DrivetrainConstants.RIGHT_FRONT_MOTOR
  );
  private val rightBack: DcMotor = opMode.hardwareMap.dcMotor.get(
    DrivetrainConstants.RIGHT_BACK_MOTOR
  );
  private val imu: IMU;

  init {
    // Set motor directions (Reverse left motors so all motors rotate in the same direction)
    leftFront.direction = DrivetrainConstants.LFM_DIRECTION;
    leftBack.direction = DrivetrainConstants.LBM_DIRECTION;
    rightFront.direction = DrivetrainConstants.RFM_DIRECTION;
    rightBack.direction = DrivetrainConstants.RBM_DIRECTION;

    // Configure motors behavior
    for(motor in arrayOf<DcMotor>(leftFront, leftBack, rightFront, rightBack)) {
      motor.mode = RunMode.STOP_AND_RESET_ENCODER;
      motor.mode = RunMode.RUN_USING_ENCODER;
      motor.zeroPowerBehavior = ZeroPowerBehavior.BRAKE;
    }

    // Init IMU
    imu = opMode.hardwareMap.get(
      IMU::class.java,
      IMUConstants.CONFIG_NAME
    );
    imu.initialize(IMUConstants.PARAMETERS);
  }

  /** Calculate motor powers based on controller 1's input and make the robot move. */
  override fun update() {
    // Activate the drivetrain's boost if the left trigger is pressed down
    boost = if(gamepad.left_trigger >= 0.25) BOOST else BASE_BOOST;

    // Get controller 1's input and apply control sensitivity
    y = gamepad.left_stick_y * Y_SENSITIVITY;
    x = gamepad.left_stick_x * X_SENSITIVITY;
    rx = gamepad.right_stick_x * RX_SENSITIVITY;

    // Get the robot's heading (yaw) from the IMU
    botHeading = -imu.robotYawPitchRollAngles.getYaw(AngleUnit.RADIANS);

    // Translate to robot heading from field heading for motor values
    rotX = x * cos(botHeading) + y * sin(botHeading);
    rotY = x * sin(botHeading) - y * cos(botHeading);

    // Denominator is the largest motor power
    val denominator = max(abs(rotY) + abs(rotX) + abs(rx), 1.0);
    leftFrontPower = (rotY + rotX + rx) / denominator;
    leftBackPower = (rotY - rotX + rx) / denominator;
    rightFrontPower = (rotY - rotX - rx) / denominator;
    rightBackPower = (rotY + rotX - rx) / denominator;

    // Set motor power using calculated values
    leftFront.power = leftFrontPower * boost;
    leftBack.power = leftBackPower * boost;
    rightFront.power = rightFrontPower * boost;
    rightBack.power = rightBackPower * boost;
  }

  @SuppressLint("DefaultLocale")
  /** Add the robot's telemetry data using the newly calculated values. */
  override fun getTelemetryData() {
    opMode.telemetry.addData("Left Front", this.leftFrontPower);
    opMode.telemetry.addData("Left Back", this.leftBackPower);
    opMode.telemetry.addData("Right Front", this.rightFrontPower);
    opMode.telemetry.addData("Right Back", this.rightBackPower);
    opMode.telemetry.addData(
      "Heading",
      String.format("%.2f", Math.toDegrees(this.botHeading)) + " deg"
    );
  }
}
