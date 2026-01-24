package org.firstinspires.ftc.teamcode.opModes.comp.teleOp

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.teamcode.opModes.OpModeGroups
import org.firstinspires.ftc.teamcode.season.Intake
import org.firstinspires.ftc.teamcode.season.LightController
import org.firstinspires.ftc.teamcode.season.Magazine
import org.firstinspires.ftc.teamcode.season.launcher.LauncherAdvanced
import org.firstinspires.ftc.teamcode.season.launcher.LauncherBasic
import org.firstinspires.ftc.teamcode.subsystems.drivetrain.PPDrivetrain
import org.firstinspires.ftc.teamcode.subsystems.imu.IMUConstants
import org.firstinspires.ftc.teamcode.subsystems.vision.limelight.LimelightProcessor
import org.firstinspires.ftc.teamcode.utils.components.OpModeBase

@TeleOp(name = "Competition Complete", group = OpModeGroups.COMPETITION)
class TeleOp_Comp : OpMode(), OpModeBase {
  private lateinit var mecanumDrive: PPDrivetrain;
  private lateinit var imu: IMU;
  private lateinit var limelightProcessor: LimelightProcessor;
  private lateinit var distLight: LightController;
  private lateinit var aimLight: LightController;
  private lateinit var intake: Intake;
  private lateinit var magazine: Magazine;
  private lateinit var launcher: LauncherAdvanced;

  override fun init() {
    imu = hardwareMap.get(IMU::class.java, IMUConstants.CONFIG_NAME);
    imu.initialize(IMUConstants.PARAMETERS);
    imu.resetYaw();

    mecanumDrive = PPDrivetrain(this);
    limelightProcessor = LimelightProcessor(this, imu);
    distLight = LightController(this, "distanceLight");
    aimLight = LightController(this, "aimLight");
    intake = Intake(this);
    magazine = Magazine(this);
    launcher = LauncherAdvanced(this, distLight, aimLight, limelightProcessor);
  }

  override fun start() {
    mecanumDrive.start();
  }

  override fun loop() {
    mecanumDrive.update();
    limelightProcessor.update();
    intake.update();
    magazine.update();
    launcher.update();

    updateTelemetryData();
  }

  override fun updateTelemetryData() {
    mecanumDrive.getTelemetryData();
    limelightProcessor.getTelemetryData();
    distLight.getTelemetryData();
    aimLight.getTelemetryData();
    intake.getTelemetryData();
    magazine.getTelemetryData();
    launcher.getTelemetryData();
    telemetry.update();
  }
}
