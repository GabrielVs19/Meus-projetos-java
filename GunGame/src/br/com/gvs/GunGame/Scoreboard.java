package br.com.gvs.GunGame;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import br.com.tlcm.cc.API.CoreDPlayer;


/**
 * @author Gabriel
 *
 */
public class Scoreboard {

	/**
	 * @param p - Player que sera atualizado o scoreboard
	 * Atualiza o scoreboard do player com as infos do PlayerManager
	 */
	@SuppressWarnings({ "deprecation" })
	public static void updateScoreboard(){
		for(Player p : CoreDPlayer.getPlayersWhoArentSpectators()){
			PlayerManager pM = PlayerManager.getPlayerManager(p.getName());
			if(pM.getScoreboard() == null){
				pM.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			}
			if(p.getScoreboard() != pM.getScoreboard()){
				p.setScoreboard(pM.getScoreboard());
			}
			Objective obj = pM.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
			if(obj != null){
				obj.unregister();
			}
			obj = p.getScoreboard().registerNewObjective("Gun Game", "dummy");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			obj.setDisplayName("§e§lGUN GAME");
			for(Entry<String, PlayerManager> e : PlayerManager.getPlayers().entrySet()){
				Team team = p.getScoreboard().getPlayerTeam(e.getValue().getPlayer());
				if(team == null){
					team = p.getScoreboard().registerNewTeam(e.getValue().getPlayer().getName());
				}
				team.addPlayer(e.getValue().getPlayer());
				team.setSuffix(" §9[§a" + e.getValue().getLevel() + "§9]");
			}
			obj.getScore(Bukkit.getOfflinePlayer(" ")).setScore(7);
			obj.getScore(Bukkit.getOfflinePlayer("§7Nível: §a" + pM.getLevel())).setScore(6);
			obj.getScore(Bukkit.getOfflinePlayer("§7Abates: §a" + pM.getAllKills())).setScore(5);
			obj.getScore(Bukkit.getOfflinePlayer("§7Mortes: §a" + pM.getDeaths())).setScore(4);
			int killsToNextLevel = GunLevels.Guns.getGunByLevel(pM.getLevel()).getKillsToNextGun() - pM.getLevelKills();
			obj.getScore(Bukkit.getOfflinePlayer("§7Restantes: §a" + killsToNextLevel)).setScore(3);
			obj.getScore(Bukkit.getOfflinePlayer("  ")).setScore(2);
			obj.getScore(Bukkit.getOfflinePlayer("§eCraftLandia")).setScore(1);
		}
	}

}
