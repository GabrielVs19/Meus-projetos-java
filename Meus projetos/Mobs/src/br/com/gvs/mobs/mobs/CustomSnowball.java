package br.com.gvs.mobs.mobs;

import net.minecraft.server.v1_7_R2.DamageSource;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EntitySnowball;
import net.minecraft.server.v1_7_R2.MobEffect;
import net.minecraft.server.v1_7_R2.MobEffectList;
import net.minecraft.server.v1_7_R2.MovingObjectPosition;
import net.minecraft.server.v1_7_R2.World;

public class CustomSnowball extends EntitySnowball {

	private float damage;
	private int mobLevel = 0;

	public CustomSnowball(World paramWorld, float damage) {
		super(paramWorld);
		this.damage = damage;
	}

	public CustomSnowball(World paramWorld, EntityLiving paramEntityLiving,
			float damage) {
		super(paramWorld, paramEntityLiving);
		this.damage = damage;
	}

	public CustomSnowball(World paramWorld, EntityLiving paramEntityLiving,
			float damage, int mobLevel) {
		super(paramWorld, paramEntityLiving);
		this.damage = damage;
		this.mobLevel = mobLevel;
	}

	public CustomSnowball(World paramWorld, double paramDouble1,
			double paramDouble2, double paramDouble3, float damage) {
		super(paramWorld, paramDouble1, paramDouble2, paramDouble3);
		this.damage = damage;
	}

	@Override
	protected void a(MovingObjectPosition paramMovingObjectPosition) {
		if (paramMovingObjectPosition.entity != null) {
			if (this.mobLevel >= 4) {
				int random = (int) (Math.random() * 99) + 1;
				if (random <= 30 && this.mobLevel == 4) {
					((EntityLiving) paramMovingObjectPosition.entity)
							.addEffect(new MobEffect(
									MobEffectList.SLOWER_MOVEMENT.id, 4 * 20, 3));
				}
				if (random <= 40 && this.mobLevel == 5) {
					((EntityLiving) paramMovingObjectPosition.entity)
							.addEffect(new MobEffect(
									MobEffectList.SLOWER_MOVEMENT.id, 4 * 20, 3));
				}
			}
			paramMovingObjectPosition.entity.damageEntity(
					DamageSource.projectile(this, getShooter()), this.damage);
		}
		for (int i = 0; i < 8; i++) {
			this.world.addParticle("snowballpoof", this.locX, this.locY,
					this.locZ, 0.0D, 0.0D, 0.0D);
		}
		if (!this.world.isStatic) {
			die();
		}
	}

}
