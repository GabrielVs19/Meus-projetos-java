package br.com.gvs.mobs.mobs;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EntitySnowman;
import net.minecraft.server.v1_7_R2.GenericAttributes;
import net.minecraft.server.v1_7_R2.IRangedEntity;
import net.minecraft.server.v1_7_R2.MathHelper;
import net.minecraft.server.v1_7_R2.PathfinderGoalArrowAttack;
import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;
import org.bukkit.inventory.ItemStack;

import br.com.gvs.mobs.util.AttackType;
import br.com.gvs.mobs.util.Mob;

public class CustomSnowman extends EntitySnowman implements IRangedEntity,
		CustomMob {

	private Mob mob;

	public CustomSnowman(World world, Mob mob) {
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
		a(0.4F, 1.8F);
		getNavigation().a(true);
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this,
				EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
		if (this.mob.getAttackType() != AttackType.PASSIVE) {
			this.goalSelector.a(1, new PathfinderGoalArrowAttack(this, 1.0D,
					20, 60, 15.0F));
			this.targetSelector
					.a(1, new PathfinderGoalHurtByTarget(this, true));
			if (this.mob.getAttackType() == AttackType.HOSTILE) {
				this.targetSelector.a(2,
						new PathfinderGoalNearestAttackableTarget(this,
								EntityHuman.class, 0, true));
			}
		}
		bb().b(GenericAttributes.e);
		this.setEquipment(0,
				CraftItemStack.asNMSCopy(new ItemStack(Material.SNOW_BALL)));
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
	public void e() {
		super.e();
	}

	@Override
	public void a(EntityLiving entityliving, float f) {
		CustomSnowball entitysnowball = new CustomSnowball(this.world, this, (float) getAttributeInstance(GenericAttributes.e).getValue(), this.mob.getLevel());
		double d0 = entityliving.locX - this.locX;
		double d1 = entityliving.locY + entityliving.getHeadHeight() - 1.100000023841858D - entitysnowball.locY;
		double d2 = entityliving.locZ - this.locZ;
		float f1 = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.15F;

		entitysnowball.shoot(d0, d1 + f1, d2, 1.6F, 12.0F);
		makeSound("random.bow", 1.0F, 1.0F / (aH().nextFloat() * 0.4F + 0.8F));
		this.world.addEntity(entitysnowball);
	}

}
