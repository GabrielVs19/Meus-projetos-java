package br.com.gvs.mobs.mobs;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R2.DamageSource;
import net.minecraft.server.v1_7_R2.EnchantmentManager;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityCaveSpider;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityLiving;
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

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R2.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.gvs.mobs.Mobs;
import br.com.gvs.mobs.util.AttackType;
import br.com.gvs.mobs.util.Mob;
import br.com.gvs.mobs.util.NMS;

public class CustomCaveSpider extends EntityCaveSpider implements CustomMob {

	private Mob mob;

	public CustomCaveSpider(World arg0, Mob mob) {
		super(arg0);
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
		a(0.7F, 0.5F);
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

	// Aplica veneno em quem matou
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
		// veneno
		if (this.mob.getLevel() >= 5 && entity instanceof EntityLiving && ((EntityLiving) entity) != null) {
			((EntityLiving) entity).addEffect(new MobEffect(
					MobEffectList.POISON.id, 8 * 20, 0));
			((EntityLiving) entity).addEffect(new MobEffect(
					MobEffectList.BLINDNESS.id, 5 * 20, 0));
		}
	}

	// da o veneno e tem chance de prender o player numa teia
	@Override
	public boolean n(Entity paramEntity) {
		if (super.n(paramEntity)) {
			if ((paramEntity instanceof EntityLiving)) {
				NMS.attack(this, paramEntity);
				if (this.mob.getLevel() >= 4) {
					int random = (int) ((Math.random() * 99) + 1);
					if (random <= 15) {
						final Material m = paramEntity.getBukkitEntity()
								.getLocation().getBlock().getType();
						final Location loc = paramEntity.getBukkitEntity()
								.getLocation();
						loc.getBlock().setType(Material.WEB);
						new BukkitRunnable() {

							public void run() {
								loc.getBlock().setType(m);
							}
						}.runTaskLater(Mobs.getInstance(), 20 * 3);
					}

					if (this.mob.getLevel() >= 4) {
						if (random <= 20) {
							((EntityLiving) paramEntity)
									.addEffect(new MobEffect(
											MobEffectList.POISON.id, 5 * 20, 0));
						}
					}

				}
			}
			return true;
		}
		return false;
	}

}
