package br.com.gvs.mobs.mobs;

import net.minecraft.server.v1_7_R2.DamageSource;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityEgg;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EntityPlayer;
import net.minecraft.server.v1_7_R2.MobEffect;
import net.minecraft.server.v1_7_R2.MobEffectList;
import net.minecraft.server.v1_7_R2.MovingObjectPosition;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEggThrowEvent;

public class CustomEgg extends EntityEgg {

	private float damage = 0.0F;

	public CustomEgg(World world, float damage) {
		super(world);
		this.damage = damage;
	}

	public CustomEgg(World world, EntityLiving entityliving, float damage) {
		super(world, entityliving);
		this.damage = damage;
	}

	public CustomEgg(World world, double d0, double d1, double d2, float damage) {
		super(world, d0, d1, d2);
		this.damage = damage;
	}

	// Egg customizado que faz com que nao nasça galinhas e o ovo dê dano
	// customizado
	@Override
	protected void a(MovingObjectPosition movingobjectposition) {
		if (movingobjectposition.entity != null) {
			movingobjectposition.entity.damageEntity(
					DamageSource.projectile(this, getShooter()), damage);
			if(movingobjectposition.entity instanceof EntityLiving){
				((EntityLiving)movingobjectposition.entity).addEffect(new MobEffect(MobEffectList.SLOWER_MOVEMENT.id, 6 * 20, 0));
			}
		}
		boolean hatching = (!this.world.isStatic)
				&& (this.random.nextInt(8) == 0);
		int numHatching = this.random.nextInt(32) == 0 ? 4 : 1;
		if (!hatching) {
			numHatching = 0;
		}
		EntityType hatchingType = EntityType.CHICKEN;

		Entity shooter = getShooter();
		if ((shooter instanceof EntityPlayer)) {
			Player player = shooter == null ? null : (Player) shooter
					.getBukkitEntity();

			PlayerEggThrowEvent event = new PlayerEggThrowEvent(player,
					(Egg) getBukkitEntity(), hatching, (byte) numHatching,
					hatchingType);
			this.world.getServer().getPluginManager().callEvent(event);

			hatching = event.isHatching();
			numHatching = event.getNumHatches();
			hatchingType = event.getHatchingType();
		}
		/*
		 * if (hatching) { for (int k = 0; k < numHatching; k++){
		 * //org.bukkit.entity.Entity entity = this.world.getWorld().spawn(new
		 * Location(this.world.getWorld(), this.locX, this.locY, this.locZ,
		 * this.yaw, 0.0F), hatchingType.getEntityClass(),
		 * CreatureSpawnEvent.SpawnReason.EGG); //if ((entity instanceof
		 * Ageable)) { // ((Ageable)entity).setBaby(); // } } }
		 */
		for (int j = 0; j < 8; j++) {
			this.world.addParticle("snowballpoof", this.locX, this.locY,
					this.locZ, 0.0D, 0.0D, 0.0D);
		}
		if (!this.world.isStatic) {
			die();
		}
	}

}
