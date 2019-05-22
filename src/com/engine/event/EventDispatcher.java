package com.engine.event;

import com.engine.Engine;

public abstract class EventDispatcher {
	/**
	 * Registers an event listener with an EventDispatcher object so that the listener receives notification of an event.
	 */
	public boolean addEventListener(String type, EventListener listener)
	{
		return Engine.EventManager.addEventListener(this, type, listener);
	}

	/**
	 * Removes a listener from the EventDispatcher object.
	 */
	public boolean removeEventListener(String type, EventListener listener)
	{
		if(listener!=null) return Engine.EventManager.removeEventListener(this, type, listener);
		return Engine.EventManager.removeEventListenerByType(this, type);
	}

	/**
	 * Removes all listeners from the EventDispatcher object.
	 * @return 
	 */
	public boolean removeAllEventListeners()
	{
		return Engine.EventManager.removeAllEventListeners(this);
	}

	/**
	 * Dispatches an event to its registered listeners.
	 */
	public boolean dispatchEvent(StageEvent event)
	{
		return Engine.EventManager.dispatchEvent(this, event);
	}

	/**
	 * Checks whether the EventDispatcher object has any listeners registered for a specific type of event.
	 */
	public boolean hasEventListener(String type)
	{
		return Engine.EventManager.hasEventListener(this, type);
	}
}
