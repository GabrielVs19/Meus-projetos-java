package ibieel.minigames.com;

import java.util.ArrayList;
import java.util.List;

import ibieel.minigames.com.Managers.ClassManager;
import ibieel.minigames.com.Managers.ClassManager.ClassType;
import ibieel.minigames.com.Managers.PlayerManager;
import ibieel.minigames.com.Util.ItemStackUtil;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GiveItems {

	public static void give(Player player, ClassType classType){
		giveTools(player, classType);
		player.setMaxHealth(ClassManager.getClass(classType).getHealth());
		player.setHealth(ClassManager.getClass(classType).getHealth());
		giveEquipaments(player);
		giveCompass(player);
		switch(classType){
		case BLAZE:
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
			break;
		default:
			break;
		}
	}
	
	public static void giveCompass(Player player) {
	    ItemStack compass = new ItemStack(Material.COMPASS);
	    ItemMeta compassM = compass.getItemMeta();
	    compassM.setDisplayName("§a§lRastreador");
	    compass.setItemMeta(compassM);
	    player.getInventory().addItem(compass);
	  }

	public static void giveEquipaments(Player player){
		ItemStack capacete = new ItemStack(Material.CHAINMAIL_HELMET);
		ItemMeta capaceteMeta = capacete.getItemMeta();
		capaceteMeta.addEnchant(Enchantment.DURABILITY, 50, true);
		capacete.setItemMeta(capaceteMeta);
		
		ItemStack peitoral = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		ItemMeta peitoralMeta = peitoral.getItemMeta();
		peitoralMeta.addEnchant(Enchantment.DURABILITY, 50, true);
		peitoral.setItemMeta(peitoralMeta);
		
		ItemStack calca = new ItemStack(Material.CHAINMAIL_LEGGINGS);
		ItemMeta calcaMeta = calca.getItemMeta();
		calcaMeta.addEnchant(Enchantment.DURABILITY, 50, true);
		calca.setItemMeta(calcaMeta);
		
		ItemStack bota = new ItemStack(Material.CHAINMAIL_BOOTS);
		ItemMeta botaMeta = bota.getItemMeta();
		botaMeta.addEnchant(Enchantment.DURABILITY, 50, true);
		bota.setItemMeta(botaMeta);
		
		player.getEquipment().setHelmet(capacete);
		player.getEquipment().setChestplate(peitoral);
		player.getEquipment().setLeggings(calca);
		player.getEquipment().setBoots(bota);
	}
	
	private static void giveTools(Player player, ClassType classType){
		boolean a = false;
		boolean b = false;
		for(Skills s : PlayerManager.getSkills(player.getName())){
			List<String> lore = new ArrayList<>();
			lore.add("§e§oCooldown: §f" + s.getCooldown() + "s");
			lore.add("§e§oTipo: §f" + s.getCastType().getName());
			switch(s.getSkillType()){
			case SWORD:
				ItemStack sword = ItemStackUtil.getItemStack(Material.IRON_SWORD, 1, "§9§l" + s.getName() + " §9(§e" +  s.getCastType().getName() + "§9)", lore);
				player.getInventory().setItem(0, sword);
				continue;
			case AXE:
				ItemStack axe = ItemStackUtil.getItemStack(Material.IRON_AXE, 1, "§9§l" + s.getName() + " §9(§e" +  s.getCastType().getName() + "§9)", lore);
				if(a){
					player.getInventory().setItem(2, axe);
				}else{
					player.getInventory().setItem(1, axe);
					b = true;
				}
				continue;
			case SHOVEL:
				ItemStack shovel = ItemStackUtil.getItemStack(Material.IRON_SPADE, 1, "§9§l" + s.getName() + " §9(§e" +  s.getCastType().getName() + "§9)", lore);
				if(b){
					player.getInventory().setItem(2, shovel);
				}else{
					player.getInventory().setItem(1, shovel);
					a = true;
				}
				continue;
			default:
				continue;
			}
		}
	}

}
