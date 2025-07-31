package br.com.gvs.mobs.bosses;


import java.lang.reflect.Field;

import net.minecraft.server.v1_7_R2.AttributeInstance;
import net.minecraft.server.v1_7_R2.DamageSource;
import net.minecraft.server.v1_7_R2.EnchantmentManager;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityHorse;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.GenericAttributes;
import net.minecraft.server.v1_7_R2.MobEffect;
import net.minecraft.server.v1_7_R2.MobEffectList;
import net.minecraft.server.v1_7_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_7_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_7_R2.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_7_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_7_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_7_R2.PathfinderGoalSelector;
import net.minecraft.server.v1_7_R2.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R2.util.UnsafeList;

import br.com.gvs.mobs.pathfinders.PathfinderGoalWalkToSpawn;
import br.com.gvs.mobs.util.Boss;


public class CustomShadowPelt extends EntityHorse implements CustomBoss
{


	private Boss boss;

	public CustomShadowPelt(World world, Boss boss)
	{
		super(world);
		this.boss = boss;
		setType(3);
		a(0.9F, 1.3F);
		getNavigation().a(true);
		getNavigation().b(true);
		this.W = 1.2F;
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
		this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
		this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.0D, false));
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
	public boolean damageEntity(DamageSource paramDamageSource, float paramFloat){
		if(isInvulnerable()){
			return false;
		}
		this.bo = 60;
		if(!bj()){
			AttributeInstance localAttributeInstance = getAttributeInstance(GenericAttributes.d);
			if(localAttributeInstance.a(h) == null){
				localAttributeInstance.a(i);
			}
		}
		boolean life = this.getHealth() <= ((this.getMaxHealth() / 100) * 25);
		double speed = 0.15;
		double speedOriginal = this.boss.getMoveSpeed();
		if(life){
			if(getAttributeInstance(GenericAttributes.d).getValue() != (speedOriginal + speed) && getAttributeInstance(GenericAttributes.d).getValue() < (speedOriginal + speed)){
				getAttributeInstance(GenericAttributes.d).setValue((speedOriginal + speed));
			}
		}else{
			if(getAttributeInstance(GenericAttributes.d).getValue() != this.boss.getMoveSpeed()){
				getAttributeInstance(GenericAttributes.d).setValue(this.boss.getMoveSpeed());
			}
		}
		this.target = null;
		return super.damageEntity(paramDamageSource, paramFloat);
	}

	@Override
	public boolean n(Entity target){
		AttributeInstance attribute = ((EntityLiving) this).getAttributeInstance(GenericAttributes.e);
		float damage = (float) (attribute == null ? 1.0D : attribute.getValue());
		if (((EntityLiving) this).hasEffect(MobEffectList.INCREASE_DAMAGE)) {
			damage += (3 << ((EntityLiving) this).getEffect(MobEffectList.INCREASE_DAMAGE).getAmplifier());
		}
		if (((EntityLiving) this).hasEffect(MobEffectList.WEAKNESS)) {
			damage -= (2 << ((EntityLiving) this).getEffect(MobEffectList.WEAKNESS).getAmplifier());
		}
		int knockbackLevel = 2;
		boolean success = target.damageEntity(DamageSource.mobAttack((EntityLiving) this), damage);
		if (!success) {
			return false;
		}
		if (knockbackLevel > 0) {
			target.g(-Math.sin(this.yaw * 3.141592653589793D / 180.0D) * knockbackLevel * 0.5D, 0.1D, Math.cos(this.yaw * 3.141592653589793D / 180.0D) * knockbackLevel * 0.5D);
			this.motX *= 0.6D;
			this.motZ *= 0.6D;
		}
		int fireAspectLevel = EnchantmentManager.getFireAspectEnchantmentLevel((EntityLiving) this);
		if (fireAspectLevel > 0) {
			target.setOnFire(fireAspectLevel * 4);
		}

		int random = (int) (Math.random() * 99) + 1;
		if(random <= 50){
			if(target instanceof EntityLiving){
				((EntityLiving) target).addEffect(new MobEffect(MobEffectList.SLOWER_MOVEMENT.id, 7 * 20, 1));
			}
		}

		return true;
	}

}
