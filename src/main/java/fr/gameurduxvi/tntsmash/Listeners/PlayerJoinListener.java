package fr.gameurduxvi.tntsmash.Listeners;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.gameurduxvi.tntsmash.Main;
import fr.gameurduxvi.tntsmash.State;
import fr.gameurduxvi.tntsmash.Storage.PlayerData;
import fr.gameurduxvi.tntsmash.Tasks.GameTask;

public class PlayerJoinListener implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage(null);
		if(!PlayerData.hasPlayerData(e.getPlayer())) {
			if(Main.gameState.equals(State.Started)) {
				GameTask.spectator(e.getPlayer());
				PlayerData pd = new PlayerData(e.getPlayer());
				pd.setLife(0);
				Main.getPlayerDatas().add(pd);
				
				GameTask.spectator(e.getPlayer());
				
				pd.getPlayer().setAllowFlight(true);
				pd.getPlayer().setFlying(true);
				
				e.getPlayer().getInventory().clear();
				
				e.getPlayer().teleport(PlayerData.getAlivePlayers().get(new Random().nextInt(PlayerData.getAlivePlayers().size())).getPlayer());
			}
			else {
				Main.getPlayerDatas().add(new PlayerData(e.getPlayer()));
				Bukkit.broadcastMessage("§7[" + Main.name + "§7] §8" + e.getPlayer().getName() + " §7a rejoint la partie ! (§e" + Main.getPlayerDatas().size() + "§7/§e8§7)");
				
				GameTask.checkTimer();

				e.getPlayer().teleport(Main.lobbyLocation);
				e.getPlayer().setGameMode(GameMode.ADVENTURE);
				
				e.getPlayer().setExp(0);
				e.getPlayer().setLevel(0);
				
				e.getPlayer().setMaxHealth(PlayerData.getPlayerData(e.getPlayer()).getLife() * 2);
				
				e.getPlayer().setHealth(e.getPlayer().getMaxHealth());
				e.getPlayer().setFoodLevel(20);
				
				e.getPlayer().getInventory().clear();
				
				ItemStack paper = new ItemStack(Material.PAPER);
				ItemMeta paperM = paper.getItemMeta();
				paperM.setDisplayName(GameTask.VOTE);
				paper.setItemMeta(paperM);
				e.getPlayer().getInventory().setItem(4, paper);
				
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
			}
		}
	}
}
