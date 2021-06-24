package me.simplicitee.superpower.ability;

import org.bukkit.entity.Player;

import me.simplicitee.superpower.entity.PowerUser;

public abstract class Ability {

	protected final PowerUser user;
	protected final Player player;
	private long startTime = -1;
	private int lifeTime = -1;
	
	public Ability(PowerUser user) {
		this.user = user;
		this.player = user.getPlayer();
	}
	
	public abstract void onStart();
	public abstract void onStop();
	public abstract boolean onUpdate(double timeDelta);
	
	public final long getStartTime() {
		return startTime;
	}
	
	public final int getLivedTicks() {
		return lifeTime;
	}
	
	public final PowerUser getUser() {
		return user;
	}
	
	public final void start() {
		startTime = System.currentTimeMillis();
	}
	
	public final void stop() {
		startTime = -1;
		lifeTime = -1;
	}
	
	public final boolean update(double timeDelta) {
		++lifeTime;
		return onUpdate(timeDelta);
	}
}
