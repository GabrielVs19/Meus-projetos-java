package ibieel.minigames.com;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

public class MobsManager {

	public static int chickens = 25;
	public static int pigs = 25;
	public static int cows = 25;
	public static int mushroomCows = 25;
	public static int pigZombies = 25;
	public static boolean firstTime = true;

	private static Location getRandomSpawnMob(){
		return Main.getSpawnsMobs().get((int) (Math.random() * Main.getSpawnsMobs().size()));
	}

	public static void spawn(){
		new BukkitRunnable(){
			@SuppressWarnings("deprecation")
			public void run(){
				Main.updateMobs();
				World world = Bukkit.getWorlds().get(0);
				for(Entry<EntityType, Integer> mobs : Main.getMobs().entrySet()){
					for(int i = 0; i < (firstTime ? 25 : 25 - mobs.getValue()); i++){
						 world.spawnCreature(getRandomSpawnMob(), mobs.getKey());
					}
				}
				if(firstTime){
					firstTime = false;
				}
				chickens = 25;
				cows = 25;
				pigs = 25;
				mushroomCows = 25;
				pigZombies = 25;
			}
		}.runTaskTimer(Main.getInstance(), 0, 20 * 30);
	}

}
