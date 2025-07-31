package br.com.minigame.speedbuilders.listeners;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import br.com.minigame.speedbuilders.SpeedBuilders;
import br.com.tlcm.cc.API.CoreD;


public class PreListeners implements Listener
{
	
	public PreListeners(SpeedBuilders plugin)
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	private void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		
		new BukkitRunnable()
		{
			
			public void run()
			{
				Location spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
				player.teleport(new Location(spawn.getWorld(), spawn.getX(), spawn.getY() + 50, spawn.getZ()));
				
				if(SpeedBuilders.isTeamMode())
				{
					player.sendMessage("\n§a» Digite §2/dupla §apara jogar com um amigo\n\n ");
				}
			}
		}.runTaskLater(SpeedBuilders.getInstance(), 3);
	}
	
	@EventHandler
	private void onPlayerQuitEvent(PlayerQuitEvent event)
	{
		if(!CoreD.isRunning())
		{
			Player player = event.getPlayer();
			Team team = player.getScoreboard().getPlayerTeam(player);
			
			if(team != null)
			{
				team.removePlayer(player);
				
				for(OfflinePlayer of : team.getPlayers())
				{
					Player p = (Player) of;
					
					if(p != null && !p.getName().equalsIgnoreCase(player.getName()))
					{
						p.sendMessage("§a[Dupla] Sua dupla desconectou, agora você está sozinho(a).");
					}
				}
			}
		}
	}
}