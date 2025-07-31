package br.com.gvs.mobs.bosses;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EntityPlayer;
import net.minecraft.server.v1_7_R2.EntitySlime;
import net.minecraft.server.v1_7_R2.GenericAttributes;
import net.minecraft.server.v1_7_R2.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R2.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.gvs.mobs.Mobs;
import br.com.gvs.mobs.pathfinders.PathfinderGoalWalkToSpawn;
import br.com.gvs.mobs.util.Boss;

public class CustomShaldren extends EntitySlime implements CustomBoss{

	private Boss boss;
	private int jumpDelay;
	private Entity lastTarget;
	public CustomShaldren(World world, Boss boss) {
		super(world);
		this.boss = boss;
		this.resetAI();
		this.setAI();
		bb().b(GenericAttributes.e);
		setSize(15);
		this.fireProof = true;
	}

	@Override
	public Boss getBoss() {
		return this.boss;
	}
	
	@Override
	public void resetAI() {
		try{
			Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
			bField.setAccessible(true);
			Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
			cField.setAccessible(true);
			bField.set(this.goalSelector, new UnsafeList<Object>());
			bField.set(this.targetSelector, new UnsafeList<Object>());
			cField.set(this.goalSelector, new UnsafeList<Object>());
			cField.set(this.targetSelector, new UnsafeList<Object>());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void setAI() {
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
	}
	
	@Override
	public void setWalkToLocation(Location loc) {
		this.goalSelector.a(5, new PathfinderGoalWalkToSpawn(this, loc));	
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

	@Override
	protected void bp(){
		w();
		Entity entityhuman = this.world.findNearbyVulnerablePlayer(this, 16.0D);
		EntityTargetEvent event = null;
		if((entityhuman != null) && (!entityhuman.equals(this.lastTarget))){
			event = CraftEventFactory.callEntityTargetEvent(this, entityhuman, EntityTargetEvent.TargetReason.CLOSEST_PLAYER);
		}else
			if((this.lastTarget != null) && (entityhuman == null)){
				event = CraftEventFactory.callEntityTargetEvent(this, entityhuman, EntityTargetEvent.TargetReason.FORGOT_TARGET);
			}
		if((event != null) && (!event.isCancelled())){
			entityhuman = event.getTarget() == null ? null : ((CraftEntity) event.getTarget()).getHandle();
		}
		this.lastTarget = entityhuman;
		if(entityhuman != null){
			a(entityhuman, 10.0F, 20.0F);
		} 
		if((this.onGround) && (this.jumpDelay-- <= 0)){
			this.jumpDelay = bR();
			if(entityhuman != null){
				this.jumpDelay /= 3;
			}
			this.bc = true;
			if(bY()){
				makeSound(bV(), be(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
			}
			this.bd = (1.0F - this.random.nextFloat() * 2.0F);
			this.be = (1 * getSize());
		}else{
			this.bc = false;
			if(this.onGround){
				this.bd = (this.bf = 0.0F);
			}
		}
	}

	@Override
	public void collide(Entity e) {
		if (e instanceof EntityPlayer) {
			((EntityLiving) e).fireTicks = 60;
			return;
		}
		super.collide(e);
	}

	public void die() {
		super.die();
		this.dead = true;
		new BukkitRunnable()
		{

			float radius = 0.2F;
			int particles = 50;
			float grow = 0.2F;
			int rings = 12;
			int step = 0;
			Location location = getBukkitEntity().getLocation();

			public void run()
			{
				if(this.radius >= 4.0)
				{
					cancel();
				}
				if(step > rings)
				{
					this.step = 0;
				}
				double y = this.step * this.grow;
				location.add(0.0D, y, 0.0D);
				for(int i = 0; i < this.particles; i++)
				{
					double angle = 6.283185307179586D * i / this.particles;
					double x = Math.cos(angle) * this.radius;
					double z = Math.sin(angle) * this.radius;
					location.add(x, 0.0D, z);
					PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles("flame", (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0.0F, 0.0F, 0.0F, 1, 1);
					for(org.bukkit.entity.Entity e : getBukkitEntity().getNearbyEntities(25, 35, 25))
					{
						if(e.getType() == EntityType.PLAYER)
						{
							((CraftPlayer) e).getHandle().playerConnection.sendPacket(packet);
						}
					}
					location.subtract(x, 0.0D, z);
				}
				location.subtract(0.0D, y, 0.0D);
				this.step += 1;
				this.radius += 0.2;
				this.particles += 15;
				for(org.bukkit.entity.Entity e : getBukkitEntity().getNearbyEntities(8, 8, 8))
				{
					if(e instanceof EntityLiving){
						e.setFireTicks(60);
					}
				}
			}
		}.runTaskTimer(Mobs.getInstance(), 0, 2);
		this.world.createExplosion(this, this.locX, this.locY, this.locZ, (float) 3.5, false, false);
	}



}
