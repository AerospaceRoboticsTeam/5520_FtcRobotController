package org.firstinspires.ftc.teamcode.teleop.dev;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.apriltags.TagProcessor;
import org.firstinspires.ftc.teamcode.constants.ArtifactNumRef;
import org.firstinspires.ftc.teamcode.constants.OpModeGroups;
import org.firstinspires.ftc.teamcode.constants.PowerConstants;
import org.firstinspires.ftc.teamcode.constants.Team;
import org.firstinspires.ftc.teamcode.libs.decodeLibs.Intake;
import org.firstinspires.ftc.teamcode.libs.decodeLibs.Launcher;
import org.firstinspires.ftc.teamcode.libs.decodeLibs.LauncherBasic;
import org.firstinspires.ftc.teamcode.mecanumdrive.MecanumDrive;
import org.firstinspires.ftc.teamcode.libs.decodeLibs.MagMotor;
import org.firstinspires.ftc.teamcode.libs.decodeLibs.Intake2;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Drive Motor Configuration Test", group = OpModeGroups.dev)
public class DriveMotorConfigTest extends LinearOpMode {

	private DcMotor frontLeft;
	private DcMotor frontRight;
	private DcMotor backLeft;
	private DcMotor backRight;

	@SuppressLint("SuspiciousIndentation")
	@Override
	public void runOpMode() throws InterruptedException {
		// Initialize mechanisms

		// Wait for Op mode to start and cancel startup if stopped
		waitForStart();
		if(isStopRequested()) return;

		frontLeft = hardwareMap.get(DcMotor.class, "frontLeftMotor");
		frontRight = hardwareMap.get(DcMotor.class, "frontRightMotor");
		backLeft = hardwareMap.get(DcMotor.class, "backLeftMotor");
		backRight = hardwareMap.get(DcMotor.class, "backRightMotor");

		frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
		backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

		// Run main loop
		while(opModeIsActive() && !isStopRequested()) {
			frontLeft.setPower(0.8);
			sleep(1000);
			frontRight.setPower(0.8);
			sleep(1000);
			backLeft.setPower(0.8);
			sleep(1000);
			backRight.setPower(0.8);
			sleep(1000);
		}
	}
}
