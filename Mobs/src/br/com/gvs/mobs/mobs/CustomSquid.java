package br.com.gvs.mobs.mobs;


import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EntitySquid;
import net.minecraft.server.v1_7_R2.GenericAttributes;
import net.minecraft.server.v1_7_R2.MobEffect;
import net.minecraft.server.v1_7_R2.MobEffectList;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;

import br.com.gvs.mobs.util.Mob;
import br.com.gvs.mobs.util.NMS;


public class CustomSquid extends EntitySquid implements CustomMob
{
	
	private Mob mob;
	
	public CustomSquid(World world, Mob mob)
	{
		super(world);
		this.mob = mob;
		try
		{
			Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
			bField.setAccessible(true);
			Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
			cField.setAccessible(true);
			bField.set(this.goalSelector, new UnsafeList<Object>());
			bField.set(this.targetSelector, new UnsafeList<Object>());
			cField.set(this.goalSelector, new UnsafeList<Object>());
			cField.set(this.targetSelector, new UnsafeList<Object>());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		a(0.95F, 0.95F);
		bb().b(GenericAttributes.e);
	}
	
	@Override
	public Mob getMob()
	{
		return this.mob;
	}

	@Override
	public void resetAI() {
		try {
			Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
			bField.setAccessible(true);
			Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
			cField.setAccessible(true);
			bField.set(this.goalSelector, new UnsafeList<Object>());
			bField.set(this.targetSelector, new UnsafeList<Object>());
			cField.set(this.goalSelector, new UnsafeList<Object>());
			cField.set(this.targetSelector, new UnsafeList<Object>());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean n(Entity paramEntity)
	{
		if(super.n(paramEntity))
		{
			if((paramEntity instanceof EntityLiving))
			{
				NMS.attack(this, paramEntity);
				int chance = 5 * this.mob.getLevel();
				int random = (int) (Math.random() * 99) + 1;
				if(chance <= random)
				{
					((EntityLiving) paramEntity).addEffect(new MobEffect(MobEffectList.BLINDNESS.id, 5 * 20, 0));
				}
			}
		}
		return true;
	}
}
