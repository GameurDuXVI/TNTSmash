package fr.gameurduxvi.tntsmash.Tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.util.Vector;

import fr.gameurduxvi.tntsmash.Main;
import fr.gameurduxvi.tntsmash.State;
import fr.gameurduxvi.tntsmash.Storage.MapData;
import fr.gameurduxvi.tntsmash.Storage.PlayerData;
import fr.gameurduxvi.tntsmash.Storage.TNTData;

public class GameTask extends BukkitRunnable {

	public static String STICK = "§6Kabé";
	public static String TNT = "§4§lTNT";
	public static String BOW = "§6Puncheur";

	public static String VOTE = "§bVote";

	public static String SNOW_BALL = "§6Grenade";
	
	public static ArrayList<ItemStack> items = new ArrayList<>();
	
	public GameTask() {
		ItemStack it1 = new ItemStack(Material.FISHING_ROD);
		it1.setDurability((short) 2);
		items.add(it1);

		ItemStack it2 = new ItemStack(Material.ARROW);
		items.add(it2);

		ItemStack it3 = new ItemStack(Material.SNOW_BALL);
		ItemMeta it3M = it3.getItemMeta();
		it3M.setDisplayName(SNOW_BALL);
		it3.setItemMeta(it3M);
		items.add(it3);
		
		//ItemStack it4 = new ItemStack(Material.ENDER_PEARL);
		//items.add(it4);
	}
	
	@Override
	public void run() {
		if(Main.currentMap != null && Main.gameState.equals(State.Started)) {
			for(PlayerData pd: PlayerData.getAlivePlayers()) {
				if(pd.getPlayer().getLocation().getY() < Main.currentMap.getHeight() && pd.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
					death(pd.getPlayer());
				}
			}
			
			if(Main.itemsTimer >= 40) {
				Main.itemsTimer = 0;
				
				Collection<Location> locs = Main.currentMap.getItemLocations();
				Collections.shuffle((List<?>) locs);
				
				int i = 0;
				for(Location loc: locs) {
					if(i < 2) {
						boolean used = false;
						Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, 3, 3, 3);
						for(Entity e: entities) {
							if(e instanceof Item) {
								used = true;
								break;
							}
						}
						if(!used) {
							i++;
							loc.getWorld().dropItemNaturally(loc, items.get(new Random().nextInt(items.size())));
						}						
					}
				}
			}
		}
		
		
		
