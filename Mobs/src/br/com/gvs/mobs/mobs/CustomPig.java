package br.com.gvs.mobs.mobs;


import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_7_R2.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import br.com.gvs.mobs.pathfinders.PathfinderGoalMeleeAttackB;
import br.com.gvs.mobs.util.AttackType;
import br.com.gvs.mobs.util.Mob;
import net.minecraft.server.v1_7_R2.DamageSource;
import net.minecraft.server.v1_7_R2.EnchantmentManager;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityLightning;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EntityPig;
import net.minecraft.server.v1_7_R2.GenericAttributes;
import net.minecraft.server.v1_7_R2.ItemStack;
import net.minecraft.server.v1_7_R2.PathfinderGoalBreed;
import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalPanic;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.World;


public class CustomPig extends EntityPig implements CustomMob
{
	
	
	private Mob mob;
	
	public CustomPig(World world, Mob mob)
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
		a(0.9F, 0.9F);
		getNavigation().a(true);
		this.W = (float) 1.0;
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
		this.goalSelector.a(6, new PathfinderGoalBreed(this, 1.0D));
		if(mob.getAttackType() != AttackType.PASSIVE)
		{
			getNavigation().b(true);
			this.goalSelector.a(1, new PathfinderGoalMeleeAttackB(this, EntityHuman.class, 1.0D, false));
			this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
			if(mob.getAttackType() == AttackType.HOSTILE)
			{
				this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 0, true));
			}
		}else{
			this.goalSelector.a(5, new PathfinderGoalPanic(this, 1.25D));
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
	public boolean bE()
	{
		return false;
	}
	
	@Override
	public boolean a(EntityHuman entityhuman)
	{
		return false;
	}
	
	@Override
	public void a(EntityLightning entitylightning)
	{
	}
	
	@Override
	public boolean c(ItemStack itemstack)
	{
		return false;
	}
	
	// faz explosão quando o porco morre
	@Override
	public void die(DamageSource damagesource)
	{
		Entity entity = damagesource.getEntity();
		EntityLiving entityliving = aW();
		if((this.ba >= 0) && (entityliving != null))
		{
			entityliving.b(this, this.ba);
		}
		if(entity != null)
		{
			entity.a(this);
		}
		this.aT = true;
		aV().g();
		if(!this.world.isStatic)
		{
			int i = 0;
			if((entity instanceof EntityHuman))
			{
				i = EnchantmentManager.getBonusMonsterLootEnchantmentLevel((EntityLiving) entity);
			}
			if((aF()) && (this.world.getGameRules().getBoolean("doMobLoot")))
			{
				dropDeathLoot(this.lastDamageByPlayerTime > 0, i);
				dropEquipment(this.lastDamageByPlayerTime > 0, i);
			}
			else
			{
				CraftEventFactory.callEntityDeathEvent(this);
			}
		}
		this.world.broadcastEntityEffect(this, (byte) 3);
		// explosao
		int random = (int) (Math.random() * 99 ) +1;
		if(this.mob.getLevel() == 5 && random <= 35)
		{
			ExplosionPrimeEvent event = new ExplosionPrimeEvent(getBukkitEntity(), (float) 4.5, false);
			this.world.getServer().getPluginManager().callEvent(event);
			if(!event.isCancelled())
			{
				this.world.createExplosion(this, this.locX, this.locY, this.locZ, event.getRadius(), event.getFire(), false);
			}
		}
	}
	
	// Quando está perto de morrer ganha velocidade
	// Obs: quando tem menos ou 25% de sua vida
	@Override
	public void e()
	{
		super.e();
		if(this.mob.getLevel() >= 4)
		{
			boolean life = this.getHealth() <= ((this.getMaxHealth() / 100) * 25);
			
			double speed = 0;
			switch(this.mob.getLevel())
			{
				case 4:
					speed = 0.24;
					break;
				default:
					speed = this.mob.getMoveSpeed();
					break;
			}
			
			if(life){
				if(getAttributeInstance(GenericAttributes.d).getValue() != speed && getAttributeInstance(GenericAttributes.d).getValue() < speed){
					getAttributeInstance(GenericAttributes.d).setValue(speed);
				}
			}else{
				if(getAttributeInstance(GenericAttributes.d).getValue() != this.mob.getMoveSpeed()){
					getAttributeInstance(GenericAttributes.d).setValue(this.mob.getMoveSpeed());
				}
			}
		}
	}
}
