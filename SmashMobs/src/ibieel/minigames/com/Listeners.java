package ibieel.minigames.com;

import ibieel.minigames.com.Managers.ClassManager;
import ibieel.minigames.com.Managers.ClassManager.ClassType;
import ibieel.minigames.com.Managers.PlayerManager;
import ibieel.minigames.com.Managers.ScoreboardManager1;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.LivingWatcher;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;
import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.effect.BleedEffect;

public class Listeners implements Listener{

	//Efeito 
	EffectManager em = new EffectManager(EffectLib.instance());
	public SmashMobs plugin;
	public Listeners(SmashMobs plugin){
		this.plugin = plugin;
	}

	private List<String> delayList = new ArrayList<>();

	private void addDelay(final String playerName){
		if (!this.delayList.contains(playerName)) {
			this.delayList.add(playerName);
		}
		new BukkitRunnable(){
			public void run()
			{
				if (delayList.contains(playerName)) {
					delayList.remove(playerName);
				}
			}
		}.runTaskLater(SmashMobs.getInstance(), 5 * 20);
	}

	@EventHandler
	private void compassClick(PlayerInteractEvent e){
		Player player = e.getPlayer();
		if (player.getItemInHand() == null) {
			return;
		}
		if (player.getItemInHand().getType() != Material.COMPASS) {
			return;
		}
		if (this.delayList.contains(player.getName())) {
			return;
		}
		addDelay(player.getName());
		String closePlayer = null;
		double distanceDouble = 100000000.0D;
		for (Player p : CoreDPlayer.getPlayersWhoArentSpectators()){
			if (p != player){
				if (!CoreDPlayer.isSpectator(p)){
					if ((closePlayer == null) || (player.getLocation().distance(p.getLocation()) < distanceDouble)){
						closePlayer = p.getName();
						distanceDouble = player.getLocation().distance(p.getLocation());
					}
				}
			}
		}
		if (closePlayer == null){
			player.sendMessage(ChatColor.GRAY + "Não há inimigos próximos a você!");
			return;
		}
		Player toFollow = Bukkit.getPlayer(closePlayer);
		if (toFollow == null){
			return;
		}
		BigDecimal distance = new BigDecimal(distanceDouble);
		distance = distance.setScale(1, 0);
		player.setCompassTarget(toFollow.getLocation());
		player.sendMessage(ChatColor.GRAY + "Player: " + closePlayer + ". Distancia: " + distance);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		if(e.getSlotType() == SlotType.ARMOR){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onRegenHealth(EntityRegainHealthEvent e){
		if(e.getEntity() instanceof Player && (e.getRegainReason() == RegainReason.SATIATED || e.getRegainReason() == RegainReason.REGEN) ){
			e.setAmount(3.0);
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e){
		if(!CoreD.isRunning()){
			if(e.getPlayer().getLocation().getY() <= 0){
				e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
			}
		}
	}


	@EventHandler
	public void onRespawnEvent(PlayerRespawnEvent e){
		final Player player = e.getPlayer();
		if(CoreD.isRunning() && PlayerManager.hasClass(player.getName()) && !CoreDPlayer.isSpectator(player)){
			ClassType classType = PlayerManager.getClasse(player.getName());
			Disguise d = DisguiseAPI.getDisguise(player);
			if(d != null){
				d.removeDisguise();
			}
			DisguiseAPI.disguiseToAll(player, new MobDisguise(ClassManager.getClass(classType).getDisguiseType()));
			Disguise disguise = DisguiseAPI.getDisguise(player);
			FlagWatcher watcher = disguise.getWatcher();
			ItemStack air = new ItemStack(Material.AIR);
			ItemStack[] armour = {air, air, air, air, air};
			watcher.setArmor(armour);
			disguise.startDisguise();
			if((disguise.getWatcher() instanceof LivingWatcher)) {
				((LivingWatcher) disguise.getWatcher()).setCustomName(player.getName());
				((LivingWatcher) disguise.getWatcher()).setCustomNameVisible(true);
			}
			player.getInventory().clear();
			GiveItems.give(player, PlayerManager.getClasse(player.getName()));
			new BukkitRunnable(){
				public void run(){
					int random = (int) (Math.random() * SmashMobs.spawns.size());
					Location spawn = SmashMobs.spawns.get(random);
					player.teleport(spawn);
					ScoreboardManager1.updateScoreboard();
				}
			}.runTaskLater(SmashMobs.getInstance(), 1);
			player.sendMessage("§9Invencibilidade> §eVocê tem 5 segundos de invencibilidade!");
			player.setNoDamageTicks(100);
			new BukkitRunnable(){
				public void run(){
					if(SkillEventUtil.playerHasSkill(player, Skills.VELOCIDADE)){
						player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
					}
				}
			}.runTaskLater(SmashMobs.getInstance(), 40);
			new BukkitRunnable(){
				public void run(){
					try{
						player.sendMessage("§9Invencibilidade> §cAcabou sua invencibilidade, boa sorte!");
					}catch(Exception e){}
				}
			}.runTaskLater(SmashMobs.getInstance(), 60);
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e){
		e.getDrops().clear();
	}

	@EventHandler
	public void cancelExplosionBlockDamage(BlockDamageEvent e){
		e.setCancelled(true);
	}


	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		if(CoreD.isRunning()){
			int vidas = PlayerManager.getVidas(e.getEntity().getName());
			PlayerManager.setVidas(e.getEntity().getName(), vidas -1);
			BleedEffect blood = new BleedEffect(em);
			blood.type = EffectType.INSTANT;
			blood.iterations = 65;
			blood.setEntity(e.getEntity());
			blood.start();
			if(e.getEntity().getKiller() instanceof Player){
				e.setDeathMessage("§9Morte> §e" + e.getEntity().getName() + " §7foi morto por §a" + e.getEntity().getKiller().getName());
				e.getEntity().getKiller().playSound(e.getEntity().getKiller().getLocation(), Sound.NOTE_PLING, 100, 1);
			}else{
				e.setDeathMessage("§9Morte> §e" + e.getEntity().getName() + " §7morreu.");
			}

			if(vidas > 0){
				e.getEntity().sendMessage("§9Vidas> §7Você morreu, agora você tem " + ScoreboardManager1.getCor(vidas) + (vidas - 1) + " §7vida(s)");
			}
		}
	}

	@EventHandler
	public void changeAttackDamage(EntityDamageByEntityEvent e){
		if(CoreD.isRunning()){
			if(e.getDamager() instanceof Player){
				Player player = (Player) e.getDamager();
				if(!CoreDPlayer.isSpectator(player)){
					if(e.getCause() == DamageCause.ENTITY_ATTACK){
						double playerDamage = ClassManager.getClass((PlayerManager.getClasse(player.getName()))).getAttackDamage(); 
						e.setDamage(e.getDamage() <=  playerDamage ? e.getDamage() : playerDamage);
					}
				}
			}
		}
	}

	@EventHandler
	public void cancelDropItem(PlayerDropItemEvent e){
		e.setCancelled(true);
	}

	@EventHandler
	public void playerQuitEvent(PlayerQuitEvent e){
		if(CoreD.isRunning()){
			new BukkitRunnable(){
				@Override
				public void run() {
					ScoreboardManager1.updateScoreboard();
				}
			}.runTaskLater(SmashMobs.getInstance(), 1);
		}
	}

	@EventHandler
	public void changeVoidDamage(EntityDamageEvent e){
		if(e.getCause() == DamageCause.VOID && e.getEntity() instanceof LivingEntity){
			double vida = ((Damageable)e.getEntity()).getHealth();
			e.setDamage(vida);
		}
	}

	@EventHandler
	public void cancelFallDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			if(e.getCause() == DamageCause.FALL){
				e.setCancelled(true);
			}
		}
	}

}
