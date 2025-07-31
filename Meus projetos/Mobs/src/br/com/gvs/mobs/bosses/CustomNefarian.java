package br.com.gvs.mobs.bosses;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.server.v1_7_R2.AxisAlignedBB;
import net.minecraft.server.v1_7_R2.Block;
import net.minecraft.server.v1_7_R2.Blocks;
import net.minecraft.server.v1_7_R2.DamageSource;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityComplexPart;
import net.minecraft.server.v1_7_R2.EntityEnderCrystal;
import net.minecraft.server.v1_7_R2.EntityEnderDragon;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.Explosion;
import net.minecraft.server.v1_7_R2.GenericAttributes;
import net.minecraft.server.v1_7_R2.Material;
import net.minecraft.server.v1_7_R2.MathHelper;
import net.minecraft.server.v1_7_R2.Vec3D;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R2.util.CraftMagicNumbers;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import br.com.gvs.mobs.mobs.CustomLargeFireball;
import br.com.gvs.mobs.pathfinders.PathfinderGoalTpToSpawn;
import br.com.gvs.mobs.util.Boss;

public class CustomNefarian extends EntityEnderDragon implements CustomBoss {

	private Boss boss;
	private Explosion explosionSource = new Explosion(null, this, (0.0D / 0.0D), (0.0D / 0.0D), (0.0D / 0.0D), (0.0F / 0.0F));
	private Entity bD;
	public CustomNefarian(World world, Boss boss) {
		super(world);
		this.boss = boss;
		bb().b(GenericAttributes.e);
	}

	@Override
	public Boss getBoss() {
		return this.boss;
	}
	
	@Override
	public void resetAI() {
	}

	@Override
	public void setAI() {
		
	}
	
	@Override
	public void setWalkToLocation(Location loc) {
		this.goalSelector.a(9, new PathfinderGoalTpToSpawn(this, loc, 200));	
	}
	
	/*@Override
	public boolean damageEntity(DamageSource paramDamageSource, float paramFloat)
	{
		BossDamageByPlayerEvent event = new BossDamageByPlayerEvent(this.getBukkitEntity(), this.boss);
		CraftEventFactory.callEvent(event);
		if(event.isCancelled()){
			return super.damageEntity(paramDamageSource, paramFloat);
		}
		return false;
	}*/

	@SuppressWarnings("unused")
	private void b(int i, int j) {
	}
	
