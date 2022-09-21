package fr.gameurduxvi.tntsmash.Storage;

import fr.gameurduxvi.tntsmash.Main;
import lombok.Data;

@Data
public class TNTData {
	private PlayerData playerData;
	private String TNT_UUID;
	
	public TNTData(PlayerData playerData, String TNT_UUID) {
		this.playerData = playerData;
		this.TNT_UUID = TNT_UUID;
	}
	
	public static TNTData getTNTData(String TNT_UUID) {
		for(TNTData td: Main.getTntData()) {
			if(td.getTNT_UUID().equals(TNT_UUID)) {
				return td;
			}
		}
		return null;
	}
	
	public static boolean hasTNTData(String TNT_UUID) {
		for(TNTData td: Main.getTntData()) {
			if(td.getTNT_UUID().equals(TNT_UUID)) {
				return true;
			}
		}
		return false;
	}
}
