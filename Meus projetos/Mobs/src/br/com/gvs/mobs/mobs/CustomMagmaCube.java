package br.com.gvs.mobs.mobs;


import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EntityMagmaCube;
import net.minecraft.server.v1_7_R2.EntitySlime;
import net.minecraft.server.v1_7_R2.EnumDifficulty;
import net.minecraft.server.v1_7_R2.GenericAttributes;
import net.minecraft.server.v1_7_R2.Item;
import net.minecraft.server.v1_7_R2.Items;
import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R2.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.SlimeSplitEvent;

import br.com.gvs.mobs.managers.MobManager;
import br.com.gvs.mobs.util.AttackType;
import br.com.gvs.mobs.util.Mob;
import br.com.gvs.mobs.util.MobType;
import br.com.gvs.mobs.util.NMS;


public class CustomMagmaCube extends EntityMagmaCube implements CustomMob
{


	private Mob mob;
	private int jumpDelay;
	private Entity lastTarget;

	public CustomMagmaCube(World world, Mob mob)
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
		this.fireProof = true;
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
	public boolean canSpawn()
	{
		return (this.world.difficulty != EnumDifficulty.PEACEFUL) && (this.world.b(this.boundingBox)) && (this.world.getCubes(this, this.boundingBox).isEmpty()) && (!this.world.containsLiquid(this.boundingBox));
	}

	@Override
	public int aU()
	{
		return getSize() * 3;
	}

	@Override
	public float d(float f)
	{
		return 1.0F;
	}

	@Override
	protected String bP()
	{
		return "flame";
	}

	@Override
	protected EntitySlime bQ()
	{
		return new EntitySlime(this.world);
	}

	@Override
	protected Item getLoot()
	{
		return Items.MAGMA_CREAM;
	}

	@Override
	public boolean isBurning()
	{
		return false;
	}

	@Override
	protected int bR()
	{
		return super.bR() * 4;
	}

	@Override
	protected void bS()
	{
		this.h *= 0.9F;
	}

	@Override
	protected void bi()
	{
		this.motY = (0.42F + getSize() * 0.1F);
		this.al = true;
	}

	@Override
	protected void b(float f)
	{
	}

	@Override
	protected boolean bT()
	{
		return true;
	}

	@Override
	protected int bU()
	{
		return super.bU() + 2;
	}

	@Override
	protected String bV()
	{
		return getSize() > 1 ? "mob.magmacube.big" : "mob.magmacube.small";
	}

	@Override
	public boolean O()
	{
		return false;
	}

	@Override
	protected boolean bW()
	{
		return true;
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
					if(random <= 30)
					{
						paramEntity.fireTicks = 80;
					}
				}
			}
			return true;
		}
		return false;
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
	public void die()
	{
		this.dead = true;
		if(this.mob.getLevel() >= 5)
		{
			this.world.createExplosion(this, this.locX, this.locY, this.locZ, (float) 2.8, false, false);
			
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
	        
	        CustomMagmaCube entityslime =  new CustomMagmaCube(this.world, MobManager.getMob(MobType.MAGMA_CUBE, this.random.nextInt(5) + 1));
	        
	        entityslime.setSize(i / 2);
	        entityslime.setPositionRotation(this.locX + f, this.locY + 0.5D, this.locZ + f1, this.random.nextFloat() * 360.0F, 0.0F);
	        MobManager.spawnMob(entityslime, this.getBukkitEntity().getLocation());
	      }
	      
	      super.die();
	    }
	}

}
