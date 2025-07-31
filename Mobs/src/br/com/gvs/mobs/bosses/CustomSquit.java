package br.com.gvs.mobs.bosses;

import java.lang.reflect.Field;
import java.util.ArrayList;

import net.minecraft.server.v1_7_R2.AchievementList;
import net.minecraft.server.v1_7_R2.DamageSource;
import net.minecraft.server.v1_7_R2.EntityArrow;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EntitySkeleton;
import net.minecraft.server.v1_7_R2.PathfinderGoalArrowAttack;
import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R2.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import br.com.gvs.mobs.Mobs;
import br.com.gvs.mobs.pathfinders.PathfinderGoalWalkToSpawn;
import br.com.gvs.mobs.util.Boss;

public class CustomSquit extends EntitySkeleton implements CustomBoss{

	private Boss boss;
	public CustomSquit(World world, Boss boss) {
		super(world);
		this.boss = boss;
		this.resetAI();
		this.setAI();
		a(0.6F, 1.8F);
		getNavigation().b(true);
	
	}
	@Override
	public Boss getBoss() {
		return boss;
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
		this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
		this.goalSelector.a(1, new PathfinderGoalArrowAttack(this, 1.0D, 20, 60, 15.0F));
		this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 0, true));
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
	
	public void die(DamageSource damagesource){
		super.die(damagesource);
		this.dead = true;
		if (((damagesource.i() instanceof EntityArrow)) && ((damagesource.getEntity() instanceof EntityHuman))){
			EntityHuman entityhuman = (EntityHuman)damagesource.getEntity();
			double d0 = entityhuman.locX - this.locX;
			double d1 = entityhuman.locZ - this.locZ;
			if (d0 * d0 + d1 * d1 >= 2500.0D) {
				entityhuman.a(AchievementList.v);
			}
		}
	}

	@Override
	protected String aS(){
		return "mob.skeleton.hurt";
	}

	private void boneExplosion(){
		new BukkitRunnable(){
			public void run(){
				if(dead){
					cancel();
				}
				final ArrayList<Item> ossos = new ArrayList<>();
				final Location location = getBukkitEntity().getLocation();
				location.setY(location.getY() + 0.5);
				for(float x = -1.0F; x <= 1.0F; x += 1.0F){
					for(float y = -1.0F; y <= 1.0F; y += 1.0F){
						for(float z = -1.0F; z <= 1.0F; z += 1.0F){
							location.setDirection(new Vector(x, y, z));
							Item osso = getBukkitEntity().getWorld().dropItem(location, new org.bukkit.inventory.ItemStack(Material.BONE));
							osso.setVelocity(location.getDirection());
							osso.setPickupDelay(90);
							ossos.add(osso);
						}
					}
				}

				Location location2 = getBukkitEntity().getLocation();
				for(Entity en : getBukkitEntity().getNearbyEntities(6.0D, 6.0D, 6.0D)){
					if((!(en instanceof Item)) && ((en instanceof LivingEntity))){
						location2.setDirection(getBukkitEntity().getLocation().subtract(en.getLocation()).toVector());
						en.setVelocity(location2.getDirection().multiply(-2.5F));
						((LivingEntity) en).damage(6.0D, getBukkitEntity());
					}
				}

				new BukkitRunnable(){
					public void run(){
						for(Item item : ossos){
							if(item != null){
								item.remove();
							}
						}
						cancel();
					}
				}.runTaskLater(Mobs.getInstance(), 40L);
			}
		}.runTaskTimer(Mobs.getInstance(), 20, 20 * 20);
	}
	
	boolean exBone = false;

	public void a(EntityLiving entityliving, float f){
		EntityArrow entityarrow = new EntityArrow(this.world, this, entityliving, 1.6F, 14 - this.world.difficulty.a() * 4);
		int i = 3;
		int j = 1;

		entityarrow.b(f * 2.0F + this.random.nextGaussian() * 0.25D + this.world.difficulty.a() * 0.11F);
		if (i > 0) {
			entityarrow.b(entityarrow.e() + i * 0.5D + 0.5D);
		}
		if (j > 0) {
			entityarrow.setKnockbackStrength(j);
		}
		entityarrow.setOnFire(100);
		EntityShootBowEvent event = CraftEventFactory.callEntityShootBowEvent(this, bd(), entityarrow, 0.8F);
		if (event.isCancelled()){
			event.getProjectile().remove();
			return;
		}
		if (event.getProjectile() == entityarrow.getBukkitEntity()) {
			this.world.addEntity(entityarrow);
		}
		makeSound("random.bow", 1.0F, 1.0F / (aH().nextFloat() * 0.4F + 0.8F));
		if(!exBone){
			this.boneExplosion();
			exBone = true;
		}
	}

}
