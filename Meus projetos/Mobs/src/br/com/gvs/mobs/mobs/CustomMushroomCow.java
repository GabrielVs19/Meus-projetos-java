package br.com.gvs.mobs.mobs;


import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R2.AttributeInstance;
import net.minecraft.server.v1_7_R2.DamageSource;
import net.minecraft.server.v1_7_R2.EntityAgeable;
import net.minecraft.server.v1_7_R2.EntityCow;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EntityMushroomCow;
import net.minecraft.server.v1_7_R2.GenericAttributes;
import net.minecraft.server.v1_7_R2.Items;
import net.minecraft.server.v1_7_R2.PathfinderGoalBreed;
import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.PathfinderGoalTempt;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;

import br.com.gvs.mobs.pathfinders.PathfinderGoalMeleeAttackB;
import br.com.gvs.mobs.util.AttackType;
import br.com.gvs.mobs.util.Mob;


public class CustomMushroomCow extends EntityMushroomCow implements CustomMob
{
	
	
	private Mob mob;
	
	public CustomMushroomCow(World world, Mob mob)
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
		a(0.9F, 1.3F);
		getNavigation().a(true);
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
		this.goalSelector.a(6, new PathfinderGoalBreed(this, 1.0D));
		this.goalSelector.a(7, new PathfinderGoalTempt(this, 1.25D, Items.WHEAT, false));
		if(mob.getAttackType() != AttackType.PASSIVE)
		{
			this.goalSelector.a(1, new PathfinderGoalMeleeAttackB(this, EntityHuman.class, 1.0D, false));
			this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
			if(mob.getAttackType() == AttackType.HOSTILE)
			{
				this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 0, true));
			}
		}
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
	public boolean damageEntity(DamageSource paramDamageSource, float paramFloat)
	{
		if(isInvulnerable())
		{
			return false;
		}
		
		this.bo = 60;
		if(!bj())
		{
			AttributeInstance localAttributeInstance = getAttributeInstance(GenericAttributes.d);
			if(localAttributeInstance.a(h) == null)
			{
				localAttributeInstance.a(i);
			}
		}
		this.target = null;
		int level = this.mob.getLevel();
		
		if(level >= 3)
		{
			int random = (int) ((Math.random() * 99) + 1);
			if(random <= 50 && paramDamageSource.getEntity() instanceof EntityLiving)
			{
				int percent = level == 3 ? 10 : level == 4 ? 20 : 30;
				float damage = (paramFloat / 100) * percent;
				
					paramDamageSource.getEntity().damageEntity(DamageSource.mobAttack(this), damage);	
				
			}
		}
		return super.damageEntity(paramDamageSource, paramFloat);
	}
	
	public EntityMushroomCow c(EntityAgeable entityageable)
	  {
	    return new EntityMushroomCow(this.world);
	  }
	  
	  public EntityCow b(EntityAgeable entityageable)
	  {
	    return c(entityageable);
	  }
	  
	  public EntityAgeable createChild(EntityAgeable entityageable)
	  {
	    return c(entityageable);
	  }
	
}
