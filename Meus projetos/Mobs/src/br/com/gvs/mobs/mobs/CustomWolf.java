package br.com.gvs.mobs.mobs;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;

import br.com.gvs.mobs.managers.MobManager;
import br.com.gvs.mobs.util.AttackType;
import br.com.gvs.mobs.util.Mob;
import br.com.gvs.mobs.util.MobType;
import br.com.gvs.mobs.util.NMS;
import net.minecraft.server.v1_7_R2.DamageSource;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityArrow;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EntityWolf;
import net.minecraft.server.v1_7_R2.GenericAttributes;
import net.minecraft.server.v1_7_R2.MobEffect;
import net.minecraft.server.v1_7_R2.MobEffectList;
import net.minecraft.server.v1_7_R2.PathfinderGoalBeg;
import net.minecraft.server.v1_7_R2.PathfinderGoalBreed;
import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R2.PathfinderGoalFollowOwner;
import net.minecraft.server.v1_7_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalLeapAtTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R2.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_7_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalOwnerHurtByTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalOwnerHurtTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.World;

public class CustomWolf extends EntityWolf implements CustomMob {

	private Mob mob;

	public CustomWolf(World world, Mob mob) {
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
		a(0.6F, 0.8F);
		getNavigation().a(true);
		this.goalSelector.a(1, new PathfinderGoalFloat(this));
		this.goalSelector.a(2, this.bp);
		this.goalSelector.a(3, new PathfinderGoalLeapAtTarget(this, 0.4F));
		this.goalSelector.a(5, new PathfinderGoalFollowOwner(this, 1.0D, 10.0F, 2.0F));
		this.goalSelector.a(6, new PathfinderGoalBreed(this, 1.0D));
		this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(8, new PathfinderGoalBeg(this, 8.0F));
		this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(10, new PathfinderGoalRandomLookaround(this));
		this.targetSelector.a(1, new PathfinderGoalOwnerHurtByTarget(this));
		this.targetSelector.a(2, new PathfinderGoalOwnerHurtTarget(this));
		this.targetSelector.a(3, new PathfinderGoalHurtByTarget(this, true));
		if (this.mob.getAttackType() != AttackType.PASSIVE) {
			this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, 1.0D, true));
			this.targetSelector.a(4, new PathfinderGoalHurtByTarget(this, true));
			this.goalSelector.a(11, new PathfinderGoalLeapAtTarget(this, 0.4F));
			if (this.mob.getAttackType() == AttackType.HOSTILE) {
				this.targetSelector.a(5, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 0, true));
			}
		}
		this.ageLocked = true;
		this.setAge(1);

		setTamed(false);
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
				int random = (int) ((Math.random() * 99) + 1);
				
				if (this.mob.getLevel() >= 4 && random <= 25) {
					int duration = (this.mob.getLevel() == 4) ? 3 : 5;
					((EntityLiving) paramEntity).addEffect(new MobEffect(MobEffectList.SLOWER_MOVEMENT.id, duration * 20, 0));
				}
				return true;
			}
		}
		return false;
	}

	private boolean readySpawnWolf = false;

	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {
		if (isInvulnerable()) {
			return false;
		}
		Entity entity = damagesource.getEntity();

		if ((entity != null) && (!(entity instanceof EntityHuman))
				&& (!(entity instanceof EntityArrow))) {
			f = (f + 1.0F) / 2.0F;
		}

		if (this.mob.getLevel() >= 5 && !readySpawnWolf && this.getAge() > 0) {
			for(int i = 0; i <= 1;i++){
				CustomWolf cw = new CustomWolf(this.world, MobManager.getMobByTypeLevel(MobType.WOLF, this.random.nextInt(4) + 1));
				cw.setAge(-1);
				this.readySpawnWolf = true;
				cw.getMob().setMoveSpeed(cw.getMob().getMoveSpeed() / 2);
				MobManager.spawnMob(cw, this.getBukkitEntity().getLocation());
				cw.target = damagesource.getEntity();
			}
		}
		return super.damageEntity(damagesource, f);
	}

}
