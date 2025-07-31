package br.com.gvs.mobs.managers;


import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import br.com.gvs.mobs.Mobs;
import br.com.gvs.mobs.bosses.CustomBoss;
import br.com.gvs.mobs.util.Boss;
import br.com.gvs.mobs.util.BossType;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityInsentient;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.GenericAttributes;


public class BossManager
{


	private static Map<BossType, Boss> bosses = new HashMap<BossType, Boss>();

	public BossManager(BossType bossType, Boss boss)
	{
		bosses.put(bossType, boss);
	}

	public static void spawnBoss(Entity entity, Location loc)
	{
		Boss boss = Mobs.getBoss(entity.getBukkitEntity());

		if(boss == null)
		{
			return;
		}

		EntityLiving customBoss = (EntityLiving) entity;
		customBoss.getAttributeInstance(GenericAttributes.a).setValue(boss.getHealth());
		customBoss.getAttributeInstance(GenericAttributes.b).setValue(boss.getFollowRange());
		customBoss.getAttributeInstance(GenericAttributes.c).setValue(boss.getKnockbackResistence());
		customBoss.getAttributeInstance(GenericAttributes.d).setValue(boss.getMoveSpeed());
		customBoss.getAttributeInstance(GenericAttributes.e).setValue(boss.getAttackDamage());

		customBoss.setHealth((float) boss.getHealth());

		for(Entry<Integer, ItemStack> eq : boss.getEquipments().entrySet())
		{
			customBoss.setEquipment(eq.getKey(), CraftItemStack.asNMSCopy(eq.getValue()));
		}

		String prefix = "§4§l";
		String name = boss.getBossType().getName();

		((EntityInsentient) customBoss).setCustomName(prefix + name);
		((EntityInsentient) customBoss).setCustomNameVisible(true);
		customBoss.setPosition(loc.getX(), loc.getY(), loc.getZ());
		((CustomBoss) customBoss).setWalkToLocation(loc);
		((CraftWorld) loc.getWorld()).getHandle().addEntity(customBoss);
		String message = boss.getDialogs().getSpawnDialog();
		if(!Mobs.bossAI){
			((CustomBoss) customBoss).resetAI();
		}

		if(message != null)
		{
			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage("§6[" + boss.getBossType().getName() + "] §a" + message);
			Bukkit.broadcastMessage("§6[" + boss.getBossType().getName() + "] §aSpawnou em [Mundo: " + Mobs.getRealWorldName(loc.getWorld().getName()) + " X: " + loc.getBlockX() + " Y: " + loc.getBlockY() + " Z: " + loc.getBlockZ() + "§a]");
			Bukkit.broadcastMessage(" ");
		}
	}

	public static Map<BossType, Boss> getBosses()
	{
		return bosses;
	}

	public static void addBoss(Boss boss)
	{
		bosses.put(boss.getBossType(), boss);
	}

	public static Boss getBossByType(BossType bt)
	{
		if(bosses.containsKey(bt))
		{
			return bosses.get(bt);
		}
		return null;
	}
}
