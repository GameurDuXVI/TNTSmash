package fr.gameurduxvi.tntsmash.Listeners;

import java.util.Collection;
import java.util.Date;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import fr.gameurduxvi.tntsmash.Main;
import fr.gameurduxvi.tntsmash.Storage.PlayerData;
import fr.gameurduxvi.tntsmash.Storage.TNTData;

public class EntityExplodeListener implements Listener {
	
	@EventHandler
	public void onExplosion(EntityExplodeEvent e) {
		if(e.getEntity() instanceof TNTPrimed) {
			e.blockList().clear();
			Collection<Entity> entities = e.getEntity().getWorld().getNearbyEntities(e.getLocation(), 5, 5, 5);
			for(Entity entity: entities) {
				if(entity instanceof Player) {
					Player victim = (Player) entity;
					Double distance = e.getLocation().distance(victim.getEyeLocation());
					if(distance < 5) {						
						if(PlayerData.hasPlayerData(victim)) {
							PlayerData pdv = PlayerData.getPlayerData(victim);
							
							if(pdv.isCanBeMoved()) {
								Vector velocity = victim.getEyeLocation().subtract(e.getLocation()).toVector().normalize().multiply((11 - distance)/4);
								victim.setVelocity(velocity);
								
								if(TNTData.hasTNTData(e.getEntity().getUniqueId().toString())) {
									Player attacker = TNTData.getTNTData(e.getEntity().getUniqueId().toString()).getPlayerData().getPlayer();
									if(!victim.equals(attacker)) {
										pdv.setLastDamageTime(new Date());
										pdv.setLastDamager(attacker);	
									}															
								}
							}
						}						
					}
				}
			}
			if(TNTData.hasTNTData(e.getEntity().getUniqueId().toString())) {
				Main.getTntData().remove(TNTData.getTNTData(e.getEntity().getUniqueId().toString()));
			}
		}
	}
}
