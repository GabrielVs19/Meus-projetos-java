package br.com.gvs.mobs.commands;


import java.lang.reflect.Constructor;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
import org.bukkit.entity.Player;

import br.com.gvs.mobs.event.MobSpawnEvent;
import br.com.gvs.mobs.managers.MobManager;
import br.com.gvs.mobs.util.Mob;
import br.com.gvs.mobs.util.MobType;
import net.minecraft.server.v1_7_R2.Entity;
import net.minecraft.server.v1_7_R2.World;


public class Command_SpawnCustomMob
{
	
	public static void processCommand(CommandSender sender, String commandLabel, String[] args)
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("§4✖ §cComando disponivel apenas para jogadores");
			return;
		}
		
		Player player = (Player) sender;
		
		if(!player.hasPermission("mobs.command.spawn"))
		{
			return;
		}
		
		if(args.length != 2)
		{
			player.sendMessage("§4✖ §cUtilize: /spawncustommob <type> <level>");
			return;
		}
		
		int level = 0;
		
		try
		{
			level = Integer.parseInt(args[1]);
		}
		catch(Exception e)
		{
			player.sendMessage("§4✖ §cO Level informado não é um número válido!");
			return;
		}
		
		MobType mobType = MobType.getMobType(args[0]);
		
		if(mobType == null)
		{
			player.sendMessage("§4✖ §cO Tipo informado não é válido. Utilize: " + Arrays.asList(MobType.values()).toString().replace("[", "").replace("]", "").replace("MobType.", ""));
			return;
		}
		
		Mob mob = MobManager.getMob(mobType, level);
		
		if(mob == null)
		{
			player.sendMessage("§4✖ §cNão existe nenhum " + mobType.name() + " com esse level!");
			return;
		}
		
		Location loc = player.getLocation();
		World w = ((CraftWorld) loc.getWorld()).getHandle();
		try
		{
			Class<? extends Entity> mobClass = mob.getMobType().getMobClass();
			Constructor<? extends Entity> mobc = mobClass.getConstructor(World.class, Mob.class);
			
			Entity mEntity = (Entity) mobc.newInstance(w, mob);
			MobSpawnEvent mobSpawnEvent = new MobSpawnEvent(mEntity, mob, loc);
			Bukkit.getPluginManager().callEvent(mobSpawnEvent);
			
			if(!mobSpawnEvent.isCancelled())
			{
				MobManager.spawnMob(mEntity, loc);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
