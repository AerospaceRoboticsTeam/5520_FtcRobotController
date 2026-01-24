package org.firstinspires.ftc.teamcode.season.launcher

import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import org.firstinspires.ftc.teamcode.season.LightController
import org.firstinspires.ftc.teamcode.season.LightMode
import org.firstinspires.ftc.teamcode.subsystems.vision.limelight.LimelightPipelines
import org.firstinspires.ftc.teamcode.subsystems.vision.limelight.LimelightProcessor
import org.firstinspires.ftc.teamcode.utils.components.Subsystem

@Configurable
/** Controller for the robot's launcher. */
class LauncherAdvanced(
  private val opMode: OpMode,
  private val distLight: LightController,
  private val aimLight: LightController,
  private val limelightProcessor: LimelightProcessor
) : Subsystem {
  companion object {
    private var SPIN_SENSITIVITY = 0.2;
    private var SHORT_DISTANCE_POWER = 0.325;
    private var LONG_DISTANCE_POWER = 0.425;
  }

  private val leftMotor: DcMotorEx = opMode.hardwareMap.get(
    DcMotorEx::class.java,
    "leftLauncherMotor"
  );
  private val rightMotor: DcMotorEx = opMode.hardwareMap.get(
    DcMotorEx::class.java,
    "rightLauncherMotor"
  );
  private val gamepad = opMode.gamepad2;
  private var leftTriggerWasPressed = false;

  private var basePowerValue = SHORT_DISTANCE_POWER;
  private var leftMotorPower = 0.0;
  private var rightMotorPower = 0.0;
  private var leftMotorActive = false;
  private var rightMotorActive = false;
  private var launcherStatus = LauncherAdvancedStatus.SHORT;

  init {
    for(motor in arrayOf<DcMotorEx>(leftMotor, rightMotor)) {
      motor.zeroPowerBehavior = ZeroPowerBehavior.FLOAT;
      motor.mode = RunMode.STOP_AND_RESET_ENCODER;
      motor.mode = RunMode.RUN_USING_ENCODER;
    }

    leftMotor.direction = Direction.REVERSE;
    rightMotor.direction = Direction.FORWARD;
    distLight.setMode(LightMode.ORANGE);
  }

  override fun update() {
    // Toggle between SHORT and LONG distance powers
    if(gamepad.squareWasPressed()) {
      launcherStatus = when(launcherStatus) {
        LauncherAdvancedStatus.SHORT -> {
          distLight.setMode(LightMode.AQUA);
          basePowerValue = LONG_DISTANCE_POWER;
          LauncherAdvancedStatus.LONG;
        }
        LauncherAdvancedStatus.LONG -> {
          distLight.setMode(LightMode.ORANGE);
          basePowerValue = SHORT_DISTANCE_POWER;
          LauncherAdvancedStatus.SHORT;
        }
      }
    }

    if(!leftTriggerWasPressed && gamepad.left_trigger >= 0.1) {
      leftTriggerWasPressed = true;
      leftMotorActive = !leftMotorActive;
      rightMotorActive = !rightMotorActive;
    }
    else leftTriggerWasPressed = false;

    val leftPower =
      if(!leftMotorActive) 0.0;
      else if(!gamepad.triangle) clampPowerVal(
        basePowerValue + gamepad.left_stick_y * (1.0 - basePowerValue) * SPIN_SENSITIVITY
      );
      else -0.25;
    val rightPower =
      if(!rightMotorActive) 0.0;
      else if(!gamepad.triangle) clampPowerVal(
        basePowerValue + gamepad.right_stick_y * (1.0 - basePowerValue) * SPIN_SENSITIVITY
      );
      else -0.25;

    checkGoalAngle();

    setPower(leftPower, rightPower);
  }

  fun setPower(leftPower: Double, rightPower: Double) {
    leftMotorPower = leftPower;
    rightMotorPower = rightPower;

    leftMotor.power = leftPower;
    rightMotor.power = rightPower;
  }

  fun stop() {
    setPower(0.0, 0.0);
  }

  fun launch() {
    setPower(0.5, 0.5);
  }

  fun checkGoalAngle() {
    if(
      limelightProcessor.pipeline != LimelightPipelines.BLUE_GOAL &&
      limelightProcessor.pipeline != LimelightPipelines.RED_GOAL
    ) return;

    val goalTargetResult = limelightProcessor.getFiducialResults();
    if(goalTargetResult == null) return;

    val isAligned = goalTargetResult.txDeg > -0.1 && goalTargetResult.txDeg < 0.1;
    aimLight.setMode(if(isAligned) LightMode.GREEN else LightMode.RED);
  }

  fun clampPowerVal(power: Double): Double {
    if(power > 1.0) return 1.0;
    return power.coerceAtLeast(0.0);
  }

  override fun getTelemetryData() {
    opMode.telemetry.addData(
      "Left Launcher Motor Power",
      leftMotorPower
    );
    opMode.telemetry.addData(
      "Right Launcher Motor Power",
      rightMotorPower
    );
    opMode.telemetry.addData("Left Launcher Motor Velocity", leftMotor.velocity);
    opMode.telemetry.addData("Right Launcher Motor Velocity", rightMotor.velocity);
    opMode.telemetry.addData("Left Launcher Motor Is Active", leftMotorActive);
    opMode.telemetry.addData("Right Launcher Motor Is Active", rightMotorActive);
    opMode.telemetry.addData("Launcher Status", launcherStatus);
  }
}

internal enum class LauncherAdvancedStatus {
  SHORT,
  LONG
}
