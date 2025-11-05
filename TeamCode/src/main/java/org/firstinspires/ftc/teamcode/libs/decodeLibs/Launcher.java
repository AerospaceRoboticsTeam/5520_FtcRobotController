package org.firstinspires.ftc.teamcode.libs.decodeLibs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.constants.ArtifactNumRef;

/** Controller for the robot's launcher. */
public class Launcher {
	private final LinearOpMode bot;
	private final DcMotor launcherMotorLeft;
	private final DcMotor launcherMotorRight;

	private LauncherStatus state;
	private final ArtifactNumRef artifactNum = new ArtifactNumRef(0);

	public Launcher(LinearOpMode opMode) {
		bot = opMode;
		launcherMotorLeft = bot.hardwareMap.get(DcMotor.class, "launcherMotorLeft");
		launcherMotorLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		launcherMotorRight = bot.hardwareMap.get(DcMotor.class, "launcherMotorRight");
		launcherMotorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		state = LauncherStatus.IDLE;
	}

	public void launch(double motorPowerLeft, double motorPowerRight) {
		launcherMotorLeft.setPower(motorPowerLeft);
		launcherMotorRight.setPower(motorPowerRight);
		state = LauncherStatus.LAUNCHING;
	}

	public void stopLaunch() {
		launcherMotorRight.setPower(0);
		launcherMotorLeft.setPower(0);
		state = LauncherStatus.IDLE;
	}

	public int[] linearVelocityToPower(float leftMotorVelocity, float rightMotorVelocity) {


		return new int[]{};
	}

	public void getTelemetryData() {
		bot.telemetry.addData("Launcher status: ", state);
	}
}

enum LauncherStatus {
	IDLE,
	LAUNCHING,
	READY
}
