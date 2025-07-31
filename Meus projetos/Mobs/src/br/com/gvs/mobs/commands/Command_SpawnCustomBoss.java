package br.com.gvs.mobs.commands;


import java.lang.reflect.Constructor;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
import org.bukkit.entity.Player;

import br.com.gvs.mobs.event.BossSpawnEvent;
import br.com.gvs.mobs.managers.BossManager;
import br.com.gvs.mobs.util.Boss;
import br.com.gvs.mobs.util.BossType;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.World;


public class Command_SpawnCustomBoss
{
	
	
	public static void processCommand(CommandSender sender, String commandLabel, String[] args)
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("§4✖ §cComando disponivel apenas para jogadores");
			return;
		}
		
		Player player = (Player) sender;
		
		if(!player.hasPermission("boss.command.spawn"))
		{
			return;
		}
		
		if(args.length != 1)
		{
			player.sendMessage("§4✖ §cUtilize: /spawncustomboss <name>");
			return;
		}
		
		BossType bossType = BossType.getBossType(args[0]);
		
		if(bossType == null)
		{
			player.sendMessage("§4✖ §cO nome informado não é válido. Utilize: " + Arrays.asList(BossType.values()).toString().replace("[", "").replace("]", "").replace("MobType.", ""));
			return;
		}
		
		Boss boss = BossManager.getBossByType(bossType);
		Location loc = player.getLocation();
		
		try
		{
			Class<? extends Entity> bossClass = BossType.getBossByID(bossType.getBossID()).getBossClass();
			Constructor<? extends Entity> bossc = bossClass.getConstructor(World.class, Boss.class);
			Entity bossEntity = (Entity) bossc.newInstance(((CraftWorld) loc.getWorld()).getHandle(), BossManager.getBossByType(BossType.getBossByID(bossType.getBossID())));
			
			BossSpawnEvent bossSpawnEvent = new BossSpawnEvent(bossEntity.getBukkitEntity(), boss, loc);
			Bukkit.getPluginManager().callEvent(bossSpawnEvent);
			
			if(!bossSpawnEvent.isCancelled())
			{
				BossManager.spawnBoss(bossEntity, loc);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
