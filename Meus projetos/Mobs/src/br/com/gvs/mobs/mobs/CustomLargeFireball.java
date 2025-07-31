package br.com.gvs.mobs.mobs;

import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
import org.bukkit.entity.Explosive;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import net.minecraft.server.v1_7_R2.DamageSource;
import net.minecraft.server.v1_7_R2.EntityLargeFireball;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.MovingObjectPosition;
import net.minecraft.server.v1_7_R2.World;

public class CustomLargeFireball extends EntityLargeFireball{

	public CustomLargeFireball(World world)
	{
		super(world);
	}

	public CustomLargeFireball(World world, EntityLiving entityliving, double d0, double d1, double d2)
	{
		super(world, entityliving, d0, d1, d2);
	}

	@Override
	protected void a(MovingObjectPosition movingobjectposition)
	{
		if (!this.world.isStatic)
		{
			if (movingobjectposition.entity != null) {
				movingobjectposition.entity.damageEntity(DamageSource.fireball(this, this.shooter), 6.0F);
			}
			ExplosionPrimeEvent event = new ExplosionPrimeEvent((Explosive)CraftEntity.getEntity(this.world.getServer(), this));
			this.world.getServer().getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				this.world.createExplosion(this, this.locX, this.locY, this.locZ, event.getRadius(), false, this.world.getGameRules().getBoolean("mobGriefing"));
			}
			die();
		}
	}
}
