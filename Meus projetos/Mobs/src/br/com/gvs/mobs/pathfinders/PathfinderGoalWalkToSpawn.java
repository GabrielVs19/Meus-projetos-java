package br.com.gvs.mobs.pathfinders;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityInsentient;
import net.minecraft.server.v1_7_R2.EntityLiving;
import net.minecraft.server.v1_7_R2.GenericAttributes;
import net.minecraft.server.v1_7_R2.Navigation;
import net.minecraft.server.v1_7_R2.PathEntity;
import net.minecraft.server.v1_7_R2.PathfinderGoal;

public class PathfinderGoalWalkToSpawn extends PathfinderGoal{

	private Location spawn;
	private Navigation navigation;
	private Entity entity;
	public PathfinderGoalWalkToSpawn(EntityInsentient entity, Location spawn){
		this.spawn = spawn;
		this.navigation = entity.getNavigation();
		this.entity = entity;
	}

	@Override
	public boolean a() {
		return true;
	}

	@Override
	public void e(){
		Location entityLoc = new Location(entity.getBukkitEntity().getWorld(), entity.locX, entity.locY, entity.locZ);
		int distance = (int) entityLoc.distance(spawn);
		if(distance >= 20 && distance < 100){
			PathEntity pathEntity = this.navigation.a(spawn.getX(), spawn.getY(), spawn.getZ());
			this.navigation.a(pathEntity, ((EntityLiving) entity).getAttributeInstance(GenericAttributes.d).getValue() + 2.1);
		}else if(distance >= 100){
			for(org.bukkit.entity.Entity e : entity.getBukkitEntity().getNearbyEntities(30, 30, 30)){
				if(e.getType() == EntityType.PLAYER){
					((Player)e).sendMessage("§6[Boss] §aO boss se teleportou para seu spawn! (X:" + spawn.getBlockX() + " Y:" + spawn.getBlockY() + " Z:" + spawn.getBlockZ() + ")");
				}
			}
			entity.getBukkitEntity().teleport(spawn);
		}

	}

}
