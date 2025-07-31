package br.com.gvs.mobs.pathfinders;

import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.EntityInsentient;
import net.minecraft.server.v1_7_R2.PathfinderGoal;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class PathfinderGoalTpToSpawn extends PathfinderGoal{

	private Location spawn;
	private int radius;
	private Entity entity;
	public PathfinderGoalTpToSpawn(EntityInsentient entity, Location spawn, int radius){
		this.spawn = spawn;
		this.radius = radius;
		this.entity = entity;
	}

	@Override
	public boolean a() {
		return true;
	}

	@Override
	public void e(){
		Location entityLoc = new Location(entity.getBukkitEntity().getWorld(), entity.locX, entity.locY, entity.locZ);
		if(entityLoc.distance(spawn) >= this.radius){
			for(org.bukkit.entity.Entity e : entity.getBukkitEntity().getNearbyEntities(30, 30, 30)){
				if(e.getType() == EntityType.PLAYER){
					((Player)e).sendMessage("§6[Boss] §aO boss se teleportou para seu spawn! (X:" + spawn.getBlockX() + " Y:" + spawn.getBlockY() + " Z:" + spawn.getBlockZ() + ")");
				}
			}
			entity.getBukkitEntity().teleport(spawn);
		}

	}
	 
}
