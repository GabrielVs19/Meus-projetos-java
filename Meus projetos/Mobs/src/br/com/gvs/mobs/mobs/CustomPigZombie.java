package br.com.gvs.mobs.mobs;


import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;

import br.com.gvs.mobs.managers.MobManager;
import br.com.gvs.mobs.util.AttackType;
import br.com.gvs.mobs.util.Mob;
import br.com.gvs.mobs.util.MobType;
import net.minecraft.server.v1_7_R2.DamageSource;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityPigZombie;
import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R2.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_7_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.World;


public class CustomPigZombie extends EntityPigZombie implements CustomMob
{
	
	
	private Mob mob;
	
	public CustomPigZombie(World world, Mob mob)
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
		this.fireProof = true;
		getNavigation().a(true);
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
		if(this.mob.getAttackType() != AttackType.PASSIVE)
		{
			this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0D, false));
			this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
			if(this.mob.getAttackType() == AttackType.HOSTILE)
			{
				this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 0, true));
			}
		}
		
		switch(getMob().getLevel())
		{
			case 1:
				this.setEquipment(0, CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(Material.STONE_SWORD)));
				break;
			case 2:
				this.setEquipment(0, CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(Material.STONE_SWORD)));
				break;
			case 3:
				this.setEquipment(0, CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(Material.GOLD_SWORD)));
				break;
			case 4:
				this.setEquipment(0, CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(Material.IRON_SWORD)));
				break;
			case 5:
				this.setEquipment(0, CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(Material.DIAMOND_SWORD)));
				break;
			
			default:
				break;
		}
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
	public void h()
	  {
	    super.h();
	  }
	
	@Override
	public Mob getMob()
	{
		return this.mob;
	}
	
	@Override
	public void die(){
		this.setEquipment(0, CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(Material.AIR)));
		super.die();
	}
	
	private boolean spawnBabys = false;
	
	@Override
	public boolean damageEntity(DamageSource damagesource, float f)
	{
		if(isInvulnerable())
		{
			return false;
		}
		
		if(!spawnBabys && !this.isBaby())
		{
			if(this.mob.getLevel() >= 4)
			{
					CustomPigZombie cz = new CustomPigZombie(this.world, MobManager.getMob(MobType.ZOMBIE, this.mob.getLevel()));
					cz.setBaby(true);
					cz.getMob().setMoveSpeed(cz.getMob().getMoveSpeed() / 2);
					cz.getMob().setAttackType(AttackType.HOSTILE);
					MobManager.spawnMob(cz, this.getBukkitEntity().getLocation());
					this.spawnBabys = true;
			}
			return super.damageEntity(damagesource, f);
		}
		
		Entity entity = damagesource.getEntity();
		if((entity instanceof EntityHuman))
		{
			@SuppressWarnings("unchecked")
			List<Entity> list = this.world.getEntities(this, this.boundingBox.grow(32.0D, 32.0D, 32.0D));
			for(int i = 0; i < list.size(); i++)
			{
				Entity entity1 = (Entity) list.get(i);
				if((entity1 instanceof EntityPigZombie))
				{
					CustomPigZombie entitypigzombie = (CustomPigZombie) entity1;
					entitypigzombie.c((EntityHuman) entity);
				}
			}
			c((EntityHuman) entity);
		}
		return super.damageEntity(damagesource, f);
	}
	
}
