package br.gvs.com.itemStackUtil;

import java.lang.reflect.Field;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemStackUtil extends JavaPlugin {

	public void onEnable(){
		System.out.println("ItemStackUtil enable - Author iBieel - v0.1");
		registerEnchant(new Glow(70));
	}

	public static void registerEnchant(Enchantment enchant){
		try {
			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Enchantment.registerEnchantment(enchant);
		}
		catch (IllegalArgumentException e){
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
