package me.simplicitee.superpower.powers.superpower;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.simplicitee.superpower.core.Ability;
import me.simplicitee.superpower.core.Attribute;
import me.simplicitee.superpower.core.PowerUser;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class SuperPassive extends Ability {

	private static final TextComponent BEAM_ON = new TextComponent(ChatColor.GREEN + "!> Eye Beams Armed <!");
	private static final TextComponent BEAM_OFF = new TextComponent(ChatColor.RED + "!> Eye Beams Disarmed <!");
	private static final PotionEffectType[] EFFECTS = {PotionEffectType.INCREASE_DAMAGE, PotionEffectType.DAMAGE_RESISTANCE, PotionEffectType.SPEED, PotionEffectType.FAST_DIGGING, PotionEffectType.JUMP, PotionEffectType.DOLPHINS_GRACE, PotionEffectType.SLOW_FALLING};
	
	@Attribute("Strength")
	private int strength;
	
	@Attribute("Speed")
	private int speed;
	
	private boolean eyeBeams = false, flight;
	
	public SuperPassive(PowerUser user, int strength, int speed) {
		super(user);
		this.strength = strength;
		this.speed = speed;
	}

	@Override
	public void onStart() {
		int[] vals = {strength, speed, Math.round((strength + speed) / 2f), 0};
		
		for (int i = 0; i < EFFECTS.length; ++i) {
			player.addPotionEffect(new PotionEffect(EFFECTS[i], Integer.MAX_VALUE, vals[i / 2], true, false, false));
		}
		
		this.flight = player.getAllowFlight();
		player.setAllowFlight(true);
	}

	@Override
	public void onStop() {
		for (PotionEffectType type : EFFECTS) {
			player.removePotionEffect(type);
		}
		
		player.setAllowFlight(flight);
	}

	@Override
	public boolean onUpdate(double timeDelta) {
		return true;
	}

	public void toggleEyeBeams() {
		eyeBeams = !eyeBeams;
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, eyeBeams ? BEAM_ON : BEAM_OFF);
	}
	
	public boolean isEyeBeamsToggled() {
		return eyeBeams;
	}
}
