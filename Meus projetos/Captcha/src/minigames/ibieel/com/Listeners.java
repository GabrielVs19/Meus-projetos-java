package minigames.ibieel.com;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener{

	private HashMap<String, Integer> captchaResult = new HashMap<String, Integer>();
	public static Captcha plugin;
	public Listeners(Captcha plugin){
		Listeners.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player player = e.getPlayer();
		Thread loginThread = new ThreadLoginKick(player.getName());
		loginThread.start();
		int numero1 = (int) (Math.random() * 9); 
		int numero2 = (int) (Math.random() * 9);
		char[] sinais = {'+', '-'};
		char c = sinais[((int) (Math.random() * 2))];
		PlayerCaptcha pCaptcha = new PlayerCaptcha(player.getName(), numero1, numero2, c, loginThread);
		PlayerCaptcha.add(pCaptcha);
		player.sendMessage("§cClique na frase que tem na descrição o resultado da seguinte conta: §6" + numero1 + " " + c + " " + numero2 + "§c.");
		int op = (int) (((Math.random() * 2) + 1));
		captchaResult.put(player.getName(), op);
		for(int i = 1;i <= 3; i++){
			if(i == op){
				int result = (c == '+' ? (numero1 + numero2) : (numero1 - numero2));
				((CraftPlayer)player).getHandle().sendMessage(HoverChatUtil.chatComando("§aOpção " + i, "§d" + result, "/opçao" + op));	
			}else{
				int result = ((int)(Math.random() * 99));
				((CraftPlayer)player).getHandle().sendMessage(HoverChatUtil.chatComando("§aOpção " + i, "§d" + result, "/opçao" + op));	
			}
		}
	}

	@EventHandler
	public void commandProcess(PlayerCommandPreprocessEvent e){
		Player player = e.getPlayer();
		PlayerCaptcha pc = PlayerCaptcha.getPlayerCaptcha(player.getName());
		if(pc != null){
			e.setCancelled(true);
			PlayerCaptcha.remove(pc);
			int op = captchaResult.get(player.getName());
			captchaResult.remove(player.getName());
			if(e.getMessage().equalsIgnoreCase("/opçao" + op)){
				player.sendMessage("§aVocê respondeu o captcha corretamente.");
				Captcha.addCaptcha(player.getName());
			}else{
				player.kickPlayer(ChatColor.RED + "Você respondeu errado o captcha.");
			}
		}
	}
	
	@EventHandler
	public void playerQuit(PlayerQuitEvent e){
		Player player = e.getPlayer();
		if(Captcha.getCaptchaPlayers().contains(player.getName())){
			Captcha.removeCaptcha(player.getName());
		}
		if(PlayerCaptcha.getPlayerCaptcha(player.getName()) != null){
			PlayerCaptcha.getPlayerCaptcha(player.getName()).getThread().interrupt();
			PlayerCaptcha.remove(PlayerCaptcha.getPlayerCaptcha(player.getName()));
		}
	}
	
}
