package me.simplicitee.superpower;

import java.util.Map;
import java.util.Optional;

import org.bukkit.entity.Player;

import me.simplicitee.superpower.ability.Ability;

public final class PowerUser {

	private final Player player;
	private Power power;
	private Map<Class<? extends Ability>, Ability> instances;
	
	public PowerUser(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Power getPower() {
		return power;
	}
	
	public void setPower(Power power) {
		this.power = power;
	}
	
	public boolean hasInstanceOf(Class<? extends Ability> clazz) {
		return instances.containsKey(clazz);
	}
	
	public <T extends Ability> Optional<T> getInstanceOf(Class<T> clazz) {
		if (clazz == null) {
			return Optional.empty();
		}
		
		return Optional.ofNullable(clazz.cast(instances.get(clazz)));
	}
	
	public void addInstance(Ability ability) {
		instances.put(ability.getClass(), ability);
	}
}
