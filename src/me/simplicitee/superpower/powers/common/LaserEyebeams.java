package me.simplicitee.superpower.powers.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Particle.DustTransition;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import me.simplicitee.superpower.core.Ability;
import me.simplicitee.superpower.core.Attribute;
import me.simplicitee.superpower.core.PowerUser;
import me.simplicitee.superpower.util.EntityUtil;
import me.simplicitee.superpower.util.LocationUtil;

public final class LaserEyebeams extends Ability {

	@Attribute("Damage")
	private double damage;
	
	@Attribute("Range")
	private double range;
	
	@Attribute("Speed")
	private double speed;
	
	private DustOptions dust;
	private Predicate<PowerUser> removal;
	private List<Beam> beams;
	
	public LaserEyebeams(PowerUser user, Color color, Predicate<PowerUser> removal, double damage, double range, double speed) {
		super(user);
		this.dust = new DustTransition(color, Color.WHITE, 0.8f);
		this.removal = removal;
		this.damage = damage;
		this.range = range;
		this.speed = speed;
	}

	@Override
	public void onStart() {
		this.beams = new ArrayList<>();
	}

	@Override
	public void onStop() {
		this.beams.clear();
	}

	@Override
	public boolean onUpdate(double timeDelta) {
		if (removal.test(user)) {
			return false;
		}
		
		Iterator<Beam> iter = beams.iterator();
		while (iter.hasNext()) {
			Beam curr = iter.next();
			if (curr.update(timeDelta)) {
				player.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, curr.loc, 1, 0, 0, 0, dust);
				
				for (Entity e : EntityUtil.getNearby(curr.loc, 0.2, (e) -> e instanceof LivingEntity && !e.isDead())) {
					((LivingEntity) e).damage(damage, player);
				}
			} else {
				iter.remove();
			}
		}
		
		beams.add(new Beam(LocationUtil.moveLeft(player.getEyeLocation(), 0.1)));
		beams.add(new Beam(LocationUtil.moveRight(player.getEyeLocation(), 0.1)));
		
		return true;
	}

	private class Beam {
		
		private Location loc, origin;
		
		public Beam(Location loc) {
			this.loc = loc;
			this.origin = loc.clone();
		}
		
		public boolean update(double timeDelta) {
			loc.add(origin.getDirection().multiply(timeDelta * speed));
			
			if (loc.getBlock().getType().isOccluding()) {
				return false;
			} else if (loc.distance(origin) > range) {
				return false;
			}
			
			return true;
		}
	}
}
