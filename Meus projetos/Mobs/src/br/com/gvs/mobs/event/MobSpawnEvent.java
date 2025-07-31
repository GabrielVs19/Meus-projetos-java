package br.com.gvs.mobs.event;


import org.bukkit.Location;
import net.minecraft.server.v1_7_R2.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import br.com.gvs.mobs.util.Mob;


public class MobSpawnEvent extends Event implements Cancellable
{
	
	
	private static final HandlerList handlers = new HandlerList();
	private Entity mobEntity;
	private Mob mob;
	private Location location;
	private boolean cancelled;
	
	public MobSpawnEvent(Entity mEntity, Mob mob, Location location)
	{
		this.mobEntity = mEntity;
		this.mob = mob;
		this.location = location;
		this.cancelled = false;
	}
	
	public Entity getMobEntity()
	{
		return mobEntity;
	}
	
	public Mob getMob()
	{
		return mob;
	}
	
	public Location getLocation()
	{
		return location;
	}
	
	public boolean isCancelled()
	{
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
	}
	
	public HandlerList getHandlers()
	{
		return handlers;
	}
	
	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}