	public void bQ(){
		this.bz = false;
		if ((this.random.nextInt(2) == 0) && (!this.world.players.isEmpty())){
			Entity target = (Entity)this.world.players.get(this.random.nextInt(this.world.players.size()));
			EntityTargetEvent event = new EntityTargetEvent(getBukkitEntity(), target.getBukkitEntity(), EntityTargetEvent.TargetReason.RANDOM_TARGET);
			this.world.getServer().getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				if (event.getTarget() == null) {
					this.bD = null;
				} else {
					this.bD = ((CraftEntity)event.getTarget()).getHandle();
					double d5 = this.bD.locX - this.locX;
					double d6 = this.bD.boundingBox.b + this.bD.length / 2.0F - (this.locY + this.length / 2.0F);
					double d7 = this.bD.locZ - this.locZ;
					CustomLargeFireball entitylargefireball = new CustomLargeFireball(this.world, this, d5, d6, d7);
					entitylargefireball.bukkitYield = (entitylargefireball.bukkitYield = 3.0f);
					double d8 = 4.0D;
					Vec3D vec3d = j(1.0F);
					entitylargefireball.locX = (this.locX + vec3d.a * d8);
					entitylargefireball.locY = (this.locY + this.length / 2.0F + 0.5D);
					entitylargefireball.locZ = (this.locZ + vec3d.c * d8);
					this.world.addEntity(entitylargefireball);
				}
			}
		}else{
			boolean flag = false;
			do{
				this.h = 0.0D;
				this.i = (70.0F + this.random.nextFloat() * 50.0F);
				this.bm = 0.0D;
				this.h += this.random.nextFloat() * 120.0F - 60.0F;
				this.bm += this.random.nextFloat() * 120.0F - 60.0F;
				double d0 = this.locX - this.h;
				double d1 = this.locY - this.i;
				double d2 = this.locZ - this.bm;

				flag = d0 * d0 + d1 * d1 + d2 * d2 > 100.0D;
			} while (!flag);
			this.bD = null;
		}
	}

	@Override
	public void e(){
		if (this.world.isStatic)
		{
			float f = MathHelper.cos(this.by * 3.141593F * 2.0F);
			float f1 = MathHelper.cos(this.bx * 3.141593F * 2.0F);
			if ((f1 <= -0.3F) && (f >= -0.3F)) {
				this.world.a(this.locX, this.locY, this.locZ, "mob.enderdragon.wings", 5.0F, 0.8F + this.random.nextFloat() * 0.3F, false);
			}
		}
		this.bx = this.by;
		if (getHealth() <= 0.0F)
		{
			float f = (this.random.nextFloat() - 0.5F) * 8.0F;
			float f1 = (this.random.nextFloat() - 0.5F) * 4.0F;
			float f2 = (this.random.nextFloat() - 0.5F) * 8.0F;
			this.world.addParticle("largeexplode", this.locX + f, this.locY + 2.0D + f1, this.locZ + f2, 0.0D, 0.0D, 0.0D);
		}
		else
		{
			bP();
			float f = 0.2F / (MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 10.0F + 1.0F);
			f *= (float)Math.pow(2.0D, this.motY);
			if (this.bA) {
				this.by += f * 0.5F;
			} else {
				this.by += f;
			}
			this.yaw = MathHelper.g(this.yaw);
			if (this.bo < 0) {
				for (int d05 = 0; d05 < this.bn.length; d05++)
				{
					this.bn[d05][0] = this.yaw;
					this.bn[d05][1] = this.locY;
				}
			}
			if (++this.bo == this.bn.length) {
				this.bo = 0;
			}
			this.bn[this.bo][0] = this.yaw;
			this.bn[this.bo][1] = this.locY;
			if (this.world.isStatic)
			{
				if (this.bg > 0)
				{
					double d0 = this.locX + (this.bh - this.locX) / this.bg;
					double d1 = this.locY + (this.bi - this.locY) / this.bg;
					double d2 = this.locZ + (this.bj - this.locZ) / this.bg;
					double d3 = MathHelper.g(this.bk - this.yaw);
					this.yaw = ((float)(this.yaw + d3 / this.bg));
					this.pitch = ((float)(this.pitch + (this.bl - this.pitch) / this.bg));
					this.bg -= 1;
					setPosition(d0, d1, d2);
					b(this.yaw, this.pitch);
				}
			}
			else
			{
				double d0 = this.h - this.locX;
				double d1 = this.i - this.locY;
				double d2 = this.bm - this.locZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				if (this.bD != null)
				{
					this.h = this.bD.locX;
					this.bm = this.bD.locZ;
					double d4 = this.h - this.locX;
					double d5 = this.bm - this.locZ;
					double d6 = Math.sqrt(d4 * d4 + d5 * d5);
					double d7 = 0.4000000059604645D + d6 / 80.0D - 1.0D;
					if (d7 > 10.0D) {
						d7 = 10.0D;
					}
					this.i = (this.bD.boundingBox.b + d7);
				}
				else
				{
					this.h += this.random.nextGaussian() * 2.0D;
					this.bm += this.random.nextGaussian() * 2.0D;
				}
				if ((this.bz) || (d3 < 100.0D) || (d3 > 22500.0D) || (this.positionChanged) || (this.F)) {
					bQ();
				}
				d1 /= MathHelper.sqrt(d0 * d0 + d2 * d2);
				float f3 = 0.6F;
				if (d1 < -f3) {
					d1 = -f3;
				}
				if (d1 > f3) {
					d1 = f3;
				}
				this.motY += d1 * 0.1000000014901161D;
				this.yaw = MathHelper.g(this.yaw);
				double d8 = 180.0D - Math.atan2(d0, d2) * 180.0D / 3.141592741012573D;
				double d9 = MathHelper.g(d8 - this.yaw);
				if (d9 > 50.0D) {
					d9 = 50.0D;
				}
				if (d9 < -50.0D) {
					d9 = -50.0D;
				}
				Vec3D vec3d = Vec3D.a(this.h - this.locX, this.i - this.locY, this.bm - this.locZ).a();
				Vec3D vec3d1 = Vec3D.a(MathHelper.sin(this.yaw * 3.141593F / 180.0F), this.motY, -MathHelper.cos(this.yaw * 3.141593F / 180.0F)).a();
				float f4 = (float)(vec3d1.b(vec3d) + 0.5D) / 1.5F;
				if (f4 < 0.0F) {
					f4 = 0.0F;
				}
				this.bf *= 0.8F;
				float f5 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 1.0F + 1.0F;
				double d10 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 1.0D + 1.0D;
				if (d10 > 40.0D) {
					d10 = 40.0D;
				}
				this.bf = ((float)(this.bf + d9 * (0.699999988079071D / d10 / f5)));
				this.yaw += this.bf * 0.1F;
				float f6 = (float)(2.0D / (d10 + 1.0D));
				float f7 = 0.06F;

				a(0.0F, -1.0F, f7 * (f4 * f6 + (1.0F - f6)));
				if (this.bA) {
					move(this.motX * 0.800000011920929D, this.motY * 0.800000011920929D, this.motZ * 0.800000011920929D);
				} else {
					move(this.motX, this.motY, this.motZ);
				}
				Vec3D vec3d2 = Vec3D.a(this.motX, this.motY, this.motZ).a();
				float f8 = (float)(vec3d2.b(vec3d1) + 1.0D) / 2.0F;

				f8 = 0.8F + 0.15F * f8;
				this.motX *= f8;
				this.motZ *= f8;
				this.motY *= 0.910000026226044D;
			}
			this.aM = this.yaw;
			this.bq.width = (this.bq.length = 3.0F);
			this.bs.width = (this.bs.length = 2.0F);
			this.bt.width = (this.bt.length = 2.0F);
			this.bu.width = (this.bu.length = 2.0F);
			this.br.length = 3.0F;
			this.br.width = 5.0F;
			this.bv.length = 2.0F;
			this.bv.width = 4.0F;
			this.bw.length = 3.0F;
			this.bw.width = 4.0F;
			float f1 = (float)(b(5, 1.0F)[1] - b(10, 1.0F)[1]) * 10.0F / 180.0F * 3.141593F;
			float f2 = MathHelper.cos(f1);
			float f9 = -MathHelper.sin(f1);
			float f10 = this.yaw * 3.141593F / 180.0F;
			float f11 = MathHelper.sin(f10);
			float f12 = MathHelper.cos(f10);

			this.br.h();
			this.br.setPositionRotation(this.locX + f11 * 0.5F, this.locY, this.locZ - f12 * 0.5F, 0.0F, 0.0F);
			this.bv.h();
			this.bv.setPositionRotation(this.locX + f12 * 4.5F, this.locY + 2.0D, this.locZ + f11 * 4.5F, 0.0F, 0.0F);
			this.bw.h();
			this.bw.setPositionRotation(this.locX - f12 * 4.5F, this.locY + 2.0D, this.locZ - f11 * 4.5F, 0.0F, 0.0F);
			if ((!this.world.isStatic) && (this.hurtTicks == 0))
			{
				a(this.world.getEntities(this, this.bv.boundingBox.grow(4.0D, 2.0D, 4.0D).d(0.0D, -2.0D, 0.0D)));
				a(this.world.getEntities(this, this.bw.boundingBox.grow(4.0D, 2.0D, 4.0D).d(0.0D, -2.0D, 0.0D)));
				b(this.world.getEntities(this, this.bq.boundingBox.grow(1.0D, 1.0D, 1.0D)));
			}
			double[] adouble = b(5, 1.0F);
			double[] adouble1 = b(0, 1.0F);

			float f3 = MathHelper.sin(this.yaw * 3.141593F / 180.0F - this.bf * 0.01F);
			float f13 = MathHelper.cos(this.yaw * 3.141593F / 180.0F - this.bf * 0.01F);

			this.bq.h();
			this.bq.setPositionRotation(this.locX + f3 * 5.5F * f2, this.locY + (adouble1[1] - adouble[1]) * 1.0D + f9 * 5.5F, this.locZ - f13 * 5.5F * f2, 0.0F, 0.0F);
			for (int j = 0; j < 3; j++)
			{
				EntityComplexPart entitycomplexpart = null;
				if (j == 0) {
					entitycomplexpart = this.bs;
				}
				if (j == 1) {
					entitycomplexpart = this.bt;
				}
				if (j == 2) {
					entitycomplexpart = this.bu;
				}
				double[] adouble2 = b(12 + j * 2, 1.0F);
				float f14 = this.yaw * 3.141593F / 180.0F + b(adouble2[0] - adouble[0]) * 3.141593F / 180.0F * 1.0F;
				float f15 = MathHelper.sin(f14);
				float f16 = MathHelper.cos(f14);
				float f17 = 1.5F;
				float f18 = (j + 1) * 2.0F;

				entitycomplexpart.h();
				entitycomplexpart.setPositionRotation(this.locX - (f11 * f17 + f15 * f18) * f2, this.locY + (adouble2[1] - adouble[1]) * 1.0D - (f18 + f17) * f9 + 1.5D, this.locZ + (f12 * f17 + f16 * f18) * f2, 0.0F, 0.0F);
			}
			if (!this.world.isStatic) {
				this.bA = (a(this.bq.boundingBox) | a(this.br.boundingBox));
			}
		}
	}

	private float b(double d0)
	{
		return (float)MathHelper.g(d0);
	}

	@SuppressWarnings("deprecation")
	private boolean a(AxisAlignedBB axisalignedbb)
	{
		int i = MathHelper.floor(axisalignedbb.a);
		int j = MathHelper.floor(axisalignedbb.b);
		int k = MathHelper.floor(axisalignedbb.c);
		int l = MathHelper.floor(axisalignedbb.d);
		int i1 = MathHelper.floor(axisalignedbb.e);
		int j1 = MathHelper.floor(axisalignedbb.f);
		boolean flag = false;
		boolean flag1 = false;


		List<org.bukkit.block.Block> destroyedBlocks = new ArrayList<>();
		CraftWorld craftWorld = this.world.getWorld();
		for (int k1 = i; k1 <= l; k1++) {
			for (int l1 = j; l1 <= i1; l1++) {
				for (int i2 = k; i2 <= j1; i2++)
				{
					Block block = this.world.getType(k1, l1, i2);
					if (block.getMaterial() != Material.AIR) {
						if ((block != Blocks.OBSIDIAN) && (block != Blocks.WHITESTONE) && (block != Blocks.BEDROCK) && (this.world.getGameRules().getBoolean("mobGriefing")))
						{
							flag1 = true;
							destroyedBlocks.add(craftWorld.getBlockAt(k1, l1, i2));
						}
						else
						{
							flag = true;
						}
					}
				}
			}
		}
		if (flag1)
		{
			org.bukkit.entity.Entity bukkitEntity = getBukkitEntity();
			EntityExplodeEvent event = new EntityExplodeEvent(bukkitEntity, bukkitEntity.getLocation(), destroyedBlocks, 0.0F);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				return flag;
			}
			if (event.getYield() == 0.0F) {
				for (org.bukkit.block.Block block : event.blockList()) {
					this.world.setAir(block.getX(), block.getY(), block.getZ());
				}
			} else {
				for (org.bukkit.block.Block block : event.blockList())
				{
					org.bukkit.Material blockId = block.getType();
					if (blockId != org.bukkit.Material.AIR)
					{
						int blockX = block.getX();
						int blockY = block.getY();
						int blockZ = block.getZ();

						Block nmsBlock = CraftMagicNumbers.getBlock(blockId);
						if (nmsBlock.a(this.explosionSource)) {
							nmsBlock.dropNaturally(this.world, blockX, blockY, blockZ, block.getData(), event.getYield(), 0);
						}
						nmsBlock.wasExploded(this.world, blockX, blockY, blockZ, this.explosionSource);

						this.world.setAir(blockX, blockY, blockZ);
					}
				}
			}
			double d0 = axisalignedbb.a + (axisalignedbb.d - axisalignedbb.a) * this.random.nextFloat();
			double d1 = axisalignedbb.b + (axisalignedbb.e - axisalignedbb.b) * this.random.nextFloat();
			double d2 = axisalignedbb.c + (axisalignedbb.f - axisalignedbb.c) * this.random.nextFloat();

			this.world.addParticle("largeexplode", d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
		return flag;
	}

	@SuppressWarnings("rawtypes")
	private void a(List list)
	{
		double d0 = (this.br.boundingBox.a + this.br.boundingBox.d) / 2.0D;
		double d1 = (this.br.boundingBox.c + this.br.boundingBox.f) / 2.0D;
		Iterator iterator = list.iterator();
		while (iterator.hasNext())
		{
			Entity entity = (Entity)iterator.next();
			if ((entity instanceof EntityLiving))
			{
				double d2 = entity.locX - d0;
				double d3 = entity.locZ - d1;
				double d4 = d2 * d2 + d3 * d3;

				entity.g(d2 / d4 * 4.0D, 0.2000000029802322D, d3 / d4 * 4.0D);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void b(List list)
	{
		for (int i = 0; i < list.size(); i++)
		{
			Entity entity = (Entity)list.get(i);
			if ((entity instanceof EntityLiving)) {
				entity.damageEntity(DamageSource.mobAttack(this), 10.0F);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void bP()
	{
		if (this.bC != null) {
			if (this.bC.dead)
			{
				if (!this.world.isStatic)
				{
					EntityDamageEvent event = new EntityDamageEvent(getBukkitEntity(), EntityDamageEvent.DamageCause.ENTITY_EXPLOSION, 10.0D);
					Bukkit.getPluginManager().callEvent(event);
					if (!event.isCancelled())
					{
						getBukkitEntity().setLastDamageCause(event);
						a(this.bq, DamageSource.explosion((Explosion)null), (float)event.getDamage());
					}
				}
				this.bC = null;
			}
			else if ((this.ticksLived % 10 == 0) && (getHealth() < getMaxHealth()))
			{
				EntityRegainHealthEvent event = new EntityRegainHealthEvent(getBukkitEntity(), 1.0D, EntityRegainHealthEvent.RegainReason.ENDER_CRYSTAL);
				this.world.getServer().getPluginManager().callEvent(event);
				if (!event.isCancelled()) {
					setHealth((float)(getHealth() + event.getAmount()));
				}
			}
		}
		if (this.random.nextInt(10) == 0)
		{
			float f = 32.0F;
			List list = this.world.a(EntityEnderCrystal.class, this.boundingBox.grow(f, f, f));
			EntityEnderCrystal entityendercrystal = null;
			double d0 = 1.7976931348623157E+308D;
			Iterator iterator = list.iterator();
			while (iterator.hasNext())
			{
				EntityEnderCrystal entityendercrystal1 = (EntityEnderCrystal)iterator.next();
				double d1 = entityendercrystal1.f(this);
				if (d1 < d0)
				{
					d0 = d1;
					entityendercrystal = entityendercrystal1;
				}
			}
			this.bC = entityendercrystal;
		}
	}


}
