package br.com.gvs.GunGame;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.gvs.GunGame.GunLevels.Guns;
import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;
import br.com.tlcm.cc.API.enumerators.TeleportToLobbyReason;

/**
 * @author Gabriel
 *
 */
public class StartAndFinish {

	/**
	 * Inicia o mini-game
	 */
	public static void start(){
		CoreD.setRunning();
		new Timer(600);
		for(Player p : CoreDPlayer.getPlayersWhoArentSpectators()){
			PlayerManager pManager = new PlayerManager(p);
			PlayerManager.addPlayer(p.getName(), pManager);
			p.teleport(Main.getRandomLocation());
			p.setNoDamageTicks(85);
			p.getInventory().addItem(Guns.GUN_01.getGun());
			Util.setPlayerRandomArmor(p);
			((Damageable)p).setMaxHealth(40.0);
			((Damageable)p).setHealth(40.0);
		}
		Scoreboard.updateScoreboard();
		CoreD.setDisableDamage(false);
		CoreD.setDisablePvP(false);
		if(Main.getWaterDamageState()){
			new WaterDamage().runTaskTimer(Main.getPlugin(), 0, 20);
		}
	}

	/**
	 * Finaliza o mini-game sem vencedores, tempo esgotado.
	 */
	public static void finishTime(){
		Bukkit.broadcastMessage("§c§lTempo esgotado, partida sem vencedores!");
		for(Player p : CoreDPlayer.getPlayersWhoArentSpectators()){
			CoreD.sendToLobby(p, TeleportToLobbyReason.LOSER);
		}
	}

	/**
	 * @param player - Vencedor
	 * Finaliza o mini-game
	 */
	public static void finishWinner(final Player player){
		Bukkit.broadcastMessage("§9§l" + player.getName() + " §a§lganhou!!");
		CoreD.setDisableDamage(true);
		CoreD.setDisablePvP(true);
		new BukkitRunnable(){
			public void run(){
				CoreD.award(player);
				for(Player p : CoreDPlayer.getPlayersWhoArentSpectators()){
					CoreD.sendToLobby(p, p.getName().equalsIgnoreCase(player.getName()) ? TeleportToLobbyReason.WINNER : TeleportToLobbyReason.LOSER);
				}
			}
		}.runTaskLater(Main.getPlugin(), 20 * 4);
	}

}
