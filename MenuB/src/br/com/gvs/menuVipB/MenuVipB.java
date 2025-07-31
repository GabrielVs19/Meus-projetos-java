package br.com.gvs.menuVipB;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MenuVipB extends JavaPlugin implements CommandExecutor {
	
	private static Plugin plugin;
	
	@Override
	public void onEnable(){
		plugin = this;
		Menu.setupMenu();
		getCommand("vip").setExecutor(this);
		getServer().getPluginManager().registerEvents(new Listeners(), this);
	}
	
	public static Plugin getInstance(){
		return plugin;
	}
	
	public static String getVipExpire(Player player){
		return "---";
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(commandLabel.equalsIgnoreCase("vip")){
			if(sender instanceof Player){
				
				Player player = (Player) sender;
				
				if(player.hasPermission("menu.vip")){
					Menu.openMenu(player);
				}else{
					player.sendMessage("§6[VIP] §cComando apenas para jogadores VIPs.");
				}
				
			}
		}
		return false;
	}

}
