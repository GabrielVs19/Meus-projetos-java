package br.com.gvs.converttocustommob;


import java.util.Map;
import java.util.Random;

import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityInsentient;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.GenericAttributes;

import org.bukkit.command.CommandExecutor;
import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.gvs.converttocustommob.listeners.MonsterListener;
import br.com.gvs.mobs.mobs.CustomHorse;
import br.com.gvs.mobs.util.Mob;
import br.com.gvs.mobs.util.MobType;


public class ConvertToCustomMob extends JavaPlugin implements CommandExecutor
{
	public void onEnable()
	{
		new BukkitRunnable()
		{
			
			
			public void run()
			{
				registerEvents();
			}
		}.runTaskLater(this, 20);
	}
	
	private void registerEvents()
	{
		getServer().getPluginManager().registerEvents(new MonsterListener(), this);
	}
	
	
	public static void convertToCustomMob(Mob mob, Entity entity){
		    if (mob == null) {
		      return;
		    }
		    EntityLiving customMob = (EntityLiving)entity;
		    customMob.getAttributeInstance(GenericAttributes.a).setValue(mob.getHealth());
		    customMob.getAttributeInstance(GenericAttributes.b).setValue(mob.getFollowRange());
		    customMob.getAttributeInstance(GenericAttributes.c).setValue(mob.getKnockbackResistence());
		    customMob.getAttributeInstance(GenericAttributes.d).setValue(mob.getMoveSpeed());
		    customMob.getAttributeInstance(GenericAttributes.e).setValue(mob.getAttackDamage());
		    
		    customMob.setHealth((float)mob.getHealth());
		    for (Map.Entry<Integer, ItemStack> eq : mob.getEquipments().entrySet()) {
		      customMob.setEquipment(((Integer)eq.getKey()).intValue(), CraftItemStack.asNMSCopy((ItemStack)eq.getValue()));
		    }
		    String prefix = mob.getAttackType().getPrefix();
		    String name = mob.getMobType().getName();
		    String suffix = "§7[" + mob.getLevel() + "]";
		    if (mob.getMobType() == MobType.HORSE)
		    {
		      int type = new Random().nextInt(3);
		      ((CustomHorse)customMob).setType(type);
		      if (type == 1) {
		        name = "Burro";
		      }
		    }
		    ((EntityInsentient)customMob).setCustomName(prefix + name + " " + suffix);
		    ((EntityInsentient)customMob).setCustomNameVisible(true);
	}
}
