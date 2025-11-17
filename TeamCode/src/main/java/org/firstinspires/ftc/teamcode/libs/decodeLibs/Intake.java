package org.firstinspires.ftc.teamcode.libs.decodeLibs;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.constants.PowerConstants;

/** Controller for intake located at the front of the robot. */
public class Intake {
	private final OpMode bot;
	private final DcMotor leftIntakeMotor;
	private final DcMotor rightIntakeMotor;

	public Intake(OpMode opMode) {
		// Attach variable to motor hardware and set up
		bot = opMode;

		leftIntakeMotor = bot.hardwareMap.dcMotor.get("leftIntakeMotor");
		leftIntakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		leftIntakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		leftIntakeMotor.setDirection(DcMotor.Direction.FORWARD); // TODO: Change to REVERSE if motor rotates in wrong direction

		rightIntakeMotor = bot.hardwareMap.dcMotor.get("rightIntakeMotor");
		rightIntakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		rightIntakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		rightIntakeMotor.setDirection(DcMotor.Direction.REVERSE); // TODO: Change to REVERSE if motor rotates in wrong direction

	}

	public void intakeIn() {
		leftIntakeMotor.setPower(PowerConstants.INTAKE_IN_POWER);
		rightIntakeMotor.setPower(PowerConstants.INTAKE_IN_POWER);
	}

	public void intakeOut() {
		leftIntakeMotor.setPower(PowerConstants.INTAKE_OUT_POWER);
		rightIntakeMotor.setPower(PowerConstants.INTAKE_OUT_POWER);
	}

	public void intakeStop() {
		leftIntakeMotor.setPower(0.0);
		rightIntakeMotor.setPower(0.0);
	}

	/**
	 * Set a custom intake power and direction.
	 * @param direction True is forward, towards the inside of the robot; False is backward, away from the robot
	 */
	public void intakeCustom(double power, boolean direction){
		power = Math.abs(power);
		if(!direction) power = -power;
		leftIntakeMotor.setPower(power);
		rightIntakeMotor.setPower(power);
	}

	public void getTelemetryData() {
		bot.telemetry.addData("Intake state: ",
			leftIntakeMotor.getPower() > 0 && rightIntakeMotor.getPower() > 0 ? "IN" : "OUT"
		);
	}
}
