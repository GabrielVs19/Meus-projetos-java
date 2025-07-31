package br.com.gvs.GunGame;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.gvs.GunGame.GunLevels.Guns;
import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;
import br.com.tlcm.cc.store.StoreHandler;

/**
 * @author Gabriel
 *
 */
public class Listeners implements Listener {

	/**
	 * Registra os eventos desta classe
	 */
	public Listeners(){
		Main.getPlugin().getServer().getPluginManager().registerEvents(this, Main.getPlugin());
	}

	/**
	 * Muda o dano que flechas e a maozinha dá
	 */
	@EventHandler
	public void onDamageEntity(EntityDamageByEntityEvent e){
		if(CoreD.isRunning()){
			if(e.getEntityType() == EntityType.PLAYER){ 
				if(e.getDamager().getType() == EntityType.ARROW){
					e.setDamage(5.0);
					return;
				}
				if(e.getDamager().getType() == EntityType.PLAYER && ((Player)e.getDamager()).getItemInHand().getType() == Material.AIR){
					e.setDamage(3.5);
					return;
				}
				for(Guns g : GunLevels.Guns.values()){
					if(g.getGun().getType().equals(((Player)e.getEntity()).getItemInHand().getType())){
						if(((Player)e.getEntity()).hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)){
							e.setDamage(g.getDamage() + (g.getDamage() * 0.20));
						}else{
							e.setDamage(g.getDamage());
						}
						break;
					}
				}
			}
		}
	}

	/**
	 * @param e
	 * Faz com que regenere a vida mais rápido
	 */
	@EventHandler
	public void onRegenHealth(EntityRegainHealthEvent e){
		if(e.getEntity() instanceof Player && (e.getRegainReason() == RegainReason.SATIATED || e.getRegainReason() == RegainReason.REGEN) ){
			e.setAmount(3.0);
		}
	}


	/**
	 * Faz com que nenhum item quebre
	 */
	@EventHandler
	public void onItemDamage(PlayerItemDamageEvent e){
		if(CoreD.isRunning()){
			e.setCancelled(true);
		}
	}

	/**
	 * Msg qnd loga
	 */
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent e){
		new BukkitRunnable(){
			public void run(){
				e.getPlayer().sendMessage("§aUse o comando §9/menu §apara ver todas as armas.");
			}
		}.runTaskLater(Main.getPlugin(), 2 * 20);
	}


	/**
	 * Ao player dar respawn seta a vida dele em 35.0, da a arma de seu nível, uma nova armadura e teleporta ele para algum spawn
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		if(CoreD.isRunning() && !CoreDPlayer.isSpectator(e.getPlayer().getName())){
			e.getPlayer().getInventory().remove(Guns.getGunByLevel(PlayerManager.getPlayerManager(e.getPlayer().getName()).getLevel()).getGun().getType());
			e.getPlayer().updateInventory();
			e.getPlayer().getInventory().addItem(Guns.getGunByLevel(PlayerManager.getPlayerManager(e.getPlayer().getName()).getLevel()).getGun());
			if(Guns.getGunByLevel(PlayerManager.getPlayerManager(e.getPlayer().getName()).getLevel()).getGun().getType() == Material.BOW){
				e.getPlayer().getInventory().addItem(new ItemStack(Material.ARROW, 64));
			}
			((Damageable)e.getPlayer()).setMaxHealth(40.0);
			((Damageable)e.getPlayer()).setHealth(40.0);
			e.setRespawnLocation(Main.getRandomLocation());
			Util.setPlayerRandomArmor(e.getPlayer());
			e.getPlayer().setNoDamageTicks(80);
			new BukkitRunnable(){
				public void run(){
					Scoreboard.updateScoreboard();
				}
			}.runTaskLater(Main.getPlugin(), 2);
		}
	}

	/**
	 * Realiza a parte lógica do mini-game, fazendo a contagem de mortes/kills e incrementando/decrementando os níveis
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		e.getDrops().clear();
		if(CoreD.isRunning()){
			if(e.getEntity().getKiller() != null && e.getEntity().getKiller().getType() == EntityType.PLAYER){
				Player death = e.getEntity();
				Player killer = e.getEntity().getKiller();
				death.getWorld().playEffect(death.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
				PlayerManager mDeath = PlayerManager.getPlayerManager(death.getName());
				mDeath.addDeath();
				//mDeath.resetLevelKills();
				if(killer.getItemInHand().getType() == Material.AIR && (mDeath.getLevel() - 1) > 0){
					mDeath.setLevel(mDeath.getLevel() - 1);
					mDeath.resetLevelKills();
					death.sendMessage("§a" + killer.getName() + " §7te matou sem usar armas, e seu nível foi §crebaixado§7.");
					killer.sendMessage("§7Você matou §a" + death.getName() + " §7sem usar armas e §crabaixou §7o nível dele.");
				}
				PlayerManager mKiller = PlayerManager.getPlayerManager(killer.getName());
				//	String gun = Guns.getGunByLevel(mKiller.getLevel()).getGun().getItemMeta().getDisplayName();
				killer.getInventory().remove(Guns.getGunByLevel(mKiller.getLevel()).getGun().getType());
				killer.updateInventory();
				mKiller.addKillToAllKills();
				mKiller.addKillToLevelKills();
				Util.setPlayerRandomArmor(killer);
				killer.playSound(killer.getLocation(), Sound.NOTE_PLING, 100, 1);
				if(Guns.getGunByLevel(mKiller.getLevel()).getKillsToNextGun() <= mKiller.getLevelKills()){
					if(mKiller.getLevel() == 15){
						StartAndFinish.finishWinner(killer);
						return;
					}
					mKiller.setLevel(mKiller.getLevel() + 1);
					mKiller.resetLevelKills();
					StoreHandler.give(killer);
					Guns guns1 = Guns.getGunByLevel(mKiller.getLevel());
					killer.getInventory().addItem(guns1.getGun());
					if(guns1.getGun().getType() == Material.BOW){
						killer.getInventory().addItem(new ItemStack(Material.ARROW, 1));
					}
					killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 100, 1);
					killer.sendMessage("§eVocê subiu de nível, agora mate §a" + guns1.getKillsToNextGun() + " jogador(es) §ecom um(a) " + guns1.getGun().getItemMeta().getDisplayName().replace("§e", "§a") + " §epara ir para o próximo nível.");
				}else{
					Guns guns = Guns.getGunByLevel(mKiller.getLevel());
					killer.getInventory().addItem(guns.getGun());
					if(guns.getGun().getType() == Material.BOW){
						killer.getInventory().addItem(new ItemStack(Material.ARROW, 1));
					}
				}
				e.setDeathMessage("§a" + killer.getName() + " §7matou §c" + death.getName());
			}else{
				e.setDeathMessage("§a" + e.getEntity().getName() + " §7morreu.");
			}
		}
	}

}
