package me.simplicitee.superpower;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import me.simplicitee.superpower.ability.Ability;
import me.simplicitee.superpower.ability.Activation;
import me.simplicitee.superpower.event.UserPowerChangeEvent;
import me.simplicitee.superpower.util.EventUtil;

public final class PowerUser {

	private final Player player;
	private Power power = null;
	private Map<Class<? extends Ability>, Ability> instances = new HashMap<>();
	private boolean toggled = true;
	
	PowerUser(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Power getPower() {
		return power;
	}
	
	public void setPower(Power power) {
		if (this.power == power) {
			return;
		}
		
		UserPowerChangeEvent event = EventUtil.call(new UserPowerChangeEvent(this, power));
		if (this.power == event.getResult()) {
			return;
		}
		
		instances.clear();
		this.power = event.getResult();
	}
	
	public boolean isToggled() {
		return toggled;
	}
	
	public void setToggled(boolean toggled) {
		if (this.toggled == toggled) {
			return;
		}
		
		if (toggled) {
			PowerManager.activate(this, Activation.PASSIVE, null);
		} else {
			instances.clear();
		}
		
		this.toggled = toggled;
	}
	
	public Optional<Ability> usePower(Activation trigger, Event provider) {
		if (power == null || trigger == null) {
			return Optional.empty();
		}
		
		return Optional.ofNullable(power.use(this, trigger, provider));
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
	
	public void removeInstance(Ability ability) {
		instances.remove(ability.getClass());
	}
}
