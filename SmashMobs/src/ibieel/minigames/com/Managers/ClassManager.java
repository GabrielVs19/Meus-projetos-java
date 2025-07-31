package ibieel.minigames.com.Managers;



import ibieel.minigames.com.Util.SkillsUtil.Skills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;

import org.bukkit.inventory.ItemStack;

public class ClassManager {
	public enum ClassType{
		BLAZE, SPIDER, SKELETON, IRON_GOLEM, CHICKEN, CREEPER, WITHER_SKELETON, PIG, SNOWMAN, SLIME, WOLF, PIG_ZOMBIE;
	}
	public static Map<ClassType, ClassManager> classList = new HashMap<>();
	private double health;
	private double attackDamage;
	private DisguiseType disguiseType;
	private ItemStack iconMenu;
	private ArrayList<Skills> classSkills;
	private String name;
	
	public ClassManager(DisguiseType disguiseType, String name, double health, double attackDamage, ItemStack iconMenu, ArrayList<Skills> classSkills){
		this.health = health;
		this.disguiseType = disguiseType;
		this.attackDamage = attackDamage;
		this.iconMenu = iconMenu;
		this.classSkills = classSkills;
		this.name = name;
	}
	
	public static void addClass(ClassType classType, ClassManager classe){
		classList.put(classType, classe);
	}
	
	public static void removeClass(ClassType classType){
		classList.remove(classType);
	}
	
	public static ClassManager getClass(ClassType classType){
		if(classList.containsKey(classType)){
			return classList.get(classType);
		}
		return null;
	}
	
	public static ClassType getRandomClass(){
		@SuppressWarnings("unchecked")
		Entry<ClassType, ClassManager> classType = (Entry<ClassType, ClassManager>) classList.entrySet().toArray()[(int) ((classList.size() * Math.random()))];
		return classType.getKey();
	}
	
	public double getHealth(){
		return this.health;
	}
	
	public double getAttackDamage(){
		return this.attackDamage;
	}
	
	public DisguiseType getDisguiseType(){
		return this.disguiseType;
	}
	
	public ItemStack getIconMenu(){
		return this.iconMenu;
	}
	
	public ArrayList<Skills> getClassSkills(){
		return this.classSkills;
	}
	
	public String getName(){
		return this.name;
	}
}
