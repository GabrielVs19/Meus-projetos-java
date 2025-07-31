package br.com.gvs.entityutil;

import java.util.UUID;

import net.minecraft.server.v1_7_R2.EntityCreature;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.NBTTagCompound;
import net.minecraft.server.v1_7_R2.NBTTagDouble;
import net.minecraft.server.v1_7_R2.NBTTagInt;
import net.minecraft.server.v1_7_R2.NBTTagList;
import net.minecraft.server.v1_7_R2.NBTTagLong;
import net.minecraft.server.v1_7_R2.NBTTagString;

import org.bukkit.craftbukkit.v1_7_R2.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityUtil extends JavaPlugin{
	
	public void onEnable(){
		System.out.println("ItemStackUtil enable - Author iBieel - v0.1");
	}


	public static void walkTo(Entity e, double x, double y, double z, double velocity){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		entity.getNavigation().a(x, y, z, velocity);
	}

	public static void jump(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		entity.getControllerJump().a();
	}

	public static void setTarget(Entity e, Entity target){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		entity.setTarget(((CraftEntity)target).getHandle());
	}

	public static void setLeashHolder(Entity e, Entity leashHolder){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		entity.setLeashHolder(((CraftEntity)leashHolder).getHandle(), true);
	}

	public static void setCustomName(Entity e, String name){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		entity.setCustomName(name);
		entity.setCustomNameVisible(true);
	}

	public static String getCustomName(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		return entity.getCustomName();
	}

	public static boolean hasCustomName(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		return entity.hasCustomName();
	}

	public static void setEquipament(Entity e, int equipamentSlot, ItemStack equipament){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		entity.setEquipment(equipamentSlot, CraftItemStack.asNMSCopy(equipament));
	}

	public static void setCanPickUpItem(Entity e, boolean b){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		entity.canPickUpLoot = b;
	}

	public static boolean fromMobSpawner(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		return entity.fromMobSpawner;
	}

	public static boolean inWater(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		return entity.inWater;
	}

	public static boolean inBlock(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		return entity.inBlock();
	}

	public static boolean isOnGround(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		return entity.onGround;
	}

	public static double getLastDamage(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		return entity.lastDamage;
	}

	public static Entity getLastDamager(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		return entity.getLastDamager().getBukkitEntity();
	}

	public static void setNoDamageTicks(Entity e, int ticks){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		entity.noDamageTicks = ticks;
	}

	public static void setMaxAirTicks(Entity e, int ticks){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		entity.maxAirTicks = ticks;
	}

	public static void setPersistent(Entity e, boolean b){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		entity.persistent = b;
	}

	public static void collide(Entity e, Entity collide){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		entity.collide(((CraftEntity)collide).getHandle());
	}

	public static void enderTeleport(Entity e, double x, double y, double z){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		entity.enderTeleportTo(x, y, z);
	}
	
	public static void setInvisible(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		entity.setInvisible(true);
	}
	
	public static boolean isInvisible(Entity e){
		EntityCreature entity = ((CraftCreature)e).getHandle();
		return entity.isInvisible();
	}

	public static void setNoAI(Entity e, boolean noAI){
		net.minecraft.server.v1_7_R2.Entity nms = ((CraftEntity)e).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		nms.c(tag);
		tag.setBoolean("NoAI", noAI);
		EntityLiving el = (EntityLiving) nms;
		el.a(tag);
	}
	
	public static void setNoGravity(Entity e, boolean noGravity){
		net.minecraft.server.v1_7_R2.Entity nms = ((CraftEntity)e).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		nms.c(tag);
		tag.setBoolean("NoGravity", noGravity);
		EntityLiving el = (EntityLiving) nms;
		el.a(tag);
	}

	public static void setSilent(Entity e, boolean silent){
		net.minecraft.server.v1_7_R2.Entity nms = ((CraftEntity)e).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		nms.c(tag);
		tag.setBoolean("Silent", silent);
		EntityLiving el = (EntityLiving) nms;
		el.a(tag);
	}
	
	public static void setInvunerable(Entity e, boolean invunerable){
		net.minecraft.server.v1_7_R2.Entity nms = ((CraftEntity)e).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		nms.c(tag);
		tag.setBoolean("Invulnerable", invunerable);
		EntityLiving el = (EntityLiving) nms;
		el.a(tag);
	}
	
	public static void setAttackDamage(Entity e, double damageValue){
		net.minecraft.server.v1_7_R2.Entity nms = ((CraftEntity)e).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		nms.c(tag);
		NBTTagList modifiers = new NBTTagList();
		NBTTagCompound damage = new NBTTagCompound();
		damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
		damage.set("Name", new NBTTagString("attackDamage"));
		damage.set("Amount", new NBTTagDouble(damageValue));
		damage.set("Operation", new NBTTagInt(0));
		UUID randUUID = UUID.randomUUID();
		damage.set("UUIDLeast", new NBTTagLong(randUUID.getLeastSignificantBits()));
		damage.set("UUIDMost", new NBTTagLong(randUUID.getMostSignificantBits()));
		modifiers.add(damage);
		tag.set("AttributeModifiers", modifiers);
		EntityLiving el = (EntityLiving) nms;
		el.a(tag);
	}
}
