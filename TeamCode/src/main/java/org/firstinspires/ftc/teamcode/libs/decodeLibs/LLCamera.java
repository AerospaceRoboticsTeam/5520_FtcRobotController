package org.firstinspires.ftc.teamcode.libs.decodeLibs;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.apriltags.TagProcessor;

/** Controller for the Limelight itself as well as the servo it's attached to. */
@Configurable
public class LLCamera {
	private final ServoImplEx cameraServo;
	private final TagProcessor tagProcessor;

	private double cameraAngle;

	private static final double rotationRate = 1.0; // The degrees per second the limelight should rotate
	private double lastAngle;

	public LLCamera(LinearOpMode opMode, TagProcessor tagProcessor) {
		cameraServo = opMode.hardwareMap.get(ServoImplEx.class, "limeLightCameraServo");
		this.tagProcessor = tagProcessor;

		cameraAngle = 0;
	}

	public void spin() {}
}
