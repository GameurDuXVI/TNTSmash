package fr.gameurduxvi.tntsmash.Listeners;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class EntityDamageListener implements Listener {
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			e.setDamage(0);	
			if(e.getCause().equals(DamageCause.ENTITY_EXPLOSION) || e.getCause().equals(DamageCause.FALL)) {
				e.setCancelled(true);		
			}
		}
		else if(e.getEntity() instanceof Item) {
			if(e.getCause().equals(DamageCause.ENTITY_EXPLOSION)) {
				e.setCancelled(true);					
			}
		}
	}
}
