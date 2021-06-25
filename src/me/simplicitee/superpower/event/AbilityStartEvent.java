package me.simplicitee.superpower.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.simplicitee.superpower.ability.Ability;

public class AbilityStartEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();
	
	private Ability ability;
	private boolean cancelled = false;
	
	public AbilityStartEvent(Ability ability) {
		this.ability = ability;
	}
	
	public Ability getAbility() {
		return ability;
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
	
	public static HandlerList getHanderList() {
		return HANDLERS;
	}
}
