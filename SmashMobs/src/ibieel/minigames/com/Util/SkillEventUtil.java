package ibieel.minigames.com.Util;

import java.util.ArrayList;
import java.util.List;

import ibieel.minigames.com.Managers.PlayerManager;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class SkillEventUtil {

	public static List<Entity> getNearbyEntitiesFromLocation(Location loc, double raio){
		List<Entity> entities = new ArrayList<Entity>();
		for(Entity ent : loc.getWorld().getEntities()){
			double distance = loc.distance(ent.getLocation());
			if(distance <= raio){
				entities.add(ent);
			}
		}
		return entities;
	}

	public static boolean playerHasSkill(Player player, Skills skill){
		if(PlayerManager.getSkills(player.getName()).contains(skill)){
			return true;
		}else{
			return false;
		}
	}
	
	public static void sendUseSkillMessage(Player player, Skills skill){
		player.sendMessage("§9Skill> §7Você usou sua skill §a" + skill.getName() + "§7.");
	}

	public static void soundCooldownSkill(Player player){
		player.playSound(player.getLocation(), Sound.NOTE_STICKS, 100, 1);
	}

	public static boolean rightClickSword(Action action, Player player){
		if(action.name().contains("RIGHT") && player.getItemInHand().getType().name().contains("SWORD")){
			return true;
		}else{
			return false;
		}
	}

	public static boolean leftClickSword(Action action, Player player){
		if(action.name().contains("LEFT") && player.getItemInHand().getType().name().contains("SWORD")){
			return true;
		}else{
			return false;
		}
	}

	public static boolean rightClickAxe(Action action, Player player){
		if(action.name().contains("RIGHT") && player.getItemInHand().getType().name().contains("AXE")){
			return true;
		}else{
			return false;
		}
	}

	public static boolean leftClickAxe(Action action, Player player){
		if(action.name().contains("LEFT") && player.getItemInHand().getType().name().contains("AXE")){
			return true;
		}else{
			return false;
		}
	}

	public static boolean rightClickShovel(Action action, Player player){
		if(action.name().contains("RIGHT") && player.getItemInHand().getType().name().contains("SPADE")){
			return true;
		}else{
			return false;
		}
	}

	public static boolean leftClickShovel(Action action, Player player){
		if(action.name().contains("LEFT") && player.getItemInHand().getType().name().contains("SPADE")){
			return true;
		}else{
			return false;
		}
	}

}
