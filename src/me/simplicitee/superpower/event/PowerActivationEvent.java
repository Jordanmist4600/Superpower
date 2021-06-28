package me.simplicitee.superpower.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.simplicitee.superpower.core.Activation;
import me.simplicitee.superpower.core.PowerUser;

public class PowerActivationEvent extends Event implements Cancellable {
	
	private static final HandlerList HANDLERS = new HandlerList();

	private PowerUser user;
	private Activation trigger;
	private Event provider;
	private boolean cancelled = false;
	
	public PowerActivationEvent(PowerUser user, Activation trigger, Event provider) {
		this.user = user;
		this.trigger = trigger;
		this.provider = provider;
	}
	
	public PowerUser getUser() {
		return user;
	}
	
	public Activation getTrigger() {
		return trigger;
	}
	
	public Event getProvider() {
		return provider;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
