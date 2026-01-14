package org.firstinspires.ftc.teamcode.season;

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor.RunMode
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import org.firstinspires.ftc.teamcode.utils.components.Subsystem

/** Controller for the robot's launcher. */
class LauncherBasic(private val opMode: OpMode) : Subsystem {
  companion object {
    private const val SPIN_SENSITIVITY = 0.75;
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

  private var leftMotorPower = 0.0;
  private var rightMotorPower = 0.0;

  init {
    for(motor in arrayOf<DcMotorEx>(leftMotor, rightMotor)) {
      motor.zeroPowerBehavior = ZeroPowerBehavior.FLOAT;
      motor.mode = RunMode.STOP_AND_RESET_ENCODER;
      motor.mode = RunMode.RUN_USING_ENCODER;
    }

    leftMotor.direction = Direction.REVERSE;
    rightMotor.direction = Direction.FORWARD;
  }

  override fun update() {
    val leftPower = clampPowerVal(gamepad.left_trigger * SPIN_SENSITIVITY);
    val rightPower = clampPowerVal(gamepad.right_trigger * SPIN_SENSITIVITY);

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
  }
}
