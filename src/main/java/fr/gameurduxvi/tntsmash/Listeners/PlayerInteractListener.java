package fr.gameurduxvi.tntsmash.Listeners;

import java.util.Date;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import fr.gameurduxvi.tntsmash.Panels.VotePanel;
import fr.gameurduxvi.tntsmash.Storage.PlayerData;
import fr.gameurduxvi.tntsmash.Tasks.GameTask;

public class PlayerInteractListener implements Listener {
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getItem() == null) return;
		ItemStack it = e.getItem();

		
		if(it.getType().equals(Material.TNT) && it.getItemMeta() != null && it.getItemMeta().getDisplayName() != null && it.getItemMeta().getDisplayName().equals(GameTask.TNT) && e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
			e.setCancelled(true);
			if(PlayerData.hasPlayerData(e.getPlayer())) {
				PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
				Date now = new Date();
				
				long diffInMillies = Math.abs(now.getTime() - pd.getLaunchedTNTDate().getTime());
				
				if(diffInMillies > 2000) {
					GameTask.shootTNT(e.getPlayer());
					pd.setLaunchedTNTDate(now);
				}
			}
		}
		else if(it.getType().equals(Material.PAPER) && it.getItemMeta() != null && it.getItemMeta().getDisplayName() != null && it.getItemMeta().getDisplayName().equals(GameTask.VOTE)) {
			e.setCancelled(true);
			VotePanel.openVotePanel(e.getPlayer());
		}
	}
}
