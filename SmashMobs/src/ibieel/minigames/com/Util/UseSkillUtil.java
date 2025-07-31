package ibieel.minigames.com.Util;

import ibieel.minigames.com.Util.SkillsUtil.Skills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

public class UseSkillUtil {

	private static HashMap<Skills, List<String>> useSkill = new HashMap<>();


	public static void add(Player player, Skills skill, boolean msg){
		List<String> players = null;
		if(useSkill.get(skill) != null){
			players = useSkill.get(skill);	
		}else{
			players = new ArrayList<String>();
		}
		players.add(player.getName());
		useSkill.put(skill, players);
		if(msg){
			player.sendMessage("§9Skill> §7Você usou sua skill §a" + skill.getName() + "§7.");
		}
	}
	
	public static void remove(Player player, Skills skill, boolean msg){
		List<String> players = null;
		if(useSkill.get(skill) != null){
			players = useSkill.get(skill);	
		}else{
			players = new ArrayList<String>();
		}
		players.remove(player.getName());
		useSkill.put(skill, players);
		if(msg){
			player.sendMessage("§9Skill> §7Você não está mais usando a sua skill §a" + skill.getName() + "§7.");
		}
	}
	
	public static boolean using(Player player, Skills skill){
		List<String> players = null;
		if(useSkill.get(skill) != null){
			players = useSkill.get(skill);	
		}else{
			players = new ArrayList<String>();
		}
		if(players.contains(player.getName())){
			return true;
		}else{
			return false;
		}
	}

}
