package fr.gameurduxvi.tntsmash;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fr.gameurduxvi.tntsmash.Listeners.BlockRedstoneListener;
import fr.gameurduxvi.tntsmash.Listeners.EntityDamageByEntityListener;
import fr.gameurduxvi.tntsmash.Listeners.EntityDamageListener;
import fr.gameurduxvi.tntsmash.Listeners.EntityExplodeListener;
import fr.gameurduxvi.tntsmash.Listeners.EntitySpawnListener;
import fr.gameurduxvi.tntsmash.Listeners.FoodLevelChangeListener;
import fr.gameurduxvi.tntsmash.Listeners.InventoryClickListener;
import fr.gameurduxvi.tntsmash.Listeners.InventoryDragListener;
import fr.gameurduxvi.tntsmash.Listeners.PlayerDropItemListener;
import fr.gameurduxvi.tntsmash.Listeners.PlayerFishListener;
import fr.gameurduxvi.tntsmash.Listeners.PlayerInteractListener;
import fr.gameurduxvi.tntsmash.Listeners.PlayerJoinListener;
import fr.gameurduxvi.tntsmash.Listeners.PlayerQuitListener;
import fr.gameurduxvi.tntsmash.Listeners.PlayerSwapHandItemsListener;
import fr.gameurduxvi.tntsmash.Listeners.ProjectileHitListener;
import fr.gameurduxvi.tntsmash.Listeners.ServerListPingListener;
import fr.gameurduxvi.tntsmash.Storage.MapData;
import fr.gameurduxvi.tntsmash.Storage.PlayerData;
import fr.gameurduxvi.tntsmash.Storage.TNTData;
import fr.gameurduxvi.tntsmash.Tasks.ActionBarTask;
import fr.gameurduxvi.tntsmash.Tasks.GameTask;
import fr.gameurduxvi.tntsmash.Tasks.ScoreboardTask;

public class Main extends JavaPlugin {

	@Getter
	private static Main instance;
	
	public static String name = "§4TNT§6Smash";
	public static String prefix = "§f[" + name + "§f]";
	public static String path = "plugins/TNTSmash/";
	
	public static State gameState = State.Idle;
	
	public static int secondes = -7;
	public static int minutes = 0;
	public static int startTimer = 0;	
	public static int itemsTimer = 0;
	
	public static int min = 2;
	public static int max = 8;
	

	public ScoreboardManager manager;

	public static Location lobbyLocation;
	public static MapData currentMap = null;
	
	
	/*
	 * Storage Lists
	 */
	@Getter
	private static ArrayList<PlayerData> playerDatas = new ArrayList<>();
	@Getter
	private static ArrayList<MapData> mapData = new ArrayList<>();
	@Getter
	private static ArrayList<TNTData> tntData = new ArrayList<>();
	
	
	
