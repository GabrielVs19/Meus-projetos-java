package ibieel.minigames.com;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreD;

public class Timer {

	private static int time = 300;
	
	private static String secToMin(int i){
		int ms = i / 60;
		int ss = i % 60;
		String m = (ms < 10 ? "0" : "") + ms;
		String s = (ss < 10 ? "0" : "") + ss;
		String f = m + ":" + s;
		return f;
	}

	public static void countDownTimer(){
		new BukkitRunnable(){
			public void run(){
				time--;
				CoreD.setBossBar("§e§lTempo restante: §a§l" + secToMin(time));	
				if(time < 11){
					for(Player p : Bukkit.getOnlinePlayers()){
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
					}
					Bukkit.broadcastMessage("§c§lTempo restante: §4§l" +time);
				}
				if(time == 0){
					cancel();
					Bukkit.broadcastMessage("§c§lAcabou o tempo!");
					Bukkit.broadcastMessage("§a§lIniciando a contagem de pontos...");
					Main.useEnderChest = false;
					CoreD.setCancelBlockBreakEvent(true);
					CountPoints.countPoints();
				}
			}
		}.runTaskTimer(Main.getInstance(), 0L, 20L);
	}
	
}
