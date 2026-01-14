package org.firstinspires.ftc.teamcode.season

import com.qualcomm.robotcore.eventloop.opmode.OpMode

import com.qualcomm.robotcore.hardware.CRServo
import org.firstinspires.ftc.teamcode.utils.components.Subsystem

class Magazine(private val bot: OpMode) : Subsystem {
  companion object {
    const val POWER = 0.5;
  }

  /** Super Speed - Max 230 RPM. */
  private val upperServo: CRServo = bot.hardwareMap.get(
    CRServo::class.java, "upperMagMotor"
  );
  /** Speed - Max 115 RPM. */
  private val lowerServo: CRServo = bot.hardwareMap.get(
    CRServo::class.java, "lowerMagMotor"
  );
  private val gamepad = bot.gamepad2;

  override fun update() {
    if(gamepad.cross) move();
    else stop();
  }

  fun move() {
    upperServo.power = POWER / 2;
    lowerServo.power = POWER;
  }

  fun stop() {
    upperServo.power = 0.0;
    lowerServo.power = 0.0;
  }

  override fun getTelemetryData() {
    bot.telemetry.addData("Upper Mag Servo Power", upperServo.power);
    bot.telemetry.addData("Lower Mag Servo Power", lowerServo.power);
  }
}
