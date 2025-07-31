package br.com.gvs.mobs.managers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import br.com.gvs.mobs.Mobs;
import br.com.gvs.mobs.mobs.CustomHorse;
import br.com.gvs.mobs.util.Mob;
import br.com.gvs.mobs.util.MobType;
import br.com.gvs.mobs.util.MobType.MobB;
import br.com.gvs.mobs.util.SpawnRadius;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityInsentient;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.GenericAttributes;


public class MobManager
{
	
	
	private static ArrayList<MobManager> mobsManager = new ArrayList<>();
	private MobType mobType;
	private Map<String, SpawnRadius> spawnRadius = new HashMap<String, SpawnRadius>();
	private ArrayList<Mob> mobs;
	
	public MobManager(MobType mobType, ArrayList<Mob> mobs)
	{
		this.mobType = mobType;
		this.mobs = mobs;
		
		for(Mob mob : mobs)
		{
			for(Entry<String, SpawnRadius> sr : mob.getSpawnRadius().entrySet())
			{
				if(!spawnRadius.containsKey(sr.getKey()))
				{
					spawnRadius.put(sr.getKey(), new SpawnRadius(sr.getValue().getMin(), sr.getValue().getMax()));
					continue;
				}
				
				SpawnRadius sr2 = spawnRadius.get(sr.getKey());
				
				if(sr.getValue().getMin() < sr2.getMin())
				{
					sr2.setMin(sr.getValue().getMin());
				}
				
				if(sr.getValue().getMax() > sr2.getMax())
				{
					sr2.setMax(sr.getValue().getMax());
				}
				
				spawnRadius.put(sr.getKey(), sr2);
			}
		}
	}
	
	public static Mob getMobByTypeLevel(MobType mobType, int level)
	{
		for(Mob mob : getMobsByType(mobType))
		{
			if(mob.getLevel() == level)
			{
				return mob;
			}
		}
		return null;
	}
	
	public static void spawnMob(Entity entity, Location loc)
	{
		Mob mob = Mobs.getMob(entity.getBukkitEntity());
		
		if(mob == null)
		{
			return;
		}
		
		EntityLiving customMob = (EntityLiving) entity;
		customMob.getAttributeInstance(GenericAttributes.a).setValue(mob.getHealth());
		customMob.getAttributeInstance(GenericAttributes.b).setValue(mob.getFollowRange());
		customMob.getAttributeInstance(GenericAttributes.c).setValue(mob.getKnockbackResistence());
		customMob.getAttributeInstance(GenericAttributes.d).setValue(mob.getMoveSpeed());
		customMob.getAttributeInstance(GenericAttributes.e).setValue(mob.getAttackDamage());
		
		customMob.setHealth((float) mob.getHealth());
		
		for(Entry<Integer, ItemStack> eq : mob.getEquipments().entrySet())
		{
			customMob.setEquipment(eq.getKey(), CraftItemStack.asNMSCopy(eq.getValue()));
		}
		
		String prefix = mob.getAttackType().getPrefix();
		String name = mob.getMobType().getName();
		String suffix = "§7[" + mob.getLevel() + "]";
		
		
		
		if(mob.getMobType() == MobType.HORSE){
			int type = new Random().nextInt(3);
			((CustomHorse)customMob).setType(type);
			if(type == 1){
				name = "Burro";
			}
		}
		
		((EntityInsentient) customMob).setCustomName(prefix + name + " " + suffix);
		((EntityInsentient) customMob).setCustomNameVisible(true);
		
		customMob.setPosition(loc.getX(), loc.getY(), loc.getZ());
		((CraftWorld) loc.getWorld()).getHandle().addEntity(customMob);
	}
	
	public static ArrayList<MobManager> getMobsManager()
	{
		return mobsManager;
	}
	
	public static void addMob(MobManager mobManager)
	{
		mobsManager.add(mobManager);
	}
	
	public static Mob getMob(MobType mobType, int level)
	{
		for(Mob mob : getMobsByType(mobType))
		{
			if(mob.getLevel() == level)
			{
				return mob;
			}
		}
		
		return null;
	}
	
	public static ArrayList<Mob> getMobsByType(MobType mobType)
	{
		for(MobManager mobManager : mobsManager)
		{
			if(mobManager.getMobType() == mobType)
			{
				return mobManager.getMobs();
			}
		}
		return new ArrayList<Mob>();
	}
	
