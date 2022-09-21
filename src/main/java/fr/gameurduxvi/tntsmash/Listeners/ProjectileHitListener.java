package fr.gameurduxvi.tntsmash.Listeners;

import java.util.Date;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import fr.gameurduxvi.tntsmash.Main;
import fr.gameurduxvi.tntsmash.Storage.PlayerData;
import fr.gameurduxvi.tntsmash.Storage.TNTData;

public class ProjectileHitListener implements Listener {
	
	@EventHandler
	public void onHit(ProjectileHitEvent e) {
		if(e.getEntity() instanceof Snowball) {
			Snowball snowball = (Snowball) e.getEntity();
			
			if(snowball.getShooter() instanceof Player) {
				Player player = (Player) snowball.getShooter();
				
				TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.PRIMED_TNT);
				tnt.setFuseTicks(0);
				Main.getTntData().add(new TNTData(PlayerData.getPlayerData(player), tnt.getUniqueId().toString()));
			}
		}
		else if(e.getEntity() instanceof Arrow) {
			Arrow arrow = (Arrow) e.getEntity();
			
			if(arrow.getShooter() instanceof Player && e.getHitEntity() != null && e.getHitEntity() instanceof Player) {
				Player attacker = (Player) arrow.getShooter();
				Player victim  = (Player) e.getHitEntity();
				
				if(PlayerData.hasPlayerData(victim) && !victim.equals(attacker)) {
					PlayerData pdv = PlayerData.getPlayerData(victim);
					pdv.setLastDamager(attacker);
					pdv.setLastDamageTime(new Date());
				}				
			}
		}
	}
}
