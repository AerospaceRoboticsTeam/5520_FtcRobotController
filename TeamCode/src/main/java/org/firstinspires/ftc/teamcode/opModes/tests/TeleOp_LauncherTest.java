package org.firstinspires.ftc.teamcode.opModes.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.opModes.OpModeGroups;

@TeleOp(name = "Launcher Motor Test", group = OpModeGroups.TEST)
public class TeleOp_LauncherTest extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		DcMotor leftLauncherMotor = hardwareMap.get(DcMotor.class, "launcherMotorLeft");
		DcMotor rightLauncherMotor = hardwareMap.get(DcMotor.class, "launcherMotorRight");

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
