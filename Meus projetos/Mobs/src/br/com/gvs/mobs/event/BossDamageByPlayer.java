package br.com.gvs.mobs.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import br.com.gvs.mobs.util.Boss;

public class BossDamageByPlayer extends Event implements Cancellable{

	private static final HandlerList handlers = new HandlerList();
	private Entity bossEntity;
	private Boss boss;
	private Player player;
	private double damage;
	private boolean cancelled;
	public BossDamageByPlayer(Player player, Entity bossEntity, Boss boss, double damage)
	{
		this.bossEntity = bossEntity;
		this.boss = boss;
		this.player = player;
		this.damage = damage;
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

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
