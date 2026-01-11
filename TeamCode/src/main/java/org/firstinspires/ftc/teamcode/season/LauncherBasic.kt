package org.firstinspires.ftc.teamcode.season;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.DcMotor.*;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.utils.components.Subsystem;

/** Controller for the robot's launcher. */
class LauncherBasic(private val opMode: OpMode) : Subsystem {
  private val leftLauncherMotor: DcMotorEx = opMode.hardwareMap.get(
    DcMotorEx::class.java,
    "leftLauncherMotor"
  );
  private val rightLauncherMotor: DcMotorEx = opMode.hardwareMap.get(
    DcMotorEx::class.java,
    "rightLauncherMotor"
  );

  private var leftLauncherMotorPower = 0.0;
  private var rightLauncherMotorPower = 0.0;

  init {
    leftLauncherMotor.zeroPowerBehavior = ZeroPowerBehavior.FLOAT;
    rightLauncherMotor.zeroPowerBehavior = ZeroPowerBehavior.FLOAT;

    leftLauncherMotor.mode = RunMode.STOP_AND_RESET_ENCODER;
    rightLauncherMotor.mode = RunMode.STOP_AND_RESET_ENCODER;

    leftLauncherMotor.mode = RunMode.RUN_USING_ENCODER;
    rightLauncherMotor.mode = RunMode.RUN_USING_ENCODER;

    leftLauncherMotor.direction = Direction.REVERSE;
  }

  fun setMotorsPower(leftMotorPower: Double, rightMotorPower: Double) {
    leftLauncherMotorPower = leftMotorPower;
    rightLauncherMotorPower = rightMotorPower;

    leftLauncherMotor.power = leftMotorPower;
    rightLauncherMotor.power = rightMotorPower;
  }

  fun stopMotors() {
    setMotorsPower(0.0, 0.0);
  }

  fun launch() {
    setMotorsPower(0.5, 0.5);
  }

  override fun getTelemetryData() {
    opMode.telemetry.addData(
      "Left Launcher Motor Power",
      leftLauncherMotorPower
    );
    opMode.telemetry.addData(
      "Right Launcher Motor Power",
      rightLauncherMotorPower
    );
  }
}
