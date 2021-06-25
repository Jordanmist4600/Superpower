package me.simplicitee.superpower.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.simplicitee.superpower.PowerManager;
import me.simplicitee.superpower.ability.Ability;

public class AbilityStopEvent extends Event {
	
	public enum Reason {
		/**
		 * This means the ability was stopped by its own logic and
		 * {@link Ability#update(double)} returned false when the manager
		 * tried to update it.
		 */
		NATURAL,
		
		/**
		 * This means the ability was stopped by an external factor, anything
		 * that would call {@link PowerManager#remove(Ability)}.
		 */
		FORCED;
	}

	private static final HandlerList HANDLERS = new HandlerList();
	
	private Ability ability;
	private Reason reason;
	
	public AbilityStopEvent(Ability ability, Reason reason) {
		this.ability = ability;
		this.reason = reason;
	}
	
	public Ability getAbility() {
		return ability;
	}
	
	public Reason getReason() {
		return reason;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
