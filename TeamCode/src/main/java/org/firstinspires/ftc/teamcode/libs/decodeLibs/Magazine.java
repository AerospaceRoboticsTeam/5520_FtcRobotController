package org.firstinspires.ftc.teamcode.libs.decodeLibs;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;

public final class Magazine {
	private final OpMode bot;
	private final CRServo upperServo;
	private final CRServo lowerServo;
	private final Gamepad gamepad;
	private static final double POWER = 0.5;

	public Magazine(OpMode bot) {
		this.bot = bot;
		upperServo = bot.hardwareMap.get(CRServo.class, "upperMagMotor");
		lowerServo = bot.hardwareMap.get(CRServo.class, "lowerMagMotor");;
		gamepad = bot.gamepad2;
	}

	public void update() {
		if(this.gamepad.cross) this.move();
		else stop();
	}

	public void move() {
		upperServo.setPower(0.25);
		lowerServo.setPower(0.5);
	}

	public void stop() {
		upperServo.setPower(0.0);
		lowerServo.setPower(0.0);
	}

	public void getTelemetryData() {
		bot.telemetry.addData("Upper Mag Servo Power", upperServo.getPower());
		bot.telemetry.addData("Lower Mag Servo Power", lowerServo.getPower());
	}
}
