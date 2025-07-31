package br.com.gvs.entityutil;

import net.minecraft.server.v1_7_R2.EntityCreature;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.GenericAttributes;

import org.bukkit.craftbukkit.v1_7_R2.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityUtil extends JavaPlugin{

	public void onEnable(){
		System.out.println("EntityUtil enable - Author iBieel - v0.1");
	}

	public static void walkTo(Entity e, double x, double y, double z, double velocity){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return;
		}
		entity.getNavigation().a(x, y, z, velocity);
	}

	public static void jump(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return;
		}
		entity.getControllerJump().a();
	}

	public static void setTarget(Entity e, Entity target){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return;
		}
		entity.setTarget(((CraftEntity)target).getHandle());
	}
	
	public static Entity getTarget(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return null;
		}
		return entity.target.getBukkitEntity();
	}

	public static void setLeashHolder(Entity e, Entity leashHolder){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return;
		}
		entity.setLeashHolder(((CraftEntity)leashHolder).getHandle(), true);
	}

	public static void setCustomName(Entity e, String name){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return;
		}
		entity.setCustomName(name);
		entity.setCustomNameVisible(true);
	}

	public static String getCustomName(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return "";
		}
		return entity.getCustomName();
	}

	public static boolean hasCustomName(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return false;
		}
		return entity.hasCustomName();
	}

	public static void setEquipament(Entity e, int equipamentSlot, ItemStack equipament){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return;
		}
		entity.setEquipment(equipamentSlot, CraftItemStack.asNMSCopy(equipament));
	}

	public static void setCanPickUpItem(Entity e, boolean b){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return;
		}
		entity.canPickUpLoot = b;
	}

	public static boolean fromMobSpawner(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return false;
		}
		return entity.fromMobSpawner;
	}

	public static boolean inWater(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return false;
		}
		return entity.inWater;
	}

	public static boolean inBlock(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return false;
		}
		return entity.inBlock();
	}

	public static boolean isOnGround(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return false;
		}
		return entity.onGround;
	}

	public static double getLastDamage(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return 0.0;
		}
		return entity.lastDamage;
	}

	public static Entity getLastDamager(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return null;
		}
		return entity.getLastDamager().getBukkitEntity();
	}

	public static void setNoDamageTicks(Entity e, int ticks){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return;
		}
		entity.noDamageTicks = ticks;
	}

	public static void setMaxAirTicks(Entity e, int ticks){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return;
		}
		entity.maxAirTicks = ticks;
	}

	public static void setPersistent(Entity e, boolean b){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return;
		}
		entity.persistent = b;
	}

	public static void collide(Entity e, Entity collide){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return;
		}
		entity.collide(((CraftEntity)collide).getHandle());
	}

	public static void enderTeleport(Entity e, double x, double y, double z){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return;
		}
		entity.enderTeleportTo(x, y, z);
	}

	public static void setInvisible(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return;
		}
		entity.setInvisible(true);
	}

	public static boolean isInvisible(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		if (entity == null) {
			return false;
		}
		return entity.isInvisible();
	}

	public static void setAttackDamage(Entity e, double damageValue){
		net.minecraft.server.v1_7_R2.Entity nms = ((CraftEntity)e).getHandle();
		if (nms == null) {
			return;
		}
		if(!(nms instanceof EntityLiving)){
			return;
		}
		((EntityLiving) nms).getAttributeInstance(GenericAttributes.e).setValue(damageValue);
	}
	
	public static double getAttackDamage(Entity e){
		net.minecraft.server.v1_7_R2.Entity nms = ((CraftEntity)e).getHandle();
		if (nms == null) {
			return 0.0;
		}
		if(!(nms instanceof EntityLiving)){
			return 0.0;
		}
		return ((EntityLiving) nms).getAttributeInstance(GenericAttributes.e).getValue();
	}

	public static void setFollowRange(Entity e, double range){
		net.minecraft.server.v1_7_R2.Entity nms = ((CraftEntity)e).getHandle();
		if (nms == null) {
			return;
		}
		if(!(nms instanceof EntityLiving)){
			return;
		}
		((EntityLiving) nms).getAttributeInstance(GenericAttributes.b).setValue(range);
	}
	
	public static double getFollowRange(Entity e){
		net.minecraft.server.v1_7_R2.Entity nms = ((CraftEntity)e).getHandle();
		if (nms == null) {
			return 0.0;
		}
		if(!(nms instanceof EntityLiving)){
			return 0.0;
		}
		return ((EntityLiving) nms).getAttributeInstance(GenericAttributes.b).getValue();
	}

	public static void setKnockbackResistence(Entity e, double knock){
		net.minecraft.server.v1_7_R2.Entity nms = ((CraftEntity)e).getHandle();
		if (nms == null) {
			return;
		}
		if(!(nms instanceof EntityLiving)){
			return;
		}
		((EntityLiving) nms).getAttributeInstance(GenericAttributes.c).setValue(knock);
	}
	
	public static double getKnockbackResistence(Entity e){
		net.minecraft.server.v1_7_R2.Entity nms = ((CraftEntity)e).getHandle();
		if (nms == null) {
			return 0.0;
		}
		if(!(nms instanceof EntityLiving)){
			return 0.0;
		}
		return ((EntityLiving) nms).getAttributeInstance(GenericAttributes.c).getValue();
	}

	public static void setMovementSpeed(Entity e, double speed){
		net.minecraft.server.v1_7_R2.Entity nms = ((CraftEntity)e).getHandle();
		if (nms == null) {
			return;
		}
		if(!(nms instanceof EntityLiving)){
			return;
		}
		((EntityLiving) nms).getAttributeInstance(GenericAttributes.d).setValue(speed);
	}
	
	public static double getMovementSpeed(Entity e){
		net.minecraft.server.v1_7_R2.Entity nms = ((CraftEntity)e).getHandle();
		if (nms == null) {
			return 0.0;
		}
		if(!(nms instanceof EntityLiving)){
			return 0.0;
		}
		return ((EntityLiving) nms).getAttributeInstance(GenericAttributes.d).getValue();
	}

	public static void look(Entity e, float yaw, float pitch){
		net.minecraft.server.v1_7_R2.Entity nms = ((CraftEntity)e).getHandle();
		if (nms == null) {
			return;
		}
		nms.yaw = yaw;
		nms.pitch = pitch;
	}
}

