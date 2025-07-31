package br.com.minigame.speedbuilders.manager;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import br.com.minigame.speedbuilders.SpeedBuilders;
import br.com.tlcm.cc.API.CoreDPlayer;


public class BuildField
{
	
	private static List<BuildField> buildFieldList = new ArrayList<BuildField>();
	
	private String players = null;
	private Location location = null;
	
	private List<Block> playerBlocks = new ArrayList<Block>();
	private List<Block> fieldBlocks = new ArrayList<Block>();
	private int radius;
	private int score;
	
	public BuildField(Location location, int radius, int deep)
	{
		this.location = location;
		this.radius = radius;
		
		for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++)
		{
			for(int y = location.getBlockY() - deep; y <= location.getBlockY() + deep; y++)
			{
				for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++)
				{
					Block block = location.getWorld().getBlockAt(x, y, z);
					
					if(block == null || block.getType() == Material.AIR)
					{
						continue;
					}
					
					getFieldBlocks().add(block);
				}
			}
		}
	}
	
	public static List<BuildField> getBuildFieldList()
	{
		return buildFieldList;
	}
	
	public static BuildField getBuildField(String player)
	{
		for(BuildField bf : getBuildFieldList())
		{
			if(bf.getPlayersNick() == null)
			{
				continue;
			}
			
			for(String p : bf.getPlayers())
			{
				if(p.equalsIgnoreCase(player))
				{
					return bf;
				}
			}
		}
		
		return null;
	}
	
	public static BuildField getBuildField(Location location)
	{
		for(BuildField bf : getBuildFieldList())
		{
			int blockX = location.getBlockX();
			int blockZ = location.getBlockZ();
			
			int minX = bf.getLocation().getBlockX() - bf.getRadius();
			int minZ = bf.getLocation().getBlockZ() - bf.getRadius();
			
			int maxX = bf.getLocation().getBlockX() + bf.getRadius();
			int maxZ = bf.getLocation().getBlockZ() + bf.getRadius();
			
			boolean x = blockX >= minX && blockX <= maxX;
			boolean z = blockZ >= minZ && blockZ <= maxZ;
			
			if(x && z)
			{
				return bf;
			}
		}
		
		return null;
	}
	
	public static BuildField getNextEmptyBuildField()
	{
		for(BuildField bf : buildFieldList)
		{
			if(bf.getPlayers() == null)
			{
				return bf;
			}
		}
		
		return null;
	}
	
	public String[] getPlayers()
	{
		if(players == null)
		{
			return null;
		}
		
		return players.split(" e ");
	}
	
	public String getPlayersNick()
	{
		return players;
	}
	
	public void setPlayersNick(String players)
	{
		this.players = players;
	}
	
	public Location getLocation()
	{
		return location;
	}
	
	public List<Block> getPlayerBlocks()
	{
		return playerBlocks;
	}
	
	public List<Block> getFieldBlocks()
	{
		return fieldBlocks;
	}
	
	public int getRadius()
	{
		return radius;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public void setScore(int score)
	{
		this.score = score;
	}
	
	public void destroy(boolean explode)
	{
		List<Block> allBlocks = new ArrayList<>();
		allBlocks.addAll(getPlayerBlocks());
		allBlocks.addAll(getFieldBlocks());
		
		for(Block block : allBlocks)
		{
			if(explode)
			{
				block.breakNaturally();
				block.getLocation().getWorld().playEffect(block.getLocation(), Effect.EXPLOSION_LARGE, 1);
				block.getLocation().getWorld().playSound(block.getLocation(), Sound.EXPLODE, 100, 1);
			}
			else
			{
				block.setType(Material.AIR);
			}
		}
	}
	
	public void clear()
	{
		for(Block block : getPlayerBlocks())
		{
			block.setType(Material.AIR);
		}
		getPlayerBlocks().clear();
	}
	
	public void sendMessagePlayers(String message)
	{
		for(String p : getPlayers())
		{
			Player player = Bukkit.getPlayer(p);
			
			if(player == null)
			{
				continue;
			}
			
			player.sendMessage(message);
		}
	}
	
	public void giveItemPlayers(ItemStack itemStack)
	{
		for(String p : getPlayers())
		{
			Player player = Bukkit.getPlayer(p);
			
			if(player == null)
			{
				continue;
			}
			
			player.getInventory().addItem(itemStack);
		}
	}
	
	public void removeItemPlayers(ItemStack itemStack)
	{
		for(String p : getPlayers())
		{
			Player player = Bukkit.getPlayer(p);
			
			if(player == null)
			{
				continue;
			}
			
			removeItemPlayer(player, itemStack);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void removeItemPlayer(Player player, ItemStack itemStack)
	{
		int amount = itemStack.getAmount();
		ItemStack[] contents = player.getInventory().getContents();
		
		for(int i = 0; i < contents.length; i++)
		{
			ItemStack is = contents[i];
			
			if(amount == 0)
			{
				break;
			}
			
			if(is == null)
			{
				continue;
			}
			
			if(is.getTypeId() != itemStack.getTypeId() || is.getDurability() != itemStack.getDurability())
			{
				continue;
			}
			
			if(is.getAmount() > amount)
			{
				is.setAmount(is.getAmount() - amount);
				amount = 0;
			}
			else
			{
				amount -= is.getAmount();
				contents[i] = new ItemStack(Material.AIR);
			}
		}
		player.getInventory().setContents(contents);
	}
	
	@SuppressWarnings("deprecation")
	public void setSideBar(List<BuildField> scores)
	{
		if(getPlayers() == null)
		{
			return;
		}
		
		Player player = Bukkit.getPlayer(getPlayers()[0]);
		
		if(player == null)
		{
			return;
		}
		
		Objective objective = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
		
		if(objective != null)
		{
			objective.unregister();
		}
		
		objective = player.getScoreboard().registerNewObjective("sbstatus", "dummy");
		objective.setDisplayName("§6§lSpeedBuilders");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		for(int i = 0; i < scores.size(); i++)
		{
			BuildField bf = scores.get(i);
			
			String toAdd = bf.getPlayersNick();
			int score = bf.getScore();
			
			if(score == 0)
			{
				continue;
			}
			
			OfflinePlayer of = null;
			if(toAdd.length() > 16)
			{
				Team team = objective.getScoreboard().getTeam(toAdd.substring(0, 16));
				
				if(team == null)
				{
					team = objective.getScoreboard().registerNewTeam(toAdd.substring(0, 16));
					team.setPrefix(team.getName());
				}
				
				if(toAdd.length() > 32)
				{
					team.setSuffix(toAdd.substring(32, toAdd.length()));
					of = Bukkit.getOfflinePlayer(toAdd.substring(16, toAdd.length() - 16));
				}
				else
				{
					of = Bukkit.getOfflinePlayer(toAdd.substring(16, toAdd.length()));
				}
				
				team.addPlayer(of);
			}
			else
			{
				of = Bukkit.getOfflinePlayer(toAdd);
			}
			
			if(of != null)
			{
				objective.getScore(of).setScore(score);
			}
		}
	}
	
	public void summon(Entity entity)
	{
		Location l = new Location(getLocation().getWorld(), getLocation().getX() + 4.5, getLocation().getY() + 0.5, getLocation().getZ() + 0.5);
		l.setDirection(SpeedBuilders.getDirectionFromTo(l, getLocation()));
		entity.teleport(l);
	}
	
	public void summonPlayers()
	{
		for(String p : getPlayers())
		{
			Player player = Bukkit.getPlayer(p);
			
			if(player == null)
			{
				return;
			}
			
			summon(player);
		}
	}
	
	public void unregister(boolean explode)
	{
		Iterator<BuildField> i = getBuildFieldList().iterator();
		
		while(i.hasNext())
		{
			BuildField bf = i.next();
			if(this == bf)
			{
				if(bf.getPlayersNick() != null)
				{
					for(String player : bf.getPlayers())
					{
						CoreDPlayer.setSpectator(player);
					}
				}
				Picture.clear(bf.getLocation());
				bf.destroy(explode);
				i.remove();
			}
		}
	}
}
