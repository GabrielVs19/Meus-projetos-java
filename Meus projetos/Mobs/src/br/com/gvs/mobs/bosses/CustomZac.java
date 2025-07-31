package br.com.gvs.mobs.bosses;


import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntitySlime;
import net.minecraft.server.v1_7_R2.GenericAttributes;
import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.gvs.mobs.pathfinders.PathfinderGoalWalkToSpawn;
import br.com.gvs.mobs.util.Boss;


public class CustomZac extends EntitySlime implements CustomBoss
{
	
	
	private Boss boss;
	
	public CustomZac(World world, Boss boss)
	{
		super(world);
		this.boss = boss;
		setSize(15);
		this.resetAI();
		this.setAI();
		
		bb().b(GenericAttributes.e);
	}
	
	@Override
	public Boss getBoss()
	{
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
	
	public void die()
	{
		this.world.createExplosion(this, this.locX, this.locY, this.locZ, 4.0F, false, false);
		for(org.bukkit.entity.Entity e : this.getBukkitEntity().getNearbyEntities(7.5, 7.5, 7.5))
		{
			if(e instanceof LivingEntity)
			{
				((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 10, 0));
				((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 60, 0));
			}
		}
		super.die();
	}
	
}
