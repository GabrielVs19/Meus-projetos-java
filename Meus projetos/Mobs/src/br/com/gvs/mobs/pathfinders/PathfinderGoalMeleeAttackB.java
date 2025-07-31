package br.com.gvs.mobs.pathfinders;

import net.minecraft.server.v1_7_R2.EntityCreature;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.MathHelper;
import net.minecraft.server.v1_7_R2.PathEntity;
import net.minecraft.server.v1_7_R2.PathfinderGoal;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.craftbukkit.v1_7_R2.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent;

import br.com.gvs.mobs.util.NMS;

public class PathfinderGoalMeleeAttackB extends PathfinderGoal {

	World a;
	EntityCreature b;
	int c;
	double d;
	boolean e;
	PathEntity f;
	Class<?> g;
	private int h;
	private double i;
	private double j;
	private double k;

	public PathfinderGoalMeleeAttackB(EntityCreature entitycreature,
			Class<?> oclass, double d0, boolean flag) {
		this(entitycreature, d0, flag);
		this.g = oclass;
	}

	public PathfinderGoalMeleeAttackB(EntityCreature entitycreature, double d0,
			boolean flag) {
		this.b = entitycreature;
		this.a = entitycreature.world;
		this.d = d0;
		this.e = flag;
		a(3);
	}

	public boolean a() {
		EntityLiving entityliving = this.b.getGoalTarget();
		if (entityliving == null) {
			return false;
		}
		if (!entityliving.isAlive()) {
			return false;
		}
		if ((this.g != null)
				&& (!this.g.isAssignableFrom(entityliving.getClass()))) {
			return false;
		}
		this.f = this.b.getNavigation().a(entityliving);
		return this.f != null;
	}

	public boolean b() {
		EntityLiving entityliving = this.b.getGoalTarget();
		EntityTargetEvent.TargetReason reason = this.b.getGoalTarget() == null ? EntityTargetEvent.TargetReason.FORGOT_TARGET
				: EntityTargetEvent.TargetReason.TARGET_DIED;
		if ((this.b.getGoalTarget() == null)
				|| ((this.b.getGoalTarget() != null) && (!this.b
						.getGoalTarget().isAlive()))) {
			CraftEventFactory.callEntityTargetEvent(this.b, null, reason);
		}
		return !this.e ? false : !this.b.getNavigation().g() ? true
				: !entityliving.isAlive() ? false
						: entityliving == null ? false : this.b.b(
								MathHelper.floor(entityliving.locX),
								MathHelper.floor(entityliving.locY),
								MathHelper.floor(entityliving.locZ));
	}

	public void c() {
		this.b.getNavigation().a(this.f, this.d);
		this.h = 0;
	}

	public void d() {
		this.b.getNavigation().h();
	}

	public void e() {
		EntityLiving entityliving = this.b.getGoalTarget();

		this.b.getControllerLook().a(entityliving, 30.0F, 30.0F);
		double d0 = this.b.e(entityliving.locX, entityliving.boundingBox.b,
				entityliving.locZ);
		double d1 = this.b.width * 2.0F * this.b.width * 2.0F
				+ entityliving.width;

		this.h -= 1;
		if (((this.e) || (this.b.getEntitySenses().canSee(entityliving)))
				&& (this.h <= 0)
				&& (((this.i == 0.0D) && (this.j == 0.0D) && (this.k == 0.0D))
						|| (entityliving.e(this.i, this.j, this.k) >= 1.0D) || (this.b
						.aH().nextFloat() < 0.05F))) {
			this.i = entityliving.locX;
			this.j = entityliving.boundingBox.b;
			this.k = entityliving.locZ;
			this.h = (4 + this.b.aH().nextInt(7));
			if (d0 > 1024.0D) {
				this.h += 10;
			} else if (d0 > 256.0D) {
				this.h += 5;
			}
			if (!this.b.getNavigation().a(entityliving, this.d)) {
				this.h += 15;
			}
		}
		this.c = Math.max(this.c - 1, 0);
		if ((d0 <= d1) && (this.c <= 20)) {
			this.c = 20;
			if (this.b.bd() != null) {
				this.b.aZ();
			}
			NMS.attack(this.b, entityliving);
		}
	}
}
