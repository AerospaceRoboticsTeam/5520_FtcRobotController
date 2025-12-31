package org.firstinspires.ftc.teamcode.libs.decodeLibs;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

public class IntakeSensors {
	private NormalizedColorSensor colorSensor;

	public IntakeSensors(OpMode opMode) {
		colorSensor = opMode.hardwareMap.get(NormalizedColorSensor.class, "intakeColorSensor");
	}
	/*
	public SensorColor getDetectedColor() {
		NormalizedRGBA colors = colorSensor.getNormalizedColors();


	}
	 */
}
