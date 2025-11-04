package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;

import com.pedropathing.follower.Follower;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.constants.DeviceConstants;

public class Constants {
	/**
	 * Consist of values from the automatic, PID, and centripetal tuners.
	 * <p>For more details, see <a href="https://pedropathing.com/docs/pathing/constants">
	 * https://pedropathing.com/docs/pathing/constants</a>.
	 */
	public static FollowerConstants followerConstants = new FollowerConstants()
		.mass(5)
		.useSecondaryTranslationalPIDF(true)
		.useSecondaryHeadingPIDF(true)
		.useSecondaryDrivePIDF(true); // TODO: Set this to the robot's weight when building is complete in KG

	/**
	 * Contain constants specific to your drivetrain type. For example, mecanum drivetrain
	 * constants contain the motor names.
	 * <p>For more details, see <a href="https://pedropathing.com/docs/pathing/constants">
	 * https://pedropathing.com/docs/pathing/constants</a>.
	 */
	public static MecanumConstants driveConstants = new MecanumConstants()
		.maxPower(1) // NOTE: Must be a value between 0 and 1
		.leftFrontMotorName(DeviceConstants.DriveTrain.LEFT_FRONT_MOTOR)
		.leftRearMotorName(DeviceConstants.DriveTrain.LEFT_BACK_MOTOR)
		.rightFrontMotorName(DeviceConstants.DriveTrain.RIGHT_FRONT_MOTOR)
		.rightRearMotorName(DeviceConstants.DriveTrain.RIGHT_BACK_MOTOR)
		.leftFrontMotorDirection(DeviceConstants.DriveTrain.LFM_DIRECTION)
		.leftRearMotorDirection(DeviceConstants.DriveTrain.LBM_DIRECTION)
		.rightFrontMotorDirection(DeviceConstants.DriveTrain.RFM_DIRECTION)
		.rightRearMotorDirection(DeviceConstants.DriveTrain.RBM_DIRECTION);

	/**
	 * Contain constants specific to your localizer. For example, OTOS constants include
	 * the hardware map name of the OTOS and the offset.
	 * <p>For more details, see <a href="https://pedropathing.com/docs/pathing/constants">
	 * https://pedropathing.com/docs/pathing/constants</a>.
	 */
	public static PinpointConstants localizerConstants = new PinpointConstants()
		.strafePodX(0.5)
		.forwardPodY(-5)
		.distanceUnit(DistanceUnit.CM)
		.hardwareMapName("pinpoint")
		.encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
		.forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
		.strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD);

	/**
	 * Determine under what conditions a path may end.
	 * <p>For more details, see <a href="https://pedropathing.com/docs/pathing/constants">
	 * https://pedropathing.com/docs/pathing/constants</a>.
	 */
	public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

	public static Follower createFollower(HardwareMap hardwareMap) {
		return new FollowerBuilder(followerConstants, hardwareMap)
			.mecanumDrivetrain(driveConstants)
			.pinpointLocalizer(localizerConstants)
			.pathConstraints(pathConstraints)
			.build();
	}
}
