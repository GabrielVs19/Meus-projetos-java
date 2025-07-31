package br.com.gvs.mobs.bosses;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R2.DamageSource;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityGiantZombie;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EnumDifficulty;
import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;

import br.com.gvs.mobs.managers.MobManager;
import br.com.gvs.mobs.pathfinders.PathfinderGoalWalkToSpawn;
import br.com.gvs.mobs.util.Boss;
import br.com.gvs.mobs.util.Mob;
import br.com.gvs.mobs.util.MobType;


public class CustomMvP extends EntityGiantZombie implements CustomBoss
{


	private Boss boss;
	private int spawnZombiesTicks = (20 * 60) * 3;

	public CustomMvP(World arg0, Boss boss)
	{
		super(arg0);
		this.boss = boss;
		this.resetAI();
		this.setAI();

		this.height *= 6.0F;
		a(this.width * 6.0F, this.length * 6.0F);
	}

	@Override
	public Boss getBoss()
	{
		return this.boss;
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
	public void setAI() {
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
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

	public void h()
	{
		this.spawnZombiesTicks--;
		if((!this.world.isStatic) && (this.world.difficulty == EnumDifficulty.PEACEFUL))
		{
			die();
		}
		super.h();
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f){
		super.damageEntity(damagesource, f);
		if(this.spawnZombiesTicks <= 0)
		{
			int random = (int) (Math.random() * 99) + 1;
			if(random <= 50)
			{
				this.spawnZombiesTicks = (20 * 60) * 3;
				for(int i = 0; i <= 3; i++)
				{
					Mob mob = MobManager.getMob(MobType.ZOMBIE, 5);
					try
					{
						Class<? extends Entity> mobClass = mob.getMobType().getMobClass();
						Constructor<? extends Entity> mobc = mobClass.getConstructor(World.class, Mob.class);
						MobManager.spawnMob((Entity) mobc.newInstance(this.world, mob), this.getBukkitEntity().getLocation());
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
			}
		}
		return true;
	}

	@Override
	public void setWalkToLocation(Location loc) {
		this.goalSelector.a(5, new PathfinderGoalWalkToSpawn(this, loc));
		
	}


}
