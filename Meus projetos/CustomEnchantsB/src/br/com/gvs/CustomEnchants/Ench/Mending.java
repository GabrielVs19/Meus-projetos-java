package br.com.gvs.CustomEnchants.Ench;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.ItemStack;

import br.com.gvs.CustomEnchants.CustomEnchants;
import br.com.gvs.CustomEnchants.Main;

public class Mending extends Enchantment implements Listener{

	public Mending(int id) {
		super(id);
		Main.getPlugin().getServer().getPluginManager().registerEvents(this, Main.getPlugin());
	}

	@Override
	public boolean canEnchantItem(ItemStack i) {
	//	String type = i.getType().toString();
	//	return type.contains("PICKAXE");
		return true;
	}

	@Override
	public boolean conflictsWith(Enchantment ench){
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget(){
		return EnchantmentTarget.TOOL;
	}

	@Override
	public int getMaxLevel() {
		return 2;
	}

	@Override
	public String getName() {
		return "Auto-conserto";
	}

	@Override
	public int getStartLevel() {
		return 1;
	}
	
	@EventHandler
	public void onExpChange(PlayerExpChangeEvent e){
		Player player = e.getPlayer();
		ItemStack item = player.getItemInHand();
		if(item != null && item.hasItemMeta() && item.getItemMeta().hasEnchants() && item.containsEnchantment(CustomEnchants.MENDING.getEnchantment())){
			int exp = e.getAmount();
			int durability = item.getDurability();
			int mending = ((item.getEnchantmentLevel(CustomEnchants.MENDING.getEnchantment()) * 4) + exp);
			player.sendMessage(durability + " - " + exp);
			durability = durability - mending;
			item.setDurability((short)(durability < 0 ? 0 : durability));
		}
	}

}
