package org.firstinspires.ftc.teamcode.Libs.DecodeLibs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

public class IntakeLib {
    private DcMotor intakeMotor;
    public IntakeLib(LinearOpMode iBot, String motorName)
    {
        // Attach variable to motor hardware and set up.
        this.intakeMotor = iBot.hardwareMap.dcMotor.get(motorName);
        this.intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Not needed but left here for future additions if needed.
        this.intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);  // This tells the motor to run with raw power values and not to listen to the built in encoders. We can still get the data we need from the encoders.
        this.intakeMotor.setDirection(DcMotor.Direction.FORWARD); //Change to reverse if motor position is reversed
        // Instantiate new PID Controller for this joint.
        //this.newPID = new AR_PIDController(iBot, jointMotor, iJointName, iP, iI, iD, iF, fuzzyLogicActive);
    }

    public void intakeIn(){
        this.intakeMotor.setPower(0.8);
    }

    public void intakeOut(){
        this.intakeMotor.setPower(-0.8);
    }

    public void intakeStop(){
        this.intakeMotor.setPower(0.0);
    }

    public void intakeCustom(double power, boolean direction){
        power = Math.abs(power);
        if (direction){
            power = power;
        } else {
            power = -power;
        }
        this.intakeMotor.setPower(power);
    }
}
