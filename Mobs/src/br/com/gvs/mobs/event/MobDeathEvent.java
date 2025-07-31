package br.com.gvs.mobs.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import br.com.gvs.mobs.util.Mob;

public class MobDeathEvent extends Event implements Cancellable{

	private static final HandlerList handlers = new HandlerList();
	private Entity mobEntity;
	private Mob mob;
	private Player player;
	private boolean cancelled;
	public MobDeathEvent(Player player, Entity mobEntity, Mob mob)
	{
		this.player = player;
		this.mobEntity = mobEntity;
		this.mob = mob;
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

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
}
