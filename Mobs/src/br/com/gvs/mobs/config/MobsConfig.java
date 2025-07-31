package br.com.gvs.mobs.config;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import br.com.gvs.mobs.Mobs;
import br.com.gvs.mobs.managers.MobManager;
import br.com.gvs.mobs.util.AttackType;
import br.com.gvs.mobs.util.Mob;
import br.com.gvs.mobs.util.MobType;
import br.com.gvs.mobs.util.SpawnRadius;
import br.com.smooph.treasures.Treasures;
import br.com.smooph.treasures.utils.Book;


public class MobsConfig
{
	
	
	private static int maxPerChunk = 3;
	
	public static int getMaxPerChunk()
	{
		return maxPerChunk;
	}
	
	public static void loadConfig()
	{
		Mobs.getInstance().getConfig().set("maxPerChunk", Mobs.getInstance().getConfig().get("maxPerChunk", maxPerChunk));
		Mobs.getInstance().saveConfig();
		
		maxPerChunk = Mobs.getInstance().getConfig().getInt("maxPerChunk");
		
		File mobsFolder = new File(Mobs.getInstance().getDataFolder(), "Mobs");
		if(!mobsFolder.exists())
		{
			mobsFolder.mkdir();
		}
		
		File[] m = mobsFolder.listFiles();
		List<File> mobs = new ArrayList<File>();
		
		for(File file : m)
		{
			if(file.getName().toLowerCase().endsWith(".yml"))
			{
				mobs.add(file);
			}
		}
		
		if(mobs.size() == 0)
		{
			for(MobType mobType : MobType.values())
			{
				String mobName = mobType.name();
				
				File mobFile = new File(mobsFolder, "/" + mobName + ".yml");
				
				if(mobFile.exists())
				{
					continue;
				}
				
				InputStream is = Mobs.getInstance().getClass().getResourceAsStream("/br/com/gvs/mobs/resources/" + mobName + ".yml");
				
				if(is == null)
				{
					continue;
				}
				BufferedWriter bw = null;
				try
				{
					String sourceText = IOUtils.toString(is, "UTF-8");
					bw = new BufferedWriter(new FileWriter(mobFile));
					bw.write(sourceText);
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				finally
				{
					try
					{
						bw.close();
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
				}
				
				mobs.add(mobFile);
			}
		}
		
		for(File config : mobs)
		{
			FileConfiguration fconfig = YamlConfiguration.loadConfiguration(config);
			
			try
			{
				loadMob(fconfig);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Mobs.getInstance().getLogger().log(Level.INFO, config.getName() + " loading error!");
			}
		}
	}
	
	private static void loadMob(FileConfiguration config)
	{
		MobType mobType = MobType.getMobByID(config.getInt("id"));
		ArrayList<Mob> mobs = new ArrayList<>();
		
		for(String key : config.getKeys(false))
		{
			if(!key.equalsIgnoreCase("id"))
			{
				int level = Integer.parseInt(key);
				double attackDamage = config.getDouble(key + ".attackDamage");
				double moveSpeed = config.getDouble(key + ".moveSpeed");
				double health = config.getDouble(key + ".health");
				double followRange = config.getDouble(key + ".followRange");
				double knockbackResistence = config.getDouble(key + ".knockbackResistence");
				double reward = config.getDouble(key + ".reward");
				AttackType attackType = AttackType.getAttackTypeByID(config.getInt(key + ".attackType"));
				Map<ItemStack, Double> drops = loadDrops((ArrayList<String>) config.getStringList(key + ".drops"));
				int maxDrops = config.getInt(key + ".maxDrops");
				HashMap<Integer, ItemStack> equipments = loadEquipments((ArrayList<String>) config.getStringList(key + ".equipments"));
				
				Map<String, SpawnRadius> spawnRadius = new HashMap<String, SpawnRadius>();
				
				for(World world : Bukkit.getWorlds())
				{
					double min = config.getDouble(key + ".spawn." + world.getName() + ".min");
					double max = config.getDouble(key + ".spawn." + world.getName() + ".max");
					
					if(max > min && min >= 0)
					{
						spawnRadius.put(world.getName(), new SpawnRadius(min, max));
					}
				}
				
				Mob mob = new Mob(mobType, level, attackDamage, moveSpeed, health, followRange, knockbackResistence, reward, attackType, drops, maxDrops, equipments, spawnRadius);
				mobs.add(mob);
			}
			
		}
		
		MobManager mobManager = new MobManager(mobType, mobs);
		MobManager.addMob(mobManager);
	}
	
	public static HashMap<Integer, ItemStack> loadEquipments(ArrayList<String> list)
	{
		HashMap<Integer, ItemStack> equipments = new HashMap<>();
		for(String s : list)
		{
			String[] dataS = s.split(";");
			@SuppressWarnings("deprecation")
			ItemStack is = new ItemStack(Material.getMaterial(Integer.parseInt(dataS[0].split(":")[0])), Integer.parseInt(dataS[0].split(":")[2]), Short.parseShort(dataS[0].split(":")[1]));
			Integer slot = Integer.parseInt(dataS[1]);
			if(dataS.length >= 3)
			{
				for(String ench : dataS[2].split(","))
				{
					String[] enchData = ench.split(":");
					is.addUnsafeEnchantment(Enchantment.getByName(enchData[0]), Integer.parseInt(enchData[1]));
				}
			}
			equipments.put(slot, is);
		}
		return equipments;
	}
	
	public static Map<ItemStack, Double> loadDrops(ArrayList<String> list)
	{
		HashMap<ItemStack, Double> drops = new HashMap<>();
		double porcentagem = 0.0;
		
		for(String s : list)
		{
			String[] dataS = s.split(";");
			porcentagem = Double.parseDouble(dataS[1]);
			if(dataS[0].split(":")[0].equalsIgnoreCase("book"))
			{
				ItemStack enchantedBook = new ItemStack(Material.ENCHANTED_BOOK, Integer.parseInt(dataS[0].split(":")[2]));
				EnchantmentStorageMeta ebM = (EnchantmentStorageMeta) enchantedBook.getItemMeta();
				if(dataS.length >= 3)
				{
					for(String ench : dataS[2].split(","))
					{
						String[] enchData = ench.split(":");
						ebM.addStoredEnchant(Enchantment.getByName(enchData[0]), Integer.parseInt(enchData[1]), true);
					}
					enchantedBook.setItemMeta(ebM);
				}
				drops.put(enchantedBook, porcentagem);
				continue;
			}
			
			if(dataS[0].split(":")[0].equalsIgnoreCase("book_treasure"))
			{
				Book book = Treasures.getBookByID(Integer.parseInt(dataS[0].split(":")[1]));
				drops.put(book.getItemStack(), porcentagem);
				continue;
			}
			
			@SuppressWarnings("deprecation")
			ItemStack is = new ItemStack(Material.getMaterial(Integer.parseInt(dataS[0].split(":")[0])), Integer.parseInt(dataS[0].split(":")[2]), Short.parseShort(dataS[0].split(":")[1]));
			if(dataS.length >= 3)
			{
				for(String ench : dataS[2].split(","))
				{
					String[] enchData = ench.split(":");
					is.addUnsafeEnchantment(Enchantment.getByName(enchData[0]), Integer.parseInt(enchData[1]));
				}
			}
			drops.put(is, porcentagem);
		}
		return drops;
	}
	
}
