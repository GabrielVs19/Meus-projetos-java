package br.com.gvs.menuVipB;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Menu {
	
	private static Inventory inv = Bukkit.createInventory(null, 54);
	
	public static void setupMenu(){
		
		inv.setItem(IconType.HAT.getSlot(), Icons.getIconHat());
		inv.setItem(IconType.TAG.getSlot(), Icons.getIcontTag());
		inv.setItem(IconType.FERTILIZAR.getSlot(), Icons.getIcontFertilizar());
		inv.setItem(IconType.MONTARIA.getSlot(), Icons.getIcontMontaria());
		inv.setItem(IconType.BENCH.getSlot(), Icons.getIcontBench());
		inv.setItem(IconType.BAU.getSlot(), Icons.getIconBau());
		inv.setItem(IconType.FURNACE.getSlot(), Icons.getIconFurnace());
		inv.setItem(IconType.KIT_NBVIP.getSlot(), Icons.getIconKitNbVip());
		inv.setItem(IconType.MC.getSlot(), Icons.getIconMc());
		inv.setItem(IconType.SPAWN.getSlot(), Icons.getIconSpawn());
		inv.setItem(IconType.DESENCANTAR.getSlot(), Icons.getIcontDesencantar());
		
		
		inv.setItem(IconType.WARP_VIP.getSlot(), Icons.getIconWarpVip());
		inv.setItem(IconType.WARP_REGION.getSlot(), Icons.getIconWarpRegion());
		inv.setItem(IconType.WARP_BLOCOS.getSlot(), Icons.getIconWarpBlocos());
		inv.setItem(IconType.MINERAR.getSlot(), Icons.getIconMinerar());
		inv.setItem(IconType.DUMMY.getSlot(), Icons.getIconWarpDummy());
		
	}
	
	public static void openMenu(Player player){
		
		Inventory inv2 = Bukkit.createInventory(null, 54, "§1§lMENU VIP");
		inv2.setContents(inv.getContents());
		
		inv2.setItem(IconType.PVP_OFF.getSlot(), Icons.getIconPvpOff(player));
		
		inv2.setItem(IconType.LAST_DEATH.getSlot(), Icons.getIconLastDeath(player));
		inv2.setItem(IconType.LAST_KILL.getSlot(), Icons.getIconLastKill(player));
		inv2.setItem(IconType.VIP.getSlot(), Icons.getIconTempoVip(player));
		inv2.setItem(IconType.KARMA.getSlot(), Icons.getIconKarma(player));
		inv2.setItem(IconType.INSURE.getSlot(), Icons.getIconInsure(player));
		
	 for(int i = 0; i <= inv2.getSize() -1; i++){
		 if(inv2.getItem(i) == null || inv2.getItem(i).getType() == Material.AIR){
			 ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(" ");
				item.setItemMeta(meta);
				item.setDurability((short) 7);
				inv2.setItem(i, item);
		 }
	 }
		
		player.openInventory(inv2);
		
		
	}

}
