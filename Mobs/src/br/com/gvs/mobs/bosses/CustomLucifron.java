package br.com.gvs.mobs.bosses;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EntityPlayer;
import net.minecraft.server.v1_7_R2.EntityWither;
import net.minecraft.server.v1_7_R2.EntityWitherSkull;
import net.minecraft.server.v1_7_R2.IRangedEntity;
import net.minecraft.server.v1_7_R2.MathHelper;
import net.minecraft.server.v1_7_R2.PathfinderGoalArrowAttack;
import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;

import br.com.gvs.mobs.pathfinders.PathfinderGoalTpToSpawn;
import br.com.gvs.mobs.util.Boss;

public class CustomLucifron extends EntityWither implements CustomBoss, IRangedEntity{

	private Boss boss;
	public CustomLucifron(World world, Boss boss) {
		super(world);
		this.boss = boss;
		 setHealth(getMaxHealth());
		    a(0.9F, 4.0F);
		    this.fireProof = true;
		    getNavigation().e(true);
		    this.b = 50;
		    this.resetAI();
		    this.setAI();
	}

	@Override
	public Boss getBoss() {
		return this.boss;
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
	public void setAI() {
		   this.goalSelector.a(0, new PathfinderGoalFloat(this));
		    this.goalSelector.a(2, new PathfinderGoalArrowAttack(this, 1.0D, 40, 20.0F));
		    this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 1.0D));
		    this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		    this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
		    this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false));
		    this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 0, true));
	}
	
	
	@Override
	public void setWalkToLocation(Location loc) {
		this.goalSelector.a(9, new PathfinderGoalTpToSpawn(this, loc, 100));	
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

	@SuppressWarnings("unused")
	private void a(int i, double d0, double d1, double d2, boolean flag){
		this.world.a((EntityHuman)null, 1014, (int)this.locX, (int)this.locY, (int)this.locZ, 0);
		double d3 = u(i);
		double d4 = v(i);
		double d5 = w(i);
		double d6 = d0 - d3;
		double d7 = d1 - d4;
		double d8 = d2 - d5;
		EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.world, this, d6, d7, d8);
		if (flag) {
			entitywitherskull.a(true);
		}
		entitywitherskull.locY = d4;
		entitywitherskull.locX = d3;
		entitywitherskull.locZ = d5;
		this.world.addEntity(entitywitherskull);

		int random = (int) (Math.random() * 99) +1;
		if(random <= 50){
			shootSkulls(i);
		}
	}

	public void shootSkulls(int i){
		for(org.bukkit.entity.Entity e : this.getBukkitEntity().getNearbyEntities(16, 16, 16)){
			Entity entity = ((CraftEntity)e).getHandle();
			if(entity instanceof EntityLiving){
				if(entity instanceof EntityPlayer){
					double d3 = u(i);
					double d4 = v(i);
					double d5 = w(i);
					double d6 = entity.locX - d3;
					double d7 = entity.locY + entity.getHeadHeight() * 0.5D - d4;
					double d8 = entity.locZ - d5;
					EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.world, this, d6, d7, d8);
					if((i == 0) && (this.random.nextFloat() < 0.001F)) {
						entitywitherskull.a(true);
					}
					entitywitherskull.locY = d4;
					entitywitherskull.locX = d3;
					entitywitherskull.locZ = d5;
					this.world.addEntity(entitywitherskull);
				}
			}
		}
	}

	private double u(int i){
		if (i <= 0) {
			return this.locX;
		}
		float f = (this.aM + 180 * (i - 1)) / 180.0F * 3.141593F;
		float f1 = MathHelper.cos(f);

		return this.locX + f1 * 1.3D;
	}

	private double v(int i){
		return i <= 0 ? this.locY + 3.0D : this.locY + 2.2D;
	}

	private double w(int i){
		if (i <= 0) {
			return this.locZ;
		}
		float f = (this.aM + 180 * (i - 1)) / 180.0F * 3.141593F;
		float f1 = MathHelper.sin(f);

		return this.locZ + f1 * 1.3D;
	}


}
