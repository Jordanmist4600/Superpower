package me.simplicitee.superpower.powers.superpower;

import org.bukkit.Color;
import org.bukkit.event.Event;

import me.simplicitee.superpower.configuration.Configure;
import me.simplicitee.superpower.core.Ability;
import me.simplicitee.superpower.core.Activation;
import me.simplicitee.superpower.core.Power;
import me.simplicitee.superpower.core.PowerUser;
import me.simplicitee.superpower.powers.common.Flying;
import me.simplicitee.superpower.powers.common.LaserEyebeams;
import net.md_5.bungee.api.ChatColor;

public class SuperPower extends Power {
	
	public static ChatColor SUPER_BLUE = ChatColor.of("#0074D9");
	public static Color BLUE_COLOR = Color.fromRGB(SUPER_BLUE.getColor().getRGB());
	
	@Configure("Passive.Strength")
	private int passiveStrength = 5;
	
	@Configure("Passive.Speed")
	private int passiveSpeed = 3;
	
	@Configure("Flight.Speed")
	private double flightSpeed = 2;
	
	@Configure("LaserEyebeams.Damage")
	private double laserDamage = 2;
	
	@Configure("LaserEyebeams.Speed")
	private double laserSpeed = 3;
	
	@Configure("LaserEyebeams.Range")
	private double laserRange = 20;

	public SuperPower() {
		super("SuperPower", SUPER_BLUE, "Classic superhuman abilities like flight, strength, speed, and laser-eyebeams!");
	}

	@Override
	public Ability use(PowerUser user, Activation trigger, Event provider) {
		if (trigger == Activation.PASSIVE) {
			return new SuperPassive(user, passiveStrength, passiveSpeed);
		} else if (trigger == Activation.FLIGHT_ON) {
			return new Flying(user, flightSpeed);
		} else if (trigger == Activation.OFFHAND_SWAP) {
			if (user.hasInstanceOf(Flying.class)) {
				user.getInstanceOf(Flying.class).get().toggleGliding();
			} else if (user.hasInstanceOf(SuperPassive.class)) {
				user.getInstanceOf(SuperPassive.class).get().toggleEyeBeams();
			}
		} else if (trigger == Activation.SNEAK_DOWN) {
			return new LaserEyebeams(user, BLUE_COLOR, (u) -> !u.getPlayer().isSneaking(), laserDamage, laserRange, laserSpeed);
		}
		
		return null;
	}

	@Override
	public String getDisplayName() {
		return SUPER_BLUE + (ChatColor.BOLD + "Super") + ChatColor.RED + (ChatColor.BOLD + "Power");
	}
}
