package fr.gameurduxvi.tntsmash.Tasks;

import java.util.Date;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.gameurduxvi.tntsmash.Main;
import fr.gameurduxvi.tntsmash.State;
import fr.gameurduxvi.tntsmash.Storage.PlayerData;

public class ActionBarTask extends BukkitRunnable {

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		if(Main.gameState.equals(State.Started)) {
			for(PlayerData pd: Main.getPlayerDatas()) {
				if(pd.getPlayer().getItemInHand() != null
						&& pd.getPlayer().getItemInHand().getType().equals(Material.TNT)
						&& pd.getPlayer().getItemInHand().getItemMeta() != null
						&& pd.getPlayer().getItemInHand().getItemMeta().getDisplayName() != null
						&& pd.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(GameTask.TNT)) {
					
					Date now = new Date();					
					long diffInMillies = Math.abs(now.getTime() - pd.getLaunchedTNTDate().getTime());
					
					String text = "";
					if(diffInMillies > 2000) {
						text = "§aReady";
					}
					else {
						text = "§4";
						int times = ((int)(diffInMillies/100));
						for(int t = 0; t <= times; t++) {
							text += "|";
						}
						text += "§6";
						for(int t = 0; t <= (19 - times); t++) {
							text += "|";
						}					
					}
					pd.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§7[" + GameTask.TNT + "§7] " + text));
				}
				else if(pd.getPlayer().getItemInHand() != null
						&& pd.getPlayer().getItemInHand().getType().equals(Material.STICK)
						&& pd.getPlayer().getItemInHand().getItemMeta() != null
						&& pd.getPlayer().getItemInHand().getItemMeta().getDisplayName() != null
						&& pd.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(GameTask.STICK)) {
					Date now = new Date();					
					long diffInMillies = Math.abs(now.getTime() - pd.getStickDate().getTime());
					
					String text = "";
					if(diffInMillies > 8000) {
						text = "§aReady";
					}
					else {
						text = "§4";
						int times = ((int)(diffInMillies/400));
						for(int t = 0; t <= times; t++) {
							text += "|";
						}
						text += "§6";
						for(int t = 0; t <= (19 - times); t++) {
							text += "|";
						}					
					}
					pd.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("�7[" + GameTask.STICK + "�7] " + text));
				}
				else if(pd.getPlayer().getItemInHand() != null
						&& pd.getPlayer().getItemInHand().getType().equals(Material.BOW)
						&& pd.getPlayer().getItemInHand().getItemMeta() != null
						&& pd.getPlayer().getItemInHand().getItemMeta().getDisplayName() != null
						&& pd.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(GameTask.BOW)) {
					
					int amount = 0;
					for(ItemStack it: pd.getPlayer().getInventory().getContents()) {
						if(it != null && it.getType().equals(Material.ARROW)) {
							amount += it.getAmount();
						}
					}
					pd.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("�7[" + GameTask.BOW + "�7] �b" + amount + " �7fl�ches"));
				}
				else {
					pd.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("�a"));
				}
			}			
		}
	}
}
