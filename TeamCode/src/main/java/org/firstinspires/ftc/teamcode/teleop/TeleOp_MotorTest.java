package org.firstinspires.ftc.teamcode.teleop;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Basic TeleOp", group = "TeleOp")
public class TeleOp_MotorTest extends LinearOpMode {

	@SuppressLint("SuspiciousIndentation")
	@Override
	public void runOpMode() throws InterruptedException {
		DcMotor leftLauncherMotor = hardwareMap.get(DcMotor.class, "leftLauncherMotor");
		DcMotor rightLauncherMotor = hardwareMap.get(DcMotor.class, "rightLauncherMotor");

		waitForStart();

		// Wait for Op mode to start and cancel startup if stopped
		if(isStopRequested()) return;

		// Run main loop
		while(opModeIsActive() && !isStopRequested()) {
			if(gamepad1.a) {
				leftLauncherMotor.setPower(gamepad1.left_trigger);
				rightLauncherMotor.setPower(gamepad1.right_trigger);
				break;
			}

			leftLauncherMotor.setPower(1);
			rightLauncherMotor.setPower(1);

			telemetry.addData("Left Motor Power", leftLauncherMotor.getPower());
			telemetry.addData("Right Motor Power", rightLauncherMotor.getPower());
			telemetry.update();
		}
	}
}
