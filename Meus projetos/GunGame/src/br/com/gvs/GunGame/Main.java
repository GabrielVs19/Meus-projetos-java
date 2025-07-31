package br.com.gvs.GunGame;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.MiniGame;

/**
 * @author Gabriel
 *
 */
public class Main extends JavaPlugin{
	
	private static Plugin plugin;
	private static ArrayList<Location> spawns = new ArrayList<>();
	private static boolean waterDamage = false;
	public void onEnable(){
		plugin = this;
		getLogger().info("Ativando GunGame - Desenvolvido por: iBieel. v: " + plugin.getDescription().getVersion());
		getCommand("addspawn").setExecutor(this);
		getCommand("start").setExecutor(this);
		Bukkit.getWorlds().get(0).setGameRuleValue("keepInventory", "true");
		getCommand("waterDamage").setExecutor(this);
		getCommand("menu").setExecutor(this);
		new Listeners();
		new MenuLevels();
		new Buffs();
		
		CoreD.setMiniGame(new MiniGame("Gun Game", "start", 10));
		CoreD.setMaxPlayers(20);
		CoreD.setAllowJoinSpectators(false);
		CoreD.setAllowTeamChat(false);
		CoreD.setCancelBlockBreakEvent(true);
		CoreD.setCancelBlockPlaceEvent(true);
		CoreD.setDisableDeathScreen(true);
		CoreD.setDisablePvP(true);
		CoreD.setCancelHangingBreakByEntityEvent(true);
		CoreD.setCancelWeatherChangeEvent(true);
		CoreD.setToSpectatorOnRespawn(false);
		CoreD.setStore(true);
		CoreD.setCancelFoodLevelChangeEvent(true);
		CoreD.setCancelPlayerDropItemEvent(true);
		CoreD.setCancelPlayerPickupItemEvent(true);
		CoreD.setDisableDamage(true);
		CoreD.setAllowVirtualChest(false);
	}
	
	/**
	 * @return - Plugin
	 */
	public static Plugin getPlugin(){
		return plugin;
	}
	
	/**
	 * @return - Retorna a lista com os spawns do mapa
	 */
	public static ArrayList<Location> getSpawns(){
		return spawns;
	}
	
	/**
	 * @return - Retorna se o mapa dará dano na água
	 */
	public static boolean getWaterDamageState(){
		return waterDamage;
	}
	
	/**
	 * @return - Retorna um spawn aleatorio da lista de spawns do mapa
	 */
	public static Location getRandomLocation(){
		int rnd = (int) (Math.random() * getSpawns().size());
		return spawns.get(rnd);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(commandLabel.equalsIgnoreCase("menu")){
			((Player)sender).openInventory(MenuLevels.getMenu());
			return true;
		}
		if(sender.isOp()){
			if(commandLabel.equalsIgnoreCase("start")){
				StartAndFinish.start();
				return true;
			}
			if(commandLabel.equalsIgnoreCase("waterDamage")){
				waterDamage = true;
				System.out.println("Dano na água ativo para este mapa.");	
				return true;
			}
			if(commandLabel.equalsIgnoreCase("addspawn")){
				try{
					World world = Bukkit.getWorlds().get(0);
					int x = Integer.parseInt(args[0]);
					int y = Integer.parseInt(args[1]);
					int z = Integer.parseInt(args[2]);
					spawns.add(new Location(world, x, y, z));
					System.out.println("Spawn Adicionado!");	
				}catch(Exception e){
					System.out.println("Coordenadas Invalidas! /addspawn <x> <y> <z>");
				}
				return true;
			}
		}
		return false;
	}

}
