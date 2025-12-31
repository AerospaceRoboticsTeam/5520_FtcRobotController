package org.firstinspires.ftc.teamcode.libs.decodeLibs;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.constants.PowerConstants;

/** Controller for intake located at the front of the robot. */
public class Intake2 {
	private final OpMode bot;
	private final DcMotor intakeMotor;

	public Intake2(OpMode opMode) {
		// Attach variable to motor hardware and set up
		bot = opMode;

		intakeMotor = bot.hardwareMap.dcMotor.get("intakeMotor");
		intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		intakeMotor.setDirection(DcMotor.Direction.FORWARD); // TODO: Change to REVERSE if motor rotates in wrong direction
		/*
		rightIntakeMotor = bot.hardwareMap.dcMotor.get("rightIntakeMotor");
		rightIntakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
		rightIntakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
		rightIntakeMotor.setDirection(DcMotor.Direction.REVERSE); // TODO: Change to REVERSE if motor rotates in wrong direction
		*/
	}

	public void intakeIn() {
		intakeMotor.setPower(PowerConstants.INTAKE_IN_POWER);
	}

	public void intakeOut() {
		intakeMotor.setPower(PowerConstants.INTAKE_OUT_POWER);
	}

	public void intakeStop() {
		intakeMotor.setPower(0.0);
	}

	/**
	 * Set a custom intake power and direction.
	 * @param direction True is forward, towards the inside of the robot; False is backward, away from the robot
	 */
	public void intakeCustom(double power, boolean direction){
		power = Math.abs(power);
		if(!direction) power = -power;
		intakeMotor.setPower(power);
	}

	public void getTelemetryData() {
		bot.telemetry.addData("Intake Power: ", intakeMotor.getPower()
		);
	}
}
