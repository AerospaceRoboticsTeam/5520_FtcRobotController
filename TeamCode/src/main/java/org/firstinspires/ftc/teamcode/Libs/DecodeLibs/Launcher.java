package org.firstinspires.ftc.teamcode.Libs.DecodeLibs;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/** Controller for the robot's launcher. */
public class Launcher {
	private final DcMotor launcherMotor;

	public Launcher(DcMotor launcherMotor) { //GET MOTOR FROM HARDWARE MAP VIA iBot
		this.launcherMotor = launcherMotor;
	}

	public void launch() { launcherMotor.setPower(PowerConstants.LAUNCH_POWER); }
}

enum LauncherStatus {
	READY,
	NONE,
	UNREADY,
	UNLOADED,
}
