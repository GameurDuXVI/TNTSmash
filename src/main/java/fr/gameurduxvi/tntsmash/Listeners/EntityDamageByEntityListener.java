package fr.gameurduxvi.tntsmash.Listeners;

import java.util.Date;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import fr.gameurduxvi.tntsmash.Storage.PlayerData;
import fr.gameurduxvi.tntsmash.Tasks.GameTask;

public class EntityDamageByEntityListener implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			Player victim = (Player) e.getEntity();
			Player attacker = (Player) e.getDamager();
			
			PlayerData pdv = PlayerData.getPlayerData(victim);
			PlayerData pda = PlayerData.getPlayerData(attacker);
			
			long diffInMillies = Math.abs(new Date().getTime() - pda.getStickDate().getTime());
			
			if(pda.getPlayer().getItemInHand() != null
					&& pda.getPlayer().getItemInHand().getType().equals(Material.STICK)
					&& pda.getPlayer().getItemInHand().getItemMeta() != null
					&& pda.getPlayer().getItemInHand().getItemMeta().getDisplayName() != null
					&& pda.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(GameTask.STICK)) {
				if(diffInMillies < 8000) {
					e.setCancelled(true);
				}
				else {
					pdv.setLastDamager(attacker);
					pdv.setLastDamageTime(new Date());
					pda.setStickDate(new Date());
				}
			}
			else {
				e.setCancelled(true);
			}
		}
		else if(e.getEntity() instanceof Player && e.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) e.getDamager();
			
			if(!(arrow.getShooter() instanceof Player)) return;
			Player victim = (Player) e.getEntity();
			Player attacker = (Player) arrow.getShooter();
			
			PlayerData pdv = PlayerData.getPlayerData(victim);
			PlayerData pda = PlayerData.getPlayerData(attacker);

			if(!victim.equals(attacker)) {
				pdv.setLastDamager(attacker);
				pdv.setLastDamageTime(new Date());
				pda.setStickDate(new Date());
			}
		}
	}
}
