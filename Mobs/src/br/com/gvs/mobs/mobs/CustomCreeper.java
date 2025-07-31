package br.com.gvs.mobs.mobs;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R2.EntityCreeper;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.NBTTagCompound;
import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R2.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_7_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.PathfinderGoalSwell;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.gvs.mobs.util.AttackType;
import br.com.gvs.mobs.util.Mob;

public class CustomCreeper extends EntityCreeper implements CustomMob {

	@SuppressWarnings("unused")
	private int bp;
	private int fuseTicks;
	private int maxFuseTicks = 30;
	private int explosionRadius = 3;
	private Mob mob;

	public CustomCreeper(World world, Mob mob) {
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
		a(0.9F, 0.9F);
		getNavigation().a(true);
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this,
				EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
		if (mob.getAttackType() != AttackType.PASSIVE) {
			this.goalSelector.a(1, new PathfinderGoalSwell(this));
			this.goalSelector.a(5, new PathfinderGoalMeleeAttack(this, 1.0D,
					false));
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

	@Override
	public void h() {
		if (isAlive()) {
			this.bp = this.fuseTicks;
			if (cc()) {
				a(1);
			}
			int i = cb();
			if ((i > 0) && (this.fuseTicks == 0)) {
				makeSound("creeper.primed", 1.0F, 0.5F);
			}
			this.fuseTicks += i;
			if (this.fuseTicks < 0) {
				this.fuseTicks = 0;
			}
			if (this.fuseTicks >= this.maxFuseTicks) {
				this.fuseTicks = this.maxFuseTicks;
				this.noDamageTicks = 60;
				boom();
			}
		}
		super.h();
	}

	@Override
	public void a(NBTTagCompound nbttagcompound)
	{
		super.a(nbttagcompound);
		this.datawatcher.watch(17, Byte.valueOf((byte)(nbttagcompound.getBoolean("powered") ? 1 : 0)));
		if (nbttagcompound.hasKeyOfType("Fuse", 99)) {
			this.maxFuseTicks = nbttagcompound.getShort("Fuse");
		}
		if (nbttagcompound.hasKeyOfType("ExplosionRadius", 99)) {
			this.explosionRadius = nbttagcompound.getByte("ExplosionRadius");
		}
		if (nbttagcompound.getBoolean("ignited")) {
			cd();
		}
	}

	@Override
	public void b(NBTTagCompound nbttagcompound) {
		super.b(nbttagcompound);
		if (this.datawatcher.getByte(17) == 1) {
			nbttagcompound.setBoolean("powered", true);
		}
		nbttagcompound.setShort("Fuse", (short) this.maxFuseTicks);
		nbttagcompound.setByte("ExplosionRadius", (byte) this.explosionRadius);
		nbttagcompound.setBoolean("ignited", cc());
	}

	@Override
	protected void b(float f) {
		super.b(f);
		this.fuseTicks = ((int) (this.fuseTicks + f * 1.5F));
		if (this.fuseTicks > this.maxFuseTicks - 5) {
			this.fuseTicks = (this.maxFuseTicks - 5);
		}
	}


	private void boom() {
		if (!this.world.isStatic) {
			boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
			float radius = isPowered() ? 6.0F : 3.0F;
			ExplosionPrimeEvent event = new ExplosionPrimeEvent(getBukkitEntity(), radius, false);
			this.world.getServer().getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				this.world.createExplosion(this, this.locX, this.locY, this.locZ, event.getRadius(), event.getFire(), flag);
				if (!this.isBaby()) {
					if (this.mob.getLevel() >= 4){
						for(Entity e : this.getBukkitEntity().getNearbyEntities(7.5, 7.5, 7.5)){
							if(e.getType() == EntityType.PLAYER){
								((CraftEntity) e).getHandle().motY *= 1.4;
								int random = (int) (Math.random() * 99) + 1;
								if (this.mob.getLevel() >= 5 && random <= 25){
									((Player)e).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 6, 1));
								}
							}
						}
					}
				}
			}else{
				this.fuseTicks = 0;
			}
		}
	}

}
