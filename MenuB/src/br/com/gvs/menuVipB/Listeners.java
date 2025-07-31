package br.com.gvs.menuVipB;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import br.com.gvs.menuVipB.IconType.ClickType;
import br.com.tlcm.pvpd.PvPData;

public class Listeners implements Listener{

	public static HashMap<String, String> lastKill = new HashMap<>();
	public static HashMap<String, String> lastDeath = new HashMap<>();


	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		Inventory inv = e.getClickedInventory();
		ItemStack item = e.getCurrentItem();

		if(inv == null || e.getClickedInventory().getName() == null){
			return;
		}

		if(inv.getName().startsWith("§1§lMENU VIP")){
			e.setCancelled(true);

			if(item == null || item.getType() == Material.AIR){
				e.getWhoClicked().closeInventory();
				Menu.openMenu((Player) e.getWhoClicked());
				return;
			}

			IconType ic = IconType.getIconTypeBySlot(e.getSlot());

			if(ic == null){
				e.getWhoClicked().closeInventory();
				Menu.openMenu((Player) e.getWhoClicked());
				return;
			}

			if(ic.getClickType() != ClickType.COMMAND){
				e.getWhoClicked().closeInventory();
				Menu.openMenu((Player) e.getWhoClicked());
				return;
			}
			String command = ic.getCommand();
			if(ic == IconType.PVP_OFF){
				boolean pvp = PvPData.getPvP((Player) e.getWhoClicked()).isPvPDisabled();
				if(pvp){
					command = "/pvp on";
				}
			}

			e.getWhoClicked().closeInventory();
			((Player)e.getWhoClicked()).playSound(((Player)e.getWhoClicked()).getLocation(), Sound.ANVIL_LAND, 100, 100);
			((Player)e.getWhoClicked()).chat(command);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		if(e.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent lastDmg = (EntityDamageByEntityEvent) e.getEntity().getLastDamageCause();
			if(getDamager(lastDmg.getDamager()) != null){
				Player damager = getDamager(lastDmg.getDamager());
				lastKill.put(damager.getName(), e.getEntity().getName());
				lastDeath.put(e.getEntity().getName(), damager.getName());
			}
		}
	}


	@SuppressWarnings("deprecation")
	private Player getDamager(Entity entity){
		if ((entity instanceof Player)) {
			return (Player)entity;
		}
		if ((entity instanceof Projectile)){
			LivingEntity livingEntity = ((Projectile)entity).getShooter();
			if ((livingEntity instanceof Player)) {
				return (Player)livingEntity;
			}
			return null;
		}
		if ((entity instanceof ThrownPotion)){
			LivingEntity livingEntity = ((ThrownPotion)entity).getShooter();
			if ((livingEntity instanceof Player)) {
				return (Player)livingEntity;
			}
			return null;
		}
		return null;
	}




}
