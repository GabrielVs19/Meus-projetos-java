package ibieel.eventos.com;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin{

	public static Plugin plugin;
	private static String world = null;
	private static boolean andamento = false;
	private static boolean status = false;
	private static String warp = null;
	private static String warpSaida = null;
	private static String region = null;
	private static ArrayList<String> playerList = new ArrayList<String>();


	public void onEnable(){
		plugin = this;
		playerList.clear();
		loadConfig();
		world = getConfig().getString("mundo");
		warp = getConfig().getString("warp");
	    warpSaida = getConfig().getString("warpSaida");
	    region = getConfig().getString("region");
		getCommand("chuvadeblocos").setExecutor(this);
		getServer().getPluginManager().registerEvents(new Listeners(), this);
	}

	
	public static List<String> getPlayerList(){
		return playerList;
	}
	
	public static boolean isRunning(){
		return andamento;
	}
	
	public static String getWorld(){
		return world;
	}
	
	public static boolean getStatus(){
		return status;
	}
	
	public static void setStatus(boolean status1){
		status = status1;
	}
	
	public static String getWarp(){
		return warp;
	}
	
	public static String getRegion(){
		return region;
	}
	
	public static String getExitWarp(){
		return warpSaida;
	}
	
	public static void setRunning(boolean running){
		andamento = running;
	}
	
	 public void loadConfig(){
	    getConfig().options().copyDefaults(true);
	    saveDefaultConfig();
	  }

	 public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	  {
	    if (commandLabel.equalsIgnoreCase("chuvadeblocos")) {
	      if (sender.hasPermission("chuvadeblocos.chuvadeblocos"))
	      {
	        if ((args.length == 1) && 
	          (args[0].equalsIgnoreCase("start")))
	        {
	          new BukkitRunnable()
	          {
	            int counter = 0;
	            
	            public void run()
	            {
	              if (this.counter < getConfig().getInt("numero_de_anuncios"))
	              {
	                setRunning(false);
	                setStatus(true);
	                if (getConfig().getBoolean("servers.normais.status")) {
	                  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skript disable " + getConfig().getString("servers.normais.skriptEvento"));
	                }
	                if (getConfig().getBoolean("servers.hc.status")) {
	                  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "skript disable " + getConfig().getString("servers.hc.skriptEvento"));
	                }
	                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&d[Evento Automático] Evento Chuva de Blocos!!"));
	                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&d[Evento Automático] Para participar digite: /evento"));
	                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&d[Evento Automático] Este evento é PVP: [&COFF&d]"));
	              }
	              if (this.counter == getConfig().getInt("numero_de_anuncios"))
	              {
	                setRunning(true);
	                setStatus(false);
	                
	                Bukkit.broadcastMessage("");
	                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&d[Evento Automático] Iniciando evento!"));
	                Bukkit.broadcastMessage("");
	                
	                Chuva.start();
	                cancel();
	              }
	              this.counter += 1;
	            }
	          }.runTaskTimer(plugin, 0L, 600L);
	          return true;
	        }
	      }
	      else
	      {
	        Player player = (Player)sender;
	        if ((!getStatus()) && (!isRunning())) {
	          player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[Chuva de Blocos] &eO evento Chuva de Blocos nao está acontecendo no momento!"));
	        }
	        if ((getStatus()) && (!isRunning())) {
	        	getPlayerList().add(player.getName());
	          Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "warp -s @" + player.getName() + " " + getWarp());
	        }
	        if ((!getStatus()) && (isRunning())) {
	          player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[Chuva de Blocos] &eO evento Chuva de Blocos já começou!"));
	        }
	        return true;
	      }
	    }
	    return false;
	  }
}
