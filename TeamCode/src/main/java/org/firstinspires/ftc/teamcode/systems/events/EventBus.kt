package org.firstinspires.ftc.teamcode.systems.events

import kotlin.reflect.KClass

/** Manages events. Used to register or dispatch events. */
object EventBus {
  @PublishedApi
  internal val listeners = HashMap<KClass<out Event>, MutableList<(Event) -> Unit>>();

  inline fun <reified T : Event> addEventListener(noinline listener: (T) -> Unit) {
    val eventType = T::class;
    val wrapped: (Event) -> Unit = { event -> listener(event as T) };

    listeners.getOrPut(eventType) { mutableListOf() }.add(wrapped);
  }

  fun dispatch(event: Event) {
    val eventType = event::class;

    listeners[eventType]?.forEach { listener -> listener(event) };
  }
}
