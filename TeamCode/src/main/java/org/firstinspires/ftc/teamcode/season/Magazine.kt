package org.firstinspires.ftc.teamcode.season

import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.robotcore.eventloop.opmode.OpMode

import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction
import org.firstinspires.ftc.teamcode.utils.components.Subsystem

@Configurable
class Magazine(private val opMode: OpMode) : Subsystem {
  companion object {
    var POWER = 0.75;

    var UPPER_SERVO_DIRECTION = Direction.FORWARD;
    var LOWER_SERVO_DIRECTION = Direction.REVERSE;
  }

  /** Super Speed - Max 230 RPM. */
  private val upperServo: CRServo = opMode.hardwareMap.get(
    CRServo::class.java, "upperMagMotor"
  );
  /** Speed - Max 115 RPM. */
  private val lowerServo: CRServo = opMode.hardwareMap.get(
    CRServo::class.java, "lowerMagMotor"
  );
  private val gamepad = opMode.gamepad2;

  init {
    upperServo.direction = UPPER_SERVO_DIRECTION;
    lowerServo.direction = LOWER_SERVO_DIRECTION;
  }

  override fun update() {
    // Move both servos
    if(gamepad.circle) moveUp();
    else if(gamepad.cross) moveDown();
    else if(
      !gamepad.dpad_up &&
      !gamepad.dpad_down &&
      !gamepad.dpad_left &&
      !gamepad.dpad_right
    ) stop();

    // Move upper servo
    if(gamepad.dpad_up) moveUpUpper();
    else if(gamepad.dpad_right) moveDownUpper();
    else if(!gamepad.cross && !gamepad.circle) stopUpper();

    // Move lower servo
    if(gamepad.dpad_down) moveUpLower();
    else if(gamepad.dpad_left) moveDownLower();
    else if(!gamepad.cross && gamepad.circle) stopLower();
  }

  fun moveUp() {
    upperServo.power = POWER;
    lowerServo.power = POWER;
  }

  fun moveDown() {
    upperServo.power = -POWER;
    lowerServo.power = -POWER;
  }

  fun moveUpUpper() {
    upperServo.power = POWER;
  }

  fun moveUpLower() {
    lowerServo.power = POWER;
  }

  fun moveDownUpper() {
    upperServo.power = -POWER;
  }

  fun moveDownLower() {
    lowerServo.power = -POWER;
  }

  fun stop() {
    upperServo.power = 0.0;
    lowerServo.power = 0.0;
  }

  fun stopUpper() {
    upperServo.power = 0.0;
  }

  fun stopLower() {
    lowerServo.power = 0.0;
  }

  override fun getTelemetryData() {
    opMode.telemetry.addData("Upper Mag Servo Power", upperServo.power);
    opMode.telemetry.addData("Lower Mag Servo Power", lowerServo.power);
  }
}
