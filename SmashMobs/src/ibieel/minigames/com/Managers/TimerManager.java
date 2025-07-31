package ibieel.minigames.com.Managers;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.StartAndFinish;
import ibieel.minigames.com.Util.ColorScrollPlus;
import ibieel.minigames.com.Util.ColorScrollPlus.ScrollType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreD;

public class TimerManager {

	private static int timer = 0;
	private static int tempo = 3;
	private static boolean stop = false;

	public TimerManager(int time){
		TimerManager.timer = time;
	}

	public void run(){
		new BukkitRunnable(){
			@Override
			public void run() {
				timer--;
				if(timer == 60){
					Bukkit.broadcastMessage("§6Tempo restante: §4§l1 minuto");
					for(Player p : Bukkit.getOnlinePlayers()){
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
					}
					stop = true;
				}
				if(timer == 0){
					StartAndFinish.finishTime();
					cancel();
				}
				if(timer <= 60){
					//CoreD.setBossBar("§6§lTEMPO RESTANTE: §C§l" + secToMin(timer));
				}
				if(timer < 11){
					for(Player p : Bukkit.getOnlinePlayers()){
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
					}
					Bukkit.broadcastMessage("§c§lTEMPO RESTANTE: §4§l" +timer);
				}
			}
		}.runTaskTimer(SmashMobs.getInstance(), 0, 20);
	}

	public static void craftLandia(){
		final String craftlandia = "CRAFTLANDIA NETWORK";
		final ColorScrollPlus cs = new ColorScrollPlus(ChatColor.BLUE,
				craftlandia,
				"§b", 
				"§f",  
				"§f", 
				true, 
				true, 
				ScrollType.FORWARD);
		cs.stringLength();
		new BukkitRunnable(){
			int b = 0;
			@Override
			public void run() {
				if(stop){
					cancel();
				}
				String a = cs.next();
				if(a.equalsIgnoreCase("§9§lCRAFTLANDIA NETW§f§lO§b§lR§f§lK")){
					b++;
					tempo = 12;
				}
				if(b != 0){
					if(b == 1){
						CoreD.setBossBar("§b§lCRAFTLANDIA NETWORK");
					}
					if(b == 2){
						CoreD.setBossBar("§f§lCRAFTLANDIA NETWORK");
					}
					if(b == 3){
						CoreD.setBossBar("§b§lCRAFTLANDIA NETWORK");
					}
					if(b == 4){
						CoreD.setBossBar("§f§lCRAFTLANDIA NETWORK");
					}
					if(b == 5){
						CoreD.setBossBar("§b§lCRAFTLANDIA NETWORK");
					}
					if(b == 6){
						CoreD.setBossBar("§f§lCRAFTLANDIA NETWORK");
					}
					b++;
					if(b == 8){
						CoreD.setBossBar("§b§lCRAFTLANDIA NETWORK");
						tempo = 3;
						b = 0;
					}

				}else{
					CoreD.setBossBar(a);
				}
			}
		}.runTaskTimer(SmashMobs.getInstance(), tempo, tempo);
	}

	public String secToMin(int i){
		int ms = i / 60;
		int ss = i % 60;
		String m = (ms < 10 ? "0" : "") + ms;
		String s = (ss < 10 ? "0" : "") + ss;
		String f = m + ":" + s;
		return f;
	}

}
