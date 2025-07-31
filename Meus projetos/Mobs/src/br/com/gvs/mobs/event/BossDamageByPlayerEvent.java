package br.com.gvs.mobs.event;


import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import br.com.gvs.mobs.util.Boss;

public class BossDamageByPlayerEvent extends Event implements Cancellable{

	private static final HandlerList handlers = new HandlerList();
	private Entity bossEntity;
	private Boss boss;
	private boolean cancelled;
	public BossDamageByPlayerEvent(Entity bossEntity, Boss boss)
	{
		this.bossEntity = bossEntity;
		this.boss = boss;
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
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
		
	}
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
	
}
