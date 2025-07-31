package ibieel.minigames.com;

import ibieel.minigames.com.CustomMobs.CustomCreeper;
import ibieel.minigames.com.CustomMobs.CustomSkeleton;
import ibieel.minigames.com.CustomMobs.CustomSpider;
import ibieel.minigames.com.CustomMobs.CustomZombie;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
import net.minecraft.server.v1_7_R2.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.minecraft.server.v1_7_R2.ItemStack;

public class Mobs {

	public static void zombies(Location loc, int vida, Entity target)  {
		CustomZombie mob = new CustomZombie(((CraftWorld)loc.getWorld()).getHandle());
		mob.setHealth(vida);
		mob.setTarget(target);
		mob.setBaby(false);
		mob.setCustomName("Zombie");
		mob.setCustomNameVisible(true);
		mob.setPosition(loc.getX(), loc.getY(), loc.getZ());
		((CraftWorld) loc.getWorld()).getHandle().addEntity(mob, SpawnReason.CUSTOM);
	}

	public static void zombies(Location loc, int vida, Entity target, ItemStack item)  {
		CustomZombie mob = new CustomZombie(((CraftWorld)loc.getWorld()).getHandle());
		mob.setHealth(vida);
		mob.setTarget(target);
		mob.setBaby(false);
		mob.setCustomName("Zombie");
		mob.setEquipment(0, item);
		mob.setCustomNameVisible(true);
		mob.setPosition(loc.getX(), loc.getY(), loc.getZ());
		((CraftWorld) loc.getWorld()).getHandle().addEntity(mob, SpawnReason.CUSTOM);
	}

	public static void zombies(Location loc, int vida, Entity target, ItemStack item, ItemStack capacete, ItemStack peitoral, ItemStack calca, ItemStack bota)  {
		CustomZombie mob = new CustomZombie(((CraftWorld)loc.getWorld()).getHandle());
		mob.setHealth(vida);
		mob.setTarget(target);
		mob.setBaby(false);
		mob.setCustomName("Zombie");
		mob.setEquipment(0, item);
		mob.setEquipment(1, bota);
		mob.setEquipment(2, calca);
		mob.setEquipment(3, peitoral);
		mob.setEquipment(4, capacete);
		mob.setCustomNameVisible(true);
		mob.setPosition(loc.getX(), loc.getY(), loc.getZ());
		((CraftWorld) loc.getWorld()).getHandle().addEntity(mob, SpawnReason.CUSTOM);
	}


	@SuppressWarnings("deprecation")
	public static void pigZombie(Location loc){
		PigZombie pigzombie = (PigZombie) loc.getWorld().spawnCreature(loc, EntityType.PIG_ZOMBIE);
		pigzombie.setAngry(true);
		pigzombie.setAnger(1);
	}

	public static void creepers(Location loc, int vida, Entity target)  {
		CustomCreeper mob = new CustomCreeper(((CraftWorld)loc.getWorld()).getHandle());
		mob.setHealth(vida);
		mob.setTarget(target);
		mob.setCustomName("Creeper");
		mob.setCustomNameVisible(true);
		mob.setPosition(loc.getX(), loc.getY(), loc.getZ());
		((CraftWorld) loc.getWorld()).getHandle().addEntity(mob, SpawnReason.CUSTOM);
	}

	public static void creepersCharge(Location loc, int vida, Entity target)  {
		CustomCreeper mob = new CustomCreeper(((CraftWorld)loc.getWorld()).getHandle());
		mob.setHealth(vida);
		mob.setTarget(target);
		mob.setCustomName("Creeper");
		mob.setCustomNameVisible(true);
		mob.setPosition(loc.getX(), loc.getY(), loc.getZ());
		((CraftWorld) loc.getWorld()).getHandle().addEntity(mob, SpawnReason.CUSTOM);
	}

	public static void spiders(Location loc, int vida, Entity target)  {
		CustomSpider mob = new CustomSpider(((CraftWorld)loc.getWorld()).getHandle());
		mob.setHealth(vida);
		mob.setTarget(target);
		mob.setCustomName("Aranha");
		mob.setCustomNameVisible(true);
		mob.setPosition(loc.getX(), loc.getY(), loc.getZ());
		((CraftWorld) loc.getWorld()).getHandle().addEntity(mob, SpawnReason.CUSTOM);
	}

	public static void skeletons(Location loc, int vida, Entity target)  {
		CustomSkeleton mob = new CustomSkeleton(((CraftWorld)loc.getWorld()).getHandle());
		mob.setHealth(vida);
		mob.setTarget(target);
		mob.setCustomName("Esqueleto");
		mob.setCustomNameVisible(true);
		mob.setPosition(loc.getX(), loc.getY(), loc.getZ());
		((CraftWorld) loc.getWorld()).getHandle().addEntity(mob, SpawnReason.CUSTOM);
	}

	public static void skeletons(Location loc, int vida, Entity target, ItemStack item) {
		CustomSkeleton mob = new CustomSkeleton(((CraftWorld)loc.getWorld()).getHandle());
		mob.setHealth(vida);
		mob.setTarget(target);
		mob.setCustomName("Esqueleto");
		mob.setEquipment(0, item);
		mob.setCustomNameVisible(true);
		mob.setPosition(loc.getX(), loc.getY(), loc.getZ());
		((CraftWorld) loc.getWorld()).getHandle().addEntity(mob, SpawnReason.CUSTOM);
	}

	public static void skeletons(Location loc, int vida, Entity target, ItemStack item, ItemStack capacete, ItemStack peitoral, ItemStack calca, ItemStack bota)  {
		CustomSkeleton mob = new CustomSkeleton(((CraftWorld)loc.getWorld()).getHandle());
		mob.setHealth(vida);
		mob.setTarget(target);
		mob.setCustomName("Esqueleto");
		mob.setEquipment(0, item);
		mob.setEquipment(1, bota);
		mob.setEquipment(2, calca);
		mob.setEquipment(3, peitoral);
		mob.setEquipment(4, capacete);
		mob.setCustomNameVisible(true);
		mob.setPosition(loc.getX(), loc.getY(), loc.getZ());
		((CraftWorld) loc.getWorld()).getHandle().addEntity(mob, SpawnReason.CUSTOM);
	}
}
