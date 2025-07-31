package br.com.gvs.mobs.mobs;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;

import br.com.gvs.mobs.util.AttackType;
import br.com.gvs.mobs.util.Mob;
import br.com.gvs.mobs.util.NMS;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EntitySilverfish;
import net.minecraft.server.v1_7_R2.MobEffect;
import net.minecraft.server.v1_7_R2.MobEffectList;
import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R2.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_7_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.World;

public class CustomSilverfish extends EntitySilverfish implements CustomMob {

	private Mob mob;

	public CustomSilverfish(World world, Mob mob) {
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
		a(0.3F, 0.7F);
		getNavigation().a(true);
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this,
				EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
		if (mob.getAttackType() != AttackType.PASSIVE) {
			this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this,
					EntityHuman.class, 1.0D, false));
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
	public Mob getMob() {
		return this.mob;
	}

	// tem 25% de chance de aplicar veneno no ataque
	@Override
	public boolean n(Entity paramEntity) {
		if (super.n(paramEntity)) {
			if ((paramEntity instanceof EntityLiving)) {
				if (this.mob.getLevel() >= 5) {
					int random = (int) (Math.random() * 99) + 1;
					int percent = this.mob.getLevel() == 4 ? 15 : 25;
					int potionTime = this.mob.getLevel() == 4 ? 3 : 5;
					if (random <= percent) {
						((EntityLiving) paramEntity).addEffect(new MobEffect(
								MobEffectList.POISON.id, potionTime * 20, 0));
					}
				}
				NMS.attack(this, paramEntity);
			}
		}
		return true;
	}

	@Override
	protected void a(Entity entity, float f) {
		if ((this.attackTicks <= 0) && (f < 1.2F)
				&& (entity.boundingBox.e > this.boundingBox.b)
				&& (entity.boundingBox.b < this.boundingBox.e)) {
			if (this.getLastDamager() != null
					|| this.mob.getAttackType() == AttackType.HOSTILE) {
				this.attackTicks = 25;
				this.n(entity);
			}
		}
	}

}
