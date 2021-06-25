package me.simplicitee.superpower.ability;

import org.bukkit.entity.Player;

import me.simplicitee.superpower.PowerUser;

public abstract class Ability {

	protected final PowerUser user;
	protected final Player player;
	private long startTime = -1;
	private int lifeTime = -1;
	
	public Ability(PowerUser user) {
		this.user = user;
		this.player = user.getPlayer();
	}
	
	protected abstract void onStart();
	protected abstract void onStop();
	protected abstract boolean onUpdate(double timeDelta);
	
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
		user.addInstance(this);
		onStart();
	}
	
	public final void stop() {
		onStop();
		user.removeInstance(this);
		startTime = -1;
		lifeTime = -1;
	}
	
	public final boolean update(double timeDelta) {
		++lifeTime;
		return onUpdate(timeDelta);
	}
}
