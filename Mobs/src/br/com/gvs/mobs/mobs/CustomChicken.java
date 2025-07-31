package br.com.gvs.mobs.mobs;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R2.EntityChicken;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.GenericAttributes;
import net.minecraft.server.v1_7_R2.IRangedEntity;
import net.minecraft.server.v1_7_R2.MathHelper;
import net.minecraft.server.v1_7_R2.PathfinderGoalArrowAttack;
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

import br.com.gvs.mobs.pathfinders.PathfinderGoalMeleeAttackB;
import br.com.gvs.mobs.util.AttackType;
import br.com.gvs.mobs.util.Mob;

public class CustomChicken extends EntityChicken implements IRangedEntity,
		CustomMob {

	private Mob mob;

	public CustomChicken(World world, Mob mob) {
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
		this.goalSelector.a(6, new PathfinderGoalBreed(this, 1.0D));
		if (this.mob.getAttackType() != AttackType.PASSIVE) {
			if (this.mob.getLevel() >= 3) {
				this.goalSelector.a(1, new PathfinderGoalArrowAttack(this,
						1.0D, 20, 60, 15.0F));
			} else {
				this.goalSelector.a(1, new PathfinderGoalMeleeAttackB(this,
						EntityHuman.class, 1.0D, false));
			}
			this.targetSelector
					.a(1, new PathfinderGoalHurtByTarget(this, true));
			if (this.mob.getAttackType() == AttackType.HOSTILE) {
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
	
	private boolean flyjump = false;

	// Diminui o tempo em que a galinha plana e adicionei porcentagem para
	// diminuir mais ainda
	// Faz o pulo da galinha
	@Override
	public void e() {
		super.e();
		this.bs = this.bp;
		this.br = this.bq;
		this.bq = ((float) (this.bq + (this.onGround ? -1 : 4) * 0.3D));
		if (this.flyjump) {
			this.flyjump = false;
			this.motY *= 4.5;
			this.motX *= 1.5;
			this.motZ *= 1.5;
		} else {
			int random = (int) ((Math.random() * 99) + 1);
			if ((!this.onGround) && (this.motY < 0.0D) && random <= 20) {
				this.motY *= 0.4D;
			}
		}
	}

	// Chance de 50% quando atacada de 'pular'
	@Override
	protected String aS() {
		if (this.mob.getLevel() >= 3) {
			int random = (int) ((Math.random() * 99) + 1);
			if (random <= 50) {
				this.flyjump = true;
			}
		}
		return "mob.chicken.hurt";
	}

	// Chance de 50% quando atacada de 'pular'
	@Override
	protected String aT() {
		if (this.mob.getLevel() >= 3) {
			int random = (int) ((Math.random() * 99) + 1);
			if (random <= 50) {
				this.flyjump = true;
			}
		}
		return "mob.chicken.hurt";
	}

	// Atira os ovos
	@Override
	public void a(EntityLiving entityliving, float f) {
		CustomEgg entityegg = new CustomEgg(this.world, this,
				(float) getAttributeInstance(GenericAttributes.e).getValue());
		double d0 = entityliving.locX - this.locX;
		double d1 = entityliving.locY + entityliving.getHeadHeight()
				- 1.100000023841858D - entityegg.locY;
		double d2 = entityliving.locZ - this.locZ;
		float f1 = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.2F;

		entityegg.shoot(d0, d1 + f1, d2, 1.6F, 12.0F);
		makeSound("mob.chicken.plop", 1.0F,
				1.0F / (aH().nextFloat() * 0.4F + 0.8F));
		this.world.addEntity(entityegg);
	}

	@Override
	public Mob getMob() {
		return this.mob;
	}

}
