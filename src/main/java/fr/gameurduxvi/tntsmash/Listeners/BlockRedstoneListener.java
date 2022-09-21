package fr.gameurduxvi.tntsmash.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockRedstoneListener implements Listener {
	
	@EventHandler
	public void onRedstone(BlockRedstoneEvent e) {
		e.setNewCurrent(e.getOldCurrent());
	}
}
