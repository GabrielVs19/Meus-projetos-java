package br.com.gvs.mobs.util;

import net.minecraft.server.v1_7_R2.AttributeInstance;
import net.minecraft.server.v1_7_R2.DamageSource;
import net.minecraft.server.v1_7_R2.EnchantmentManager;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.GenericAttributes;
import net.minecraft.server.v1_7_R2.MobEffectList;

public class NMS {

	public static void attack(Entity entity, Entity target) {
		AttributeInstance attribute = ((EntityLiving) entity).getAttributeInstance(GenericAttributes.e);
		float damage = (float) (attribute == null ? 1.0D : attribute.getValue());
		if (((EntityLiving) entity).hasEffect(MobEffectList.INCREASE_DAMAGE)) {
			damage += (3 << ((EntityLiving) entity).getEffect(MobEffectList.INCREASE_DAMAGE).getAmplifier());
		}
		if (((EntityLiving) entity).hasEffect(MobEffectList.WEAKNESS)) {
			damage -= (2 << ((EntityLiving) entity).getEffect(MobEffectList.WEAKNESS).getAmplifier());
		}
		int knockbackLevel = 0;
		if ((target instanceof EntityLiving)) {
			damage += EnchantmentManager.a((EntityLiving) entity, (EntityLiving) target);
			knockbackLevel += EnchantmentManager.getKnockbackEnchantmentLevel((EntityLiving) entity, (EntityLiving) target);
		}
		boolean success = target.damageEntity(DamageSource.mobAttack((EntityLiving) entity), damage);
		if (!success) {
			return;
		}
		if (knockbackLevel > 0) {
			target.g(-Math.sin(entity.yaw * 3.141592653589793D / 180.0D) * knockbackLevel * 0.5D, 0.1D, Math.cos(entity.yaw * 3.141592653589793D / 180.0D) * knockbackLevel * 0.5D);
			entity.motX *= 0.6D;
			entity.motZ *= 0.6D;
		}
		int fireAspectLevel = EnchantmentManager.getFireAspectEnchantmentLevel((EntityLiving) entity);
		if (fireAspectLevel > 0) {
			target.setOnFire(fireAspectLevel * 4);
		}
	}

}
