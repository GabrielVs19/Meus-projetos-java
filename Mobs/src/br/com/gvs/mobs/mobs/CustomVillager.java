package br.com.gvs.mobs.mobs;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;

import br.com.gvs.mobs.pathfinders.PathfinderGoalMeleeAttackB;
import br.com.gvs.mobs.util.AttackType;
import br.com.gvs.mobs.util.Mob;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityVillager;
import net.minecraft.server.v1_7_R2.GenericAttributes;
import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalPanic;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.World;

public class CustomVillager extends EntityVillager implements CustomMob {

	private Mob mob;

	public CustomVillager(World arg0, Mob mob) {
		super(arg0);
		this.mob = mob;
		setProfession(0);
		a(0.6F, 1.8F);
		getNavigation().b(true);
		getNavigation().a(true);
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
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this,
				EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
		if (this.mob.getAttackType() != AttackType.PASSIVE) {
			this.goalSelector.a(1, new PathfinderGoalMeleeAttackB(this,
					EntityHuman.class, 1.0D, false));
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
	public boolean a(EntityHuman paramEntityHuman) {
		return false;
	}

}
