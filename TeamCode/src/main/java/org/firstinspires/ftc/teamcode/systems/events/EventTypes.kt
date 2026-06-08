package org.firstinspires.ftc.teamcode.systems.events

// Base event type
sealed class Event;

// System/Subsystem events
sealed class InputEvent : Event();

// Input events
sealed class GamePadEvent : InputEvent() {
  /**
   * Occurs when the A button is pressed.
   *
   * **IMPORTANT:** Will not be dispatched continuously while the button is held down.
   * It's only dispatched once when a user holds down the button.
   */
  class APressed() : GamePadEvent();
}
