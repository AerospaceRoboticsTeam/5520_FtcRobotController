package org.firstinspires.ftc.teamcode.libs.decodeLibs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.constants.PowerConstants;

/** Controller for intake located at the front of the robot. */
public class Intake {
	private final LinearOpMode bot;
	private final DcMotor intakeMotor;

	public Intake(LinearOpMode opMode) {
		// Attach variable to motor hardware and set up
		bot = opMode;
		intakeMotor = bot.hardwareMap.dcMotor.get("intakeMotor");
		intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Not needed but left here for future additions if needed.
		intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // This tells the motor to run with raw power values and not to listen to the built in encoders. We can still get the data we need from the encoders.
		intakeMotor.setDirection(DcMotor.Direction.FORWARD); // TODO: Change to reverse if motor position is reversed
	}

	public void intakeIn() { intakeMotor.setPower(PowerConstants.INTAKE_IN_POWER); }

	public void intakeOut() { intakeMotor.setPower(PowerConstants.INTAKE_OUT_POWER); }

	public void intakeStop() { intakeMotor.setPower(0.0); }

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
		bot.telemetry.addData("Intake state: ", intakeMotor.getPower() > 0 ? "IN" : "OUT");
	}
}
