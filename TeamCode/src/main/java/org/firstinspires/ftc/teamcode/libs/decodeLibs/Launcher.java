package org.firstinspires.ftc.teamcode.libs.decodeLibs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.constants.ArtifactNumRef;
import org.firstinspires.ftc.teamcode.constants.PowerConstants;

/** Controller for the robot's launcher. */
public class Launcher {
	private final LinearOpMode bot;
	private final DcMotor launcherMotorL;
	private final DcMotor launcherMotorR;

	private LauncherStatus state;
	private final ArtifactNumRef artifactNum = new ArtifactNumRef(0);

	public Launcher(LinearOpMode opMode) {
		bot = opMode;
		launcherMotorL = bot.hardwareMap.get(DcMotor.class, "launcherMotorL");
		launcherMotorL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		launcherMotorR = bot.hardwareMap.get(DcMotor.class, "launcherMotorR");
		launcherMotorR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		state = LauncherStatus.WAITING;
	}

	public void launch(double motorPowerL, double motorPowerR) {
		launcherMotorR.setPower(motorPowerR);
		launcherMotorL.setPower(motorPowerL);
		state = LauncherStatus.LAUNCHING;
	}

	public void stopLaunch() {
		launcherMotorR.setPower(0);
		launcherMotorL.setPower(0);
		state = LauncherStatus.WAITING;
	}

	public void getTelemetryData() {
		bot.telemetry.addData("Launcher status: ", state);
	}
}

enum LauncherStatus {
	WAITING,
	LAUNCHING
}