		if(PlayerData.getAlivePlayers().size() < 2 && Main.gameState.equals(State.Started)) {
			PlayerData pd = PlayerData.getAlivePlayers().get(0);
			
			Main.gameState = State.Ended;
			
			Bukkit.broadcastMessage(Main.prefix + " §b" + pd.getPlayer().getName() + "§6 a gagné la partie !");
			
			for(int halfSec = 0; halfSec <= 10; halfSec++) {
				for(int times = 0; times <= 5; times ++) {
					Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
						
						@Override
						public void run() {
							World w = pd.getPlayer().getWorld();
							int x = (int) pd.getPlayer().getLocation().getX() + new Random().nextInt(6) - 3;
							int y = (int) pd.getPlayer().getLocation().getY() + new Random().nextInt(3);
							int z = (int) pd.getPlayer().getLocation().getZ() + new Random().nextInt(6) - 3;
							Location loc = new Location(w, x, y, z);
							Firework fw = w.spawn(loc, Firework.class);
							FireworkMeta fwM = fw.getFireworkMeta();
							Color[] colors = {Color.AQUA, Color.BLACK, Color.BLUE, Color.FUCHSIA, Color.GRAY, Color.GREEN, Color.LIME, Color.MAROON, 
									Color.NAVY, Color.OLIVE, Color.ORANGE, Color.PURPLE, Color.RED, Color.SILVER, Color.TEAL, Color.WHITE, Color.YELLOW
							};
							fwM.addEffect(FireworkEffect.builder().with(Type.values()[new Random().nextInt(Type.values().length - 1)]).withColor(colors[new Random().nextInt(colors.length - 1)]).build());
							fwM.setPower(1);
							fw.setFireworkMeta(fwM);
						}
					}, 10 * halfSec);
				}
			}
			
			for(PlayerData pd2: Main.getPlayerDatas()) {
				if(!pd.equals(pd2)) {
					pd2.getPlayer().teleport(pd.getPlayer());
				}
			}

			Bukkit.broadcastMessage(Main.prefix + " §7Top kills:");
			int i = 0;
			Iterator<Entry<Player, Integer>> iterator = topKills().entrySet().iterator();
			while(iterator.hasNext()) {
				i++;
				Entry<Player, Integer> entry = iterator.next();
				if(i <= 3) {
					Bukkit.broadcastMessage(Main.prefix + "    §7- §b" + entry.getKey().getName() + "§7 - §6" + entry.getValue());
				}
			}
			
			pd.getPlayer().setAllowFlight(true);
			pd.getPlayer().setFlying(true);
			
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					Main.gameState = State.Idle;
					
					Main.getPlayerDatas().clear();
					
					Main.secondes = -7;
					Main.minutes = 0;
					
					for(Player player: Bukkit.getOnlinePlayers()) {
						for(Objective ob: player.getScoreboard().getObjectives()) {
							ob.unregister();
						}
						Main.getInstance().getServer().getPluginManager().callEvent(new PlayerJoinEvent(player, null));
						player.setFlying(false);
						player.setAllowFlight(false);
					}
				}
			}, 5 * 20);
		}
		
		checkTimer();
	}
	
	public static void checkTimer() {
		if(Main.gameState.equals(State.Idle) && Main.getPlayerDatas().size() >= Main.min) {
			Main.startTimer = 20 + 2;
			Main.gameState = State.Starting;
		}
		
		if(Main.gameState.equals(State.Starting)) {
			Main.startTimer--;
			if(Main.getPlayerDatas().size() < Main.min) {
				Main.gameState = State.Idle;
			}
			if(Main.startTimer == 10 || Main.startTimer == 3 || Main.startTimer == 2 || Main.startTimer == 1) {
				Bukkit.broadcastMessage(Main.prefix + " §7Début de la partie dans " + Main.startTimer + " secondes !");
			}
			if(Main.startTimer == 0) {
				// Tp
				Bukkit.broadcastMessage(Main.prefix + " §7Début de la partie !");
				Main.gameState = State.Started;
				
				int maxVotes = 0;
				for(MapData md: Main.getMapData()) {
					int mapVotes = 0;
					for(PlayerData pd: Main.getPlayerDatas()) {
						if(pd.getMap() != null && pd.getMap().equals(md.getDir())) {
							mapVotes++;
						}
					}
					if(mapVotes > maxVotes) {
						maxVotes = mapVotes;
					}
				}
				
				ArrayList<MapData> maps = new ArrayList<>();
				for(MapData md: Main.getMapData()) {
					int mapVotes = 0;
					for(PlayerData pd: Main.getPlayerDatas()) {
						if(pd.getMap() != null && pd.getMap().equals(md.getDir())) {
							mapVotes++;
						}
					}
					
					if(mapVotes == maxVotes) {
						maps.add(md);
					}
				}
				
				Random rnd = new Random();
				
				//Bukkit.broadcastMessage("Choosing between " + maps.size() + " maps");
				Main.currentMap = maps.get(rnd.nextInt(maps.size()));
				
				//Bukkit.broadcastMessage(Main.currentMap.getName());
				
				String[] split = Main.currentMap.getDir().split("/");
				String worldName = split[split.length - 1];
				boolean found = false;
				for(World w: Bukkit.getWorlds()) {
					if(w.getName().equals(worldName)) {
						found = true;
					}
				}
				if(!found) {
					WorldCreator wc = new WorldCreator(worldName);
					wc.environment(Environment.THE_END);
					World w = Bukkit.createWorld(wc);
					
					w.setGameRuleValue("doFireTick", "true");
					
					for(Location loc: Main.currentMap.getPlayerLocations()) {
						loc.setWorld(w);
					}
					for(Location loc: Main.currentMap.getItemLocations()) {
						loc.setWorld(w);
					}
				}
				else {
					World w = Bukkit.getWorld(worldName);

					w.setGameRuleValue("doFireTick", "true");
					
					for(Location loc: Main.currentMap.getPlayerLocations()) {
						loc.setWorld(w);
					}
					for(Location loc: Main.currentMap.getItemLocations()) {
						loc.setWorld(w);
					}
				}
				
				Collection<Location> playerLocs = Main.currentMap.getPlayerLocations();
				Collections.shuffle((List<?>) playerLocs);
				int i = 0;
				for(PlayerData pd: Main.getPlayerDatas()) {
					i++;
					pd.getPlayer().setAllowFlight(true);
					pd.getPlayer().setFlying(true);
					pd.getPlayer().setFlySpeed(0);
					
					pd.getPlayer().teleport(((ArrayList<Location>)playerLocs).get(i));
					
					spawnPlayer(pd.getPlayer(), false);
					
					pd.getPlayer().closeInventory();
				}
				
				Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						Collection<Entity> entities = Main.currentMap.getPlayerLocations().get(1).getWorld().getNearbyEntities(Main.currentMap.getPlayerLocations().get(1), 500, 500, 500);
						for(Entity e: entities) {
							if(!(e instanceof Player)) {
								e.remove();
							}
						}
					}
				}, 10);
			}
			new ScoreboardTask().run();
		}
	}
	
	public static void spawnPlayer(Player player, boolean teleport) {
		PlayerData pd = PlayerData.getPlayerData(player);
		
		player.getInventory().clear();
		
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {				
				player.setAllowFlight(true);
				player.setFlying(true);
				player.setFlySpeed(0);
				
				if(teleport) {
					Collection<Location> playerLocs = Main.currentMap.getPlayerLocations();
					Collections.shuffle((List<?>) playerLocs);
					
					for(Location loc: playerLocs) {
						Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, 5, 5, 5);
						
						boolean hasPlayerInZone = false;
						for(Entity e: entities) {
							if(e instanceof Player) {
								hasPlayerInZone = true;
							}
						}
						if(!hasPlayerInZone) {
							player.teleport(loc);
							break;
						}
					}
				}
				
				player.sendTitle("§63", "§bSpawning in");
			}
		}, 20 * 3);
		
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				player.sendTitle("§62", "§bSpawning in");
			}
		}, 20 * 4);
		
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				player.sendTitle("§61", "§bSpawning in");
			}
		}, 20 * 5);
		
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				player.setGameMode(GameMode.ADVENTURE);
				
				player.sendTitle("§6Go", "§a");
				player.setFlySpeed(0.1f);
				player.setFlying(false);
				player.setAllowFlight(false);
				
				ItemStack stick = new ItemStack(Material.STICK);
				ItemMeta stickM = stick.getItemMeta();
				stickM.setDisplayName(STICK);
				stickM.addEnchant(Enchantment.KNOCKBACK, 2, true);
				stick.setItemMeta(stickM);
				player.getInventory().setItem(3, stick);
				
				ItemStack tnt = new ItemStack(Material.TNT);
				ItemMeta tntM = tnt.getItemMeta();
				tntM.setDisplayName(TNT);
				tnt.setItemMeta(tntM);
				player.getInventory().setItem(4, tnt);
				
				ItemStack bow = new ItemStack(Material.BOW);
				ItemMeta bowM = bow.getItemMeta();
				bowM.setDisplayName(BOW);
				bowM.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, true);
				bowM.setUnbreakable(true);
				bow.setItemMeta(bowM);
				player.getInventory().setItem(5, bow);
				
				pd.setCanBeMoved(true);
			}
		}, 20 * 6);
		
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				player.sendTitle("§a", "§a");
			}
		}, 20 * 7);
	}
	
	public static void spectator(Player player) {
		player.setGameMode(GameMode.SPECTATOR);
		if(Main.getPlayerDatas().size() > 0) {
			player.teleport(Main.getPlayerDatas().get(1).getPlayer().getLocation());
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void death(Player player) {
		PlayerData pd = PlayerData.getPlayerData(player);
		
		pd.setCanBeMoved(false);
		
		pd.getPlayer().setGameMode(GameMode.SPECTATOR);
		
		Date now = new Date();
		
		long diffInMillies = Math.abs(now.getTime() - pd.getLastDamageTime().getTime());
		
		if(diffInMillies < 15000) {
			if(pd.getLastDamager().getName().equals(pd.getPlayer().getName())) {
				Bukkit.broadcastMessage(Main.prefix + " §b" + pd.getPlayer().getName() + "§7 s'est suicidé !");
				player.teleport(PlayerData.getAlivePlayers().get(new Random().nextInt(PlayerData.getAlivePlayers().size())).getPlayer());
			}
			else {
				String killMessage = "";
				PlayerData damager = PlayerData.getPlayerData(pd.getLastDamager());
				damager.setKill(damager.getKill() + 1);
				switch (damager.getKill()) {
					case 1:
						killMessage = "";
						break;
					case 2:
						killMessage = "§7(§aDouble Kill§7)";
						break;
					case 3:
						killMessage = "§7(§aTriple Kill§7)";
						break;
					case 4:
						killMessage = "§7(§eMulti-Kill§7)";
						break;
					case 5:
						killMessage = "§7(§eUltra Kill§7)";
						break;
					case 6:
						killMessage = "§7(§cMonster Kill§7)";
						break;
					case 7:
						killMessage = "§7(§4Rampage§7)";
						break;
					case 8:
						killMessage = "§7(§5Magic§7)";
						break;
					case 9:
						killMessage = "§7(§dGodly§7)";
						break;
					case 10:
						killMessage = "§7(§6Legendary§7)";
						break;
					case 11:
						killMessage = "§7(§5Whuuuutttt??§7)";
						break;
					case 12:
						killMessage = "§7(§5C'est quoi ta souris?§7)";
						break;
					default:
						killMessage = "§7(§5§kaaaaaaaaaaa§r§7)";
						break;
				}
				pd.getLastDamager().sendTitle("§a", killMessage);
				Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						pd.getLastDamager().sendTitle("§a", "§a");
					}
				}, 30);
				Bukkit.broadcastMessage(Main.prefix + " §b" + pd.getLastDamager().getName() + "§7 a tué §b" + pd.getPlayer().getName() + "§7 ! " + killMessage);
				player.teleport(pd.getLastDamager());
			}
		}
		else {
			Bukkit.broadcastMessage(Main.prefix + " §b" + pd.getPlayer().getName() + "§7 s'est suicidé !");
		}
		
		pd.setLife(pd.getLife() - 1);
		
		if(pd.getLife() <= 0) {
			spectator(player);
		}
		else {
			if(pd.getLife() > 0) {
				player.setMaxHealth(pd.getLife() * 2);				
			}
			spawnPlayer(player, true);
		}
	}
	
	public static void shootTNT(Player player) {
		TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.PRIMED_TNT);
		tnt.setFuseTicks(20);
		tnt.setVelocity(player.getEyeLocation().getDirection().add(new Vector(0, 0.1, 0)));
		Main.getTntData().add(new TNTData(PlayerData.getPlayerData(player), tnt.getUniqueId().toString()));
	}
	
	public static HashMap<Player, Integer> topKills() {
		HashMap<Player, Integer> hm = new HashMap<>();
		for(PlayerData pd: Main.getPlayerDatas()) {
			hm.put(pd.getPlayer(), pd.getKill());		
	    }
		
        List<Map.Entry<Player, Integer> > list =
               new LinkedList<Map.Entry<Player, Integer> >(hm.entrySet());
        
        Collections.sort(list, new Comparator<Map.Entry<Player, Integer> >() {
            public int compare(Map.Entry<Player, Integer> o1,
                               Map.Entry<Player, Integer> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        
        HashMap<Player, Integer> temp = new LinkedHashMap<Player, Integer>();
        for (Map.Entry<Player, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}
