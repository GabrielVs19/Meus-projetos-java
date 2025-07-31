package br.com.gvs.mobs.mobs;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityCow;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.GenericAttributes;
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

import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.gvs.mobs.pathfinders.PathfinderGoalMeleeAttackB;
import br.com.gvs.mobs.util.AttackType;
import br.com.gvs.mobs.util.Mob;
import br.com.gvs.mobs.util.NMS;

public class CustomCow extends EntityCow implements CustomMob {

	private Mob mob;

	public CustomCow(World world, Mob mob) {
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
		a(1.4F, 1.6F);
		this.W = (float) 1.0;
		this.fireProof = false;
		getNavigation().a(true);
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this,
				EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
		this.goalSelector.a(6, new PathfinderGoalBreed(this, 1.0D));
		if (mob.getAttackType() != AttackType.PASSIVE) {
			getNavigation().b(true);
			this.goalSelector.a(1, new PathfinderGoalMeleeAttackB(this,
					EntityHuman.class, 1.0D, false));
			this.targetSelector
					.a(1, new PathfinderGoalHurtByTarget(this, true));
			if (mob.getAttackType() == AttackType.HOSTILE) {
				this.targetSelector.a(2,
						new PathfinderGoalNearestAttackableTarget(this,
								EntityHuman.class, 0, true));
			}
		}else{
			this.goalSelector.a(4, new PathfinderGoalPanic(this, 1.25D));
		}
		bb().b(GenericAttributes.e);
	}

	@Override
	public Mob getMob() {
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
	public boolean n(Entity paramEntity) {
		if (super.n(paramEntity)) {
			if ((paramEntity instanceof EntityLiving)) {
				NMS.attack(this, paramEntity);
				if (this.mob.getLevel() >= 5) {
					int random = (int) (Math.random() * 99) + 1;
					if (random <= 5) {
						if (paramEntity.getBukkitEntity().getType() == EntityType.PLAYER) {
							((LivingEntity)paramEntity.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 20 * 10, 0));
						}
					}
				}
			}
		}
		return true;
	}
	
	// Quando está perto de morrer ganha velocidade
	// Obs: quando tem menos ou 25% de sua vida
	@Override
	public void e() {
		super.e();
		if (this.mob.getLevel() >= 4) {
			boolean life = this.getHealth() <= ((this.getMaxHealth() / 100) * 25);

			double speed = 0;
			switch (this.mob.getLevel()) {
			case 4:
				speed = 0.33;
				break;
			case 5:
				speed = 0.37;
				break;
			default:
				speed = this.mob.getMoveSpeed();
				break;
			}

			if (life) {
				if (getAttributeInstance(GenericAttributes.d).getValue() != speed
						&& getAttributeInstance(GenericAttributes.d).getValue() < speed) {
					getAttributeInstance(GenericAttributes.d).setValue(speed);
				}
			} else {
				if (getAttributeInstance(GenericAttributes.d).getValue() != this.mob
						.getMoveSpeed()) {
					getAttributeInstance(GenericAttributes.d).setValue(
							this.mob.getMoveSpeed());
				}
			}
		}
	}
}
