package fr.gameurduxvi.tntsmash.Storage;

import java.util.ArrayList;
import java.util.Date;

import lombok.Data;
import org.bukkit.entity.Player;

import fr.gameurduxvi.tntsmash.Main;

@Data
public class PlayerData {
	private Player player;
	private int life = 3;
	private String map = null;
	
	private Date launchedTNTDate = new Date();
	private Date stickDate = new Date();
	
	private Date lastDamageTime = new Date();
	private Player lastDamager = null;
	
	private int kill = 0;
	
	private boolean canBeMoved = true;
	
	public PlayerData(Player player) {
		this.player = player;
		player.setFlySpeed(0.1f);
		player.setFlying(false);
		player.setAllowFlight(false);
	}

	public static PlayerData getPlayerData(Player player) {
		for(PlayerData pd: Main.getPlayerDatas()) {
			if(pd.getPlayer().equals(player)) {
				return pd;
			}
		}
		return null;
	}
	
	public static boolean hasPlayerData(Player player) {
		for(PlayerData pd: Main.getPlayerDatas()) {
			if(pd.getPlayer().equals(player)) {
				return true;
			}
		}
		return false;
	}
	
	public static ArrayList<PlayerData> getAlivePlayers() {
		ArrayList<PlayerData> list = new ArrayList<>();
		for(PlayerData pd: Main.getPlayerDatas()) {
			if(pd.life > 0) {
				list.add(pd);
			}
		}
		return list;
	}
}
