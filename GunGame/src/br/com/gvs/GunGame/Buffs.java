package br.com.gvs.GunGame;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;

/**
 * @author Gabriel
 *
 */
public class Buffs implements Listener {

	/**
	 * Registra os eventos desta classe
	 */
	public Buffs(){
		Main.getPlugin().getServer().getPluginManager().registerEvents(this, Main.getPlugin());
	}

	private static ArrayList<String> cooldowns = new ArrayList<>();
	private int cooldown = 60;

	/**
	 * @param p - Adiciona o player na lista de cooldown e executa o runnable para realizar o cooldown
	 */
	public void runCooldown(final Player p){
		cooldowns.add(p.getName());
		new BukkitRunnable(){
			public void run(){
				cooldowns.remove(p.getName());
				p.sendMessage("§aO cooldown de seus buffs acabaram, você já pode usar novamente.");
				cancel();
			}
		}.runTaskTimer(Main.getPlugin(), cooldown * 20, 0);
	}

	/**
	 * @param p - Player para verificar se está com cooldown
	 * @return - true para o player com cooldown e false para sem cooldown
	 */
	public static boolean isCooldown(String p){
		return cooldowns.contains(p) ? true : false;
	}

	/*
	 * REDSTONE - Buff de vida
	 * GHAST_TEAR - Buff de regeneração
	 * GOLD_NUGGET - Buff de força
	 * SUGAR - Buff de velocidade
	 */
	@EventHandler
	public void onInteractPlayer(PlayerInteractEvent e){
		if(CoreD.isRunning() && !CoreDPlayer.isSpectator(e.getPlayer())){
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
				if(isCooldown(e.getPlayer().getName()) && (e.getPlayer().getItemInHand().getType() == Material.GHAST_TEAR || e.getPlayer().getItemInHand().getType() == Material.REDSTONE || e.getPlayer().getItemInHand().getType() == Material.GOLD_NUGGET || e.getPlayer().getItemInHand().getType() == Material.SUGAR)){
					e.getPlayer().sendMessage("§cEspere para usar este buff.");
				}else{
					if(e.getPlayer().getItemInHand().getType() != Material.AIR){
						switch(e.getPlayer().getItemInHand().getType()){
						case REDSTONE:
							runCooldown(e.getPlayer());
							removeItemInHand(e.getPlayer());
							Damageable dmg = ((Damageable)e.getPlayer());
							dmg.setHealth(40.0);
							e.getPlayer().sendMessage("§aVocê usou um buff para recuperar sua §cvida§a.");
							break;
						case GHAST_TEAR:
							removeItemInHand(e.getPlayer());
							runCooldown(e.getPlayer());
							e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 12 * 20, 1));
							e.getPlayer().sendMessage("§aVocê usou um buff para ganhar §cregeneração de vida§a.");
							break;
						case GOLD_NUGGET:
							removeItemInHand(e.getPlayer());
							runCooldown(e.getPlayer());
							e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10 * 20, 0));
							e.getPlayer().sendMessage("§aVocê usou um buff para ganhar §cforça§a.");
							break;
						case SUGAR:
							removeItemInHand(e.getPlayer());
							runCooldown(e.getPlayer());
							e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 25 * 20, 2));
							e.getPlayer().sendMessage("§aVocê usou um buff para ganhar §bvelocidade§a.");
							break;
						default:
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * @param p - Player que será removido item da mão.
	 * Este método só remove 1 item da mão, caso o player só tenha 1 item ele seta como AIR
	 */
	public static void removeItemInHand(Player p){
		if(p.getItemInHand().getAmount() == 1){
			p.setItemInHand(new ItemStack(Material.AIR, 1));
		}else{
			p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
		}
	}
}
