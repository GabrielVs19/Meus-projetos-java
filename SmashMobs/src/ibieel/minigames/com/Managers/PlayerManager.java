package ibieel.minigames.com.Managers;

import ibieel.minigames.com.Managers.ClassManager.ClassType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.scoreboard.Scoreboard;

public class PlayerManager {

	private static HashMap<String, PlayerManager> players = new HashMap<String, PlayerManager>();
	private ClassType playerClass;
	private int vidas;
	private ArrayList<Skills> playerSkills;
	private Scoreboard scoreboard;
	public PlayerManager(ClassType classType){
		playerClass = classType;
		playerSkills = ClassManager.getClass(classType).getClassSkills();
		this.vidas = 0;
	}

	public static int getVidas(String player){
		if(players.containsKey(player)){
			return players.get(player).vidas;
		}else{
			return 0;
		}
	}

	public static Scoreboard getScoreboard(String player){
		return players.get(player).scoreboard;
	}

	public static void setScoreboard(String player, Scoreboard sco){
		if(players.containsKey(player)){
			PlayerManager pm = players.get(player);
			players.remove(player);
			pm.scoreboard = sco;
			players.put(player, pm);
		}
	}

	public static void setVidas(String player, int vidas){
		if(players.containsKey(player)){
			PlayerManager pm = players.get(player);
			players.remove(player);
			pm.vidas = vidas;
			players.put(player, pm);
		}
	}

	public static void add(String player, PlayerManager manager){
		if(players.containsKey(player)){
			players.remove(player);
		}
		players.put(player, manager);
	}

	public static ArrayList<Skills> getSkills(String player){
		return players.get(player).playerSkills;
	}

	public static ClassType getClasse(String player){
		return players.get(player).playerClass;
	}

	public static boolean hasClass(String player){
		return players.containsKey(player);
	}
}
