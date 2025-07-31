package br.com.gvs.GunGame;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 * @author Gabriel
 *
 */
public class Util {

	/**
	 * @param p - Player que sera dado uma armadura colorida aleatoria de couro
	 */
	public static void setPlayerRandomArmor(Player p){
		p.getEquipment().setBoots(getRandomLeatherArmorColor(Material.LEATHER_BOOTS));
		p.getEquipment().setLeggings(getRandomLeatherArmorColor(Material.LEATHER_LEGGINGS));
		p.getEquipment().setChestplate(getRandomLeatherArmorColor(Material.LEATHER_CHESTPLATE));
		p.getEquipment().setHelmet(getRandomLeatherArmorColor(Material.LEATHER_HELMET));
	}
	
	/**
	 * @param material - Parte da armadura
	 * @return - Retorna uma parte de armadura colorida aleatoriamente
	 */
	private static ItemStack getRandomLeatherArmorColor(Material material){
		int red = (int) (Math.random() * 254) + 1;
		int green = (int) (Math.random() * 254) + 1;
		int blue = (int) (Math.random() * 254) + 1;
		Color color = Color.fromRGB(red, green, blue);
		ItemStack armor = new ItemStack(material, 1);
		LeatherArmorMeta mArmor = (LeatherArmorMeta) armor.getItemMeta();
		mArmor.setColor(color);
		armor.setItemMeta(mArmor);
		return armor;
	}
	
}
