package fr.gameurduxvi.tntsmash.Tasks;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import fr.gameurduxvi.tntsmash.Main;
import fr.gameurduxvi.tntsmash.State;
import fr.gameurduxvi.tntsmash.Storage.PlayerData;

public class ScoreboardTask extends BukkitRunnable {

	@Override
	public void run() {
		if(Main.gameState.equals(State.Started) || Main.gameState.equals(State.Ended)) {
			/*******************************************************
			 * Time
			 *******************************************************/
			if(Main.gameState.equals(State.Started)) {
				Main.secondes++;
				Main.itemsTimer++;
				if(Main.secondes >= 60) {
					Main.secondes = 0;
					Main.minutes++;
				}
			}
							
			
			/*******************************************************
			 * Scoreboard
			 *******************************************************/
			int secondes = Main.secondes;
			if(secondes < 0) secondes = 0;
			String secAffichage = "" + secondes;
		    if(secondes < 10) secAffichage = "0" + secondes;
		    
		    String minAffichage = "" + Main.minutes;
		    if(Main.minutes < 10) minAffichage = "0" + Main.minutes;
		    
		    for(PlayerData pd: Main.getPlayerDatas()) {
				if(pd.getPlayer().getScoreboard().equals(Main.getInstance().manager.getMainScoreboard())) pd.getPlayer().setScoreboard(Main.getInstance().manager.getNewScoreboard());
				
				Scoreboard board = pd.getPlayer().getScoreboard();
				boolean notExist = board.getObjective(pd.getPlayer().getName()) == null;
			    Objective objective = board.getObjective(pd.getPlayer().getName()) == null ? board.registerNewObjective(pd.getPlayer().getName(), "dummy") : board.getObjective(pd.getPlayer().getName());
			    
			    if(notExist) objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			    
			    objective.setDisplayName(Main.prefix);
			    
			    Map<Integer, String> list = new HashMap<Integer, String>();
			    
			    list.put(6 + Main.getPlayerDatas().size(), "§a");
				list.put(5 + Main.getPlayerDatas().size(), "§a" + PlayerData.getAlivePlayers().size() + " Joueurs");
				list.put(4 + Main.getPlayerDatas().size(), "§9Timer: " + minAffichage + ":" + secAffichage);
				list.put(3 + Main.getPlayerDatas().size(), "§b");
				
				int i = 0;
				for(PlayerData pd2: Main.getPlayerDatas()) {
					i++;
					if(pd2.getLife() > 0) {
						list.put(2 + i, "§b>> §7" + pd2.getPlayer().getName() + ":§6 " + pd2.getLife());
					}
					else {
						list.put(2 + i, "§b>> §c§m" + pd2.getPlayer().getName() + ":§6 " + pd2.getLife());
					}
				}
				list.put(2, "§c");
				list.put(1, "§7Serveur: ####");
				list.put(0, "§dplay.menelia.fr");
				
				for(Map.Entry<Integer, String> entry : list.entrySet()) {
					replaceScore(objective, entry.getKey(), ChatColor.translateAlternateColorCodes('§', "" + entry.getValue()));
				}
				pd.getPlayer().setScoreboard(board);
			}
		}
		else {
			for(PlayerData pd: Main.getPlayerDatas()) {
				if(pd.getPlayer().getScoreboard().equals(Main.getInstance().manager.getMainScoreboard())) pd.getPlayer().setScoreboard(Main.getInstance().manager.getNewScoreboard());
				
				Scoreboard board = pd.getPlayer().getScoreboard();
				boolean notExist = board.getObjective(pd.getPlayer().getName()) == null;
			    Objective objective = board.getObjective(pd.getPlayer().getName()) == null ? board.registerNewObjective(pd.getPlayer().getName(), "dummy") : board.getObjective(pd.getPlayer().getName());
			    
			    if(notExist) objective.setDisplaySlot(DisplaySlot.SIDEBAR);

			    objective.setDisplayName(Main.prefix);
			    
			    Map<Integer, String> list = new HashMap<Integer, String>();
				list.put(5, "§a");
				list.put(4, "§a" + Main.getPlayerDatas().size() + " Joueurs");
				if(Main.gameState.equals(State.Starting)) {
					list.put(3, "§fDémarrage dans " + (Main.startTimer - 1));
				}
				else if(Main.gameState.equals(State.NoMaps)) {
					list.put(3, "§cAucun monde chargé");					
				}
				else {
					list.put(3, "§fEn attente d'autres joueurs");
				}
				list.put(2, "§b");
				list.put(1, "§7Serveur: ####");
				list.put(0, "§dplay.menelia.fr");
				
				for(Map.Entry<Integer, String> entry : list.entrySet()) {
					replaceScore(objective, entry.getKey(), ChatColor.translateAlternateColorCodes('§', "" + entry.getValue()));
				}
				pd.getPlayer().setScoreboard(board);
			}
		}
	}
	
	/*******************************************************
	 * Scoreboard Function
	 *******************************************************/
	public String getEntryFromScore(Objective o, int score) {
	    if(o == null) return null;
	    if(!hasScoreTaken(o, score)) return null;
	    for (String s : o.getScoreboard().getEntries()) {
	        if(o.getScore(s).getScore() == score) return o.getScore(s).getEntry();
	    }
	    return null;
	}

	public boolean hasScoreTaken(Objective o, int score) {
	    for (String s : o.getScoreboard().getEntries()) {
	        if(o.getScore(s).getScore() == score) return true;
	    }
	    return false;
	}

	public void replaceScore(Objective o, int score, String name) {
	    if(hasScoreTaken(o, score)) {
	        if(getEntryFromScore(o, score).equalsIgnoreCase(name)) return;
	        if(!(getEntryFromScore(o, score).equalsIgnoreCase(name))) o.getScoreboard().resetScores(getEntryFromScore(o, score));
	    }
	    o.getScore(name).setScore(score);
	}
}
