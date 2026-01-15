package org.firstinspires.ftc.teamcode.subsystems.drivetrain

import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import org.firstinspires.ftc.teamcode.utils.components.Subsystem

class PPMecanumDrive(private val opMode: OpMode, startPos: Pose) : Subsystem {
  companion object {
    private const val BASE_MULTIPLIER = 0.5;
    private const val BOOST_MULTIPLIER = 1.0;
  }

  /** The PedroPathing object that allows movement of the robot via PedroPathing methods.  */
  private val follower = Constants.createFollower(opMode.hardwareMap);
  private val gamepad = opMode.gamepad1;

  /**
   * A multiplier to control the sensitivity of inputs to the drive train.
   * Originally known as boost.
   */
  private var multiplier = BASE_MULTIPLIER;
  private var isRobotCentric = false;

  init {
    follower.update();
    follower.setStartingPose(startPos);
  }

  constructor(opMode: OpMode) : this(opMode, Pose(0.0, 0.0, 0.0));

  fun start() {
    follower.startTeleOpDrive(true);
  }

  override fun update() {
    follower.update();

    multiplier = if(gamepad.left_trigger >= 0.25) BOOST_MULTIPLIER else BASE_MULTIPLIER;
    if(!gamepad.crossWasPressed() && gamepad.cross) isRobotCentric = !isRobotCentric;

    // Provide new input values to pedro pathing for TeleOp driving
    follower.setTeleOpDrive(
      -gamepad.left_stick_y * multiplier,
      -gamepad.left_stick_x * multiplier,
      -gamepad.right_stick_x * multiplier,
      isRobotCentric
    );
  }

  override fun getTelemetryData() {
    opMode.telemetry.addData("Bot X Pos", "%.2f", follower.pose.x);
    opMode.telemetry.addData("Bot Y Pos", "%.2f", follower.pose.y);
    opMode.telemetry.addData(
      "Bot Heading", "%.2f",
      Math.toDegrees(follower.pose.heading)
    );
    opMode.telemetry.addData(
      "Reference Frame", if(isRobotCentric) "Robot-Centric" else "Field-Centric"
    );
  }
}