	/*
	 * Start and Stop Functions
	 */
	@SuppressWarnings({ "deprecation" })
	@Override
	public void onEnable() {
		getServer().getConsoleSender().sendMessage(prefix + " §eStarting " + name);
		
		/*
		 * Instances
		 */
		instance = this;
		
		manager = Bukkit.getScoreboardManager();
		
		/*
		 * Listeners
		 */
		getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
		getServer().getPluginManager().registerEvents(new FoodLevelChangeListener(), this);
		getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
		getServer().getPluginManager().registerEvents(new EntitySpawnListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
		getServer().getPluginManager().registerEvents(new EntityExplodeListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerDropItemListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryClickListener(), this); 
		getServer().getPluginManager().registerEvents(new ServerListPingListener(), this);
		getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerSwapHandItemsListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryDragListener(), this);
		getServer().getPluginManager().registerEvents(new ProjectileHitListener(), this);
		getServer().getPluginManager().registerEvents(new BlockRedstoneListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerFishListener(), this);
		
		/*
		 * Config Files
		 */
		File dir = new File(path);
		if(!dir.exists()) {
			dir.mkdir();
		}
		
		File configFile = new File(path + "config.json");
		if(!configFile.exists()) {
			try {
				configFile.createNewFile();
				FileWriter fw = new FileWriter(path + "config.json");
				fw.write("{}");
				fw.flush();
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//JSONObject jo = new JSONObject();
			
			lobbyLocation = new Location(Bukkit.getWorlds().get(0), 0, 100, 0);
		}
		else {
			try {
				JSONObject jo = (JSONObject) new JSONParser().parse(getJsonFromFileToUTF8(path + "config.json"));
				
				try {
					String strLoc = (String) jo.get("lobby");
					String[] split = strLoc.split(",");
					
					World w;
					
					if(Bukkit.getWorld(split[0]) != null) {
						w = Bukkit.getWorld(split[0]);
					}
					else {
						w = new WorldCreator(split[0]).createWorld();
					}
					
					if(split.length > 4) {
						lobbyLocation = new Location(w, Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]));
					}
					else {
						lobbyLocation = new Location(w, Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
					}
				} catch (Exception e) {
					getServer().getConsoleSender().sendMessage(prefix + " §cAn error occurs with the lobby location in §econfig.json");
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * Tasks
		 */
		Bukkit.getScheduler().runTaskTimer(this, new ScoreboardTask(), 20, 20);
		Bukkit.getScheduler().runTaskTimer(this, new GameTask(), 20, 20);
		Bukkit.getScheduler().runTaskTimer(this, new ActionBarTask(), 2, 2);
		
		
		/*
		 * Refreshing actual players
		 */
		for(Player player: Bukkit.getOnlinePlayers()) {
			for(Objective ob: player.getScoreboard().getObjectives()) {
				ob.unregister();
			}
			getServer().getPluginManager().callEvent(new PlayerJoinEvent(player, null));
		}
		

		for(World w: Bukkit.getWorlds()) {
			for(Entity e: w.getEntities()) {
				if(!(e instanceof Player)) {
					e.remove();
				}
			}
		}
		
		
		/*
		 * Getting active maps
		 */
		File serverDir = new File("");
		serverDir = new File(serverDir.getAbsolutePath() + "/");
		for(File file: serverDir.listFiles()) {
			if(file.isDirectory()) {
				File mapConfigFile = new File(file.getAbsolutePath() + "/data.json");
				if(mapConfigFile.exists()) {
					try {
						JSONObject jo = (JSONObject) new JSONParser().parse(getJsonFromFileToUTF8(file.getAbsolutePath() + "/data.json"));
						
						boolean error = false;
						
						String name = "";
						try {
							name = (String) jo.get("name");
							if(name == null) {
								error = true;
								getServer().getConsoleSender().sendMessage(prefix + " §c\"name\" has not been set. §e" + file.getName() + "/data.json");
							}
						} catch (Exception e) {
							error = true;
							getServer().getConsoleSender().sendMessage(prefix + " §cAn error occurs with the \"name\". §e" + file.getName() + "/data.json");
							e.printStackTrace();
						}
						
						int height = 0;
						try {
							height = Integer.parseInt("" + jo.get("height"));
							if(name == null) {
								error = true;
								getServer().getConsoleSender().sendMessage(prefix + " §c\"height\" has not been set. §e" + file.getName() + "/data.json");
							}
						} catch (Exception e) {
							error = true;
							getServer().getConsoleSender().sendMessage(prefix + " §cAn error occurs with the \"height\". §e" + file.getName() + "/data.json");
							e.printStackTrace();
						}
						
						ArrayList<Location> playersLocations = new ArrayList<>();
						try {
							JSONObject joLocation = (JSONObject) jo.get("locations");
														
							JSONArray jaPlayers = (JSONArray) joLocation.get("players");
							
							for(Object obj: jaPlayers) {
								String strLoc = (String) obj;
								String[] split = strLoc.split(",");
								playersLocations.add(new Location(Bukkit.getWorld(file.getName()), Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4])));
							}
							
							if(playersLocations.size() < 8) {
								error = true;
								getServer().getConsoleSender().sendMessage(prefix + " §e" + file.getName() + "§c hasn't enough player locations! §e" + file.getName() + "/data.json");
							}
						} catch (Exception e) {
							error = true;
							getServer().getConsoleSender().sendMessage(prefix + " §cAn error occurs with the \"location.players\". §e" + file.getName() + "/data.json");
							e.printStackTrace();
						}
						
						ArrayList<Location> itemLocations = new ArrayList<>();
						try {
							JSONObject joLocation = (JSONObject) jo.get("locations");
														
							JSONArray jaPlayers = (JSONArray) joLocation.get("items");
							
							for(Object obj: jaPlayers) {
								String strLoc = (String) obj;
								String[] split = strLoc.split(",");
								itemLocations.add(new Location(Bukkit.getWorld(file.getName()), Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4])));
							}
						} catch (Exception e) {
							error = true;
							getServer().getConsoleSender().sendMessage(prefix + " §cAn error occurs with the \"location.item\". §e" + file.getName() + "/data.json");
							e.printStackTrace();
						}
						
						if(!error) {
							MapData md = new MapData(name, file.getAbsolutePath());
							md.setHeight(height);
							md.setPlayerLocations(playersLocations);
							md.setItemLocations(itemLocations);
							getMapData().add(md);
						}						
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		if(getMapData().size() == 0) {
			gameState = State.NoMaps;
		}
		
		getServer().getConsoleSender().sendMessage(prefix + " §aStarting complete");
	}
	
	@Override
	public void onDisable() {
		getServer().getConsoleSender().sendMessage(prefix + " §eStopping " + name);
		
		if(currentMap != null) {
			getServer().getConsoleSender().sendMessage(prefix + " §eUnloading §b" + currentMap.getName());
			String[] split = currentMap.getDir().split("/");
			String worldName = split[split.length - 1];
			try {
				Bukkit.unloadWorld(worldName, true);
				System.gc();
				getServer().getConsoleSender().sendMessage(prefix + " §aUnloading complete");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		getServer().getConsoleSender().sendMessage(prefix + " §aStopping complete");
	}
	
	public static String getJsonFromFileToUTF8(String path) {
		try {
			Path Vpath = Paths.get(path);
			List<String> list = Files.readAllLines(Vpath, StandardCharsets.UTF_8);
			String json = "";
	        for(String t: list) {
	        	//Bukkit.broadcastMessage(t);
	        	json += t;
	        }
	        json = json.replace("\t", "");
	        return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "{}";
	}
}
