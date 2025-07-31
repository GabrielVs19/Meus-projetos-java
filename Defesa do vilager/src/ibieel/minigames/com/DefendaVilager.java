package ibieel.minigames.com;

import ibieel.minigames.com.CustomMobs.CustomVillager;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R2.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;

import br.com.tlcm.cc.CraftLandiaCoreD;
import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;
import br.com.tlcm.cc.API.enumerators.TeleportToLobbyReason;

public class DefendaVilager implements Listener {

	public static ScoreboardManager manager = Bukkit.getScoreboardManager();
	public static Scoreboard board = manager.getMainScoreboard();
	public static Objective objective = board.registerNewObjective("Kills", "dummy");
	public static HashMap<Player, Integer> mostKilled = new HashMap<Player, Integer>();
	public static HashMap<String, Integer> playerPoints = new HashMap<String, Integer>();
	public static int numberMobs = 0;
	public static boolean hitVillager = false;
	public static int wave = 0;
	public static CustomVillager villager1;

	public static Main plugin;
	public DefendaVilager(Main plugin){
		DefendaVilager.plugin = plugin;
	}

	public static void createScoreboard(){
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Mais matou:");
	}

	public static Player getPlayerHighestScore(){
		int highestScore = 0;
		Player player = null;
		for(OfflinePlayer p : board.getPlayers()){
			int score = objective.getScore(p).getScore();
			if(score > highestScore){
				highestScore = score;
				player = (Player) p;
			}
		}
		return player;
	}

	public static Map<String, Integer> scoreboadTop15() {
		Map<String, Integer> top = new HashMap<String, Integer>();
		for (int i = 0; i < 15; i++){
			String nick = null;
			int score = -1;
			for (Map.Entry<String, Integer> e : playerPoints.entrySet()) {
				if (!top.containsKey(e.getKey())) {
					if ((nick == null) || e.getValue() > score){
						nick = (String)e.getKey();
						score = e.getValue();
					}
				}
			}
			if (nick != null) {
				top.put(nick, score);
			}
		}
		return top;
	}

	public static void start() {
		Bukkit.getWorlds().get(0).strikeLightningEffect(Main.getLocVillager());
		for(Player p : CoreDPlayer.getPlayersWhoArentSpectators()){
			p.setLevel(0);
		}		
		Bukkit.broadcastMessage("§6§lDefenda o José!!!!");
		starterKit();
		createScoreboard();
		spawnVilager();
		startWave(villager1);
	}

