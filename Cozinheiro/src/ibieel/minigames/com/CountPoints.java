package ibieel.minigames.com;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import br.com.tlcm.cc.API.CoreDPlayer;

public class CountPoints {

	private static Map<String, Double> pontuacao = new HashMap<String, Double>();

	public static Map<String, Double> getTop15(){
		Map<String, Double> top = new HashMap<String, Double>();		
		for(int i = 0; i < 15; i++){
			String nick = null;
			double score = -1;
			for(Entry<String, Double> e : pontuacao.entrySet()){
				if(top.containsKey(e.getKey())){
					continue;
				}
				if(nick == null || e.getValue() > score){
					nick = e.getKey();
					score = e.getValue();
				}
			}
			if(nick != null){
				top.put(nick, score);
			}
		}
		return top;
	}

	public static void countPoints(){
		final ArrayList<Player> players = new ArrayList<Player>(CoreDPlayer.getPlayersWhoArentSpectators());
		for(Player p : players){
			p.closeInventory();
		}
		final Scoreboard sco = Bukkit.getScoreboardManager().getMainScoreboard();
		final Objective obj = sco.registerNewObjective("§6Pontos", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		pontuacao.clear();
		new BukkitRunnable(){
			int count = players.size() - 1;
			public void run(){	
				if(count >= 0){
					Player p = players.get(count);
					if(p == null){
						return;
					}
					Inventory enderchest = p.getEnderChest();
					ItemStack[] itens = enderchest.getContents();
					double score = 0;
					for(int i = 0; i < itens.length; i++){
						if(itens[i] == null){
							continue;
						}
						if(Main.getComidas().containsKey(itens[i].getType())){
							score += Main.getComidas().get(itens[i].getType()) * itens[i].getAmount();
						}
					}
					double pontos = score; 
					BigDecimal bd = new BigDecimal(pontos).setScale(2, RoundingMode.HALF_EVEN); 
					pontos = bd.doubleValue();
					p.sendMessage("§aParabéns! Você fez: §6" + pontos + " §apontos.");
					p.playSound(p.getLocation(), Sound.NOTE_PLING, 100, 1);
					pontuacao.put(p.getName(), score);
					for(Player p1 : Bukkit.getOnlinePlayers()) {
						sco.resetScores(p1);
					}
					for(Entry<String, Double> e : getTop15().entrySet()){
						@SuppressWarnings("deprecation")
						Score score1 = obj.getScore(Bukkit.getOfflinePlayer(e.getKey()));
						score1.setScore(e.getValue().intValue());
					}
				}else{
					StartAndFinish.finalizar();
					cancel();
				}
				count--;
			}
		}.runTaskTimer(Main.instance, 0, 20);


	}

}

