package fr.gameurduxvi.tntsmash.Storage;

import java.util.ArrayList;

import lombok.Data;
import org.bukkit.Location;

@Data
public class MapData {
	private String name;
	private String dir;
	private int height;
	private ArrayList<Location> playerLocations = new ArrayList<>();
	private ArrayList<Location> itemLocations = new ArrayList<>();
	
	public MapData(String name, String dir) {
		this.name = name;
		this.dir = dir;
	}
}
