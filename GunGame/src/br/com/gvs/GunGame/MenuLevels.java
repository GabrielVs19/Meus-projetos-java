package br.com.gvs.GunGame;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.gvs.GunGame.GunLevels.Guns;

/**
 * @author Gabriel
 *
 */
public class MenuLevels implements Listener{
	private static Inventory menu;
	
	public MenuLevels(){
		buildMenu();
		Main.getPlugin().getServer().getPluginManager().registerEvents(this, Main.getPlugin());
	}
	
	/**
	 * @param e
	 * Impede que o jogador pegue os itens do menu
	 */
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		if(e.getInventory().getName().equalsIgnoreCase(menu.getName())){
			e.setCancelled(true);
		}
	}
	
	/**
	 * Gera o inventário com os itens dos níveis
	 */
	private void buildMenu(){
		Inventory m = Bukkit.createInventory(null, 18, "§6Armas");
		for(Guns g : Guns.values()){
			ItemStack item = g.getGun();
			ItemMeta iM = item.getItemMeta();
			String[] lore = {"§eLevel: §f" + g.getLevel(), "§eAbates para prox. arma: §f" + g.getKillsToNextGun(), "§eDano: §f" + g.getDamage()};
			iM.setLore(Arrays.asList(lore));
			item.setItemMeta(iM);
			m.addItem(item);
		}
		menu = m;
	}
	
	/**
	 * @return - Retorna o inventário gerado pelo método buildMenu()
	 */
	public static Inventory getMenu(){
		return menu;
	}

}
