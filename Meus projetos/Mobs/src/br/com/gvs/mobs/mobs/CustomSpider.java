package br.com.gvs.mobs.mobs;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R2.DamageSource;
import net.minecraft.server.v1_7_R2.EnchantmentManager;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EntitySpider;
import net.minecraft.server.v1_7_R2.GenericAttributes;
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

import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R2.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTargetEvent;

import br.com.gvs.mobs.util.AttackType;
import br.com.gvs.mobs.util.Mob;

public class CustomSpider extends EntitySpider implements CustomMob {

	private Mob mob;

	public CustomSpider(World world, Mob mob) {
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
		a(1.4F, 0.9F);
		getNavigation().a(true);
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this,
				EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
		if (this.mob.getAttackType() != AttackType.PASSIVE) {
			this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0D, false));
			this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
			if (this.mob.getAttackType() == AttackType.HOSTILE) {
				this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 0, true));
			}
		}
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
	protected Entity findTarget(){
		EntityHuman entityhuman = this.world.findNearbyVulnerablePlayer(this, 16.0D);

		return (entityhuman != null) && (p(entityhuman)) ? entityhuman : null;
	}

	@Override
	public void die(DamageSource damagesource) {
		Entity entity = damagesource.getEntity();
		EntityLiving entityliving = aW();
		if ((this.ba >= 0) && (entityliving != null)) {
			entityliving.b(this, this.ba);
		}
		if (entity != null) {
			entity.a(this);
		}
		this.aT = true;
		aV().g();
		if (!this.world.isStatic) {
			int i = 0;
			if ((entity instanceof EntityHuman)) {
				i = EnchantmentManager
						.getBonusMonsterLootEnchantmentLevel((EntityLiving) entity);
			}
			if ((aF()) && (this.world.getGameRules().getBoolean("doMobLoot"))) {
				dropDeathLoot(this.lastDamageByPlayerTime > 0, i);
				dropEquipment(this.lastDamageByPlayerTime > 0, i);
			} else {
				CraftEventFactory.callEntityDeathEvent(this);
			}
		}
		this.world.broadcastEntityEffect(this, (byte) 3);

		if (this.mob.getLevel() >= 4) {
			for(org.bukkit.entity.Entity e : this.getBukkitEntity().getNearbyEntities(5.5, 5.5, 5.5)){
				if(e instanceof LivingEntity && ((EntityLiving) entity) != null){
					if(this.mob.getLevel() == 4){
							((EntityLiving) entity).addEffect(new MobEffect(MobEffectList.BLINDNESS.id, 3 * 20, 0));
					}else{
						((EntityLiving) entity).addEffect(new MobEffect(MobEffectList.POISON.id, 5 * 20, 0));
					}
				}
			}
		}
	}

	// Quando é atacada tem seu speed alterado
	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {
		if (isInvulnerable()) {
			return false;
		}

		double speed = 0;

		switch (this.mob.getLevel()) {
		case 1:
			speed = 0.21;
			break;
		case 2:
			speed = 0.24;
			break;
		case 3:
			speed = 0.27;
			break;
		default:
			speed = this.mob.getMoveSpeed();
			break;
		}

		if (getAttributeInstance(GenericAttributes.d).getValue() != speed) {
			getAttributeInstance(GenericAttributes.d).setValue(speed);
		}

		if (super.damageEntity(damagesource, f)) {
			Entity entity = damagesource.getEntity();
			if ((this.passenger != entity) && (this.vehicle != entity)) {
				if (entity != this) {
					if ((entity != this.target)) {
						EntityTargetEvent event = CraftEventFactory
								.callEntityTargetEvent(
										this,
										entity,
										EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY);
						if (!event.isCancelled()) {
							if (event.getTarget() == null) {
								this.target = null;
							} else {
								this.target = ((CraftEntity) event.getTarget())
										.getHandle();
							}
						}
					} else {
						this.target = entity;
					}
				}
				return true;
			}
			return true;
		}
		return false;
	}

}
