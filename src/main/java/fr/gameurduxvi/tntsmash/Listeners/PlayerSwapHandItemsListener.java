package fr.gameurduxvi.tntsmash.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerSwapHandItemsListener implements Listener {
	
	@EventHandler
	public void onSwap(PlayerSwapHandItemsEvent e) {
		e.setCancelled(true);
	}
}
