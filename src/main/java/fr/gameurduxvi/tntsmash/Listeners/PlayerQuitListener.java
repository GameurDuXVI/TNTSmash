package fr.gameurduxvi.tntsmash.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.gameurduxvi.tntsmash.Main;
import fr.gameurduxvi.tntsmash.Storage.PlayerData;
import fr.gameurduxvi.tntsmash.Tasks.GameTask;

public class PlayerQuitListener implements Listener {
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if(PlayerData.hasPlayerData(e.getPlayer())) {
			PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
			Main.getPlayerDatas().remove(pd);
		}
		
		e.setQuitMessage(null);
		Bukkit.broadcastMessage("§7[" + Main.name + "§7] �8" + e.getPlayer().getName() + " §7a quitt§ la partie ! (�e" + Main.getPlayerDatas().size() + "§7/§e8§7)");
		
		GameTask.checkTimer();
	}
}
