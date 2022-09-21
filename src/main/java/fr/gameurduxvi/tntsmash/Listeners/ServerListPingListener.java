package fr.gameurduxvi.tntsmash.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import fr.gameurduxvi.tntsmash.Main;
import fr.gameurduxvi.tntsmash.State;

public class ServerListPingListener implements Listener {
	
	@EventHandler
	public void onPing(ServerListPingEvent e) {
		if(Main.gameState.equals(State.NoMaps)) {
			e.setMotd(Main.prefix + " §cAucun monde charg§");
		}
		else if(Main.gameState.equals(State.Idle)) {
			e.setMotd(Main.prefix + " §7En attente de joueurs");
		}
		else if(Main.gameState.equals(State.Starting)) {
			e.setMotd(Main.prefix + " §7Commence dans §b" + Main.startTimer + " §7secondes");
		}
		else if(Main.gameState.equals(State.Started)) {
			e.setMotd(Main.prefix + " §cPartie en cours");
		}
		else if(Main.gameState.equals(State.Ended)) {
			e.setMotd(Main.prefix + " §aPartie termin§e");
		}
	}
}
