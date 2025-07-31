package ibieel.minigames.com.Managers;

import ibieel.minigames.com.StartAndFinish;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;
import br.com.tlcm.cc.API.enumerators.TeleportToLobbyReason;

public class ScoreboardManager1 {
	public static void updateScoreboard(){
		for(Player p : new ArrayList<Player>(CoreDPlayer.getPlayersWhoArentSpectators())){
			if(PlayerManager.getScoreboard(p.getName()) == null){
				PlayerManager.setScoreboard(p.getName(), Bukkit.getScoreboardManager().getNewScoreboard());
			}
			if(p.getScoreboard() != PlayerManager.getScoreboard(p.getName())){
				p.setScoreboard(PlayerManager.getScoreboard(p.getName()));
			}
			Objective obj = p.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
			if(obj != null){
				obj.unregister();
			}
			obj = p.getScoreboard().registerNewObjective("Vidas", "dummy");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			obj.setDisplayName("§e§lVIDAS §c♥");
			if(PlayerManager.getVidas(p.getName()) <= 0){
				CoreD.sendToLobby(p, TeleportToLobbyReason.LOSER);
				continue;
			}
			for(Player p1 : new ArrayList<Player>(CoreDPlayer.getPlayersWhoArentSpectators())){
				Team team = p.getScoreboard().getPlayerTeam(p1);
				if(team != null){
					team.unregister();	
				}
				team = p.getScoreboard().registerNewTeam(p1.getName());
				team.addPlayer(p1);
				int vidas = PlayerManager.getVidas(p1.getName());
				String prefixo = getCor(vidas) + ((p.getName() == p1.getName()) ? "§l" : "");
				team.setPrefix(prefixo);
				obj.getScore(p1).setScore(vidas);
			}
		}
		if(CoreDPlayer.getPlayersWhoArentSpectators().size() == 1){
			StartAndFinish.finishWinner();
		}
	}

	public static String getCor(int score){
		switch(score){
		case 4:
			return "§a";
		case 3:
			return "§e";
		case 2:
			return "§6";
		case 1:
			return "§4";
		case 0:
			return "§4";
		default:
			return "§a";
		}
	}

}
