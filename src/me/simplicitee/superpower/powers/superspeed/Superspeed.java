package me.simplicitee.superpower.powers.superspeed;

import org.bukkit.Color;
import org.bukkit.event.Event;

import me.simplicitee.superpower.configuration.Configure;
import me.simplicitee.superpower.core.Ability;
import me.simplicitee.superpower.core.Activation;
import me.simplicitee.superpower.core.Power;
import me.simplicitee.superpower.core.PowerUser;
import net.md_5.bungee.api.ChatColor;

public class Superspeed extends Power {
	
	private static final ChatColor SPEED_GREEN = ChatColor.of("#0cb153");
	private static final Color GREEN_COLOR = Color.fromRGB(SPEED_GREEN.getColor().getRed(), SPEED_GREEN.getColor().getGreen(), SPEED_GREEN.getColor().getBlue());

	@Configure("Speed")
	private int speed = 7;
	
	public Superspeed() {
		super("Superspeed", SPEED_GREEN, "Run super fast! You also regenerate quickly, but you consume your hunger bar faster!");
	}

	@Override
	public String getDisplayName() {
		return SPEED_GREEN + (ChatColor.BOLD + "Superspeed");
	}

	@Override
	public Ability use(PowerUser user, Activation trigger, Event provider) {
		if (trigger == Activation.PASSIVE) {
			return new SpeedPassive(user, GREEN_COLOR, speed);
		}
		
		return null;
	}

}
