package me.simplicitee.superpower.powers.common;

import me.simplicitee.superpower.core.Ability;
import me.simplicitee.superpower.core.Attribute;
import me.simplicitee.superpower.core.PowerUser;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public final class Flying extends Ability {
	
	private static final TextComponent GLIDE_ON = new TextComponent(ChatColor.GREEN + "!> Gliding mode enabled <!");
	private static final TextComponent GLIDE_OFF = new TextComponent(ChatColor.RED + "!> Gliding mode disabled <!");

	@Attribute("Speed")
	private double speed;
	
	private boolean gliding = false;
	private float original;
	
	public Flying(PowerUser user, double speed) {
		super(user);
		this.speed = speed;
	}

	@Override
	public void onStart() {
		this.original = player.getFlySpeed();
		player.setFlying(true);
		player.setFlySpeed(Math.min(1, (float) speed / 10));
	}

	@Override
	public void onStop() {
		player.setFlying(false);
		player.setFlySpeed(original);
	}

	@Override
	public boolean onUpdate(double timeDelta) {
		player.setGliding(gliding);
		if (gliding) {
			player.setVelocity(player.getLocation().getDirection().multiply(speed));
		}
		
		return true;
	}
	
	public void toggleGliding() {
		gliding = !gliding;
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, gliding ? GLIDE_ON : GLIDE_OFF);
	}
}
