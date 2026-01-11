package org.firstinspires.ftc.teamcode.season;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor.*;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.utils.components.Subsystem

/** Controller for the robot's magazine motor.  */
class MagMotor(private val opMode: OpMode) : Subsystem {
  private val magMotor: DcMotorEx = opMode.hardwareMap.get(
    DcMotorEx::class.java,
    "magMotor"
  );

  private var magMotorPower = 0.0;

  init {
    magMotor.zeroPowerBehavior = ZeroPowerBehavior.BRAKE;
    magMotor.mode = RunMode.STOP_AND_RESET_ENCODER;
    magMotor.mode = RunMode.RUN_USING_ENCODER;
  }

  fun stop() {
    magMotorPower = 0.0;
    magMotor.power = 0.0;
  }

  fun setPower(power: Double) {
    magMotorPower = power;
    magMotor.power = magMotorPower;
  }

  fun run() {
    magMotor.power = opMode.gamepad2.left_stick_y.toDouble();
  }

  override fun getTelemetryData() {
    opMode.telemetry.addData("Magazine Motor Power", magMotorPower);
  }
}
