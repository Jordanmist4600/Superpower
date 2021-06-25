package me.simplicitee.superpower.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.simplicitee.superpower.Power;
import me.simplicitee.superpower.PowerUser;

public class UserPowerChangeEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	private PowerUser user;
	private Power result;
	
	public UserPowerChangeEvent(PowerUser user, Power result) {
		this.user = user;
		this.result = result;
	}
	
	public PowerUser getUser() {
		return user;
	}
	
	public Power getResult() {
		return result;
	}
	
	public void setResult(Power result) {
		this.result = result;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
