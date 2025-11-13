package org.firstinspires.ftc.teamcode.libs.decodeLibs;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.HashMap;

public class LightController {
	private final OpMode bot;
	private final Servo light;
	private final LightState state;

	public LightController(OpMode opMode) {
		bot = opMode;
		light = opMode.hardwareMap.get(Servo.class, "indicatorLight");
		state = new LightState();

		setMode(LightMode.OFF);
	}

	public void setMode(LightMode mode) {
		state.modeState = mode;
		light.setPosition(state.getModeValue(mode));
	}

	public void getTelemetryData() {
		bot.telemetry.addData("Light Mode/State: ", state.modeState);
	}
}

class LightState {
	private static final HashMap<LightMode, Double> colors = new HashMap<>();
	public LightMode modeState;

	public LightState() {
		modeState = LightMode.OFF;

		colors.put(LightMode.OFF, 0.0);
		colors.put(LightMode.RED, 0.279);
		colors.put(LightMode.ORANGE, 0.333);
		colors.put(LightMode.YELLOW, 0.388);
		colors.put(LightMode.LIME, 0.444);
		colors.put(LightMode.GREEN, 0.5);
		colors.put(LightMode.AQUA, 0.555);
		colors.put(LightMode.BLUE, 0.611);
		colors.put(LightMode.PURPLE, 0.666);
		colors.put(LightMode.PINK, 0.722);
		colors.put(LightMode.WHITE, 1.0);
	}

	public double getModeValue(LightMode mode) {
		return colors.get(mode);
	}
}

enum LightMode {
	OFF,
	RED,
	ORANGE,
	YELLOW,
	LIME,
	GREEN,
	AQUA,
	BLUE,
	PURPLE,
	PINK,
	WHITE
}
