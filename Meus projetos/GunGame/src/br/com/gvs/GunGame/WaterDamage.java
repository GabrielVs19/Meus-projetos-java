package br.com.gvs.GunGame;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreDPlayer;

/**
 * @author Gabriel
 *
 */
public class WaterDamage extends BukkitRunnable {

	@Override
	public void run() {
		for(Player player : CoreDPlayer.getPlayersWhoArentSpectators()){
			Location loc = player.getLocation();
			if (loc.getBlock().getType() == Material.WATER ||  loc.getBlock().getType() == Material.STATIONARY_WATER){
				player.damage(13.0);
			}
		}
	}

}
