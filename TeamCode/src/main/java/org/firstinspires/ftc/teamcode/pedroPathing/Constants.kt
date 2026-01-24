package org.firstinspires.ftc.teamcode.pedroPathing

import com.pedropathing.control.FilteredPIDFCoefficients
import com.pedropathing.control.PIDFCoefficients
import com.pedropathing.follower.Follower
import com.pedropathing.follower.FollowerConstants
import com.pedropathing.ftc.FollowerBuilder
import com.pedropathing.ftc.drivetrains.MecanumConstants
import com.pedropathing.ftc.localization.constants.PinpointConstants
import com.pedropathing.paths.PathConstraints
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.DrivetrainConstants

object Constants {
  /**
   * Consist of values from the automatic, PID, and centripetal tuners.
   * <p>For more details, see <a href="https://pedropathing.com/docs/pathing/constants">
   * https://pedropathing.com/docs/pathing/constants</a>.
   */
  val followerConstants: FollowerConstants = FollowerConstants()
    .mass(16.5) // TODO: Set this to the robot's weight when building is complete in KG
    .forwardZeroPowerAcceleration(-25.915156)
    .lateralZeroPowerAcceleration(-67.34249184408006)
    .translationalPIDFCoefficients(
      PIDFCoefficients(0.1, 0.1, 0.1, 0.1)
    )
    .translationalPIDFSwitch(4.0)
    .headingPIDFCoefficients(
      PIDFCoefficients(0.1, 0.1, 0.1, 0.1)
    )
    .drivePIDFCoefficients(
      FilteredPIDFCoefficients(0.1, 0.1, 0.1, 0.1, 0.1)
    )
    .drivePIDFSwitch(15.0)
    .centripetalScaling(0.0005);

  /**
   * Contain constants specific to your drivetrain type. For example, mecanum drivetrain
   * constants contain the motor names.
   * <p>For more details, see <a href="https://pedropathing.com/docs/pathing/constants">
   * https://pedropathing.com/docs/pathing/constants</a>.
   */
  val driveConstants: MecanumConstants = MecanumConstants()
    .maxPower(0.75) // NOTE: Must be a value between 0 and 1
    .leftFrontMotorName(DrivetrainConstants.LEFT_FRONT_MOTOR)
    .leftRearMotorName(DrivetrainConstants.LEFT_BACK_MOTOR)
    .rightFrontMotorName(DrivetrainConstants.RIGHT_FRONT_MOTOR)
    .rightRearMotorName(DrivetrainConstants.RIGHT_BACK_MOTOR)
    .leftFrontMotorDirection(DrivetrainConstants.LFM_DIRECTION)
    .leftRearMotorDirection(DrivetrainConstants.LBM_DIRECTION)
    .rightFrontMotorDirection(DrivetrainConstants.RFM_DIRECTION)
    .rightRearMotorDirection(DrivetrainConstants.RBM_DIRECTION)
    .useBrakeModeInTeleOp(true)
    .xVelocity(45.791001687)
    .yVelocity(38.00510268324003);

  /**
   * Contain constants specific to your localizer. For example, OTOS constants include
   * the hardware map name of the OTOS and the offset.
   * <p>For more details, see <a href="https://pedropathing.com/docs/pathing/constants">
   * https://pedropathing.com/docs/pathing/constants</a>.
   */
  val localizerConstants: PinpointConstants = PinpointConstants()
    .strafePodX(-16.0)
    .forwardPodY(-7.0)
    .distanceUnit(DistanceUnit.CM)
    .hardwareMapName("pinpoint")
    .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
    .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
    .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);

  /**
   * Determine under what conditions a path may end.
   * <p>For more details, see <a href="https://pedropathing.com/docs/pathing/constants">
   * https://pedropathing.com/docs/pathing/constants</a>.
   */
  val pathConstraints: PathConstraints = PathConstraints(
    0.99,
    100.0,
    1.0,
    1.0
  );

  @JvmStatic
  fun createFollower(hardwareMap: HardwareMap): Follower {
    return FollowerBuilder(followerConstants, hardwareMap)
      .mecanumDrivetrain(driveConstants)
      .pinpointLocalizer(localizerConstants)
      .pathConstraints(pathConstraints)
      .build();
  }
}
