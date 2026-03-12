package org.firstinspires.ftc.teamcode.season.launcher

import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import org.firstinspires.ftc.teamcode.season.LightController
import org.firstinspires.ftc.teamcode.season.LightMode
import org.firstinspires.ftc.teamcode.utils.components.Subsystem

@Configurable
/** Controller for the robot's launcher. */
class LauncherBasic(
  private val opMode: OpMode,
  private val distLight: LightController
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
  private var launcherStatus = LauncherBasicStatus.SHORT;

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
        LauncherBasicStatus.SHORT -> {
          distLight.setMode(LightMode.AQUA);
          basePowerValue = LONG_DISTANCE_POWER;
          LauncherBasicStatus.LONG;
        }
        LauncherBasicStatus.LONG -> {
          distLight.setMode(LightMode.ORANGE);
          basePowerValue = SHORT_DISTANCE_POWER;
          LauncherBasicStatus.SHORT;
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
    opMode.telemetry.addData("Left Launcher Motor Is Active", leftMotorActive);
    opMode.telemetry.addData("Right Launcher Motor Is Active", rightMotorActive);
    opMode.telemetry.addData("Launcher Status", launcherStatus);
  }
}

internal enum class LauncherBasicStatus {
  SHORT,
  LONG
}
