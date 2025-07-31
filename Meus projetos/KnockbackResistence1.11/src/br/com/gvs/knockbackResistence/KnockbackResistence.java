package br.com.gvs.knockbackResistence;
import net.minecraft.server.v1_7_R2.EntityHuman;
import net.minecraft.server.v1_7_R2.GenericAttributes;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class KnockbackResistence extends JavaPlugin {

	private static double knockbackResistence;
	private static Plugin plugin;
	
	@Override
	public void onEnable(){
		plugin = this;
		this.loadConfig();
		knockbackResistence = this.getConfig().getInt("knockbackResistence") / 100;
		new Listeners();
	}
	
	@Override
	public void onDisable(){
		for(Player player : Bukkit.getOnlinePlayers()){
			try{
				EntityHuman entityHuman = ((CraftPlayer)player).getHandle();
				entityHuman.getAttributeInstance(GenericAttributes.c).setValue(0.0);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	private void loadConfig(){
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
	}
	
	public static double getKnockbackResistence(){
		return knockbackResistence;
	}
	
	public static Plugin getInstance(){
		return plugin;
	}
	
}
