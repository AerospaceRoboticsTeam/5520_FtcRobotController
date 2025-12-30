package org.firstinspires.ftc.teamcode.libs.decodeLibs;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.apriltags.TagProcessor;
import org.firstinspires.ftc.teamcode.constants.ArtifactNumRef;

/** Controller for the robot's magazine motor. */
public class MagMotor {
	private final OpMode bot;
	private final DcMotorEx magMotor;

	private double magMotorPower = 0;

	public MagMotor(OpMode opMode) {
		bot = opMode;

		magMotor = bot.hardwareMap.get(DcMotorEx.class, "magMotor");

		magMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		magMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

		magMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
	}

	public void stopMotor() {
		magMotor.setPower(0);
	}

	public void setPower(double power) {
		magMotorPower = power;
		magMotor.setPower(magMotorPower);
	}

	public void turnOn(){
		magMotor.setPower(magMotorPower);
	}

	public void go(){
		double joystickVal = bot.gamepad2.left_stick_y;
		magMotor.setPower(joystickVal);
	}

	public void getTelemetryData() {
		bot.telemetry.addData("Magazine Motor Power: ", magMotorPower);
	}
}


