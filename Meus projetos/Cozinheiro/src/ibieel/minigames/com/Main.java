package ibieel.minigames.com;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.libraryaddict.virtual.Furnaces;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.MiniGame;


public class Main extends JavaPlugin{

	private static Map<Material, Double> comidas = new HashMap<Material, Double>();
	private static Map<EntityType, Integer> mobs = new HashMap<EntityType, Integer>();
	private static List<Material> plantacoes = null;
	private static List<Location> spawnsMobs = new ArrayList<Location>();
	public static boolean useEnderChest = false;
	private static Inventory comidasInv = null;
	public static Main instance;

	public void onEnable(){
		instance = this;
		Scoreboard sco = Bukkit.getScoreboardManager().getMainScoreboard();
		if(sco.getObjective(DisplaySlot.SIDEBAR) != null){
			sco.getObjective(DisplaySlot.SIDEBAR).unregister();
		}
		for(Entity e : Bukkit.getWorlds().get(0).getEntities()){
			if(!(e instanceof Player)){
				e.remove();
			}
		}
		Bukkit.getWorlds().get(0).setTime(700);
		Bukkit.getWorlds().get(0).setStorm(false);
		Bukkit.getWorlds().get(0).setGameRuleValue("mobGriefing", "false");
		Bukkit.getWorlds().get(0).setGameRuleValue("doMobSpawning", "false");
		Bukkit.getWorlds().get(0).setGameRuleValue("doDaylightCycle", "false");
		CoreD.setAllowJoinSpectators(true);
		CoreD.setCancelBlockBreakEvent(true);
		CoreD.setCancelBlockPlaceEvent(true);
		CoreD.setCancelWeatherChangeEvent(true);
		CoreD.setDisableDeathScreen(true);
		CoreD.setDisablePvP(true);
		CoreD.setDisableDamage(true);
		CoreD.setCancelHangingBreakByEntityEvent(true);
		CoreD.setAllowSpectatorsNearPlayers(true);
		CoreD.setStore(true);
		CoreD.setCancelFoodLevelChangeEvent(true);
		CoreD.setCancelPlayerDropItemEvent(false);
		CoreD.setCancelPlayerInteractEvent(true);
		CoreD.setCancelPlayerPickupItemEvent(false);
		CoreD.setMiniGame(new MiniGame("CookingMama", "start", 15));
		CoreD.setMaxPlayers(30);
		updatePlantacoes();
		updateComidas();
		updateMobs();
		Furnaces.clearFurnaces();
		comidasInv = comidas();
		getCommand("start").setExecutor(this);
		getCommand("comidas").setExecutor(this);
		getCommand("addspawn").setExecutor(this);
		Bukkit.getPluginManager().registerEvents(new Listeners(), this);
	}

	public static Main getInstance(){
		return instance;
	}

	public static Map<Material, Double> getComidas(){
		return comidas;
	}

	public static Map<EntityType, Integer> getMobs(){
		return mobs;
	}

	public static List<Material> getPlantacoes(){
		return plantacoes;
	}

	public static List<Location> getSpawnsMobs(){
		return spawnsMobs;
	}

	public static boolean isUseEnderChest(){
		return useEnderChest;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(sender.isOp()){
			if(commandLabel.equalsIgnoreCase("start")){
				CoreD.setRunning();
				StartAndFinish.start();
				return true;
			}
			if(commandLabel.equalsIgnoreCase("addspawn")){
				try{
					World world = Bukkit.getWorlds().get(0);
					int x = Integer.parseInt(args[0]);
					int y = Integer.parseInt(args[1]);
					int z = Integer.parseInt(args[2]);

					spawnsMobs.add(new Location(world, x, y, z));
					System.out.println("Spawn Adicionado!");	
				}catch(Exception e){
					System.out.println("Coordenadas Invalidas! /addspawn <x> <y> <z>");
				}
				return true;
			}
		} 
		if(commandLabel.equalsIgnoreCase("comidas")){
			Player player = (Player) sender;
			player.openInventory(comidasInv);
		}
		return false;
	}

