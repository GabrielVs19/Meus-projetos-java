package br.com.gvs.CustomEnchants;

import org.bukkit.enchantments.Enchantment;

import br.com.gvs.CustomEnchants.Ench.Glow;
import br.com.gvs.CustomEnchants.Ench.Mending;


public enum CustomEnchants {

	GLOW(new Glow(100)), MENDING(new Mending(101));
	
	private Enchantment ench;
	CustomEnchants(Enchantment ench){
		this.ench = ench;
	}
	
	public Enchantment getEnchantment(){
		return this.ench;
	}
}
