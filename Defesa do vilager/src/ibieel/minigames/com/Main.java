package ibieel.minigames.com;

import ibieel.minigames.com.CustomMobs.CustomCreeper;
import ibieel.minigames.com.CustomMobs.CustomSkeleton;
import ibieel.minigames.com.CustomMobs.CustomSpider;
import ibieel.minigames.com.CustomMobs.CustomVillager;
import ibieel.minigames.com.CustomMobs.CustomZombie;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_7_R2.EntityInsentient;
import net.minecraft.server.v1_7_R2.EntityTypes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.MiniGame;

public class Main extends JavaPlugin{

	private static Location locVillager = null;
	private static  ArrayList<Location> basesSpawnMobs = new ArrayList<Location>();
	private static ArrayList<Location> basesBonus = new ArrayList<Location>();

	public void onEnable(){
		registerEntity("ZombieMA", 54, CustomZombie.class);
		registerEntity("CreeperMA", 50, CustomCreeper.class);
		registerEntity("SkeletonMA", 51, CustomSkeleton.class);
		registerEntity("SpiderMA", 52, CustomSpider.class);
		registerEntity("VillagerMA", 120, CustomVillager.class);
		try{
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			Scoreboard board = manager.getMainScoreboard();
			Objective objective = board.getObjective("Kills");
			objective.unregister();
		}catch(Exception e){
			System.out.println("");
		}
		getServer().getPluginManager().registerEvents(new DefendaVilager(this), this);
		getServer().getPluginManager().registerEvents(new Listener(this), this);
		getCommand("start").setExecutor(this);
		getCommand("addbaselocation").setExecutor(this);
		getCommand("addbonusloc").setExecutor(this);
		getCommand("addvillagerloc").setExecutor(this);
		Bukkit.getWorlds().get(0).setGameRuleValue("mobGriefing", "false");
		Bukkit.getWorlds().get(0).setTime(19000);
		Bukkit.getWorlds().get(0).setGameRuleValue("doDaylightCycle", "false");
		Bukkit.getWorlds().get(0).setGameRuleValue("doMobLoot ", "false");
		CoreD.setAllowJoinSpectators(true);
		CoreD.setAllowTeamChat(true);
		CoreD.setCancelBlockBreakEvent(true);
		CoreD.setCancelBlockPlaceEvent(true);
		CoreD.setDisableDeathScreen(true);
		CoreD.setDisablePvP(true);
		CoreD.setCancelHangingBreakByEntityEvent(true);
		CoreD.setToSpectatorOnRespawn(true);
		CoreD.setStore(true);
		CoreD.setCancelFoodLevelChangeEvent(false);
		CoreD.setCancelPlayerDropItemEvent(true);
		CoreD.setCancelPlayerPickupItemEvent(false);
		CoreD.setMiniGame(new MiniGame("Monster Attack", "start", 15));
		CoreD.setMaxPlayers(30);
	}
	
	public static void setLocVillager(Location loc){
		locVillager = loc;
	}
	
	public static Location getLocVillager(){
		return locVillager;
	}
	
	public static ArrayList<Location> getMobSpawn(){
		return basesSpawnMobs;
	}
	
	public static ArrayList<Location> getBonusSpawn(){
		return basesBonus;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(commandLabel.equalsIgnoreCase("start")){
			if(sender.isOp()){
				CoreD.setRunning();
				try {
					DefendaVilager.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if(commandLabel.equalsIgnoreCase("addbaselocation")){
			if(sender.isOp()){
				Location loc = new Location(Bukkit.getWorlds().get(0), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
				basesSpawnMobs.add(loc);
			}
		}else if(commandLabel.equalsIgnoreCase("addbonusloc")){
			if(sender.isOp()){
				Location loc = new Location(Bukkit.getWorlds().get(0), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
				basesBonus.add(loc);
			}
		}else if(commandLabel.equalsIgnoreCase("addvillagerloc")){
			locVillager = new Location(Bukkit.getWorlds().get(0), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public void registerEntity(String name, int id, Class<? extends EntityInsentient> customClass){
		try{
			List<Map<?, ?>> dataMaps = new ArrayList<Map<?, ?>>();
			for(Field f : EntityTypes.class.getDeclaredFields()){
				if(f.getType().getSimpleName().equals(Map.class.getSimpleName())){
					f.setAccessible(true);
					dataMaps.add((Map<?, ?>) f.get(null));
				}
			}
			((Map<Class<? extends EntityInsentient>, String>) dataMaps.get(1)).put(customClass, name);
			((Map<Class<? extends EntityInsentient>, Integer>) dataMaps.get(3)).put(customClass, id);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
