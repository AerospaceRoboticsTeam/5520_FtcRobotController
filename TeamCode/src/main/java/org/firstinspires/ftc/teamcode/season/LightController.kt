package org.firstinspires.ftc.teamcode.season;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.utils.components.Subsystem

class LightController(private val opMode: OpMode) : Subsystem {
  private val light: Servo = opMode.hardwareMap.get(
    Servo::class.java,
    "indicatorLight"
  );
  private val state: LightState = LightState();

  init {
    setMode(LightMode.OFF);
  }

  override fun update() {}

  fun setMode(mode: LightMode) {
    state.modeState = mode;
    light.position = state.getModeValue(mode);
  }

  override fun getTelemetryData() {
    opMode.telemetry.addData("Light Mode/State", state.modeState);
  }
}

internal class LightState {
  private val colors = HashMap<LightMode, Double>();
  var modeState: LightMode = LightMode.OFF;

  init {
    colors.put(LightMode.OFF, 0.0);
    colors.put(LightMode.RED, 0.279);
    colors.put(LightMode.ORANGE, 0.333);
    colors.put(LightMode.YELLOW, 0.388);
    colors.put(LightMode.LIME, 0.444);
    colors.put(LightMode.GREEN, 0.5);
    colors.put(LightMode.AQUA, 0.555);
    colors.put(LightMode.BLUE, 0.611);
    colors.put(LightMode.PURPLE, 0.666);
    colors.put(LightMode.PINK, 0.722);
    colors.put(LightMode.WHITE, 1.0);
  }

  /**
   * @return The value of the inputted light mode, otherwise `-1.0` if the mode doesn't have a value.
   */
  fun getModeValue(mode: LightMode?): Double {
    return colors[mode] ?: -1.0;
  }
}

enum class LightMode {
  OFF,
  RED,
  ORANGE,
  YELLOW,
  LIME,
  GREEN,
  AQUA,
  BLUE,
  PURPLE,
  PINK,
  WHITE
}
