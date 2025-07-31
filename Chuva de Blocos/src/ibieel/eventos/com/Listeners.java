package ibieel.eventos.com;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

public class Listeners implements Listener{

	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event){
		if(Main.getStatus()){
			String[] msg = event.getMessage().split(" ");
			String cmd = msg[0].replace("/", "");
			if (cmd.equalsIgnoreCase("evento")){
				event.getPlayer().chat("/chuvadeblocos");
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player player = (Player) e.getEntity();
			if(Main.getPlayerList().contains(player.getName()) && Main.isRunning()){
				if(e.getCause() == DamageCause.FALLING_BLOCK){
					e.setDamage(20);
				}
			}
		}
	}

	@EventHandler
	public void playerDeathEvent(PlayerDeathEvent e){
		Player player = e.getEntity();
		if(Main.getPlayerList().contains(player.getName()) && Main.isRunning()){
			Main.getPlayerList().remove(player.getName());
			player.sendMessage("§6[SPLEEF] §cVocê perdeu!");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "warp -s @" + player.getName() + " " + Main.getExitWarp());
		}
	}

	@EventHandler
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e){
		Player player = e.getPlayer();
		if(Main.getPlayerList().contains(player.getName()) && Main.isRunning() && e.getItem().getType() == Material.MILK_BUCKET){
			e.setCancelled(true);
			player.sendMessage("§cVocê não pode beber leite durante este evento.");
		}
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent e){
		Player player = e.getPlayer();
		if(Main.getPlayerList().contains(player.getName()) && (Main.isRunning() || Main.getStatus())){
			Main.getPlayerList().remove(player.getName());
			e.getPlayer().removePotionEffect(PotionEffectType.JUMP);
		}
	}

	@EventHandler
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent e){
		Player player = e.getPlayer();
		if(Main.getPlayerList().contains(player.getName()) && (Main.isRunning() || Main.getStatus())){
			Main.getPlayerList().remove(player.getName());
			e.getPlayer().removePotionEffect(PotionEffectType.JUMP);
		}
	}

}
