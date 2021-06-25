package me.simplicitee.superpower;

import java.io.File;

import org.bukkit.event.Event;

import me.simplicitee.superpower.ability.Ability;
import me.simplicitee.superpower.ability.Activation;
import me.simplicitee.superpower.configuration.Configurable;
import net.md_5.bungee.api.ChatColor;

public abstract class Power implements Configurable {
	
	private File file;
	private String name, internal, description;
	private ChatColor color;
	
	public Power(String name, ChatColor color, String description) {
		this.internal = ChatColor.stripColor(name).trim().toLowerCase();
		this.name = name;
		this.color = color;
		this.description = description;
		this.file = new File(SuperpowerPlugin.configurationFolder("powers"), name + ".yml");
	}
	
	public abstract String getDisplayName();
	
	/**
	 * Use this power with the given user and trigger with an optional event. The
	 * event is passed through under <code>MONITOR</code> and cannot effect the event's outcome.
	 * @param user Who to use the power for
	 * @param trigger What activation did they use
	 * @param provider optional for extra information
	 * @return the ability or null if nothing happens
	 */
	public abstract Ability use(PowerUser user, Activation trigger, Event provider);
	
	public final Ability use(PowerUser user, Activation trigger) {
		return use(user, trigger, null);
	}
	
	@Override
	public final File getFile() {
		return file;
	}
	
	public final String getName() {
		return name;
	}
	
	public final String internal() {
		return internal;
	}
	
	public final String getDescription() {
		return description;
	}
	
	public final ChatColor getColor() {
		return color;
	}
}
