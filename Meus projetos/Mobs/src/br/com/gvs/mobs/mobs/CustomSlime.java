package br.com.gvs.mobs.mobs;


import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EntitySlime;
import net.minecraft.server.v1_7_R2.GenericAttributes;
import net.minecraft.server.v1_7_R2.MobEffect;
import net.minecraft.server.v1_7_R2.MobEffectList;
import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R2.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.gvs.mobs.managers.MobManager;
import br.com.gvs.mobs.util.AttackType;
import br.com.gvs.mobs.util.Mob;
import br.com.gvs.mobs.util.MobType;
import br.com.gvs.mobs.util.NMS;


public class CustomSlime extends EntitySlime implements CustomMob
{


	private int jumpDelay;
	private Entity lastTarget;
	private Mob mob;

	public CustomSlime(World world, Mob mob)
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
		this.height = 0.0F;
		this.jumpDelay = (this.random.nextInt(20) + 10);
		setSize(3);
		getNavigation().a(true);
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
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
	protected void bp(){
		w();
		if(this.mob.getAttackType() == AttackType.HOSTILE || this.getLastDamager() != null){
			Entity entityhuman = this.world.findNearbyVulnerablePlayer(this, 16.0D);
			EntityTargetEvent event = null;
			if((entityhuman != null) && (!entityhuman.equals(this.lastTarget))){
				event = CraftEventFactory.callEntityTargetEvent(this, entityhuman, EntityTargetEvent.TargetReason.CLOSEST_PLAYER);
			}else
				if((this.lastTarget != null) && (entityhuman == null)){
					event = CraftEventFactory.callEntityTargetEvent(this, entityhuman, EntityTargetEvent.TargetReason.FORGOT_TARGET);
				}
			if((event != null) && (!event.isCancelled())){
				entityhuman = event.getTarget() == null ? null : ((CraftEntity) event.getTarget()).getHandle();
			}
			this.lastTarget = entityhuman;
			if(entityhuman != null){
				a(entityhuman, 10.0F, 20.0F);
			}
			if((this.onGround) && (this.jumpDelay-- <= 0)){
				this.jumpDelay = bR();
				if(entityhuman != null){
					this.jumpDelay /= 3;
				}
				this.bc = true;
				if(bY()){
					makeSound(bV(), be(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
				}
				this.bd = (1.0F - this.random.nextFloat() * 2.0F);
				this.be = (1 * getSize());
			}else{
				this.bc = false;
				if(this.onGround){
					this.bd = (this.bf = 0.0F);
				}
			}
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
				int random = (int) ((Math.random() * 99) + 1);
				if(this.mob.getLevel() >= 4)
				{
					if(random <= 15 && ((EntityLiving) paramEntity) != null)
					{
						((EntityLiving) paramEntity).addEffect(new MobEffect(MobEffectList.SLOWER_MOVEMENT.id, 5 * 20, 0));
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void die()
	{
		this.dead = true;
		if(this.mob.getLevel() >= 5)
		{
			for(org.bukkit.entity.Entity e : this.getBukkitEntity().getNearbyEntities(5, 5, 5))
			{
				if(e instanceof LivingEntity){
					((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 15 * 20, 1));
				}
			}
		}
		int i = getSize();
	    if ((!this.world.isStatic) && (i > 1) && (getHealth() <= 0.0F))
	    {
	      int j = 2 + this.random.nextInt(3);
	      

	      SlimeSplitEvent event = new SlimeSplitEvent((Slime)getBukkitEntity(), j);
	      this.world.getServer().getPluginManager().callEvent(event);
	      if ((!event.isCancelled()) && (event.getCount() > 0))
	      {
	        j = event.getCount();
	      }
	      else
	      {
	        super.die();
	        return;
	      }
	      for (int k = 0; k < j; k++)
	      {
	        float f = (k % 2 - 0.5F) * i / 4.0F;
	        float f1 = (k / 2 - 0.5F) * i / 4.0F;
	        CustomSlime entityslime = new CustomSlime(this.world, MobManager.getMob(MobType.SLIME, this.random.nextInt(4) + 1));
	        
	        entityslime.setSize(i / 2);
	        entityslime.setPositionRotation(this.locX + f, this.locY + 0.5D, this.locZ + f1, this.random.nextFloat() * 360.0F, 0.0F);
	        MobManager.spawnMob(entityslime, this.getBukkitEntity().getLocation());
	      }
	    }
		super.die();
	}
	
	 protected EntitySlime bQ(){
	    return new EntitySlime(this.world);
	  }
	
	

}
