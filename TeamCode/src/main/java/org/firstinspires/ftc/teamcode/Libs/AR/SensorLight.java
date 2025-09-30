//Light.javapackage.org.firstinspires.ftc.teamcode.Libs.AR;
package org.firstinspires.ftc.teamcode.Libs.AR;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * This class create a Light object that is used to encapsulate all the code used to control and use
 * the 2024-2025 Aerospace Robotics Robot Light and Color Sensor.
 *
 * Instantiate that class for each Light (Currently just one in our design).
 *
 * Creation Date: 11/3/2024
 * Updated: 9/30/2025
 */


public class SensorLight
{
    //public boolean pressed = false;

    private ColorSensor sensor;
    //private TouchSensor touch;

    private LinearOpMode bot;

    AR_Light light;

    //private int lastState = START;
    //private int currentState = START;

    /**
     *
     * @param iBot Handle to the LinearOpMode.
     */
    public SensorLight(LinearOpMode iBot )
    {
        // Take the passed in value and assign to class variables.
        this.bot = iBot;

        //Declare Instances of Light and Color Sensor
        this.sensor = bot.hardwareMap.get(ColorSensor.class, "color_sensor");
        this.light = new AR_Light("status_light", iBot);
        // Set FTC Dashboard Telemetry
    }

    public int getDetectedColor() {
        int red = sensor.red();
        int green = sensor.green();
        int blue = sensor.blue();

        if (red > green && red > blue) {
            return 0; // Red detected
        } else if (blue > red && blue > green) {
            return 1; // Blue detected
        } else if (red > 500 && green > 500 && blue < 500) {
            return 2; // Yellow detected (Red + Green strong, Blue weak)
        } else {
            return -1; // No clear detection
        }
    }

    public void turnBlue(){
        light.blueLight();
    }
    public void turnRed(){
        light.redLight();
    }
    public void turnYellow(){
        light.yellowLight();
    }

    public void turnGreen(){
        light.greenLight();
    }

    public void turnNeutral(){
        light.policeLights();
    }

    public void updateLight(){
        light.updateLight();
    }

    public void getTelemetry(){
        //bot.telemetry.addData("First Joint: ", (jointFirst.getTelemetry()*(360/5281.1)));
        //bot.telemetry.addData("Second Joint: ", (jointSecond.getTelemetry()*(360/5281.1)));
        bot.telemetry.addData("Red", sensor.red());
        bot.telemetry.addData("Blue", sensor.blue());
        bot.telemetry.addData("Green", sensor.green());
    }
}