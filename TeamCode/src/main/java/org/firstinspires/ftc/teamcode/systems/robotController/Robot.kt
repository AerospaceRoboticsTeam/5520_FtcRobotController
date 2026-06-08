package org.firstinspires.ftc.teamcode.systems.robotController

import com.qualcomm.robotcore.hardware.HardwareMap

/**
 * Used to store a reference to every subsystem for easy access in any subsystem.
 * All subsystems must be initialized in the `inti` method, which should be called
 * in the init method or the init section of an Op Mode.
 *
 * Use this pattern to add subsystems:
 * ```
 * lateinit var drivetrain: Drivetrain
 *     private set;
 * ```
 *
 * **IMPORTANT:** This class **MUST** be imported for it to be loaded and its init
 * method **MUST** be called before accessing any subsystems to avoid `nullptr` errors.
 */
abstract class Robot {
  /** Initialize subsystems here using the hardware map from an Op Mode. */
  abstract fun init(hardwareMap: HardwareMap);
}
