package org.firstinspires.ftc.teamcode.season;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import org.firstinspires.ftc.teamcode.utils.components.Subsystem
import org.firstinspires.ftc.teamcode.subsystems.vision.limelight.LimelightProcessor;

/** Controller for the Limelight itself as well as the servo it's attached to. */
@Configurable
class LLCamera(opMode: OpMode, private val llProcessor: LimelightProcessor) : Subsystem {
  companion object {
    private const val ROTATION_RATE = 1.0 // The degrees per second the limelight should rotate
  }

  private val cameraServo: ServoImplEx = opMode.hardwareMap.get(
    ServoImplEx::class.java,
    "limeLightCameraServo"
  );

  private val cameraAngle = 0.0;
  private val lastAngle = 0.0;

  fun spin() {}

  override fun getTelemetryData() {
    TODO("Not yet implemented");
  }
}
