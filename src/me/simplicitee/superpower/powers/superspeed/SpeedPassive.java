package me.simplicitee.superpower.powers.superspeed;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.simplicitee.superpower.core.Ability;
import me.simplicitee.superpower.core.PowerUser;

public class SpeedPassive extends Ability {
	
	private int speed;
	private PotionEffect speedEffect, jumpEffect;
	private DustOptions dust;
	
	public SpeedPassive(PowerUser user, Color color, int speed) {
		super(user);
		this.speed = speed;
		this.dust = new DustOptions(color, 0.75f);
	}

	@Override
	protected void onStart() {
		this.speedEffect = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speed, true, false, false);
		this.jumpEffect = new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, speed - 1, true, false, false);
		player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1, true, false, false));
	}

	@Override
	protected void onStop() {
		player.removePotionEffect(PotionEffectType.SPEED);
		player.removePotionEffect(PotionEffectType.REGENERATION);
	}

	@Override
	protected boolean onUpdate(double timeDelta) {
		if (player.isSprinting()) {
			player.addPotionEffect(speedEffect);
			player.addPotionEffect(jumpEffect);
			player.getLocation().getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(0, 1, 0), 1, 0.1, 0.6, 0.1, dust);
		} else {
			player.removePotionEffect(PotionEffectType.SPEED);
			player.removePotionEffect(PotionEffectType.JUMP);
		}
		return true;
	}
}
