package fr.gameurduxvi.tntsmash.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.gameurduxvi.tntsmash.Main;
import fr.gameurduxvi.tntsmash.Panels.VotePanel;
import fr.gameurduxvi.tntsmash.Storage.MapData;
import fr.gameurduxvi.tntsmash.Storage.PlayerData;

public class InventoryClickListener implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Inventory inv = e.getClickedInventory(); if(inv == null) return;		
		ItemStack it = e.getCurrentItem(); if(it == null) return;
		
		if(e.getSlot() == 40) {
			e.setCancelled(true);
		}
		
		ItemMeta itM = it.getItemMeta(); if(itM == null) return;
		Player player = (Player) e.getWhoClicked();
		
		if(inv.getName().equals(VotePanel.VOTE)) {
			e.setCancelled(true);
			if(PlayerData.hasPlayerData(player)) {
				PlayerData pd = PlayerData.getPlayerData(player);
				if(itM.getDisplayName().equals(VotePanel.VOTE_RANDOM)) {
					pd.setMap(null);
					for(PlayerData pd2: Main.getPlayerDatas()) {
						if(pd2.getPlayer().getOpenInventory() != null && pd2.getPlayer().getOpenInventory().getTitle().equals(VotePanel.VOTE)) {
							VotePanel.openVotePanel(pd2.getPlayer());								
						}
					}
				}
				for(MapData md: Main.getMapData()) {
					if(itM.getDisplayName().equals(VotePanel.VOTE_MAP_PREFIX + md.getName())) {
						pd.setMap(md.getDir());
						for(PlayerData pd2: Main.getPlayerDatas()) {
							if(pd2.getPlayer().getOpenInventory() != null && pd2.getPlayer().getOpenInventory().getTitle().equals(VotePanel.VOTE)) {
								VotePanel.openVotePanel(pd2.getPlayer());								
							}
						}
					}
				}
			}
		}
	}
}
