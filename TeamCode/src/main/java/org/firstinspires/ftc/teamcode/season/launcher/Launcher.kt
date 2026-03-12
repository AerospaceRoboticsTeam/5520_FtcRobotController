package org.firstinspires.ftc.teamcode.season.launcher

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor.*;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.utils.components.Subsystem
import org.firstinspires.ftc.teamcode.subsystems.vision.limelight.DeprecatedLimelightProcessor;
import kotlin.math.*;

/** Controller for the robot's launcher.  */
class Launcher(private val bot: OpMode, private val tagProcessor: DeprecatedLimelightProcessor) : Subsystem {
  companion object {
    /** The angle of the launcher from the ground in degrees. */
    private const val LAUNCHER_ANGLE = 45.0;
    private const val GRAVITATIONAL_ACCELERATION = 9.81;
  }

  private val leftLauncherMotor: DcMotorEx = bot.hardwareMap.get(
    DcMotorEx::class.java,
    "leftLauncherMotor"
  );
  private val rightLauncherMotor: DcMotorEx = bot.hardwareMap.get(
    DcMotorEx::class.java,
    "rightLauncherMotor"
  );

  private var state: LauncherStatus = LauncherStatus.IDLE;
  private var leftLauncherMotorPower = 0.0;
  private var rightLauncherMotorPower = 0.0;

  init {
    leftLauncherMotor.zeroPowerBehavior = ZeroPowerBehavior.FLOAT;
    rightLauncherMotor.zeroPowerBehavior = ZeroPowerBehavior.FLOAT;

    leftLauncherMotor.mode = RunMode.STOP_AND_RESET_ENCODER;
    rightLauncherMotor.mode = RunMode.STOP_AND_RESET_ENCODER;

    leftLauncherMotor.mode = RunMode.RUN_USING_ENCODER;
    rightLauncherMotor.mode = RunMode.RUN_USING_ENCODER;
  }

  override fun update() {
    when(state) {
      LauncherStatus.START_UP, LauncherStatus.IDLE -> run {
        val cameraPos = tagProcessor.getVectorsToGoalTag();
        val linearVelocities = calculateLinearVelocities(cameraPos!!);

        if(linearVelocities == null) return@run;

        val motorPowers = linearVelocityToPower(
          linearVelocities[0],
          linearVelocities[1]
        );
        activateMotors(motorPowers[0], motorPowers[1]);
      }

      else -> Unit;
    }
  }

  fun calculateLinearVelocities(cameraPos: Pose3D): DoubleArray? {
    if(state == LauncherStatus.IDLE || state == LauncherStatus.START_UP) return null;

    val cameraPoint = cameraPos.getPosition();
    cameraPoint.toUnit(DistanceUnit.METER);

    // Goal point
    val goalPos = object {
      val x = 0.0;
      val y = 0.0;
      val z = 0.0;
    };

    // Launch point
    val launchPos = object {
      val x = cameraPoint.x;
      val y = cameraPoint.y;
      val z = cameraPoint.z;
    };

    // Calculate the distances on the X and Z axes
    val deltaX = goalPos.x - launchPos.x;
    val deltaZ = launchPos.z - goalPos.z;

    // Calculate the launcher angles
    val launcherPitch = Math.toRadians(LAUNCHER_ANGLE);
    val launcherYaw = atan(deltaZ / deltaX);

    // Calculate the numerator and denominator of the linear velocity
    val numerator = 0.5 * GRAVITATIONAL_ACCELERATION * deltaX.pow(2.0);
    val denominator =
      cos(launcherYaw).pow(2.0) * cos(launcherPitch).pow(2.0) *
      (goalPos.y - launchPos.y) + deltaX * (tan(launcherPitch) / cos(launcherYaw));

    // Calculate the linear velocity
    val linearVelocity = sqrt(numerator / denominator);

    return doubleArrayOf(linearVelocity / 2, linearVelocity / 2);
  }

  /**
   * Calculate launcher motor power values from linear velocities.
   * @return A double array with 2 elements:
   *
   *  1. The calculated left motor power value.
   *  1. the calculated right motor power value.
   */
  fun linearVelocityToPower(leftMotorVelocity: Double, rightMotorVelocity: Double): DoubleArray {
    val leftRPS = leftMotorVelocity / LauncherConstants.GECKO_WHEEL_CIRCUMFERENCE;
    val rightRPS = rightMotorVelocity / LauncherConstants.GECKO_WHEEL_CIRCUMFERENCE;

    val leftPower = leftRPS / LauncherConstants.MAX_RPS;
    val rightPower = rightRPS / LauncherConstants.MAX_RPS;

    return doubleArrayOf(leftPower, rightPower);
  }

  /** Set the motor's velocity using angular velocity rather than power.  */
  fun setMotorVelocity(leftMotorVelocityLinear: Double, rightMotorVelocityLinear: Double) {
    val leftAngularVelocity = leftMotorVelocityLinear / LauncherConstants.GECKO_WHEEL_RADIUS;
    val rightAngularVelocity = rightMotorVelocityLinear / LauncherConstants.GECKO_WHEEL_RADIUS;

    leftLauncherMotor.setVelocity(leftAngularVelocity, AngleUnit.RADIANS);
    rightLauncherMotor.setVelocity(rightAngularVelocity, AngleUnit.RADIANS);
  }

  fun activateMotors(leftMotorPower: Double, rightMotorPower: Double) {
    if(state == LauncherStatus.IDLE || state == LauncherStatus.START_UP) {
      leftLauncherMotorPower = leftMotorPower;
      rightLauncherMotorPower = rightMotorPower;

      leftLauncherMotor.power = leftMotorPower;
      rightLauncherMotor.power = rightMotorPower;

      state = LauncherStatus.START_UP;
    }
  }

  fun stopMotors() {
    leftLauncherMotorPower = 0.0;
    rightLauncherMotorPower = 0.0;

    rightLauncherMotor.power = 0.0;
    leftLauncherMotor.power = 0.0;
    state = LauncherStatus.IDLE;
  }

  fun launch() {
    if(
      state == LauncherStatus.START_UP &&
      leftLauncherMotor.power > leftLauncherMotorPower - 0.1 &&
      leftLauncherMotor.power < leftLauncherMotorPower + 0.1 &&
      rightLauncherMotor.power > rightLauncherMotorPower - 0.1 &&
      rightLauncherMotor.power < rightLauncherMotorPower + 0.1
    ) state = LauncherStatus.LAUNCHING;
  }

  override fun getTelemetryData() {
    bot.telemetry.addData("Launcher Status", state);
    bot.telemetry.addData("Left Launcher Motor Power", leftLauncherMotorPower);
    bot.telemetry.addData("Right Launcher Motor Power", rightLauncherMotorPower);
  }
}

internal enum class LauncherStatus {
  IDLE,
  LAUNCHING,
  START_UP,
  READY
}

internal object LauncherConstants {
  /** The radius of the Gecko wheels used in the launcher in meters. */
  const val GECKO_WHEEL_RADIUS = (96f / 2f) / 1000f;

  /** The circumference of the Gecko wheels used in the launcher in meters. */
  const val GECKO_WHEEL_CIRCUMFERENCE = 2 * Math.PI.toFloat() * GECKO_WHEEL_RADIUS;

  /** The max <u>Revolutions Per Second</u> of the launcher motors. */
  const val MAX_RPS: Float = 6000f / 60f;

  /** The maximum possible linear velocity of the launcher. */
  const val MAX_LINEAR_VELOCITY = GECKO_WHEEL_CIRCUMFERENCE * MAX_RPS;
}
