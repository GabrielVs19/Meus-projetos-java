package br.com.gvs.mobs.bosses;


import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntitySheep;
import net.minecraft.server.v1_7_R2.EntitySmallFireball;
import net.minecraft.server.v1_7_R2.EntityWitherSkull;
import net.minecraft.server.v1_7_R2.GenericAttributes;
import net.minecraft.server.v1_7_R2.MathHelper;
import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import br.com.gvs.mobs.pathfinders.PathfinderGoalMeleeAttackB;
import br.com.gvs.mobs.pathfinders.PathfinderGoalWalkToSpawn;
import br.com.gvs.mobs.util.Boss;


public class CustomOvelhaDoCapiroto extends EntitySheep implements CustomBoss
{
	
	
	private Boss boss;
	private int roflCounter = 0;
	private int roflMaxCounter = 3;
	
	public CustomOvelhaDoCapiroto(World world, Boss boss)
	{
		super(world);
		this.boss = boss;
		a(0.9F, 1.3F);
		getNavigation().b(true);
		this.resetAI();
		this.setAI();
		bb().b(GenericAttributes.e);
	}
	
	@Override
	protected String t()
	{
		return "";
	}
	
	@Override
	public void resetAI() {
		try{
			Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
			bField.setAccessible(true);
			Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
			cField.setAccessible(true);
			bField.set(this.goalSelector, new UnsafeList<Object>());
			bField.set(this.targetSelector, new UnsafeList<Object>());
			cField.set(this.goalSelector, new UnsafeList<Object>());
			cField.set(this.targetSelector, new UnsafeList<Object>());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void setAI() {
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
		this.goalSelector.a(1, new PathfinderGoalMeleeAttackB(this, EntityHuman.class, 1.0D, false));
		this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 0, true));
	}
	
	/*@Override
	public boolean damageEntity(DamageSource paramDamageSource, float paramFloat)
	{
		BossDamageByPlayerEvent event = new BossDamageByPlayerEvent(this.getBukkitEntity(), this.boss);
		CraftEventFactory.callEvent(event);
		if(event.isCancelled()){
			return super.damageEntity(paramDamageSource, paramFloat);
		}
		return false;
	}*/
	
	@Override
	protected String aS()
	{
		rofl();
		
		getBukkitEntity().getLocation().getWorld().playSound(getBukkitEntity().getLocation(), Sound.WOLF_HOWL, 100.0F, -1.0F);
		return "";
	}
	
	@Override
	protected String aT()
	{
		for(org.bukkit.entity.Entity e : this.getBukkitEntity().getNearbyEntities(10, 10, 10))
		{
			if(e instanceof LivingEntity)
			{
				if(e.getType() == EntityType.PLAYER)
				{
					((Player)e).setVelocity(new Vector(0, 2.55, 0));
				}
			}
		}
		getBukkitEntity().getLocation().getWorld().playSound(getBukkitEntity().getLocation(), Sound.EXPLODE, 100.0F, -1.0F);
		return "";
	}
	
	private void rofl()
	{
		if(getGoalTarget() == null)
		{
			return;
		}
		this.roflCounter += 1;
		if(this.roflCounter > this.roflMaxCounter)
		{
			this.roflCounter = 0;
		}
		if(this.roflCounter != 0)
		{
			return;
		}
		float f = MathHelper.c(MathHelper.sqrt(this.locY)) * 0.5F;
		
		double d2 = getGoalTarget().locX - this.locX;
		double d3 = getGoalTarget().boundingBox.b + getGoalTarget().length / 2.0F - (this.locY + this.length / 2.0F);
		double d4 = getGoalTarget().locZ - this.locZ;
		for(int i = 0; i < 5; i++)
		{
			EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.world, this, d2 + this.random.nextGaussian() * f, d3, d4 + this.random.nextGaussian() * f);
			entitywitherskull.locY = (this.locY + this.length / 2.0F + 0.9D);
			this.world.addEntity(entitywitherskull);
			
			EntitySmallFireball localEntitySmallFireball = new EntitySmallFireball(this.world, this, d2 + this.random.nextGaussian() * f, d3, d4 + this.random.nextGaussian() * f);
			localEntitySmallFireball.locY = (this.locY + this.length / 2.0F + 0.9D);
			
			this.world.addEntity(localEntitySmallFireball);
		}
	}
	
	@Override
	public Boss getBoss()
	{
		return this.boss;
	}

	@Override
	public void setWalkToLocation(Location loc) {
		this.goalSelector.a(5, new PathfinderGoalWalkToSpawn(this, loc));
	}
	
}
