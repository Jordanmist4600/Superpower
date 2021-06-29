package me.simplicitee.superpower.core;

import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import me.simplicitee.superpower.SuperpowerPlugin;

public class ActivationListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		new BukkitRunnable() {

			@Override
			public void run() {
				PowerManager.activate(event.getPlayer(), Activation.PASSIVE, event);
			}
			
		}.runTaskLater(SuperpowerPlugin.instance(), 100);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		PowerManager.clearUser(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onKick(PlayerKickEvent event) {
		PowerManager.clearUser(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onClick(PlayerInteractEvent event) {
		if (event.getHand() != EquipmentSlot.HAND) {
			return;
		}
		
		Activation activation = null;
		
		if (event.useInteractedBlock() != Result.ALLOW) {
			if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
				activation = Activation.LEFT_CLICK;
			} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				activation = Activation.RIGHT_CLICK_BLOCK;
			}
		} else if (event.useItemInHand() != Result.ALLOW) {
			if (event.getAction() == Action.LEFT_CLICK_AIR) {
				activation = Activation.LEFT_CLICK;
			} else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
				activation = Activation.RIGHT_CLICK_ITEM;
			}
		}
		
		PowerManager.activate(event.getPlayer(), activation, event);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityClick(PlayerInteractEntityEvent event) {
		PowerManager.activate(event.getPlayer(), Activation.RIGHT_CLICK_ENTITY, event);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onOffhandSwap(PlayerSwapHandItemsEvent event) {
		PowerManager.activate(event.getPlayer(), Activation.OFFHAND_SWAP, event);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onFlightToggle(PlayerToggleFlightEvent event) {
		PowerManager.activate(event.getPlayer(), event.isFlying() ? Activation.FLIGHT_ON : Activation.FLIGHT_OFF, event);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onSprintToggle(PlayerToggleSprintEvent event) {
		PowerManager.activate(event.getPlayer(), event.isSprinting() ? Activation.SPRINT_ON : Activation.SPRINT_OFF, event);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onSneakToggle(PlayerToggleSneakEvent event) {
		PowerManager.activate(event.getPlayer(), event.isSneaking() ? Activation.SNEAK_DOWN : Activation.SNEAK_UP, event);
	}
}
