package org.firstinspires.ftc.teamcode.opModes.tests.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.subsystems.drivetrain.DrivetrainConstants;

@TeleOp(name = "Motor & Encoder Button Test", group = "Diagnostics")
public class TeleOp_DrivetrainWiring extends LinearOpMode {
	DcMotor frontLeft;
	DcMotor frontRight;
	DcMotor backLeft;
	DcMotor backRight;

	@Override
	public void runOpMode() {
		// Map motors (names must match Driver Station config)
		frontLeft  = hardwareMap.get(DcMotor.class, DrivetrainConstants.LEFT_FRONT_MOTOR);
		frontRight = hardwareMap.get(DcMotor.class, DrivetrainConstants.RIGHT_FRONT_MOTOR);
		backLeft   = hardwareMap.get(DcMotor.class, DrivetrainConstants.LEFT_BACK_MOTOR);
		backRight  = hardwareMap.get(DcMotor.class, DrivetrainConstants.RIGHT_BACK_MOTOR);

		// Reset encoders
		frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

		// Enable encoder reading
		frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		telemetry.addLine("Motor & Encoder Button Test Ready");
		telemetry.addLine("A=FL  B=FR  X=BL  Y=BR");
		telemetry.update();

		waitForStart();

		while (opModeIsActive()) {

			double power = 0.4;

			// Button → motor mapping
			frontLeft.setPower(gamepad1.a ? power : 0);
			frontRight.setPower(gamepad1.b ? power : 0);
			backLeft.setPower(gamepad1.x ? power : 0);
			backRight.setPower(gamepad1.y ? power : 0);

			// Encoder telemetry
			telemetry.addData("Front Left",  frontLeft.getCurrentPosition());
			telemetry.addData("Front Right", frontRight.getCurrentPosition());
			telemetry.addData("Back Left",   backLeft.getCurrentPosition());
			telemetry.addData("Back Right",  backRight.getCurrentPosition());

			telemetry.update();
		}
	}
}
