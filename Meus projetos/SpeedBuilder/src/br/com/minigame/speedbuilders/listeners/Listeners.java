package br.com.minigame.speedbuilders.listeners;


import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import br.com.minigame.speedbuilders.SpeedBuilders;
import br.com.minigame.speedbuilders.manager.BuildField;
import br.com.minigame.speedbuilders.manager.Round;
import br.com.minigame.speedbuilders.manager.Round.RoundStatus;


public class Listeners implements Listener
{
	
	
	public Listeners(SpeedBuilders plugin)
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityTargetEvent(EntityTargetEvent e)
	{
		if(!(e.getEntity() instanceof Blaze))
		{
			return;
		}
		e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void cancelBreakFarmWithJump(PlayerInteractEvent e)
	{
		if((e.getAction() == Action.PHYSICAL) && (e.getClickedBlock().getType() == Material.SOIL))
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockFadeEvent(BlockFadeEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockGrowEvent(BlockGrowEvent e)
	{
		e.setCancelled(true);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerInteractEvent(PlayerInteractEvent e)
	{
		if(Round.getRoundStatus() != RoundStatus.DRAW_TIME)
		{
			e.setCancelled(true);
			return;
		}
		
		if(e.getAction() != Action.LEFT_CLICK_BLOCK || e.getClickedBlock() == null)
		{
			return;
		}
		
		Block block = e.getClickedBlock();
		
		BuildField bf = BuildField.getBuildField(e.getPlayer().getName());
		
		if(bf == null)
		{
			return;
		}
		
		if(!bf.getPlayerBlocks().contains(block))
		{
			return;
		}
		
		bf.getPlayerBlocks().remove(block);
		
		int data = block.getData();
		if(block instanceof Skull)
		{
			Skull skull = (Skull) block.getState();
			switch(skull.getSkullType())
			{
				case SKELETON:
					data = 0;
					break;
				case WITHER:
					data = 1;
					break;
				case ZOMBIE:
					data = 2;
					break;
				case PLAYER:
					data = 3;
					break;
				case CREEPER:
					data = 4;
					break;
			}
		}
		
		List<ItemStack> is = SpeedBuilders.getRightItem(block.getTypeId(), 1, data);
		
		for(ItemStack itemStack : is)
		{
			block.getWorld().dropItemNaturally(block.getLocation(), itemStack);
		}
		
		block.setType(Material.AIR);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockPlaceEvent(BlockPlaceEvent event)
	{
		if(Round.getRoundStatus() != RoundStatus.DRAW_TIME)
		{
			event.setCancelled(true);
			return;
		}
		
		BuildField bf = BuildField.getBuildField(event.getPlayer().getName());
		
		if(bf == null)
		{
			event.setCancelled(true);
			return;
		}
		
		Block block = event.getBlock();
		Location bL = block.getLocation();
		Location fL = bf.getLocation();
		
		if(Math.max(bL.getX(), fL.getX()) - Math.min(bL.getX(), fL.getX()) >= 4 || Math.max(bL.getZ(), fL.getZ()) - Math.min(bL.getZ(), fL.getZ()) >= 4)
		{
			event.getPlayer().sendMessage("§cVocê só pode construir dentro da arena!");
			event.setCancelled(true);
			return;
		}
		
		bf.getPlayerBlocks().add(block);
		
		if(block.getType() == Material.WOODEN_DOOR || block.getType() == Material.IRON_DOOR_BLOCK)
		{
			bf.getPlayerBlocks().add(block.getRelative(BlockFace.UP));
		}
		
		if(block.getType() == Material.NETHER_WARTS)
		{
			block.setData((byte) 4);
		}
		
		if(block.getType() == Material.COCOA)
		{
			block.setData((byte) (block.getData() + 8));
		}
		
		if(block.getType() == Material.CROPS || block.getType() == Material.PUMPKIN_STEM || block.getType() == Material.MELON_STEM || block.getType() == Material.CARROT || block.getType() == Material.POTATO)
		{
			block.setData((byte) 7);
		}
		
		List<ItemStack> is = SpeedBuilders.getRightItem(block.getTypeId(), 1, (short) block.getData());
		if(SpeedBuilders.isTeamMode() && bf.getPlayers().length > 1)
		{
			for(String playerName : bf.getPlayers())
			{
				Player player = Bukkit.getPlayer(playerName);
				if(player == null || playerName.equalsIgnoreCase(event.getPlayer().getName()))
				{
					continue;
				}
				
				for(ItemStack itemStack : is)
				{
					bf.removeItemPlayer(player, itemStack);
				}
				player.updateInventory();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBurnEvent(BlockBurnEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockIgniteEvent(BlockIgniteEvent event)
	{
		if(event.getCause() != IgniteCause.FLINT_AND_STEEL)
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityChangeBlockEvent(EntityChangeBlockEvent event)
	{
		if(event.getEntityType() == EntityType.FALLING_BLOCK)
		{
			BuildField bf = BuildField.getBuildField(event.getBlock().getLocation());
			
			if(bf == null)
			{
				event.setCancelled(true);
				return;
			}
			
			bf.getPlayerBlocks().add(event.getBlock());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityExplodeEvent(EntityExplodeEvent event)
	{
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerMoveEvent(PlayerMoveEvent event)
	{
		if(Round.getRoundStatus() == RoundStatus.SCORE_TIME || Round.getRoundStatus() == RoundStatus.AWARD_TIME || Round.getRoundStatus() == RoundStatus.FINISH_TIME)
		{
			return;
		}
		
		Location from = event.getFrom();
		Location to = event.getTo();
		
		if(from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ())
		{
			return;
		}
		
		BuildField bf = BuildField.getBuildField(event.getPlayer().getName());
		
		if(bf == null)
		{
			return;
		}
		
		Location bL = event.getPlayer().getLocation();
		Location fL = bf.getLocation();
		
		int dX = Math.abs(Math.subtractExact(bL.getBlockX(), fL.getBlockX()));
		int dY = Math.subtractExact(bL.getBlockY(), fL.getBlockY());
		int dZ = Math.abs(Math.subtractExact(bL.getBlockZ(), fL.getBlockZ()));
		
		if(dX > bf.getRadius() || dY < -2 || dZ > bf.getRadius())
		{
			event.getPlayer().sendMessage("§cVocê não pode sair da arena!");
			bf.summon(event.getPlayer());
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerQuitEvent(PlayerQuitEvent event)
	{
		BuildField bf = BuildField.getBuildField(event.getPlayer().getName());
		
		if(bf != null)
		{
			String[] players = bf.getPlayers();
			
			if(SpeedBuilders.isTeamMode() && players.length > 1)
			{
				String player = players[0].equalsIgnoreCase(event.getPlayer().getName()) ? players[1] : players[0];
				bf.setPlayersNick(player);
			}
			else
			{
				bf.unregister(false);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onItemSpawnEvent(ItemSpawnEvent e)
	{
		if(Round.getRoundStatus() == RoundStatus.DRAW_TIME)
		{
			return;
		}
		
		e.setCancelled(true);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent e)
	{
		if(Round.getRoundStatus() != RoundStatus.DRAW_TIME)
		{
			e.getItem().remove();
			return;
		}
		
		if(e.getItem() == null || e.getItem().getItemStack() == null)
		{
			return;
		}
		
		Player player = e.getPlayer();
		ItemStack itemStack = e.getItem().getItemStack();
		
		BuildField bf = BuildField.getBuildField(player.getName());
		
		if(bf == null)
		{
			return;
		}
		
		if(SpeedBuilders.isTeamMode() && bf.getPlayers().length > 1)
		{
			for(String p : bf.getPlayers())
			{
				Player pl = Bukkit.getPlayer(p);
				
				if(pl.getName().equalsIgnoreCase(player.getName()))
				{
					continue;
				}
				
				pl.getInventory().addItem(itemStack);
				pl.updateInventory();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerInteractEvent2(PlayerInteractEvent e)
	{
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK)
		{
			return;
		}
		
		if(e.getItem() == null || e.getItem().getType() != Material.INK_SACK)
		{
			return;
		}
		
		if(e.getItem().getDurability() != (short) 15)
		{
			return;
		}
		
		e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockPistonExtendEvent(BlockPistonExtendEvent e)
	{
		e.setCancelled(true);
	}
}
