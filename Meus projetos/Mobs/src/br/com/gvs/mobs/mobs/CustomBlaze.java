package br.com.gvs.mobs.mobs;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityBlaze;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntitySmallFireball;
import net.minecraft.server.v1_7_R2.MathHelper;
import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;

import br.com.gvs.mobs.util.AttackType;
import br.com.gvs.mobs.util.Mob;

public class CustomBlaze extends EntityBlaze implements CustomMob {

	private int br;
	private Mob mob;

	public CustomBlaze(World world, Mob mob) {
		super(world);
		this.mob = mob;
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
		getNavigation().a(true);
		this.fireProof = true;
		this.b = 10;
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this,
				EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
		if (mob.getAttackType() != AttackType.PASSIVE) {
			this.targetSelector
					.a(1, new PathfinderGoalHurtByTarget(this, true));
			if (mob.getAttackType() == AttackType.HOSTILE) {
				this.targetSelector.a(2,
						new PathfinderGoalNearestAttackableTarget(this,
								EntityHuman.class, 0, true));
			}
		}
	}

	@Override
	public Mob getMob() {
		return this.mob;
	}
 
	@Override
	protected void a(Entity entity, float f) {
		if (mob.getAttackType() != AttackType.PASSIVE) {
			if ((this.attackTicks <= 0) && (f < 2.0F)
					&& (entity.boundingBox.e > this.boundingBox.b)
					&& (entity.boundingBox.b < this.boundingBox.e)) {
				this.attackTicks = 20;
				if (mob.getAttackType() == AttackType.HOSTILE
						|| this.getLastDamager() != null) {
					n(entity);
				}
			} else if (f < 30.0F) {
				double d0 = entity.locX - this.locX;
				double d1 = entity.boundingBox.b + entity.length / 2.0F
						- (this.locY + this.length / 2.0F);
				double d2 = entity.locZ - this.locZ;
				if (this.attackTicks == 0) {
					this.br += 1;
					if (this.br == 1) {
						this.attackTicks = 60;
						a(true);
					} else if (this.br <= 4) {
						this.attackTicks = 6;
					} else {
						this.attackTicks = 100;
						this.br = 0;
						a(false);
					}
					boolean life = this.getHealth() <= ((this.getMaxHealth() / 100) * 25);
					if (this.mob.getLevel() >= 4 && life) {
						this.attackTicks = (this.mob.getLevel() == 4) ? 40 : 30;
					}
					if (mob.getAttackType() == AttackType.HOSTILE
							|| this.getLastDamager() != null) {
						if (this.br > 1) {
							float f1 = MathHelper.c(f) * 0.5F;
							this.world.a((EntityHuman) null, 1009,
									(int) this.locX, (int) this.locY,
									(int) this.locZ, 0);
							for (int i = 0; i < 1; i++) {
								EntitySmallFireball entitysmallfireball = new EntitySmallFireball(this.world, this, d0 + this.random.nextGaussian() * f1, d1, d2 + this.random.nextGaussian() * f1);
								entitysmallfireball.locY = (this.locY + this.length / 2.0F + 0.5D);
								this.world.addEntity(entitysmallfireball);
							}
						}
					}
				}
				this.yaw = ((float) (Math.atan2(d2, d0) * 180.0D / 3.141592741012573D) - 90.0F);
				this.bn = true;
			}
		}
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

}
