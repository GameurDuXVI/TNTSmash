package fr.gameurduxvi.tntsmash.Panels;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.gameurduxvi.tntsmash.Main;
import fr.gameurduxvi.tntsmash.Storage.MapData;
import fr.gameurduxvi.tntsmash.Storage.PlayerData;

public class VotePanel {
	
	public static String VOTE = "§bVote";
	
	public static String VOTE_MAP_PREFIX = "§b";
	
	public static String VOTE_RANDOM = "§bAléatoire";
	
	public static void openVotePanel(Player player) {
		Inventory inv = Bukkit.createInventory(null, 27, VOTE);
		
		PlayerData pd = PlayerData.getPlayerData(player);
		
		ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, Short.parseShort(7 + ""));
		for(int i = 0; i <= 26; i++) {
			inv.setItem(i, pane);
		}
		
		int i = 9;
		for(MapData md: Main.getMapData()) {
			i++;
			ItemStack it = new ItemStack(Material.PAPER);
			ItemMeta itM = it.getItemMeta();
			itM.setDisplayName(VOTE_MAP_PREFIX + md.getName().trim());
			if(pd.getMap() != null && pd.getMap().equals(md.getDir())) {
				itM.addEnchant(Enchantment.KNOCKBACK, 1, true);
				itM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			
			int votes = 0;
			for(PlayerData pd2: Main.getPlayerDatas()) {
				if(pd2.getMap() != null && pd2.getMap().equals(md.getDir())) {
					votes++;
				}
			}
			itM.setLore(Arrays.asList(votes + " votes"));
			it.setItemMeta(itM);
			inv.setItem(i, it);
		}
		
		ItemStack random = new ItemStack(Material.PAPER);
		ItemMeta randomM = random.getItemMeta();
		randomM.setDisplayName(VOTE_RANDOM);
		if(pd.getMap() == null) {
			randomM.addEnchant(Enchantment.KNOCKBACK, 1, true);
			randomM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		int votes = 0;
		for(PlayerData pd2: Main.getPlayerDatas()) {
			if(pd2.getMap() == null) {
				votes++;
			}
		}
		randomM.setLore(Arrays.asList(votes + " votes"));
		random.setItemMeta(randomM);
		inv.setItem(22, random);
		
		player.openInventory(inv);
	}
}