	public static List<Mob> getMobsByRadius(Location loc, MobB mobB)
	{
		List<Mob> m = new ArrayList<Mob>();
		
		for(MobManager mobManager : mobsManager)
		{
			if(mobManager.mobType == MobType.SQUID){
				continue;
			}
			
			if(mobManager.mobType.getMobTypeB() != mobB){
				continue;
			}
			
			double x = Math.max(loc.getWorld().getSpawnLocation().getX(), loc.getX()) - Math.min(loc.getWorld().getSpawnLocation().getX(), loc.getX());
			double z = Math.max(loc.getWorld().getSpawnLocation().getZ(), loc.getZ()) - Math.min(loc.getWorld().getSpawnLocation().getZ(), loc.getZ());
			double radius = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
			
			SpawnRadius spawnRadius = mobManager.getSpawnRadius(loc.getWorld().getName());
			
			if(spawnRadius == null || radius < spawnRadius.getMin() || radius > spawnRadius.getMax())
			{
				continue;
			}
			
			
			for(Mob mb : mobManager.getMobs())
			{
				SpawnRadius spawnRadius2 = mb.getSpawnRadius(loc.getWorld().getName());
				
				if(spawnRadius2 == null || radius < spawnRadius2.getMin() || radius > spawnRadius2.getMax())
				{
					continue;
				}
				m.add(mb);
			}
		}
		return m;
	}
	
	public static List<Mob> getMobsByRadius(Location loc)
	{
		List<Mob> m = new ArrayList<Mob>();
		
		for(MobManager mobManager : mobsManager)
		{
			if(mobManager.mobType == MobType.SQUID){
				continue;
			}
			
			double x = Math.max(loc.getWorld().getSpawnLocation().getX(), loc.getX()) - Math.min(loc.getWorld().getSpawnLocation().getX(), loc.getX());
			double z = Math.max(loc.getWorld().getSpawnLocation().getZ(), loc.getZ()) - Math.min(loc.getWorld().getSpawnLocation().getZ(), loc.getZ());
			double radius = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
			
			SpawnRadius spawnRadius = mobManager.getSpawnRadius(loc.getWorld().getName());
			
			if(spawnRadius == null || radius < spawnRadius.getMin() || radius > spawnRadius.getMax())
			{
				continue;
			}
			
			for(Mob mb : mobManager.getMobs())
			{
				SpawnRadius spawnRadius2 = mb.getSpawnRadius(loc.getWorld().getName());
				
				if(spawnRadius2 == null || radius < spawnRadius2.getMin() || radius > spawnRadius2.getMax())
				{
					continue;
				}
				m.add(mb);
			}
		}
		return m;
	}
	
	public static List<Mob> getMobsByRadiusSquid(Location loc)
	{
		List<Mob> m = new ArrayList<Mob>();
		
		for(MobManager mobManager : mobsManager)
		{
			if(mobManager.mobType != MobType.SQUID){
				continue;
			}
			
			double x = Math.max(loc.getWorld().getSpawnLocation().getX(), loc.getX()) - Math.min(loc.getWorld().getSpawnLocation().getX(), loc.getX());
			double z = Math.max(loc.getWorld().getSpawnLocation().getZ(), loc.getZ()) - Math.min(loc.getWorld().getSpawnLocation().getZ(), loc.getZ());
			double radius = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
			
			SpawnRadius spawnRadius = mobManager.getSpawnRadius(loc.getWorld().getName());
			
			if(spawnRadius == null || radius < spawnRadius.getMin() || radius > spawnRadius.getMax())
			{
				continue;
			}
			
			for(Mob mb : mobManager.getMobs())
			{
				SpawnRadius spawnRadius2 = mb.getSpawnRadius(loc.getWorld().getName());
				
				if(spawnRadius2 == null || radius < spawnRadius2.getMin() || radius > spawnRadius2.getMax())
				{
					continue;
				}
				m.add(mb);
			}
			
			break;
			
		}
		return m;
	}
	
	public MobType getMobType()
	{
		return mobType;
	}
	
	public ArrayList<Mob> getMobs()
	{
		return mobs;
	}
	
	public Map<String, SpawnRadius> getSpawnRadius()
	{
		return spawnRadius;
	}
	
	public SpawnRadius getSpawnRadius(String worldName)
	{
		if(spawnRadius.containsKey(worldName))
		{
			return spawnRadius.get(worldName);
		}
		return null;
	}
	
}
