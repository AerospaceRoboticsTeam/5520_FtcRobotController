package org.firstinspires.ftc.teamcode.systems

import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.systems.events.EventBus
import org.firstinspires.ftc.teamcode.systems.events.GamePadEvent

object InputManager {
  fun update(gamepad1: Gamepad, gamepad2: Gamepad) {
    if(gamepad1.aWasPressed()) {
      EventBus.dispatch(GamePadEvent.APressed());
    }
  }
}

