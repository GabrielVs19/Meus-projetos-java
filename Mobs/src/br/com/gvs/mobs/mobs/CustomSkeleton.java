package br.com.gvs.mobs.mobs;


import java.lang.reflect.Field;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R2.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import br.com.gvs.mobs.Mobs;
import br.com.gvs.mobs.util.AttackType;
import br.com.gvs.mobs.util.Mob;
import net.minecraft.server.v1_7_R2.AchievementList;
import net.minecraft.server.v1_7_R2.DamageSource;
import net.minecraft.server.v1_7_R2.Enchantment;
import net.minecraft.server.v1_7_R2.EnchantmentManager;
import net.minecraft.server.v1_7_R2.EntityArrow;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.EntitySkeleton;
import net.minecraft.server.v1_7_R2.GenericAttributes;
import net.minecraft.server.v1_7_R2.ItemStack;
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


public class CustomSkeleton extends EntitySkeleton implements CustomMob
{
	
	
	private Mob mob;
	
	public CustomSkeleton(World world, Mob mob)
	{
		super(world);
		this.mob = mob;
		try
		{
			Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
			bField.setAccessible(true);
			Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
			cField.setAccessible(true);
			bField.set(this.goalSelector, new UnsafeList<Object>());
			bField.set(this.targetSelector, new UnsafeList<Object>());
			cField.set(this.goalSelector, new UnsafeList<Object>());
			cField.set(this.targetSelector, new UnsafeList<Object>());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		a(0.6F, 1.8F);
		getNavigation().b(true);
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
		if(this.mob.getAttackType() != AttackType.PASSIVE)
		{
			this.goalSelector.a(1, new PathfinderGoalArrowAttack(this, 1.0D, 20, 60, 15.0F));
			this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
			if(this.mob.getAttackType() == AttackType.HOSTILE)
			{
				this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 0, true));
			}
		}
		this.setEquipment(0, CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(Material.BOW)));
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
	
	// Explosao de ossos
	@Override
	public void die(DamageSource damagesource)
	{
		super.die(damagesource);
		if(((damagesource.i() instanceof EntityArrow)) && ((damagesource.getEntity() instanceof EntityHuman)))
		{
			EntityHuman entityhuman = (EntityHuman) damagesource.getEntity();
			double d0 = entityhuman.locX - this.locX;
			double d1 = entityhuman.locZ - this.locZ;
			if(d0 * d0 + d1 * d1 >= 2500.0D)
			{
				entityhuman.a(AchievementList.v);
			}
			
			// explosao
			if(this.mob.getLevel() >= 4)
			{
				final ArrayList<Item> ossos = new ArrayList<>();
				final Location location = this.getBukkitEntity().getLocation();
				location.setY(location.getY() + 0.5);
				for(float x = -1.0F; x <= 1.0F; x += 1.0F)
				{
					for(float y = -1.0F; y <= 1.0F; y += 1.0F)
					{
						for(float z = -1.0F; z <= 1.0F; z += 1.0F)
						{
							location.setDirection(new Vector(x, y, z));
							Item osso = this.getBukkitEntity().getWorld().dropItem(location, new org.bukkit.inventory.ItemStack(Material.BONE));
							osso.setVelocity(location.getDirection());
							osso.setPickupDelay(90);
							ossos.add(osso);
						}
					}
				}
				Location location2 = this.getBukkitEntity().getLocation();
				for(Entity en : this.getBukkitEntity().getNearbyEntities(5.0D, 5.0D, 5.0D))
				{
					if((!(en instanceof Item)) && ((en instanceof LivingEntity)))
					{
						location2.setDirection(this.getBukkitEntity().getLocation().subtract(en.getLocation()).toVector());
						en.setVelocity(location2.getDirection().multiply(-2.4F));
						((LivingEntity) en).damage(4.0D);
					}
				}
				new BukkitRunnable()
				{
					
					
					public void run()
					{
						for(Item item : ossos)
						{
							if(item != null)
							{
								item.remove();
							}
						}
						cancel();
					}
				}.runTaskLater(Mobs.getInstance(), 40L);
			}
			// explosao
		}
	}
	
	// Altera a velocidade ao ser atacado
	@Override
	protected String aS()
	{
		double speed = 0.26;
		if(getAttributeInstance(GenericAttributes.d).getValue() != speed && getAttributeInstance(GenericAttributes.d).getValue() < speed)
		{
			getAttributeInstance(GenericAttributes.d).setValue(speed);
		}
		return "mob.skeleton.hurt";
	}
	
	@Override
	public void e()
	{
		if((this.world.w()) && (!this.world.isStatic))
		{
			float f = d(1.0F);
			if((f > 0.5F) && (this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) && (this.world.i(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ))))
			{
				ItemStack itemstack = getEquipment(4);
				if(itemstack != null)
				{
					if(itemstack.g())
					{
						itemstack.setData(itemstack.j() + this.random.nextInt(2));
						if(itemstack.j() >= itemstack.l())
						{
							a(itemstack);
							setEquipment(4, (ItemStack) null);
						}
					}
				}
			}
		}
		if((this.world.isStatic) && (getSkeletonType() == 1))
		{
			a(0.72F, 2.34F);
		}
		super.e();
	}
	
	@Override
	public void a(EntityLiving entityliving, float f)
	{
		EntityArrow entityarrow = new EntityArrow(this.world, this, entityliving, 1.6F, 14 - this.world.difficulty.a() * 4);
		int i = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_DAMAGE.id, bd());
		// Knockback
		int j = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK.id, bd());
		if(this.mob.getLevel() >= 2 && j <= 0)
		{
			int random = (int) (Math.random() * 99) + 1;
			if(random <= 50)
			{
				j = 2;
			}
		}
		
		entityarrow.b(f * 2.0F + this.random.nextGaussian() * 0.25D + this.world.difficulty.a() * 0.11F);
		if(i > 0)
		{
			entityarrow.b(entityarrow.e() + i * 0.5D + 0.5D);
		}
		if(j > 0)
		{
			entityarrow.setKnockbackStrength(j);
		}
		// fire
		if((EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_FIRE.id, bd()) > 0) || (getSkeletonType() == 1))
		{
			entityarrow.setOnFire(100);
		}
		else
			if(this.mob.getLevel() >= 3)
			{
				int random = (int) (Math.random() * 99) + 1;
				if(random <= 50)
				{
					entityarrow.setOnFire(100);
				}
			}
		EntityShootBowEvent event = CraftEventFactory.callEntityShootBowEvent(this, bd(), entityarrow, 0.8F);
		if(event.isCancelled())
		{
			event.getProjectile().remove();
			return;
		}
		if(event.getProjectile() == entityarrow.getBukkitEntity())
		{
			this.world.addEntity(entityarrow);
		}
		makeSound("random.bow", 1.0F, 1.0F / (aH().nextFloat() * 0.4F + 0.8F));
	}
	
}
