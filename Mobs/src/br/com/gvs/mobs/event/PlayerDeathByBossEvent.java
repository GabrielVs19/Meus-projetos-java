package br.com.gvs.mobs.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import br.com.gvs.mobs.util.Boss;

public class PlayerDeathByBossEvent extends Event implements Cancellable{

	private static final HandlerList handlers = new HandlerList();
	private Entity bossEntity;
	private Boss boss;
	private Player player;
	private boolean cancelled;
	public PlayerDeathByBossEvent(Player player, Entity bossEntity, Boss boss)
	{
		this.player = player;
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

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
}
