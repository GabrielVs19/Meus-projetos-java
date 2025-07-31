package br.com.gvs.mobs.mobs;


import java.lang.reflect.Field;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;

import br.com.gvs.mobs.managers.MobManager;
import br.com.gvs.mobs.util.AttackType;
import br.com.gvs.mobs.util.Mob;
import net.minecraft.server.v1_7_R2.EntityChicken;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityInsentient;
import net.minecraft.server.v1_7_R2.EntityZombie;
import net.minecraft.server.v1_7_R2.ItemStack;
import net.minecraft.server.v1_7_R2.MathHelper;
import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R2.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_7_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.World;


public class CustomZombie extends EntityZombie implements CustomMob
{


	private Mob mob;

	public CustomZombie(World world, Mob mob)
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
		getNavigation().b(true);
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

		if(getMob().getLevel() == 5)
		{
			this.setEquipment(0, CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(Material.DIAMOND_SWORD)));
		}
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
	public void e()
	{
		if((this.world.w()) && (!this.world.isStatic) && (!isBaby()))
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
		if((al()) && (getGoalTarget() != null) && ((this.vehicle instanceof EntityChicken)))
		{
			((EntityInsentient) this.vehicle).getNavigation().a(getNavigation().e(), 1.5D);
		}
		super.e();
	}

	@Override
	public void die()
	{
		this.dead = true;

		if(this.isBaby())
		{
			return;
		}
		if(this.mob.getLevel() >= 4){
			CustomZombie cz = new CustomZombie(this.world, this.mob);
			cz.setBaby(true);
			MobManager.spawnMob(cz, this.getBukkitEntity().getLocation());
		}
	}

}
