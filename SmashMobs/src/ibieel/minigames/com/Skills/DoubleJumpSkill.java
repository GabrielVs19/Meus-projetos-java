package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import br.com.tlcm.cc.API.CoreDPlayer;


public class DoubleJumpSkill implements Listener{

	private Skills skill = Skills.DOUBLE_JUMP;
	private Skills skillGalinha = Skills.DOUBLE_JUMP_GALINHA;
	private SkillType skillType = skill.getSkillType();
	public SmashMobs plugin;
	public DoubleJumpSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		Material block = player.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType();
		if(!CoreDPlayer.isSpectator(player)){
			if(SkillEventUtil.playerHasSkill(player, skill) || SkillEventUtil.playerHasSkill(player, skillGalinha)){
				if((player.getGameMode() != GameMode.CREATIVE) && block != Material.AIR && (!player.isFlying())) {
					if(!CooldownManager.isCooldown(player.getName(), skillType)){
						player.setAllowFlight(e.getFrom().getY() <= e.getTo().getY());
						player.setFlying(false);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerToggleFlight(PlayerToggleFlightEvent e){
		final Player player = e.getPlayer();
		final Skills s = SkillEventUtil.playerHasSkill(player, skillGalinha) ? skillGalinha : skill;

		if(player.getGameMode() == GameMode.CREATIVE || CoreDPlayer.isSpectator(player) || (!SkillEventUtil.playerHasSkill(player, s))){
			return;
		}

		e.setCancelled(true);
		player.setFlying(false);
		player.setAllowFlight(false);

		if(CooldownManager.isCooldown(player.getName(), s.getSkillType())){
			return;
		}

		CooldownManager.cooldown(player.getName(), s);
		final PermissionUser pu = PermissionsEx.getUser(player.getName());
		pu.addPermission("checks.moving.survivalfly");

		new BukkitRunnable(){
			int count = 0;
			public void run(){
				count++;
				if(count == (long) ((20 * s.getCooldown()) - 1)){
					pu.removePermission("checks.moving.survivalfly");
					cancel();
				}

				try{
					player.setFlying(false);
					player.setAllowFlight(false);
				}catch(Exception e2){
				}
			}
		}.runTaskTimer(SmashMobs.getInstance(), 1, 1);

		player.setVelocity(player.getLocation().getDirection().multiply(0.8).setY(0.8));
		player.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 1, 1);
	}

}


