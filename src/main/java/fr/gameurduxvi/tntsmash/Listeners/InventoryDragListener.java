package fr.gameurduxvi.tntsmash.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryDragListener implements Listener {
	
	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		for(int slot: e.getRawSlots()) {
			if(slot == 45) {
				if(e.getRawSlots().size() == 1) {
					e.setCancelled(true);					
				}
				else {
					e.getRawSlots().remove(45);
				}
			}
		}
	}
}
