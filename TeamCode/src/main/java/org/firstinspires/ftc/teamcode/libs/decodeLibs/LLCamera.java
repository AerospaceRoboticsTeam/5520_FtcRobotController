package org.firstinspires.ftc.teamcode.libs.decodeLibs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class LLCamera {
	private final Servo cameraServo;

	public LLCamera(LinearOpMode opMode) {
		cameraServo = opMode.hardwareMap.get(Servo.class, "limeLightCameraServo");

	}
}
