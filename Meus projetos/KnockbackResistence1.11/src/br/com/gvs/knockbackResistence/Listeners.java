package br.com.gvs.knockbackResistence;

import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.GenericAttributes;

import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class Listeners implements Listener {

	public Listeners(){
		Plugin plugin = KnockbackResistence.getInstance();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		try{
			Player player = e.getPlayer();
			EntityHuman entityHuman = ((CraftPlayer)player).getHandle();
			entityHuman.getAttributeInstance(GenericAttributes.c).setValue(KnockbackResistence.getKnockbackResistence());
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		try{
			Player player = e.getPlayer();
			EntityHuman entityHuman = ((CraftPlayer)player).getHandle();
			entityHuman.getAttributeInstance(GenericAttributes.c).setValue(0.0);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
