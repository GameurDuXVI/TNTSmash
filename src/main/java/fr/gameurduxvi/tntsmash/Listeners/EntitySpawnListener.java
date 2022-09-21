package fr.gameurduxvi.tntsmash.Listeners;

import org.bukkit.Location;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntitySpawnListener implements Listener {
	
	@EventHandler
	public void onSpawn(EntitySpawnEvent e) {
		if(!(e.getEntity() instanceof EnderDragon)) {
			if(!(e.getEntity() instanceof Item)) {
				e.setCancelled(true);				
			}
		}
		else {
			((EnderDragon) (e.getEntity())).setHealth(0);
			((EnderDragon) (e.getEntity())).teleport(new Location(e.getEntity().getWorld(), 10000, 10000, 10000));
		}
	}
}
