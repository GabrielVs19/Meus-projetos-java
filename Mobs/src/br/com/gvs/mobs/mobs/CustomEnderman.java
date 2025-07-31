package br.com.gvs.mobs.mobs;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;

import br.com.gvs.mobs.util.AttackType;
import br.com.gvs.mobs.util.Mob;
import net.minecraft.server.v1_7_R2.Blocks;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityEnderman;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.Item;
import net.minecraft.server.v1_7_R2.ItemStack;
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
import net.minecraft.server.v1_7_R2.Vec3D;
import net.minecraft.server.v1_7_R2.World;

public class CustomEnderman extends EntityEnderman implements CustomMob {

	private int bt;
	@SuppressWarnings("unused")
	private boolean bv;
	private Mob mob;

	public CustomEnderman(World world, Mob mob) {
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
		a(0.6F, 2.9F);
		this.W = 1.0F;
		getNavigation().a(true);
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this,
				EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
		if (this.mob.getAttackType() != AttackType.PASSIVE) {
			this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this,
					EntityHuman.class, 1.0D, false));
			this.targetSelector
					.a(1, new PathfinderGoalHurtByTarget(this, true));
			if (this.mob.getAttackType() == AttackType.HOSTILE) {
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

	@Override
	protected Entity findTarget() {
		EntityHuman entityhuman = this.world.findNearbyVulnerablePlayer(this,
				25.0D);
		if (entityhuman != null) {
			if (f(entityhuman)
					&& (this.mob.getAttackType() == AttackType.HOSTILE || this
							.getLastDamager() != null)) {
				this.bv = true;
				if (this.bt == 0) {
					this.world.makeSound(entityhuman.locX, entityhuman.locY,
							entityhuman.locZ, "mob.endermen.stare", 1.0F, 1.0F);
				}
				if (this.bt++ == 5) {
					this.bt = 0;
					a(true);
					return entityhuman;
				}
			} else {
				this.bt = 0;
			}
		}
		return null;
	}

	// Aplica cegueira ao olhar para o enderman
	private boolean f(EntityHuman entityhuman) {
		ItemStack itemstack = entityhuman.inventory.armor[3];
		if ((itemstack != null)
				&& (itemstack.getItem() == Item.getItemOf(Blocks.PUMPKIN))) {
			return false;
		}
		Vec3D vec3d = entityhuman.j(1.0F).a();
		Vec3D vec3d1 = Vec3D.a(this.locX - entityhuman.locX,
				this.boundingBox.b + this.length / 2.0F
						- (entityhuman.locY + entityhuman.getHeadHeight()),
				this.locZ - entityhuman.locZ);
		double d0 = vec3d1.b();

		vec3d1 = vec3d1.a();
		double d1 = vec3d.b(vec3d1);

		if ((d1 > 1.0D - 0.025D / d0) && (entityhuman.p(this))
				&& this.mob.getLevel() >= 4) {
			((EntityLiving) entityhuman).addEffect(new MobEffect(
					MobEffectList.BLINDNESS.id, 4 * 20, 0));
		}

		return (d1 > 1.0D - 0.025D / d0) && (entityhuman.p(this));
	}

}
