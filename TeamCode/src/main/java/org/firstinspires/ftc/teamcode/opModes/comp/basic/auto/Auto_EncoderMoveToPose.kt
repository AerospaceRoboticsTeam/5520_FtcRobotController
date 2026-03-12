package org.firstinspires.ftc.teamcode.opModes.comp.basic.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.opModes.OpModeGroups
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.DrivetrainConstants
import kotlin.math.abs

@Autonomous(name = "Encoder Move To Pose", group = OpModeGroups.BASIC)
class EncoderMoveToPose : LinearOpMode() {
  companion object {
    private const val TICKS_PER_REV = 537.7;
    private const val WHEEL_DIAMETER_IN = 4.0;
    private const val GEAR_RATIO = 1.0;

    private const val TICKS_PER_INCH = (TICKS_PER_REV * GEAR_RATIO) / (Math.PI * WHEEL_DIAMETER_IN);
  }

  data class Pose(val x: Double, val y: Double);

  private lateinit var lf: DcMotor;
  private lateinit var rf: DcMotor;
  private lateinit var lb: DcMotor;
  private lateinit var rb: DcMotor;

  private val startPose = Pose(0.0, 0.0);
  private val endPose = Pose(24.0, 12.0); // Inches

  override fun runOpMode() {
    lf = hardwareMap.get(
      DcMotor::class.java,
      DrivetrainConstants.LEFT_FRONT_MOTOR
    );
    rf = hardwareMap.get(
      DcMotor::class.java,
      DrivetrainConstants.RIGHT_FRONT_MOTOR
    );
    lb = hardwareMap.get(
      DcMotor::class.java,
      DrivetrainConstants.LEFT_BACK_MOTOR
    );
    rb = hardwareMap.get(
      DcMotor::class.java,
      DrivetrainConstants.RIGHT_BACK_MOTOR
    );

    lf.direction = DrivetrainConstants.LFM_DIRECTION;
    rf.direction = DrivetrainConstants.RFM_DIRECTION;
    lb.direction = DrivetrainConstants.LBM_DIRECTION;
    rb.direction = DrivetrainConstants.RBM_DIRECTION;

    resetEncoders();
    waitForStart();

    moveToPose(startPose, endPose, 0.4);
  }

  private fun moveToPose(start: Pose, end: Pose, power: Double) {
    val dx = end.x - start.x;
    val dy = end.y - start.y;

    // Convert XY movement into mecanum wheel distances
    val fl = dy + dx;
    val fr = dy - dx;
    val bl = dy - dx;
    val br = dy + dx;

    val max = listOf(fl, fr, bl, br).maxOf{ abs(it) };
    val scale = 1.0 / max;

    setTarget(fl * scale, fr * scale, bl * scale, br * scale, power);
    runToPosition();
  }

  private fun setTarget(fl: Double, fr: Double, bl: Double, br: Double, power: Double) {
    lf.targetPosition = (fl * TICKS_PER_INCH).toInt();
    rf.targetPosition = (fr * TICKS_PER_INCH).toInt();
    lb.targetPosition = (bl * TICKS_PER_INCH).toInt();
    rb.targetPosition = (br * TICKS_PER_INCH).toInt();

    listOf(lf, rf, lb, rb).forEach{ it.power = power };
  }

  private fun runToPosition() {
    listOf(lf, rf, lb, rb).forEach{
      it.mode = DcMotor.RunMode.RUN_TO_POSITION;
    }

    while(opModeIsActive() && (lf.isBusy || rf.isBusy || lb.isBusy || rb.isBusy)) {
      idle();
    }

    stopMotors();
  }

  private fun resetEncoders() {
    listOf(lf, rf, lb, rb).forEach{
      it.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER;
      it.mode = DcMotor.RunMode.RUN_USING_ENCODER;
    }
  }

  private fun stopMotors() {
    listOf(lf, rf, lb, rb).forEach{ it.power = 0.0 };
  }
}
