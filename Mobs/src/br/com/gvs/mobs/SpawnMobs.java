package br.com.gvs.mobs;


import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R2.block.CraftBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.gvs.mobs.config.MobsConfig;
import br.com.gvs.mobs.event.MobSpawnEvent;
import br.com.gvs.mobs.listeners.MonsterListener;
import br.com.gvs.mobs.managers.MobManager;
import br.com.gvs.mobs.util.Mob;
import br.com.gvs.mobs.util.MobType;
import br.com.gvs.mobs.util.MobType.MobB;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.World;


public class SpawnMobs
{
	
	
	public static void spawnTask()
	{
		new BukkitRunnable()
		{
			
			
			public void run()
			{
				spawn();
			}
		}.runTaskTimer(Mobs.getInstance(), 0, 20 * Mobs.getInstance().getConfig().getInt("spawnDelay"));
	}
	
	public static HashMap<String, Location> players = new HashMap<>();
	
	private static void spawn()
	{
		for(Player p : Bukkit.getOnlinePlayers())
		{
			Location locP = p.getLocation();
			
			if(players.containsKey(p.getName()))
			{
				if((locP.getX() == players.get(p.getName()).getX()) && (locP.getY() == players.get(p.getName()).getY()) && (locP.getZ() == players.get(p.getName()).getZ()))
				{
					continue;
				}
				else
				{
					players.remove(p.getName());
					players.put(p.getName(), locP);
				}
			}
			else
			{
				players.put(p.getName(), locP);
			}
			
			Location loc = getSpawnLocation(p.getLocation());
			
			if(loc == null)
			{
				continue;
			}
			
			int maxMobs = MobsConfig.getMaxPerChunk();
			boolean low_priority = Mobs.lowPriorityWorlds.contains(loc.getWorld().getName());
			
			if(low_priority)
			{
				maxMobs = Mobs.lowPriorityMaxMobs;
			}
			
			int ent = loc.getChunk().getEntities().length;
			
			if(ent > maxMobs + 1)
			{
				return;
			}
			
			World w = ((CraftWorld) loc.getWorld()).getHandle();
			List<Mob> mobs = new ArrayList<Mob>();
			
			if(!low_priority)
			{
				if(loc.getBlock().getType() == Material.STATIONARY_WATER)
				{
					mobs.addAll(MobManager.getMobsByRadiusSquid(loc));
				}
				else
				{
					int percentMobs = Mobs.percentMobs;
					int mobTypeSpawn = MonsterListener.day(loc.getWorld()) ? percentMobs : 100 - percentMobs;
					int random = (int) (Math.random() * 99) + 1;
					if(random <= mobTypeSpawn)
					{
						mobs.addAll(MobManager.getMobsByRadius(loc, MobB.ANIMAL));
					}
					else
					{
						mobs.addAll(MobManager.getMobsByRadius(loc, MobB.MONSTER));
					}
				}
			}
			else
			{
				MobType mt = MobType.getMobType(Mobs.lowPriorityMobs.get((int) (Math.random() * Mobs.lowPriorityMobs.size())));
				mobs.addAll(MobManager.getMobsByType(mt));
			}
			
			if(mobs.size() == 0)
			{
				continue;
			}
			
			if(low_priority && ((MonsterListener.day(loc.getWorld()) || ((CraftBlock) loc.getBlock()).getLightLevel() > 7)))
			{
				return;
			}
			
			Mob mob = mobs.get((int) (Math.random() * mobs.size()));
			
			try
			{
				Class<? extends Entity> mobClass = mob.getMobType().getMobClass();
				Constructor<? extends Entity> mobc = mobClass.getConstructor(World.class, Mob.class);
				
				Entity mEntity = (Entity) mobc.newInstance(w, mob);
				
				MobSpawnEvent mobSpawnEvent = new MobSpawnEvent(mEntity, mob, loc);
				Bukkit.getPluginManager().callEvent(mobSpawnEvent);
				
				if(!mobSpawnEvent.isCancelled())
				{
					MobManager.spawnMob(mEntity, loc);
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	private static Location getSpawnLocation(Location location)
	{
		int range = 5;
		
		Location spawnLocation = null;
		
		while((spawnLocation == null) && (range < 15))
		{
			int x = (int) (location.getBlockX() + range + (Math.random() * 5));
			int z = (int) (location.getBlockZ() + range + (Math.random() * 5));
			
			Block block = location.getWorld().getBlockAt(x, location.getBlockY(), z);
			
			if((block.getType() == Material.AIR) && (block.getRelative(BlockFace.UP).getType() == Material.AIR) && (block.getRelative(BlockFace.DOWN).getType() != Material.AIR))
			{
				spawnLocation = block.getLocation();
				spawnLocation.add(0.5D, 0.0D, 0.5D);
			}
			else
			{
				if((block.getType() == Material.STATIONARY_WATER) && (block.getRelative(BlockFace.UP).getType() == Material.AIR || block.getRelative(BlockFace.UP).getType() == Material.WATER || block.getRelative(BlockFace.UP).getType() == Material.STATIONARY_WATER) && (block.getRelative(BlockFace.DOWN).getType() != Material.AIR))
				{
					spawnLocation = block.getLocation();
				}
			}
			
			range++;
		}
		
		return spawnLocation;
	}
}
