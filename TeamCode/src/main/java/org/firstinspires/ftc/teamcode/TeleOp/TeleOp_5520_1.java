package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.Libs.AR.AR_Arm_Fisher;
import org.firstinspires.ftc.teamcode.Libs.AR.MecanumDrive_5518;

@TeleOp(name = "TeleOp_5518_IK", group = "TeleOp")
public class TeleOp_5520_1 extends LinearOpMode
{
    private MecanumDrive_5518 mecanumDrive;
    private AR_Arm_Fisher arm;
    private TouchSensor touch;


    //@Override
    public void runOpMode()
    {
        // Initialize the drivetrain
        mecanumDrive = new MecanumDrive_5518(this);
        arm = new AR_Arm_Fisher(this);
        this.touch = hardwareMap.get(TouchSensor.class, "touch");

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive())
        {
            //telemetry.addData("currentState", currentState);
            //telemetry.addData("lastState", lastState);
            // This call is made every loop and will read the current control pad values (for driving)
            // and update the drivetrain with those values.
            mecanumDrive.drive();
            if (gamepad1.left_trigger != 0) {
                mecanumDrive.setBoost(1);
            }
            else {
                mecanumDrive.setBoost(0.5);
            }
            if (touch.getValue() == 1){
                arm.turnGreen();
            }
            else if(arm.getDetectedColor()==0) {
                arm.turnRed();
            }
            else if(arm.getDetectedColor()==1){
                arm.turnBlue();
            }
            else if (arm.getDetectedColor()==2){
                arm.turnYellow();
            }
            else {
                arm.turnNeutral();
            }
            telemetry.addData("Pressed", touch.getValue());
            arm.updateLight();

            //**************************************************************************************
            // ---------------------Gamepad 2 Controls ---------------------------------------------

            //**************************************************************************************
            //--------------------- TELEMETRY Code -------------------------------------------------
            // Useful telemetry data in case needed for testing and to find heading of robot
            mecanumDrive.getTelemetryData();
            arm.getTelemetry();
            telemetry.update();
        }
    }
}
