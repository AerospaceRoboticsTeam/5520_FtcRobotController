package org.firstinspires.ftc.teamcode.libs.decodeLibs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.constants.PowerConstants;

/** Controller for the robot's launcher. */
public class Launcher {
	private final LinearOpMode bot;
	private final DcMotor launcherMotor;

	private LauncherStatus state;

	public Launcher(LinearOpMode opMode) {
		bot = opMode;
		launcherMotor = bot.hardwareMap.get(DcMotor.class, "launcherMotor");
		launcherMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		state = LauncherStatus.WAITING;
	}

	public void launch() {
		launcherMotor.setPower(PowerConstants.LAUNCH_POWER);
		state = LauncherStatus.LAUNCHING;
	}

	public void stopLaunch() {
		launcherMotor.setPower(0);
		state = LauncherStatus.WAITING;
	};

	public void getTelemetryData() {
		bot.telemetry.addData("Launcher status: ", state);
	}
}

enum LauncherStatus {
	WAITING,
	LAUNCHING
}
