package br.com.gvs.mobs;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.gvs.mobs.bosses.CustomBoss;
import br.com.gvs.mobs.commands.Command_SpawnCustomBoss;
import br.com.gvs.mobs.commands.Command_SpawnCustomMob;
import br.com.gvs.mobs.config.BossConfig;
import br.com.gvs.mobs.config.MobsConfig;
import br.com.gvs.mobs.listeners.MonsterListener;
import br.com.gvs.mobs.mobs.CustomMob;
import br.com.gvs.mobs.util.Boss;
import br.com.gvs.mobs.util.BossBarUtil;
import br.com.gvs.mobs.util.BossType;
import br.com.gvs.mobs.util.Mob;
import br.com.gvs.mobs.util.MobType;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityTypes;


public class Mobs extends JavaPlugin implements CommandExecutor
{
	
	
	private static Mobs instance;
	private static Economy economy = null;
	public static ArrayList<String> lowPriorityWorlds = new ArrayList<>();
	public static ArrayList<String> lowPriorityMobs = new ArrayList<>();
	public static int lowPriorityMaxMobs;
	public static int percentMobs;
	public static boolean mobAI;
	public static boolean bossAI;
	public static boolean mobSpawnerAI;
	
	public void onEnable()
	{
		instance = this;
		registerAll();
		
		this.loadConfig();
		mobAI = getConfig().getBoolean("mobAI");
		bossAI = getConfig().getBoolean("bossAI");
		mobSpawnerAI = getConfig().getBoolean("mobSpawnerAI");
		lowPriorityMaxMobs = getConfig().getInt("low-priority-max-mobs");
		lowPriorityWorlds.addAll(getConfig().getStringList("low-priority-worlds"));
		lowPriorityMobs.addAll(getConfig().getStringList("low-priority-mobs"));
		percentMobs = getConfig().getInt("percentMobs");
		
		MobsConfig.loadConfig();
		BossConfig.loadConfig();
		setupEconomy();
		checkBosses(getConfig().getInt("checkBossDelay"));
		
		new BukkitRunnable()
		{
			
			
			public void run()
			{
				registerEvents();
			}
		}.runTaskLater(this, 20);
		
		if(getConfig().getBoolean("artificialSpawn"))
		{
			getLogger().log(Level.INFO, "Artificial Spawn ATIVADO");
			SpawnMobs.spawnTask();
		}
		else
		{
			getLogger().log(Level.INFO, "Artificial Spawn DESATIVADO");
		}
	}
	
	public void onDisable()
	{
		for(World w : Bukkit.getWorlds())
		{
			for(org.bukkit.entity.Entity e : w.getEntities())
			{
				if(e.getType() != EntityType.PLAYER)
				{
					if(getMob(e) != null)
					{
						e.remove();
						continue;
					}
					
					if(getBoss(e) != null)
					{
						e.remove();
						continue;
					}
					
				}
			}
		}
	}
	
	public static String getRealWorldName(String w)
	{
		switch(w)
		{
			case "blocos":
				return "BLOCOS";
			case "rpg":
				return "RPG";
			case "mundovip":
				return "WARP VIP";
			case "world_the_end":
				return "END";
			case "world_nether":
				return "NETHER";
			case "games":
				return "WARP GAMES";
			default:
				return w;
		}
	}
	
	private void loadConfig()
	{
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
	}
	
	public static void checkBosses(int delay)
	{
		new BukkitRunnable()
		{
			
			
			@Override
			public void run()
			{
				BossConfig.checkRespawnTime();
			}
			
		}.runTaskTimer(instance, 40, 20 * delay);
		
		BossBarUtil.bossBarTask();
	}
	
	public static Plugin getInstance()
	{
		return instance;
	}
	
	public static Economy getEconomy()
	{
		return economy;
	}
	
	public boolean onCommand(final CommandSender sender, Command cmd, final String commandLabel, final String[] args)
	{
		if(commandLabel.equalsIgnoreCase("spawncustommob"))
		{
			Command_SpawnCustomMob.processCommand(sender, commandLabel, args);
			return true;
		}
		
		if(commandLabel.equalsIgnoreCase("spawncustomboss"))
		{
			Command_SpawnCustomBoss.processCommand(sender, commandLabel, args);
		}
		
		return false;
	}
	
	private void registerAll()
	{
		for(MobType mt : MobType.values())
		{
			registerEntity(mt.getName(), mt.getMinecraftID(), mt.getMobClass());
		}
		
		for(BossType bt : BossType.values())
		{
			registerEntity(bt.getName(), bt.getMinecraftID(), bt.getBossClass());
		}
	}
	
	public static Mob getMob(org.bukkit.entity.Entity entity)
	{
		Entity e = ((CraftEntity) entity).getHandle();
		return (e instanceof CustomMob) ? ((CustomMob) e).getMob() : null;
	}
	
	public static Boss getBoss(org.bukkit.entity.Entity entity)
	{
		Entity e = ((CraftEntity) entity).getHandle();
		return (e instanceof CustomBoss) ? ((CustomBoss) e).getBoss() : null;
	}
	
	@SuppressWarnings("unchecked")
	public static void registerEntity(String name, int id, Class<? extends Entity> customClass)
	{
		try
		{
			List<Map<?, ?>> dataMaps = new ArrayList<>();
			for(Field f : EntityTypes.class.getDeclaredFields())
			{
				if(f.getType().getSimpleName().equals(Map.class.getSimpleName()))
				{
					f.setAccessible(true);
					dataMaps.add((Map<?, ?>) f.get(null));
				}
			}
			((Map<Class<? extends Entity>, String>) dataMaps.get(1)).put(customClass, name);
			((Map<Class<? extends Entity>, Integer>) dataMaps.get(3)).put(customClass, Integer.valueOf(id));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void registerEvents()
	{
		getServer().getPluginManager().registerEvents(new MonsterListener(), this);
		getServer().getPluginManager().registerEvents(new BossBarUtil(), this);
	}
	
	private boolean setupEconomy()
	{
		if(getServer().getPluginManager().getPlugin("Vault") == null)
		{
			return false;
		}
		
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		
		if(rsp == null)
		{
			return false;
		}
		
		economy = (Economy) rsp.getProvider();
		return economy != null;
	}
}
