package org.firstinspires.ftc.teamcode.opModes.dev.complete.teleOp

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.season.Intake;
import org.firstinspires.ftc.teamcode.season.launcher.Launcher;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.utils.Team;
import org.firstinspires.ftc.teamcode.utils.components.OpModeBase
import org.firstinspires.ftc.teamcode.opModes.OpModeGroups;
import org.firstinspires.ftc.teamcode.subsystems.vision.limelight.LimelightProcessor;

@Configurable
@TeleOp(name = "PedroPathing Complete", group = OpModeGroups.DEV_COMPLETE)
class TeleOp_Complete : OpMode(), OpModeBase {
  companion object {
    private const val BASE_MULTIPLIER = 0.5;
    private const val BOOST_MULTIPLIER = 1.0;

    /**
     * A multiplier to control the sensitivity of inputs to the drive train.
     * Originally known as boost.
     */
    private var multiplier: Double = BASE_MULTIPLIER;
  }

  /** A manager for telemetry data inside of Panels.  */
  private var telemetryManager: TelemetryManager? = null;

  /** The PedroPathing object that allows movement of the robot via PedroPathing methods.  */
  private lateinit var follower: Follower;

  /** Determines how the robot moves.  */
  private lateinit var state: ControlState;
  private var automatedDrive = false;
  private var previousRotationPos: Pose? = null;

  private lateinit var llProcessor: LimelightProcessor;
  private lateinit var intake: Intake;
  private lateinit var launcher: Launcher;

  override fun init() {
    llProcessor = LimelightProcessor(
      this, if(gamepad1.a) Team.RED else Team.BLUE
    );
    intake = Intake(this);
    launcher = Launcher(this, llProcessor);

    telemetryManager = null; // TODO: Initialize telemetry manager for panels
    follower = Constants.createFollower(hardwareMap);
    follower.update();

    state = ControlState.MANUAL;
    automatedDrive = false;
    previousRotationPos = null;
  }

  override fun start() {
    follower.startTeleOpDrive(true);
    llProcessor.startLimelight();
  }

  override fun loop() {
    follower.update();

    when(state) {
      ControlState.MANUAL -> {
        // Switch PedroPathing to TeleOp mode
        if(automatedDrive) {
          automatedDrive = false;
          follower.startTeleOpDrive(true);
        }

        // Provide new input values to pedro pathing for TeleOp driving
        follower.setTeleOpDrive(
          -gamepad1.left_stick_y * multiplier,
          -gamepad1.left_stick_x * multiplier,
          -gamepad1.right_stick_x * multiplier,
          true
        );
      }

      ControlState.MAINTAIN_ANGLE_WITH_GOAL -> run {
        // Switch PedroPathing to path following mode
        if(!automatedDrive) {
          automatedDrive = true;
          follower.startTeleOpDrive(true);
        }

        // Get position data
        val cameraVectorsToGoal = llProcessor.getVectorsToGoalTag();
        val vectorsToGoal = cameraVectorsToGoal; // TODO: Use ODO to get vectors to goal
        val orientation = vectorsToGoal!!.orientation;

        // Don't move the robot if it's already facing the goal
        if(orientation.yaw >= -0.1 && orientation.yaw <= 0.1) return@run;

        if(follower.isBusy && follower.currentPath != null) {
          // If the robot is in the same position, do nothing
          if(
            follower.pose.x >= previousRotationPos!!.x - 0.1 &&
            follower.pose.x <= previousRotationPos!!.x + 0.1 &&
            follower.pose.y >= previousRotationPos!!.y - 0.1 &&
            follower.pose.y <= previousRotationPos!!.y + 0.1
          ) return@run;

          // Stop the follower if the robot is no longer in the same position to recalculate heading
          follower.breakFollowing();
        }

        val currentPos = follower.pose;
        previousRotationPos = currentPos;
        val targetHeading = currentPos.heading - Math.toRadians(orientation.yaw);
        val targetPos = Pose(currentPos.x, currentPos.y, targetHeading);

        val turnPath = follower.pathBuilder()
          .addPath(BezierLine(currentPos, targetPos))
          .setLinearHeadingInterpolation(currentPos.heading, targetHeading)
          .build();

        follower.followPath(turnPath);
      }
    }

    // Activate the drivetrain's boost if the left trigger is pressed down
    multiplier =
      if(gamepad1.left_trigger >= 0.25) BOOST_MULTIPLIER;
      else BASE_MULTIPLIER;

    // Switch between the MANUAL and MAINTAIN_ANGLE_WITH_GOAL states
    if(gamepad1.a) state =
      if(state == ControlState.MANUAL) ControlState.MAINTAIN_ANGLE_WITH_GOAL;
      else ControlState.MANUAL;

    /* TODO: Add input handlers for controlling other hardware */

    // Switch intake direction or turn it off
    if(gamepad1.left_bumper) intake.intakeIn();
    else if(gamepad1.right_bumper) intake.intakeOut();
    else if(gamepad1.y) intake.intakeStop();

    // Activate the launcher if the right trigger is pressed down
    if(gamepad1.right_trigger >= 0.25) launcher.activateMotors(
      gamepad1.right_trigger.toDouble(),
      gamepad1.right_trigger.toDouble()
    );
    else launcher.stopMotors();
  }

  override fun updateTelemetryData() {
    telemetry.addData("Bot X Pos", "%.2f", follower.pose.x);
    telemetry.addData("Bot Y Pos", "%.2f", follower.pose.y);
    telemetry.addData(
      "Bot Heading", "%.2f",
      Math.toDegrees(follower.pose.heading)
    );
    intake.getTelemetryData();
    launcher.getTelemetryData();
    telemetry.update();
  }
}

internal enum class ControlState {
  MANUAL,
  MAINTAIN_ANGLE_WITH_GOAL
}
