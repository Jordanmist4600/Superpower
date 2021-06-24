package me.simplicitee.superpower.powers.superpower;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.simplicitee.superpower.ability.Ability;
import me.simplicitee.superpower.ability.Attribute;
import me.simplicitee.superpower.entity.PowerUser;

public class SuperPassive extends Ability {

	private static final PotionEffectType[] EFFECTS = {PotionEffectType.INCREASE_DAMAGE, PotionEffectType.DAMAGE_RESISTANCE, PotionEffectType.SPEED, PotionEffectType.FAST_DIGGING, PotionEffectType.JUMP, PotionEffectType.DOLPHINS_GRACE};
	
	@Attribute("Strength")
	private int strength;
	
	@Attribute("Speed")
	private int speed;
	
	public SuperPassive(PowerUser user, int strength, int speed) {
		super(user);
		this.strength = strength;
		this.speed = speed;
	}

	@Override
	public void onStart() {
		int[] vals = {strength, speed, Math.round((strength + speed) / 2f)};
		
		for (int i = 0; i < EFFECTS.length; ++i) {
			player.addPotionEffect(new PotionEffect(EFFECTS[i], Integer.MAX_VALUE, vals[i / 2], true, false, false));
		}
	}

	@Override
	public void onStop() {
		for (PotionEffectType type : EFFECTS) {
			player.removePotionEffect(type);
		}
	}

	@Override
	public boolean onUpdate(double timeDelta) {
		return true;
	}

}
