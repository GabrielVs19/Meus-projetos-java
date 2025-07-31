package br.com.gvs.CustomEnchants;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin{

	private static Plugin plugin;
	private static HashMap<Integer, String> enchants = new HashMap<>();
	public void onEnable(){
		plugin = this;
		resetEnchants();
		for(CustomEnchants cE : CustomEnchants.values()){
			registerEnchant(cE.getEnchantment());
		}
	}

	public static Plugin getPlugin(){
		return plugin;
	}
	
	@SuppressWarnings("deprecation")
	public static void registerEnchant(Enchantment ench){
		try{
			try {
				Field f = Enchantment.class.getDeclaredField("acceptingNew");
				f.setAccessible(true);
				f.set(null, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Enchantment.registerEnchantment(ench);
				enchants.put(ench.getId(), ench.getName());
			} catch (IllegalArgumentException e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void resetEnchants(){
		for(Entry<Integer, String> entry : enchants.entrySet() ){
			int id = entry.getKey();
			String name = entry.getValue();
			try {
				Field byIdField = Enchantment.class.getDeclaredField("byId");
				Field byNameField = Enchantment.class.getDeclaredField("byName");

				byIdField.setAccessible(true);
				byNameField.setAccessible(true);

				@SuppressWarnings("unchecked")
				HashMap<Integer, Enchantment> byId = (HashMap<Integer, Enchantment>) byIdField.get(id);
				@SuppressWarnings("unchecked")
				HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) byNameField.get(name);

				if(byId.containsKey(id)){
					byId.remove(id);
				}
				if(byName.containsKey(name)){
					byName.remove(name);
				}
			} catch (Exception e) { 
				e.printStackTrace();
			}
		}
		enchants.clear();
	}

}
