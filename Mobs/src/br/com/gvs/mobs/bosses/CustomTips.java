package br.com.gvs.mobs.bosses;

import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityPig;
import net.minecraft.server.v1_7_R2.GenericAttributes;
import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.gvs.mobs.pathfinders.PathfinderGoalMeleeAttackB;
import br.com.gvs.mobs.pathfinders.PathfinderGoalWalkToSpawn;
import br.com.gvs.mobs.util.Boss;

public class CustomTips extends EntityPig implements CustomBoss{

	private Boss boss;
	public CustomTips(World world, Boss boss) {
		super(world);
		this.boss = boss;
		this.resetAI();
		this.setAI();
		a(0.9F, 0.9F);
		getNavigation().a(true);
		getNavigation().b(true);
		this.W = 1.0F;
		
		bb().b(GenericAttributes.e);
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
		this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
		this.goalSelector.a(1, new PathfinderGoalMeleeAttackB(this, EntityHuman.class, 1.0D, false));
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
	
	@Override
	protected String aT(){
		this.world.createExplosion(this, this.locX, this.locY, this.locZ, 1.5F, false, false);
		for(org.bukkit.entity.Entity e : this.getBukkitEntity().getNearbyEntities(6, 6, 6)){
			if(e instanceof LivingEntity){
				((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 8, 1));
			}
		}
	    return "mob.pig.death";
	  }

}