	public static Inventory comidas(){
		Inventory inventory = Bukkit.getServer().createInventory(null, 54, "§6§lComidas");
		Map<Material, Double> invComidas = new HashMap<Material, Double>();		
		for(int i = 0; i <= comidas.size(); i++){
			Material material = null;
			double pontos = -50;
			for(Entry<Material, Double> e : comidas.entrySet()){
				if(invComidas.containsKey(e.getKey())){
					continue;
				}
				if(material == null || e.getValue() >= pontos){
					material = e.getKey();
					pontos = e.getValue();
				}
			}
			if(material != null){
				invComidas.put(material, pontos);
				ItemStack item = new ItemStack(material);
				if(material == Material.INK_SACK){
					item = new ItemStack(Material.INK_SACK, 1, (short) 3);
				}
				ItemMeta meta = item.getItemMeta();
				List<String> lore = new ArrayList<String>();
				lore.add("§6Pontos: §a" + pontos);
				meta.setLore(lore);
				item.setItemMeta(meta);
				inventory.addItem(new ItemStack[] { item });
			}
		}
		return inventory;
	}

	private void updateComidas(){
		comidas.put(Material.POISONOUS_POTATO, -5.0);
		comidas.put(Material.ROTTEN_FLESH, -5.0);
		comidas.put(Material.INK_SACK, 0.1);
		comidas.put(Material.WHEAT, 0.1);
		comidas.put(Material.CARROT_ITEM, 0.15);
		comidas.put(Material.POTATO_ITEM, 0.15);
		comidas.put(Material.SUGAR_CANE, 0.15);
		comidas.put(Material.COOKIE, 0.2);
		comidas.put(Material.PUMPKIN, 0.6);
		comidas.put(Material.MELON, 0.7);
		comidas.put(Material.BROWN_MUSHROOM, 1.0);
		comidas.put(Material.RED_MUSHROOM, 1.0);
		comidas.put(Material.BREAD, 1.0);
		comidas.put(Material.BAKED_POTATO, 1.5);
		comidas.put(Material.APPLE, 2.0);
		comidas.put(Material.PORK, 2.0);
		comidas.put(Material.RAW_BEEF, 2.0);
		comidas.put(Material.RAW_CHICKEN, 2.0);
		comidas.put(Material.MILK_BUCKET, 2.5);
		comidas.put(Material.EGG, 2.5);
		comidas.put(Material.MUSHROOM_SOUP, 4.0);
		comidas.put(Material.GRILLED_PORK, 4.0);
		comidas.put(Material.COOKED_BEEF, 4.0);
		comidas.put(Material.COOKED_CHICKEN, 4.0);
		comidas.put(Material.RAW_FISH, 5.0);
		comidas.put(Material.COOKED_FISH, 7.0);
		comidas.put(Material.GOLDEN_CARROT, 7.0);
		comidas.put(Material.PUMPKIN_PIE, 11.0);
		comidas.put(Material.CAKE, 11.0);
		comidas.put(Material.GOLDEN_APPLE, 35.0);
	}

	public static void updateMobs(){
		mobs.clear();
		mobs.put(EntityType.PIG, MobsManager.pigs);
		mobs.put(EntityType.CHICKEN, MobsManager.chickens);
		mobs.put(EntityType.COW, MobsManager.cows);
		mobs.put(EntityType.MUSHROOM_COW, MobsManager.mushroomCows);
		mobs.put(EntityType.PIG_ZOMBIE, MobsManager.pigZombies);
	}

	private void updatePlantacoes(){
		plantacoes = new ArrayList<Material>();
		plantacoes.add(Material.CROPS);
		plantacoes.add(Material.POTATO);
		plantacoes.add(Material.CARROT);
		plantacoes.add(Material.MELON_BLOCK);
		plantacoes.add(Material.PUMPKIN);
		plantacoes.add(Material.SUGAR_CANE_BLOCK);
		plantacoes.add(Material.SUGAR_CANE);
		plantacoes.add(Material.CACTUS);
		plantacoes.add(Material.COCOA);
	}
}