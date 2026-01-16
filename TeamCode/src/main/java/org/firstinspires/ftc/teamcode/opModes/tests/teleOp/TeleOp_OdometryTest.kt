package org.firstinspires.ftc.teamcode.opModes.tests.teleOp

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.libs.goBilda.GoBildaPinpointDriver
import org.firstinspires.ftc.teamcode.opModes.OpModeGroups
import org.firstinspires.ftc.teamcode.utils.components.OpModeBase

@TeleOp(name = "Odometry Test", group = OpModeGroups.TEST)
class TeleOp_OdometryTest : OpMode(), OpModeBase {
  private lateinit var pinpoint: GoBildaPinpointDriver;

  override fun init() {
    pinpoint = hardwareMap.get(GoBildaPinpointDriver::class.java, "pinpoint");
    pinpoint.setOffsets(0.5, -5.0, DistanceUnit.METER);
  }

  override fun loop() {
    pinpoint.update();

    updateTelemetryData();
  }

  override fun updateTelemetryData() {
    telemetry.addData("X Pos",  "${pinpoint.getPosX(DistanceUnit.CM)} CM");
    telemetry.addData("Y Pos", "${pinpoint.getPosY(DistanceUnit.CM)} CM");
    telemetry.addData("Heading", "${pinpoint.getHeading(AngleUnit.DEGREES)} DEG");
    telemetry.update();
  }
}
