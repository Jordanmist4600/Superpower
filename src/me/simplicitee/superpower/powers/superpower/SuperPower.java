package me.simplicitee.superpower.powers.superpower;

import org.bukkit.event.Event;

import me.simplicitee.superpower.Power;
import me.simplicitee.superpower.ability.Ability;
import me.simplicitee.superpower.ability.Activation;
import me.simplicitee.superpower.configuration.Configure;
import me.simplicitee.superpower.entity.PowerUser;
import me.simplicitee.superpower.powers.common.Flying;
import net.md_5.bungee.api.ChatColor;

public class SuperPower extends Power {
	
	public static ChatColor SUPER_BLUE = ChatColor.of("#0074D9");
	
	@Configure("Passive.Strength")
	private int passiveStrength = 5;
	
	@Configure("Passive.Speed")
	private int passiveSpeed = 3;
	
	@Configure("Flight.Speed")
	private double flightSpeed = 2;

	public SuperPower() {
		super("SuperPower", SUPER_BLUE, "Classic superhuman abilities like flight, strength, speed, and laser-eyebeams!");
	}

	@Override
	public Ability use(PowerUser user, Activation trigger, Event provider) {
		if (trigger == Activation.PASSIVE) {
			return new SuperPassive(user, passiveStrength, passiveSpeed);
		} else if (trigger == Activation.FLIGHT_TOGGLE) {
			return new Flying(user, flightSpeed);
		} else if (trigger == Activation.OFFHAND_TOGGLE) {
			user.getInstanceOf(Flying.class).ifPresent((f) -> f.toggleGliding());
		}
		
		return null;
	}

	@Override
	public String getDisplayName() {
		return SUPER_BLUE + (ChatColor.BOLD + "Super") + ChatColor.RED + (ChatColor.BOLD + "Power");
	}
}
