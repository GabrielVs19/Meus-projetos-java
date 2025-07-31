package ibieel.minigames.com;

import ibieel.minigames.com.Managers.ClassManager;
import ibieel.minigames.com.Managers.ClassManager.ClassType;
import ibieel.minigames.com.Util.ItemStackUtil;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import java.util.ArrayList;
import java.util.List;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Classes{
	
	
	private static List<String> getLore(ArrayList<Skills> skills, double health, double attackDamage){
		List<String> lore = new ArrayList<String>();
		lore.add("§oCaracteristicas:");
		lore.add("  §c§oVida: " + health);
		lore.add("  §9§oDano ataque: " + attackDamage);
		lore.add(" ");
		lore.add("§oSkills:");
		for(Skills a : skills){
			lore.add("  §e§o- " + a.getName());
		} 
		return lore;
	}
	
	public static void addPigman(){
		String name = "§c§lPorco zumbi";
		double health = 35.0;
		double attackDamage = 3.5;
		DisguiseType disguiseType = DisguiseType.PIG_ZOMBIE;
		
		ArrayList<Skills> skills = new ArrayList<Skills>();
		skills.add(Skills.DOUBLE_JUMP);
		skills.add(Skills.ALIADOS);
		skills.add(Skills.LANCA_CHAMAS);
		skills.add(Skills.FURIA);
		
		ItemStack iconMenu = ItemStackUtil.getItemStack(Material.MONSTER_EGG, 1, name, getLore(skills, health, attackDamage), (short) 57);
		
		ClassManager.addClass(ClassType.PIG_ZOMBIE, new ClassManager(disguiseType, name, health, attackDamage, iconMenu, skills));
	}
	
	
	public static void addWolf(){
		String name = "§f§lLobo";
		double health = 30.0;
		double attackDamage = 3.5;
		DisguiseType disguiseType = DisguiseType.WOLF;
		
		ArrayList<Skills> skills = new ArrayList<Skills>();
		skills.add(Skills.DOUBLE_JUMP);
		skills.add(Skills.ALCATEIA);
		skills.add(Skills.UIVO);
		skills.add(Skills.MORDIDA_MORTAL);
		
		ItemStack iconMenu = ItemStackUtil.getItemStack(Material.MONSTER_EGG, 1, name, getLore(skills, health, attackDamage), (short) 95);
		
		ClassManager.addClass(ClassType.WOLF, new ClassManager(disguiseType, name, health, attackDamage, iconMenu, skills));
	}
	
	public static void addSlime(){
		String name = "§a§lSlime";
		double health = 30.0;
		double attackDamage = 4.0;
		DisguiseType disguiseType = DisguiseType.SLIME;
		
		ArrayList<Skills> skills = new ArrayList<Skills>();
		skills.add(Skills.DOUBLE_JUMP);
		skills.add(Skills.ENCOLHER);
		skills.add(Skills.GOSMA_VENENOSA);
		skills.add(Skills.EXPLOSAO_DE_MASSA);
		
		ItemStack iconMenu = ItemStackUtil.getItemStack(Material.MONSTER_EGG, 1, name, getLore(skills, health, attackDamage), (short) 55);
		
		ClassManager.addClass(ClassType.SLIME, new ClassManager(disguiseType, name, health, attackDamage, iconMenu, skills));
	}
	
	public static void addPig(){
		String name = "§c§lPorco";
		double health = 30.0;
		double attackDamage = 3.5;
		DisguiseType disguiseType = DisguiseType.PIG;
		
		ArrayList<Skills> skills = new ArrayList<Skills>();
		skills.add(Skills.DOUBLE_JUMP);
		skills.add(Skills.BACON);
		skills.add(Skills.CLONAGEM);
		skills.add(Skills.PORCO_EXPLOSIVO);
		
		ItemStack iconMenu = ItemStackUtil.getItemStack(Material.MONSTER_EGG, 1, name, getLore(skills, health, attackDamage), (short) 90);
		
		ClassManager.addClass(ClassType.PIG, new ClassManager(disguiseType, name, health, attackDamage, iconMenu, skills));
	}
	
	/*public static void addEnderman(){
		String name = "§5§lEnderman";
		double health = 30.0;
		double attackDamage = 4.5;
		DisguiseType disguiseType = DisguiseType.ENDERMAN;
		
		ArrayList<Skills> skills = new ArrayList<Skills>();
		skills.add(Skills.DOUBLE_JUMP);
		skills.add(Skills.TELEPORTE);
		skills.add(Skills.LADRAO_DE_BLOCOS);
		
		ItemStack iconMenu = ItemStackUtil.getItemStack(Material.MONSTER_EGG, 1, name, getLore(skills, health, attackDamage), (short) 58);
		
		ClassManager.addClass(ClassType.ENDERMAN, new ClassManager(disguiseType, name, health, attackDamage, iconMenu, skills));
	}
	*/
	
	public static void addWitherSkeleton(){
		String name = "§8§lEsqueleto do wither";
		double health = 34.0;
		double attackDamage = 4.0;
		DisguiseType disguiseType = DisguiseType.WITHER_SKELETON;
		
		ArrayList<Skills> skills = new ArrayList<Skills>();
		skills.add(Skills.DOUBLE_JUMP);
		skills.add(Skills.ATIRADOR_DE_CABECAS);
		skills.add(Skills.VENENO_DO_WITHER);
		skills.add(Skills.REGENERAR_VIDA);
		
		ItemStack iconMenu = ItemStackUtil.getItemStack(Material.MONSTER_EGG, 1, name, getLore(skills, health, attackDamage), (short) 60);
		
		ClassManager.addClass(ClassType.WITHER_SKELETON, new ClassManager(disguiseType, name, health, attackDamage, iconMenu, skills));
	}
	
	public static void addSnowman(){
		String name = "§b§lBoneco de neve";
		double health = 30.0;
		double attackDamage = 3.0;
		DisguiseType disguiseType = DisguiseType.SNOWMAN;
		
		ArrayList<Skills> skills = new ArrayList<Skills>();
		skills.add(Skills.DOUBLE_JUMP);
		skills.add(Skills.CONGELAR);
		skills.add(Skills.BOLA_DE_NEVE);
		skills.add(Skills.PASSOS_CONGELANTES);
		
		ItemStack iconMenu = ItemStackUtil.getItemStack(Material.PUMPKIN, 1, name, getLore(skills, health, attackDamage));
		
		ClassManager.addClass(ClassType.SNOWMAN, new ClassManager(disguiseType, name, health, attackDamage, iconMenu, skills));
	}
	
	public static void addCreeper(){
		String name = "§a§lCreeper";
		double health = 30.0;
		double attackDamage = 3.5;
		DisguiseType disguiseType = DisguiseType.CREEPER;
		
		ArrayList<Skills> skills = new ArrayList<Skills>();
		skills.add(Skills.DOUBLE_JUMP);
		skills.add(Skills.GRANADA);
		skills.add(Skills.ESCUDO_MAGICO);
		skills.add(Skills.AUTO_EXPLOSAO);
		
		ItemStack iconMenu = ItemStackUtil.getItemStack(Material.MONSTER_EGG, 1, name, getLore(skills, health, attackDamage), (short) 50);
		
		ClassManager.addClass(ClassType.CREEPER, new ClassManager(disguiseType, name, health, attackDamage, iconMenu, skills));
	}
	
	public static void addSpider(){
		String name = "§C§lAranha";
		double health = 30.0;
		double attackDamage = 4.0;
		DisguiseType disguiseType = DisguiseType.SPIDER;
		
		ArrayList<Skills> skills = new ArrayList<Skills>();
		skills.add(Skills.DOUBLE_JUMP);
		skills.add(Skills.TEIA);
		skills.add(Skills.VENENO);
		skills.add(Skills.BOTE);
		skills.add(Skills.ESCALAR_PAREDES);
		skills.add(Skills.PRISAO_DE_TEIA);
		
		ItemStack iconMenu = ItemStackUtil.getItemStack(Material.MONSTER_EGG, 1, name, getLore(skills, health, attackDamage), (short) 52);
		
		ClassManager.addClass(ClassType.SPIDER, new ClassManager(disguiseType, name, health, attackDamage, iconMenu, skills));
	}
	
	public static void addIronGolem(){
		String name = "§f§lGolem";
		double health = 35.0;
		double attackDamage = 4.0;
		DisguiseType disguiseType = DisguiseType.IRON_GOLEM;
		
		ArrayList<Skills> skills = new ArrayList<Skills>();
		skills.add(Skills.DOUBLE_JUMP);
		skills.add(Skills.ABALO_SISMICO);
		skills.add(Skills.SALTO_SISMICO);
		skills.add(Skills.TERREMOTO);
		
		ItemStack iconMenu = ItemStackUtil.getItemStack(Material.MONSTER_EGG, 1, name, getLore(skills, health, attackDamage));
		
		ClassManager.addClass(ClassType.IRON_GOLEM, new ClassManager(disguiseType, name, health, attackDamage, iconMenu, skills));
	}
	
	public static void addChicken(){
		String name = "§f§lGalinha";
		double health = 30.0;
		double attackDamage = 3.0;
		DisguiseType disguiseType = DisguiseType.CHICKEN;
		
		ArrayList<Skills> skills = new ArrayList<Skills>();
		skills.add(Skills.DOUBLE_JUMP_GALINHA);
		skills.add(Skills.RAJADA_DE_OVOS);
		skills.add(Skills.GALINHA_EXPLOSIVA);
		
		ItemStack iconMenu = ItemStackUtil.getItemStack(Material.MONSTER_EGG, 1, name, getLore(skills, health, attackDamage), (short) 93);
		
		ClassManager.addClass(ClassType.CHICKEN, new ClassManager(disguiseType, name, health, attackDamage, iconMenu, skills));
	}
	
	public static void addSkeleton(){
		String name = "§7§lEsqueleto";
		double health = 30.0;
		double attackDamage = 4.0;
		DisguiseType disguiseType = DisguiseType.SKELETON;
		
		ArrayList<Skills> skills = new ArrayList<Skills>();
		skills.add(Skills.DOUBLE_JUMP);
		skills.add(Skills.RAJADA_DE_FLECHAS);
		skills.add(Skills.EXPLOSAO_DE_OSSOS);
		
		ItemStack iconMenu = ItemStackUtil.getItemStack(Material.MONSTER_EGG, 1, name, getLore(skills, health, attackDamage), (short) 51);
		
		ClassManager.addClass(ClassType.SKELETON, new ClassManager(disguiseType, name, health, attackDamage, iconMenu, skills));
	}
	
	public static void addBlaze(){
		String name = "§6§lBlaze";
		double health = 30.0;
		double attackDamage = 4.0;
		DisguiseType disguiseType = DisguiseType.BLAZE;
		
		ArrayList<Skills> skills = new ArrayList<Skills>();
		skills.add(Skills.DOUBLE_JUMP);
		skills.add(Skills.INFERNO);
		skills.add(Skills.AVANCO_MORTIFERO);
		skills.add(Skills.VELOCIDADE);
		
		ItemStack iconMenu = ItemStackUtil.getItemStack(Material.MONSTER_EGG, 1, name, getLore(skills, health, attackDamage), (short) 61);
		
		ClassManager.addClass(ClassType.BLAZE, new ClassManager(disguiseType, name, health, attackDamage, iconMenu, skills));
	}

}



