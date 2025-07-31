package br.com.gvs.mobs.event;


import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import br.com.gvs.mobs.util.Boss;


public class BossSpawnEvent extends Event implements Cancellable
{
	
	
	private static final HandlerList handlers = new HandlerList();
	private Entity bossEntity;
	private Boss boss;
	private Location location;
	private boolean cancelled;
	
	public BossSpawnEvent(Entity bossEntity, Boss boss, Location location)
	{
		this.bossEntity = bossEntity;
		this.boss = boss;
		this.location = location;
		this.cancelled = false;
	}
	
	public Entity getMobEntity()
	{
		return bossEntity;
	}
	
	public Boss getBoss()
	{
		return boss;
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
