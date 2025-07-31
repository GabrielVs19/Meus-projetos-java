package br.com.gvs.GunGame;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author Gabriel
 *
 */
public class ItemStackUtil {

	/**
	 * @param material - Material do ItemStack a ser gerado
	 * @param qnt - Quantidade do item
	 * @param displayName - Nome do item
	 * @return - Retorna um ItemStack com as propriedades atribuidas nos parâmetros
	 */
	public static ItemStack getItemStack(Material material, int qnt, String displayName){
		ItemStack i = new ItemStack(material, qnt);
		ItemMeta iM = i.getItemMeta();
		iM.setDisplayName(displayName);
		i.setItemMeta(iM);
		return i;
	}


	/**
	 * @param material - Material do ItemStack a ser gerado
	 * @param qnt - Quantidade do item
	 * @param displayName - Nome do item
	 * @param data - Data do item
	 * @return - Retorna um ItemStack com as propriedades atribuidas nos parâmetros
	 */
	public static ItemStack getItemStack(Material material, int qnt, String displayName, short data){
		ItemStack i = new ItemStack(material, qnt, data);
		ItemMeta iM = i.getItemMeta();
		iM.setDisplayName(displayName);
		i.setItemMeta(iM);
		return i;
	}

	/**
	 * @param material - Material do ItemStack a ser gerado
	 * @param qnt - Quantidade do item
	 * @param displayName - Nome do item
	 * @param lore - Recebe um List<String> que será o lore do item
	 * @return - Retorna um ItemStack com as propriedades atribuidas nos parâmetros
	 */
	public static ItemStack getItemStack(Material material, int qnt, String displayName, List<String> lore){
		ItemStack i = new ItemStack(material, qnt);
		ItemMeta iM = i.getItemMeta();
		iM.setDisplayName(displayName);
		iM.setLore(lore);
		i.setItemMeta(iM);
		return i;
	}

	/**
	 * @param material - Material do ItemStack a ser gerado
	 * @param qnt - Quantidade do item
	 * @param displayName - Nome do item
	 * @param data - Data do item
	 * @param lore - Recebe um List<String> que será o lore do item
	 * @return - Retorna um ItemStack com as propriedades atribuidas nos parâmetros
	 */
	public static ItemStack getItemStack(Material material, int qnt, String displayName, List<String> lore, short data){
		ItemStack i = new ItemStack(material, qnt, data);
		ItemMeta iM = i.getItemMeta();
		iM.setDisplayName(displayName);
		iM.setLore(lore);
		i.setItemMeta(iM);
		return i;
	}

}
