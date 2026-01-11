package org.firstinspires.ftc.teamcode.opModes.dev.pedroPathing.teleOp;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.opModes.OpModeGroups;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.utils.components.OpModeBase;

@Configurable
@TeleOp(name = "Basic PedroPathing Drivetrain", group = OpModeGroups.PEDRO_PATHING)
class TeleOp_BasicDrivetrain : OpMode(), OpModeBase {
  companion object {
    private const val BASE_MULTIPLIER = 0.5;
    private const val BOOST_MULTIPLIER = 1.0;
  }
  /** The PedroPathing object that allows movement of the robot via PedroPathing methods. */
  private var follower: Follower? = null;

  /**
   * A multiplier to control the sensitivity of inputs to the drive train.
   * Originally known as boost.
   */
  private var multiplier: Double = BASE_MULTIPLIER;

  override fun init() {
    follower = Constants.createFollower(hardwareMap);
    follower!!.update();
  }

  override fun start() {
    follower!!.startTeleOpDrive(true);
  }

  override fun loop() {
    follower!!.update();

    // Provide new input values to pedro pathing for TeleOp driving
    follower!!.setTeleOpDrive(
      -gamepad1.left_stick_y * multiplier,
      -gamepad1.left_stick_x * multiplier,
      -gamepad1.right_stick_x * multiplier,
      false
    );

    // Activate the drivetrain's boost if the left trigger is pressed down
    multiplier =
      if(gamepad1.left_trigger >= 0.25) BOOST_MULTIPLIER;
      else BASE_MULTIPLIER;

    updateTelemetryData();
  }

  override fun updateTelemetryData() {
    telemetry.addData("Bot X pos", "%.2f", follower!!.pose.x);
    telemetry.addData("Bot Y pos", "%.2f", follower!!.pose.y);
    telemetry.addData(
      "Bot heading", "%.2f",
      Math.toDegrees(follower!!.pose.pose.heading)
    );
    telemetry.update();
  }
}
