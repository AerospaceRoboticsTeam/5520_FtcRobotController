package org.firstinspires.ftc.teamcode.season;

import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotor.*
import com.qualcomm.robotcore.hardware.DcMotorSimple.*
import org.firstinspires.ftc.teamcode.utils.components.Subsystem
import kotlin.math.abs

@Configurable
/** Controller for intake located at the front of the robot.  */
class Intake(private val opMode: OpMode) : Subsystem {
  companion object {
    private var IN_POWER = 1.0;
    private var OUT_POWER = -1.0;
  }

  private val intakeMotor: DcMotor = opMode.hardwareMap.dcMotor.get("intakeMotor");
  private val gamepad = opMode.gamepad2;

  init {
    intakeMotor.mode = RunMode.STOP_AND_RESET_ENCODER;
    intakeMotor.mode = RunMode.RUN_WITHOUT_ENCODER;
    intakeMotor.direction = Direction.REVERSE;
  }

  override fun update() {
    // Switch intake direction or turn it off
    if(gamepad.right_bumper) intakeIn();
    else if(gamepad.left_bumper) intakeOut();
    else intakeStop();
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
    opMode.telemetry.addData(
      "Intake state",
      if(intakeMotor.power > 0) "IN" else "OUT"
    );
    opMode.telemetry.addData("Intake power", intakeMotor.power);
  }
}
