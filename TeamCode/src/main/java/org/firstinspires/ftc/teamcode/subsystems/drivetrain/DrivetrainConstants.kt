package org.firstinspires.ftc.teamcode.subsystems.drivetrain;

import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;

object DrivetrainConstants {
  const val LEFT_FRONT_MOTOR = "leftFrontMotor";
  const val LEFT_BACK_MOTOR = "leftBackMotor";
  const val RIGHT_FRONT_MOTOR = "rightFrontMotor";
  const val RIGHT_BACK_MOTOR = "rightBackMotor";

  @JvmField
  val LFM_DIRECTION = Direction.REVERSE;
  @JvmField
  val LBM_DIRECTION = Direction.REVERSE;
  @JvmField
  val RFM_DIRECTION = Direction.FORWARD;
  @JvmField
  val RBM_DIRECTION = Direction.FORWARD;
}
