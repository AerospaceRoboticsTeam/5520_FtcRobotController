package org.firstinspires.ftc.teamcode.events

class EventContext {}

// Base event type
sealed class Event;

// System/Subsystem events
sealed class Input : Event();

// Input events
sealed class GamePad : Input();

// Gamepad events
class AButtonPressed : GamePad();
