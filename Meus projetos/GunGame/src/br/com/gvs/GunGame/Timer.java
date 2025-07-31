package br.com.gvs.GunGame;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreD;

/**
 * @author Gabriel
 *
 */
public class Timer {

	private int time;
	public Timer(int time){
		this.time = time;
		start();
	}

	/**
	 * Inicia o timer
	 */
	private void start(){
		new BukkitRunnable(){
			@Override
			public void run() {
				time--;
				CoreD.setBossBar("§6§lTEMPO RESTANTE: §C§l" + secToMin(time));
				if(time == 60){
					Bukkit.broadcastMessage("§6Tempo restante: §4§l1 minuto");
					for(Player p : Bukkit.getOnlinePlayers()){
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
					}
				}
				if(time < 11){
					for(Player p : Bukkit.getOnlinePlayers()){
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
					}
					Bukkit.broadcastMessage("§c§lTEMPO RESTANTE: §4§l" + time);
				}
				if(time == 0){
					StartAndFinish.finishTime();
					cancel();
				}
			}
		}.runTaskTimer(Main.getPlugin(), 0, 20);
	}

	/**
	 * @param i - Segundos
	 * @return - Retorna uma string de minuto
	 */
	private String secToMin(int i){
		int ms = i / 60;
		int ss = i % 60;
		String m = (ms < 10 ? "0" : "") + ms;
		String s = (ss < 10 ? "0" : "") + ss;
		String f = m + ":" + s;
		return f;
	}

}
