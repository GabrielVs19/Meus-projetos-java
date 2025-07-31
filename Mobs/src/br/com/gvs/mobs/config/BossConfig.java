package br.com.gvs.mobs.config;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
import org.bukkit.inventory.ItemStack;

import br.com.gvs.mobs.Mobs;
import br.com.gvs.mobs.event.BossSpawnEvent;
import br.com.gvs.mobs.managers.BossManager;
import br.com.gvs.mobs.util.AttackType;
import br.com.gvs.mobs.util.Boss;
import br.com.gvs.mobs.util.BossType;
import br.com.gvs.mobs.util.DialogsUtil;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.World;


public class BossConfig
{
	
	
	public static void loadConfig()
	{
		File bossFolder = new File(Mobs.getInstance().getDataFolder(), "Boss");
		if(!bossFolder.exists())
		{
			bossFolder.mkdir();
		}
		
		List<File> m = Arrays.asList(bossFolder.listFiles());
		List<File> bosses = new ArrayList<File>();
		for(File file : m)
		{
			if(file.getName().toLowerCase().endsWith(".yml"))
			{
				bosses.add(file);
			}
		}
		
		if(bosses.size() == 0)
		{
			for(BossType bossType : BossType.values())
			{
				String bossName = bossType.name();
				
				File mobFile = new File(bossFolder, "/" + bossName + ".yml");
				
				if(mobFile.exists())
				{
					continue;
				}
				
				InputStream is = Mobs.getInstance().getClass().getResourceAsStream("/br/com/gvs/mobs/resourcesb/" + bossName + ".yml");
				
				if(is == null)
				{
					continue;
				}
				BufferedWriter bw = null;
				try
				{
					String sourceText = IOUtils.toString(is, "UTF-8");
					bw = new BufferedWriter(new FileWriter(mobFile));
					bw.write(sourceText);
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				finally
				{
					try
					{
						bw.close();
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
				}
				
				bosses.add(mobFile);
			}
		}
		
		for(File config : bosses)
		{
			try
			{
				loadBoss(config);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Mobs.getInstance().getLogger().log(Level.INFO, config.getName() + " loading error!");
			}
		}
		
	}
	
	private static void loadBoss(File fconfig)
	{
		FileConfiguration config = YamlConfiguration.loadConfiguration(fconfig);
		int id = config.getInt("id");
		double attackDamage = config.getDouble("attackDamage");
		double moveSpeed = config.getDouble("moveSpeed");
		double health = config.getDouble("health");
		double followRange = config.getDouble("followRange");
		double knockbackResistence = config.getDouble("knockbackResistence");
		double reward = config.getDouble("reward");
		Map<ItemStack, Double> drops = MobsConfig.loadDrops((ArrayList<String>) config.getStringList("drops"));
		int maxDrops = config.getInt("maxDrops");
		HashMap<Integer, ItemStack> equipments = MobsConfig.loadEquipments((ArrayList<String>) config.getStringList("equipments"));
		
		ArrayList<Location> spawns = new ArrayList<>();
		for(String loc : config.getStringList("spawns"))
		{
			String[] location = loc.split(";");
			org.bukkit.World w = Bukkit.getWorld(location[0]);
			
			if(w != null)
			{
				spawns.add(new Location(w, Integer.parseInt(location[1]), Integer.parseInt(location[2]), Integer.parseInt(location[3])));
			}
		}
		
		List<String> spawnDialogs = config.getStringList("dialogs.spawn");
		List<String> deathDialogs = config.getStringList("dialogs.death");
		List<String> killDialogs = config.getStringList("dialogs.kill");
		DialogsUtil dialogs = new DialogsUtil(spawnDialogs, deathDialogs, killDialogs);
		
		Boss boss = new Boss(BossType.getBossByID(id), attackDamage, moveSpeed, health, followRange, knockbackResistence, reward, AttackType.HOSTILE, drops, maxDrops, equipments, spawns, dialogs);
		BossManager.addBoss(boss);
	}
	
	// private static ArrayList<File> bossesSpawnChunk = new ArrayList<>();
	
	public static void checkRespawnTime()
	{
		File bossFolder = new File(Mobs.getInstance().getDataFolder(), "Boss");
		
		if(!bossFolder.exists())
		{
			bossFolder.mkdir();
		}
		
		List<File> m = Arrays.asList(bossFolder.listFiles());
		
		for(File file : m)
		{
			// bossesSpawnChunk.add(file);
			if(checkRespawnTime(file))
			{
				break;
			}
		}
		
		/*
		 * new BukkitRunnable() {
		 * 
		 * 
		 * @Override public void run() { if(bossesSpawnChunk.isEmpty()) {
		 * cancel(); } for(File file : new ArrayList<File>(bossesSpawnChunk))
		 * { checkRespawnTime(file); }
		 * 
		 * } }.runTaskTimer(Mobs.getInstance(), 20 * 600, 20 * 600);
		 */
	}
	
	public static void attRespawnTime(BossType bossType)
	{
		if(bossType == null)
		{
			return;
		}
		
		Boss boss = BossManager.getBossByType(bossType);
		
		if(boss == null)
		{
			return;
		}
		
		File bossFolder = new File(Mobs.getInstance().getDataFolder(), "Boss");
		File file = new File(bossFolder, "/" + bossType.name() + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		// Formato da data
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SS");
		
		// Atualiza e salva a config
		config.set("respawnTime", df.format(new Date().getTime()));
		
		try
		{
			config.save(file);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	private static List<BossType> waitBoss = new ArrayList<BossType>();
	
	public static boolean checkRespawnTime(File file)
	{
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		int id = config.getInt("id");
		String respawnTime = config.getString("respawnTime");
		double respawnDelay = config.getDouble("respawnDelay");
		
		BossType bossType = BossType.getBossByID(id);
		
		if(bossType == null)
		{
			return true;
		}
		
		Boss boss = BossManager.getBossByType(bossType);
		
		if(boss == null)
		{
			return true;
		}
		
		// Formato da data
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SS");
		
		// Pega a data da config
		Date dataConfig = new Date();
		try
		{
			dataConfig = df.parse(respawnTime);
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		
		long lastSpawn = (long) (dataConfig.getTime() + (respawnDelay * 3600000));
		long now = new Date().getTime();
		
		boolean spawn = waitBoss.contains(boss.getBossType());
		// Se lastSpawn for antes ou igual a data atual
		if(lastSpawn <= now)
		{
			// Spawna o boss
			try
			{
				Class<? extends Entity> bossClass = boss.getBossType().getBossClass();
				Constructor<? extends Entity> bossc = bossClass.getConstructor(World.class, Boss.class);
				
				for(Location loc : boss.getSpawns())
				{
					/*
					 * for(org.bukkit.entity.Entity entity :
					 * loc.getChunk().getEntities()) { if(entity instanceof
					 * Player) { spawn = true; break; } }
					 */
					
					if(spawn)
					{
						// bossesSpawnChunk.remove(file);
						Entity bossEntity = (Entity) bossc.newInstance(((CraftWorld) loc.getWorld()).getHandle(), boss);
						
						BossSpawnEvent bossSpawnEvent = new BossSpawnEvent(bossEntity.getBukkitEntity(), boss, loc);
						Bukkit.getPluginManager().callEvent(bossSpawnEvent);
						
						if(!bossSpawnEvent.isCancelled())
						{
							BossManager.spawnBoss(bossEntity, loc);
						}
						
						waitBoss.remove(boss.getBossType());
					}
					else
					{
						waitBoss.add(boss.getBossType());
						Bukkit.broadcastMessage(" ");
						Bukkit.broadcastMessage("§6[" + boss.getBossType().getName() + "] §cVai spawnar em alguns minutos em: [§aMundo: " + Mobs.getRealWorldName(loc.getWorld().getName()) + " X: " + loc.getBlockX() + " Y: " + loc.getBlockY() + " Z: " + loc.getBlockZ() + "§c]");
						Bukkit.broadcastMessage(" ");
						return true;
					}
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				return true;
			}
			
			if(spawn)
			{
				// Atualiza e salva a config
				config.set("respawnTime", df.format(new Date().getTime()));
				
				try
				{
					config.save(file);
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				return true;
			}
			else
			{
				return false;
			}
		}
		return true;
	}
}
