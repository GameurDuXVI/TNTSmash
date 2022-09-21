package fr.gameurduxvi.tntsmash.Listeners;

import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.util.Vector;

import fr.gameurduxvi.tntsmash.Storage.PlayerData;

public class PlayerFishListener implements Listener {
	
	@EventHandler
	public void onFish(PlayerFishEvent e) {
		if(e.getState().equals(State.CAUGHT_ENTITY) && e.getCaught() instanceof Player) {
			Player attacker = e.getPlayer();
			Player victim = (Player) e.getCaught();
			
			//PlayerData pda = PlayerData.getPlayerData(attacker);
			PlayerData pdv = PlayerData.getPlayerData(victim);
			
			Vector velocity = victim.getEyeLocation().subtract(attacker.getLocation()).toVector().normalize().multiply(2);
			victim.setVelocity(velocity);
			
			if(!victim.equals(attacker)) {
				pdv.setLastDamager(attacker);
				pdv.setLastDamageTime(new Date());
			}		
		}
	}
}
