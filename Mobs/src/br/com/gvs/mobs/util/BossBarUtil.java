package br.com.gvs.mobs.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.gvs.mobs.Mobs;
import br.com.smooph.bossbar.BossBar;


public class BossBarUtil implements Listener
{
	
	
	public static HashMap<org.bukkit.entity.Entity, ArrayList<String>> bossBar = new HashMap<>();
	
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent e)
	{
		Boss boss = Mobs.getBoss(e.getEntity());
		if(boss != null)
		{
			
			if(boss.getBossType() == BossType.DRAGAO && e.getDamager().getType() == EntityType.FIREBALL)
			{
				e.setDamage(0.0);
			}
			
			if(bossBar.containsKey(e.getEntity()))
			{
				if(e.getDamager() instanceof Player)
				{
					if(!bossBar.get(e.getEntity()).contains(((Player) e.getDamager()).getName()))
					{
						bossBar.get(e.getEntity()).add(((Player) e.getDamager()).getName());
						for(Entry<org.bukkit.entity.Entity, ArrayList<String>> entry : bossBar.entrySet())
						{
							if(entry.getKey().equals(e.getEntity()))
							{
								continue;
							}
							if(entry.getValue().contains(((Player) e.getDamager()).getName()))
								if(Bukkit.getPlayer(((Player) e.getDamager()).getName()) != null)
								{
									Player p = Bukkit.getPlayer(((Player) e.getDamager()).getName());
									bossBar.get(entry.getKey()).remove(p);
								}
						}
					}
				}
			}
			else
			{
				ArrayList<String> players = new ArrayList<>();
				if(e.getDamager() instanceof Player)
				{
					players.add(((Player) e.getDamager()).getName());
				}
				bossBar.put(e.getEntity(), players);
			}
		}
	}
	
	public static void bossBarTask()
	{
		new BukkitRunnable()
		{
			
			@Override
			public void run()
			{
				if(bossBar != null)
				{
					for(org.bukkit.entity.Entity entity : new HashSet<org.bukkit.entity.Entity>(bossBar.keySet()))
					{
						float life = (float) ((float) ((Damageable) entity).getHealth() / ((Damageable) entity).getMaxHealth());
						for(String p1 : new ArrayList<String>(bossBar.get(entity)))
						{
							if(Bukkit.getPlayer(p1) != null)
							{
								Player p = Bukkit.getPlayer(p1);
								
								if(!p.getWorld().getName().equalsIgnoreCase(entity.getWorld().getName()) || p.getLocation().distance(entity.getLocation()) > 300)
								{
									bossBar.get(entity).remove(p);
									if(bossBar.get(entity).size() <= 0)
									{
										bossBar.remove(entity);
									}
									if(BossBar.hasBar(p))
									{
										BossBar.removeBar(p);
									}
									continue;
								}
								Boss boss = Mobs.getBoss(entity);
								if(boss != null)
									BossBar.setMessage(p, "§4§l" + boss.getBossType().getName(), life * 100);
							}
						}
					}
				}
			}
		}.runTaskTimer(Mobs.getInstance(), 20 * 5, 20 * 5);
	}
	
}