	public static void startWave(final net.minecraft.server.v1_7_R2.Entity	vilager) {
		if(wave == 16){
			finish();
		}else{
			if(wave != 0){
				if (mostKilled.containsKey(getPlayerHighestScore())) {
					mostKilled.put(getPlayerHighestScore(), Integer.valueOf(((Integer)mostKilled.get(getPlayerHighestScore())).intValue() + 1));
				} else {
					mostKilled.put(getPlayerHighestScore(), Integer.valueOf(1));
				}
				Bukkit.broadcastMessage("§a§l" + getPlayerHighestScore().getName() + " §bfoi quem matou mais nesta rodada e ganhou um bonus de §f3 §bcoins.");
				for(Player p : Bukkit.getOnlinePlayers()) {
					board.resetScores(p);
				}
				playerPoints.clear();
			}
			wave++;
			numberMobs = ((CoreDPlayer.getPlayersWhoArentSpectators().size()) * 3 + (wave * 3));
			CoreD.setBossBar("§6Iniciando rodada §9§l" + wave + " §6em §9§l5 §6segundos.");
			new BukkitRunnable() {
				public void run(){
					for(int i = 1;i <= numberMobs;i++){
						Location loc = getRandomLocation();
						int random = (int) ((Math.random() * 99) + 1);
						try{
							if(wave < 5){
								if(random < 50){
									Mobs.spiders(loc, 30, vilager);
								}else{

									Mobs.zombies(loc, 30, vilager);
								}
							}else if(wave > 4 && wave < 10){
								if(random <= 30){
									Mobs.zombies(loc, 40, vilager);
								} else if(random <= 40){
									if(random <= 65){
										Mobs.creepers(loc, 40, vilager);
									}else if(random <= 80){
										Mobs.creepersCharge(loc, 50, vilager);
									}
								} else if(random <= 80){
									Mobs.spiders(loc, 40, vilager);
								} else if(random > 80){
									Mobs.skeletons(loc, 40, vilager, CraftItemStack.asNMSCopy(new ItemStack(Material.STONE_SWORD, 1)));
								}
							} else if(wave > 9){
								if(random <= 30){
									if(random <= 15){
										Mobs.spiders(loc, 50, vilager);
									}else if(random <= 25){
										Mobs.zombies(loc, 50, vilager, CraftItemStack.asNMSCopy(new ItemStack(Material.GOLDEN_APPLE, 1)), CraftItemStack.asNMSCopy(new ItemStack(Material.LEATHER_HELMET, 1)), CraftItemStack.asNMSCopy(new ItemStack(Material.AIR, 1)), CraftItemStack.asNMSCopy(new ItemStack(Material.AIR, 1)),CraftItemStack.asNMSCopy(new ItemStack(Material.LEATHER_BOOTS, 1)));
									}else if(random <= 30){
										Mobs.skeletons(loc, 50, vilager, CraftItemStack.asNMSCopy(new ItemStack(Material.STONE_SWORD, 1)), CraftItemStack.asNMSCopy(new ItemStack(Material.LEATHER_HELMET, 1)), CraftItemStack.asNMSCopy(new ItemStack(Material.LEATHER_CHESTPLATE, 1)), CraftItemStack.asNMSCopy(new ItemStack(Material.LEATHER_LEGGINGS, 1)), CraftItemStack.asNMSCopy(new ItemStack(Material.LEATHER_BOOTS, 1)));
									}
								} else if(random <= 50){
									Mobs.creepers(loc, 60, vilager);
								} else if(random <= 55){
									Mobs.creepersCharge(loc, 60, vilager);
								} else if(random <= 75){
									Mobs.zombies(loc, 60, vilager);
								} else if(random > 75){
									if(random <= 90){
										Mobs.zombies(loc, 70, vilager, CraftItemStack.asNMSCopy(new ItemStack(Material.GOLDEN_APPLE, 1)));
									} else if(random > 90){
										Mobs.skeletons(loc, 70, vilager, CraftItemStack.asNMSCopy(new ItemStack(Material.IRON_SWORD, 1)));
									}
								}
							}
						} catch (Exception localException) {}
					}
					for(int i = 1;i <= (Math.round(CoreDPlayer.getPlayersWhoArentSpectators().size() / 2) + wave + 2);i++){
						Location loc = getRandomLocation();
						Mobs.pigZombie(loc);
					}
					CoreD.setBossBar("§6§lRodada §9§l" + wave);
					for(Player p : Bukkit.getOnlinePlayers()){
						p.playSound(p.getLocation(), Sound.BLAZE_HIT, 100, 1);
						p.playSound(p.getLocation(), Sound.BLAZE_HIT, 100, 1);
					}
					((LivingEntity) villager1.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 9999999));
					int random = (int) ((Math.random() * 99) + 1);
					if(random <= 40){
						Bukkit.broadcastMessage("§a§lUm bonus especial surgiu no mapa.");
						getRandomLocationBonus().getBlock().setType(Material.BEACON);
					}
				}
			}.runTaskLater(plugin, 20 * 5);

		}
	}


	public static Location getRandomLocationBonus(){
		int random = (int) (Math.random() * Main.getBonusSpawn().size());
		return Main.getBonusSpawn().get(random);
	}

	public static Location getRandomLocation(){
		int random = (int) (Math.random() * Main.getMobSpawn().size());
		return Main.getMobSpawn().get(random);
	}
	
	public static void spawnVilager() {
		CustomVillager mob = new CustomVillager(((CraftWorld)Main.getLocVillager().getWorld()).getHandle());
		mob.setHealth(200);
		mob.setCustomName("§d§lJosé");
		mob.setCustomNameVisible(true);
		mob.setProfession(1);
		mob.getBukkitEntity().setVelocity(new Vector(0, 0, 0));
		mob.setPosition(Main.getLocVillager().getX(), Main.getLocVillager().getY(), Main.getLocVillager().getZ());
		((CraftWorld) Main.getLocVillager().getWorld()).getHandle().addEntity(mob, SpawnReason.CUSTOM);
		villager1 = mob;
	}

	public static void hitVillager(){
		new BukkitRunnable(){
			public void run() {
				hitVillager = false;
				cancel();
			}
		}.runTaskLater(plugin, (long) (20 * 0.5));
	}

	public static void starterKit(){
		for(Player p : Bukkit.getOnlinePlayers()){
			p.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD, 1));
			p.getInventory().addItem(new ItemStack(Material.BREAD, 2));
			p.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET, 1));
			p.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
			p.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
			p.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS, 1));
		}
	}

	public static void finish(){
		Bukkit.broadcastMessage("§f§l[José] §aMuito obrigado por me defenderem, estou eternamente grato!");
		for (Player p : CoreDPlayer.getPlayersWhoArentSpectators()){
			if(mostKilled.containsKey(p)){
				CoreD.award(p, CraftLandiaCoreD.getInstance().getConfig().getInt("geral.defaultAward") + 3 * ((Integer)mostKilled.get(p)).intValue());
				CoreD.sendToLobby(p, TeleportToLobbyReason.WINNER);
			}else{
				CoreD.award(p);
				CoreD.sendToLobby(p, TeleportToLobbyReason.WINNER);
			}
		}
	}

}
