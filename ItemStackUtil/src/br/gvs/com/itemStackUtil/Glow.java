package br.gvs.com.itemStackUtil;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class Glow extends Enchantment{

	public Glow(int id) {
		super(id);
	}

	@Override
	public boolean canEnchantItem(ItemStack i) {
		return false;
	}

	@Override
	public boolean conflictsWith(Enchantment e) {
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return null;
	}

	@Override
	public int getMaxLevel() {
		return 0;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public int getStartLevel() {
		return 0;
	}	

}
