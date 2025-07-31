package br.com.gvs.converttocustommob.listeners;


import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import br.com.gvs.mobs.Mobs;
import br.com.gvs.mobs.event.MobSpawnEvent;
import br.com.gvs.mobs.managers.MobManager;
import br.com.gvs.mobs.mobs.CustomMob;
import br.com.gvs.mobs.util.Mob;
import br.com.gvs.mobs.util.MobType;
import net.citizensnpcs.api.CitizensAPI;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.World;


public class MonsterListener implements Listener
{
	
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onCreatureSpawn(CreatureSpawnEvent e)
	{
		if(e.getSpawnReason() == SpawnReason.SPAWNER)
		{
			e.setCancelled(false);
			return;
		}
		
		if(e.isCancelled() && e.getSpawnReason() != SpawnReason.SPAWNER_EGG)
		{
			return;
		}
		
		if(e.getSpawnReason() == SpawnReason.CUSTOM)
		{
			return;
		}
		
		if(Mobs.getBoss(e.getEntity()) != null)
		{
			return;
		}
		
		if(Mobs.getMob(e.getEntity()) != null || MobManager.getMobsManager().size() == 0)
		{
			return;
		}
		
		try
		{
			if(CitizensAPI.getNPCRegistry().getNPC(e.getEntity()) != null)
			{
				return;
			}
		}
		catch(Exception | NoClassDefFoundError e2)
		{
			e2.printStackTrace();
		}
		
		e.setCancelled(true);
		
		if(e.getSpawnReason() == SpawnReason.NETHER_PORTAL || e.getSpawnReason() == SpawnReason.SLIME_SPLIT || e.getSpawnReason() == SpawnReason.CHUNK_GEN || e.getSpawnReason() == SpawnReason.BUILD_IRONGOLEM || e.getSpawnReason() == SpawnReason.BUILD_SNOWMAN || e.getSpawnReason() == SpawnReason.BUILD_WITHER)
		{
			return;
		}
		
		Location loc = e.getLocation();
		
		List<Mob> mobs = new ArrayList<Mob>();
		
		MobType mobType = MobType.getMobType(e.getEntityType().name());
		
		if(e.getSpawnReason() == SpawnReason.NATURAL && e.getEntity().getType() != EntityType.SQUID)
		{
			mobs.addAll(MobManager.getMobsByRadius(e.getEntity().getLocation()));
			
			if(mobs.size() == 0)
			{
				MobType mt = MobType.getMobType(Mobs.lowPriorityMobs.get((int) (Math.random() * Mobs.lowPriorityMobs.size())));
				mobs.addAll(MobManager.getMobsByType(mt));
			}
		}
		else
		{
			if(mobType != null)
			{
				mobs.addAll(MobManager.getMobsByType(mobType));
			}
		}
		
		if(mobs.size() == 0)
		{
			return;
		}
		
		Mob mob = mobs.get((int) (Math.random() * mobs.size()));
		
		try
		{
			Class<? extends Entity> mobClass = mob.getMobType().getMobClass();
			Constructor<? extends Entity> mobc = mobClass.getConstructor(World.class, Mob.class);
			
			World w = ((CraftWorld) loc.getWorld()).getHandle();
			Entity mEntity = (Entity) mobc.newInstance(w, mob);
			
			MobSpawnEvent mobSpawnEvent = new MobSpawnEvent(mEntity, mob, loc);
			Bukkit.getPluginManager().callEvent(mobSpawnEvent);
			
			if(!Mobs.mobAI)
			{
				CustomMob entityI = (CustomMob) mEntity;
				entityI.resetAI();
			}
			
			if(e.getSpawnReason() == SpawnReason.SPAWNER && !Mobs.mobSpawnerAI)
			{
				CustomMob entityI = (CustomMob) mEntity;
				entityI.resetAI();
			}
			
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
