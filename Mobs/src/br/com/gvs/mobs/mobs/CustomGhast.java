package br.com.gvs.mobs.mobs;


import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R2.AxisAlignedBB;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityGhast;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityLargeFireball;
import net.minecraft.server.v1_7_R2.EnumDifficulty;
import net.minecraft.server.v1_7_R2.GenericAttributes;
import net.minecraft.server.v1_7_R2.MathHelper;
import net.minecraft.server.v1_7_R2.NBTTagCompound;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.Vec3D;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;
import org.bukkit.event.entity.EntityTargetEvent;

import br.com.gvs.mobs.util.AttackType;
import br.com.gvs.mobs.util.Mob;


public class CustomGhast extends EntityGhast implements CustomMob
{


	private Entity target;
	private int br;
	private int explosionPower = 1;
	private Mob mob;

	public CustomGhast(World world, Mob mob)
	{
		super(world);
		this.mob = mob;
		a(4.0F, 4.0F);
		this.fireProof = true;
		this.b = 5;
		bb().b(GenericAttributes.e);
	}

	@Override
	public Mob getMob()
	{
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
	protected void bp()
	{
		if((!this.world.isStatic) && (this.world.difficulty == EnumDifficulty.PEACEFUL))
		{
			die();
		}
		w();
		this.bo = this.bp;
		double d0 = this.i - this.locX;
		double d1 = this.bm - this.locY;
		double d2 = this.bn - this.locZ;
		double d3 = d0 * d0 + d1 * d1 + d2 * d2;
		if((d3 < 1.0D) || (d3 > 3600.0D))
		{
			this.i = (this.locX + (this.random.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.bm = (this.locY + (this.random.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.bn = (this.locZ + (this.random.nextFloat() * 2.0F - 1.0F) * 16.0F);
		}
		if(this.h-- <= 0)
		{
			this.h += this.random.nextInt(5) + 2;
			d3 = MathHelper.sqrt(d3);
			if(a(this.i, this.bm, this.bn, d3))
			{
				this.motX += d0 / d3 * 0.1D;
				this.motY += d1 / d3 * 0.1D;
				this.motZ += d2 / d3 * 0.1D;
			}
			else
			{
				this.i = this.locX;
				this.bm = this.locY;
				this.bn = this.locZ;
			}
		}
		if((this.target != null) && (this.target.dead))
		{
			EntityTargetEvent event = new EntityTargetEvent(getBukkitEntity(), null, EntityTargetEvent.TargetReason.TARGET_DIED);
			this.world.getServer().getPluginManager().callEvent(event);
			if(!event.isCancelled())
			{
				if(event.getTarget() == null)
				{
					this.target = null;
				}
				else
				{
					this.target = ((CraftEntity) event.getTarget()).getHandle();
				}
			}
		}
		if((this.target == null) || (this.br-- <= 0))
		{
			Entity target = this.world.findNearbyVulnerablePlayer(this, 100.0D);
			if(target != null && (this.mob.getAttackType() == AttackType.HOSTILE || this.getLastDamager() != null))
			{
				EntityTargetEvent event = new EntityTargetEvent(getBukkitEntity(), target.getBukkitEntity(), EntityTargetEvent.TargetReason.CLOSEST_PLAYER);
				this.world.getServer().getPluginManager().callEvent(event);
				if(!event.isCancelled())
				{
					if(event.getTarget() == null)
					{
						this.target = null;
					}
					else
					{
						this.target = ((CraftEntity) event.getTarget()).getHandle();
					}
				}
			}
			if(this.target != null)
			{
				this.br = 20;
			}
		}
		double d4 = 64.0D;
		if((this.target != null) && (this.target.f(this) < d4 * d4))
		{
			double d5 = this.target.locX - this.locX;
			double d6 = this.target.boundingBox.b + this.target.length / 2.0F - (this.locY + this.length / 2.0F);
			double d7 = this.target.locZ - this.locZ;

			this.aM = (this.yaw = -(float) Math.atan2(d5, d7) * 180.0F / 3.141593F);
			if(p(this.target))
			{
				if(this.bp == 10)
				{
					this.world.a((EntityHuman) null, 1007, (int) this.locX, (int) this.locY, (int) this.locZ, 0);
				}
				this.bp += 1;
				if(this.bp == 20)
				{
					this.world.a((EntityHuman) null, 1008, (int) this.locX, (int) this.locY, (int) this.locZ, 0);
					EntityLargeFireball entitylargefireball = new EntityLargeFireball(this.world, this, d5, d6, d7);

					entitylargefireball.bukkitYield = (entitylargefireball.bukkitYield = this.explosionPower);
					double d8 = 4.0D;
					Vec3D vec3d = j(1.0F);

					entitylargefireball.locX = (this.locX + vec3d.a * d8);
					entitylargefireball.locY = (this.locY + this.length / 2.0F + 0.5D);
					entitylargefireball.locZ = (this.locZ + vec3d.c * d8);
					this.world.addEntity(entitylargefireball);
					if(this.mob.getLevel() >= 4)
					{
						// ticks
						this.bp = -15;
					}
					else
					{
						this.bp = -40;
					}
				}
			}
			else
				if(this.bp > 0)
				{
					this.bp -= 1;
				}
		}
		else
		{
			this.aM = (this.yaw = -(float) Math.atan2(this.motX, this.motZ) * 180.0F / 3.141593F);
			if(this.bp > 0)
			{
				this.bp -= 1;
			}
		}
		if(!this.world.isStatic)
		{
			byte b0 = this.datawatcher.getByte(16);
			byte b1 = (byte) (this.bp > 10 ? 1 : 0);
			if(b0 != b1)
			{
				this.datawatcher.watch(16, Byte.valueOf(b1));
			}
		}
	}

	private boolean a(double d0, double d1, double d2, double d3)
	{
		double d4 = (this.i - this.locX) / d3;
		double d5 = (this.bm - this.locY) / d3;
		double d6 = (this.bn - this.locZ) / d3;
		AxisAlignedBB axisalignedbb = this.boundingBox.clone();
		for(int i = 1; i < d3; i++)
		{
			axisalignedbb.d(d4, d5, d6);
			if(!this.world.getCubes(this, axisalignedbb).isEmpty())
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean canSpawn(){
		return true;
	}

	@Override
	public void b(NBTTagCompound nbttagcompound)
	{
		super.b(nbttagcompound);
		nbttagcompound.setInt("ExplosionPower", this.explosionPower);
	}

	@Override
	public void a(NBTTagCompound nbttagcompound)
	{
		super.a(nbttagcompound);
		if (nbttagcompound.hasKeyOfType("ExplosionPower", 99)) {
			this.explosionPower = nbttagcompound.getInt("ExplosionPower");
		}
	}
}
