package org.firstinspires.ftc.teamcode.season;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.*;
import org.firstinspires.ftc.teamcode.utils.components.Subsystem
import kotlin.math.abs;

/** Controller for intake located at the front of the robot.  */
class Intake(private val bot: OpMode) : Subsystem {
  companion object {
    private const val IN_POWER = 0.25;
    private const val OUT_POWER = -0.25;
  }

  private val intakeMotor: DcMotor = bot.hardwareMap.dcMotor.get("intakeMotor");

  init {
    intakeMotor.mode = RunMode.STOP_AND_RESET_ENCODER;
    intakeMotor.mode = RunMode.RUN_WITHOUT_ENCODER;
    intakeMotor.direction = Direction.FORWARD;
  }

  fun intakeIn() {
    intakeMotor.power = IN_POWER;
  }

  fun intakeOut() {
    intakeMotor.power = OUT_POWER;
  }

  fun intakeStop() {
    intakeMotor.power = 0.0;
  }

  /**
   * Set a custom intake power and direction.
   * @param direction True is forward, towards the inside of the robot;
   * False is backward, away from the robot.
   */
  fun setIntakePower(power: Double, direction: Boolean) {
    var powerMagnitude = abs(power);

    if(!direction) powerMagnitude = -powerMagnitude;
    intakeMotor.power = powerMagnitude;
  }

  override fun getTelemetryData() {
    bot.telemetry.addData(
      "Intake state",
      if(intakeMotor.power > 0) "IN" else "OUT"
    );
    bot.telemetry.addData("Intake power", intakeMotor.power);
  }
}
