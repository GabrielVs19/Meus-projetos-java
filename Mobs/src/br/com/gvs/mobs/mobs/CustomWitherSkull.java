package br.com.gvs.mobs.mobs;

import java.lang.reflect.Constructor;

import net.minecraft.server.v1_7_R2.DamageSource;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EntityWitherSkull;
import net.minecraft.server.v1_7_R2.EnumDifficulty;
import net.minecraft.server.v1_7_R2.MobEffect;
import net.minecraft.server.v1_7_R2.MobEffectList;
import net.minecraft.server.v1_7_R2.MovingObjectPosition;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.Location;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import br.com.gvs.mobs.managers.MobManager;
import br.com.gvs.mobs.util.Mob;
import br.com.gvs.mobs.util.MobType;

public class CustomWitherSkull extends EntityWitherSkull{

	public CustomWitherSkull(World world) {
		super(world);
		a(0.3125F, 0.3125F);
	}

	public CustomWitherSkull(World world, EntityLiving entityliving, double d0, double d1, double d2){
		super(world, entityliving, d0, d1, d2);
		a(0.3125F, 0.3125F);
	}

	@Override
	protected void a(MovingObjectPosition movingobjectposition){
		if (!this.world.isStatic){
			if (movingobjectposition.entity != null){
				boolean didDamage = false;
				if (this.shooter != null){
					didDamage = movingobjectposition.entity.damageEntity(DamageSource.mobAttack(this.shooter), 8.0F);
					if ((didDamage) && (!movingobjectposition.entity.isAlive())) {
						this.shooter.heal(8.0F, RegainReason.WITHER);
					}
				}else{
					didDamage = movingobjectposition.entity.damageEntity(DamageSource.MAGIC, 5.0F);
				}
				if ((didDamage) && ((movingobjectposition.entity instanceof EntityLiving))){
					byte b0 = 0;
					if (this.world.difficulty == EnumDifficulty.NORMAL) {
						b0 = 10;
					} else if (this.world.difficulty == EnumDifficulty.HARD) {
						b0 = 40;
					}
					if (b0 > 0){
						((EntityLiving)movingobjectposition.entity).addEffect(new MobEffect(MobEffectList.WITHER.id, 20 * b0, 1));
						((EntityLiving)movingobjectposition.entity).addEffect(new MobEffect(MobEffectList.SLOWER_MOVEMENT.id, 20 * 5, 1));
						int random = (int) (Math.random() * 99) + 1;
						if(random <= 60){
							Location loc = this.getBukkitEntity().getLocation();
							World w = this.world;
							Mob mob = MobManager.getMob(MobType.PIG_ZOMBIE, 5);
							try{
								Class<? extends Entity> mobClass = mob.getMobType().getMobClass();
								Constructor<? extends Entity> mobc = mobClass.getConstructor(World.class, Mob.class);
								MobManager.spawnMob((Entity) mobc.newInstance(w, mob), loc);
							}catch(Exception ex){
								ex.printStackTrace();
							}
						}
					}
				}
			}

			ExplosionPrimeEvent event = new ExplosionPrimeEvent(getBukkitEntity(), 1.0F, false);
			this.world.getServer().getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				this.world.createExplosion(this, this.locX, this.locY, this.locZ, event.getRadius(), event.getFire(), this.world.getGameRules().getBoolean("mobGriefing"));
			}
			die();
		}
	}

}
