package org.firstinspires.ftc.teamcode.libs.decodeLibs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.apriltags.TagProcessor;

/** Controller for the Limelight itself as well as the servo it's attached to. */
//TODO: Update based on Pipelines, as outlined in the Programming Notes.
public class LLCamera {
	private final Servo cameraServo;
	private final TagProcessor tagProcessor;

	public LLCamera(LinearOpMode opMode, TagProcessor tagProcessor) {
		cameraServo = opMode.hardwareMap.get(Servo.class, "limeLightCameraServo");
		this.tagProcessor = tagProcessor;
	}
}
