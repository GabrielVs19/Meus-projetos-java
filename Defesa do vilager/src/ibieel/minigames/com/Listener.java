package ibieel.minigames.com;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Score;

import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;
import br.com.tlcm.cc.API.enumerators.TeleportToLobbyReason;

public class Listener implements org.bukkit.event.Listener {

	public Main plugin;
	public Listener(Main plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void playerInteractEntity(PlayerInteractEntityEvent e){
		if(e.getRightClicked() instanceof Villager){
			e.setCancelled(true);
			e.getPlayer().chat("/loja");
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void playerInteract(PlayerInteractEvent e) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		if(e.getAction() == Action.LEFT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.BEACON){
			e.getClickedBlock().setType(Material.AIR);
			Bukkit.broadcastMessage("§9§l" + e.getPlayer().getName() + " §apegou o bonus especial.");
			int random = (int) ((Math.random() * 99) + 1);
			if(random <= 10){
				for(Entity en : Bukkit.getWorlds().get(0).getEntities()){
					if(en.getType() != EntityType.PLAYER && en.getType() != EntityType.VILLAGER){
						en.getWorld().playEffect(en.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
						((Damageable)en).setHealth(0);
						if(DefendaVilager.playerPoints.containsKey(e.getPlayer().getName())){
							DefendaVilager.playerPoints.put(e.getPlayer().getName(), DefendaVilager.playerPoints.get(e.getPlayer().getName()) + 1);
						}else{
							DefendaVilager.playerPoints.put(e.getPlayer().getName(), 1);
						}
					}
				}
				for(Player p : Bukkit.getOnlinePlayers()) {
					DefendaVilager.board.resetScores(p);
				}
				for (Map.Entry<String, Integer> entry : DefendaVilager.scoreboadTop15().entrySet()) {
					Score score = DefendaVilager.objective.getScore(Bukkit.getOfflinePlayer((String)entry.getKey()));
					score.setScore(entry.getValue());
				}
				Bukkit.broadcastMessage("§a§lO bonus exterminou os monstros!");
				DefendaVilager.numberMobs = 0;
				//startWave(villager1);
			}else if(random > 10 && random <= 50){
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 8));
				e.getPlayer().sendMessage(("§a§lO bonus te curou!"));
				for(Entity en : e.getPlayer().getNearbyEntities(5, 5, 5)){
					((Player)en).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 8));
					((Player)en).sendMessage(("§a§lO bonus te curou!"));
				}
			}else if(random > 50 && random <= 80){
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 400, 4));
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 4));
				e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 400, 4));
			}else if(random > 80 && random < 100){
				Bukkit.broadcastMessage("§a§lO bonus curou José!");
				Damageable vilager1 = (Damageable) DefendaVilager.villager1.getBukkitEntity();
				if(vilager1.getMaxHealth() < vilager1.getHealth() + 20){
					vilager1.setMaxHealth(vilager1.getHealth() + 20);
					vilager1.setHealth(vilager1.getHealth() + 20);
				}else{
					vilager1.setHealth(vilager1.getHealth() + 20);
				}
				//	((LivingEntity) villager1).setCustomName("§d§lJosé §c§l" + vilager1.getHealth() + " �?�");
				CoreD.setBossBar("§bMobs vivos: §f" + DefendaVilager.numberMobs + " §b| §cJosé: §f" + vilager1.getHealth() + "§c�?�");
			}
		}
	}

	@SuppressWarnings({ "deprecation"})
	@EventHandler
	public void playersDamage(EntityDamageByEntityEvent e) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		if(e.getEntityType().equals(EntityType.VILLAGER)){
			if(e.getDamager() instanceof Player){
				e.setCancelled(true);
			}else{
				if(DefendaVilager.hitVillager == false){
					DefendaVilager.hitVillager = true;
					Villager vilager = (Villager) e.getEntity();
					DefendaVilager.villager1.getBukkitEntity().teleport(Main.getLocVillager());
					Damageable vilager1 = (Damageable) vilager;
					double vida1 = vilager1.getHealth() - e.getDamage();
					if(!(vilager1.getHealth() - e.getDamage() <= 0)){
						e.setCancelled(true);
						vilager1.setHealth(vida1);
					}
					vilager.setCustomName("§d§lJosé §c§l" + vilager1.getHealth() + " ");
					CoreD.setBossBar("§c§lJosé está sendo atacado! Vida:§f§l " + vilager1.getHealth() + " §c�?�");
					for(Player p : Bukkit.getOnlinePlayers()){
						p.playSound(p.getLocation(), Sound.NOTE_PLING, 15, 1);
					}
					DefendaVilager.hitVillager();
				}else{
					DefendaVilager.villager1.teleportTo(Main.getLocVillager(), false);
					e.setCancelled(true);
				}
			}
		} else if(!(e.getEntityType().equals(EntityType.PIG_ZOMBIE))){
			e.setCancelled(true);
			LivingEntity entity = (LivingEntity) e.getEntity();
			Damageable entity1 = (Damageable) entity;
			double vida1 = entity1.getHealth() - e.getDamage();
			Player player = (Player) e.getDamager();
			if(vida1 <= 0){
				if(e.getEntityType().equals(EntityType.ZOMBIE)){
					player.playSound(player.getLocation(), Sound.ZOMBIE_DEATH, 100, 1);
				}else if(e.getEntityType().equals(EntityType.SPIDER)){
					player.playSound(player.getLocation(), Sound.SPIDER_DEATH, 100, 1);
				}else if(e.getEntityType().equals(EntityType.SKELETON)){
					player.playSound(player.getLocation(), Sound.SKELETON_DEATH, 100, 1);
				}else if(e.getEntityType().equals(EntityType.CREEPER)){
					player.playSound(player.getLocation(), Sound.CREEPER_DEATH, 100, 1);
				}
				if(DefendaVilager.playerPoints.containsKey(player.getName())){
					DefendaVilager.playerPoints.put(player.getName(), DefendaVilager.playerPoints.get(player.getName()) + 1);
				}else{
					DefendaVilager.playerPoints.put(player.getName(), 1);
				}
				for(Player p : Bukkit.getOnlinePlayers()) {
					DefendaVilager.board.resetScores(p);
				}
				for (Map.Entry<String, Integer> entry : DefendaVilager.scoreboadTop15().entrySet()){
					Score score = DefendaVilager.objective.getScore(Bukkit.getOfflinePlayer((String)entry.getKey()));
					score.setScore(entry.getValue());
				}
				entity.setHealth(0);
				int random = (int) ((Math.random() * 99) + 1);
				if(random <= 5){
					entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLDEN_APPLE, 1));
				}
				DefendaVilager.numberMobs -= 1;
				Damageable vilager1 = (Damageable) DefendaVilager.villager1.getBukkitEntity();
				CoreD.setBossBar("§bMobs vivos: §f" + DefendaVilager.numberMobs + " §b| §cJosé: §f" + vilager1.getHealth() + "§c�?�");
				if(DefendaVilager.numberMobs == 0){
					//DefendaVilager.startWave(villager1);
				}
			}else{
				entity.setHealth(vida1);
				if(e.getEntityType().equals(EntityType.ZOMBIE)){
					player.playSound(player.getLocation(), Sound.ZOMBIE_HURT, 100, 1);
				}else if(e.getEntityType().equals(EntityType.SPIDER)){
					player.playSound(player.getLocation(), Sound.SPIDER_IDLE, 100, 1);
				}else if(e.getEntityType().equals(EntityType.SKELETON)){
					player.playSound(player.getLocation(), Sound.SKELETON_HURT, 100, 1);
				}else if(e.getEntityType().equals(EntityType.CREEPER)){
					player.playSound(player.getLocation(), Sound.CREEPER_HISS, 100, 1);
				}
			}
			e.getEntity().getWorld().playEffect(e.getEntity().getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
		}
	}

	@EventHandler
	public void targetPlayerCancel(EntityTargetEvent e){
		if(!(e.getEntity() instanceof PigZombie)){
			if((e.getEntity() instanceof Monster) && !(e.getTarget() instanceof Villager)){
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void playerDeath(PlayerDeathEvent e){
		e.setDeathMessage("§9§l" + e.getEntity().getName() + " §cfalhou em proteger §d§lJosé.");
		CoreD.sendToLobby(e.getEntity(), TeleportToLobbyReason.LOSER);
	}

	@EventHandler
	public void vilagerDeath(EntityDeathEvent e){
		e.getDrops().clear();
		if(e.getEntityType().equals(EntityType.VILLAGER)){
			Bukkit.broadcastMessage("§c§lVocês perderam, José foi morto.");
			for (Player p : CoreDPlayer.getPlayersWhoArentSpectators()) {
				CoreD.sendToLobby(p, TeleportToLobbyReason.LOSER);
			}
		}
	}

}
