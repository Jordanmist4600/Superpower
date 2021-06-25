package me.simplicitee.superpower.ability;

import java.util.HashMap;
import java.util.Map;

public final class Activation {

	private static final Map<String, Activation> CACHE = new HashMap<>();
	
	public static final Activation PASSIVE = of("passive");
	public static final Activation LEFT_CLICK = of("left_click");
	
	public static final Activation SNEAK_UP = of("sneak_up");
	public static final Activation SNEAK_DOWN = of("sneak_down");
	
	public static final Activation FLIGHT_ON = of("flight_on");
	public static final Activation FLIGHT_OFF = of("flight_off");
	
	public static final Activation SPRINT_ON = of("sprint_on");
	public static final Activation SPRINT_OFF = of("sprint_off");
	
	public static final Activation OFFHAND_SWAP = of("offhand_toggle");
	
	public static final Activation RIGHT_CLICK_ITEM = of("right_click_item");
	public static final Activation RIGHT_CLICK_BLOCK = of("right_click_block");
	public static final Activation RIGHT_CLICK_ENTITY = of("right_click_entity");
	
	private String name;
	
	private Activation(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Activation)) {
			return false;
		}
		
		return name.equals(other.toString());
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	public static Activation of(String name) {
		return CACHE.computeIfAbsent(name.toLowerCase(), (s) -> new Activation(s));
	}
}
