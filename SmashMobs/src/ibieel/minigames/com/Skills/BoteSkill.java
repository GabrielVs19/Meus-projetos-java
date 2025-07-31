package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import java.util.List;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreDPlayer;

public class BoteSkill implements Listener {


	private Skills skill = Skills.BOTE;
	private SkillType skillType = skill.getSkillType();
	//Dano
	private double dano = 6.0;
	//Hitbox
	private double hitbox = 1.0;
	public SmashMobs plugin;
	public BoteSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void shoot(PlayerInteractEvent e){
		if(!CoreDPlayer.isSpectator(e.getPlayer()) && SkillEventUtil.rightClickAxe(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
			final Player player = e.getPlayer();
			if(!CooldownManager.isCooldown(player.getName(), skillType)){
				CooldownManager.cooldown(player.getName(), skill);
				SkillEventUtil.sendUseSkillMessage(player, skill);
				player.playSound(player.getLocation(), Sound.SPIDER_IDLE, 100, 1);
				player.setVelocity(player.getEyeLocation().getDirection().multiply(1.8).setY(0.45));
				new BukkitRunnable(){
					int a = 0;
					@Override
					public void run() {
						a++;
						if(a == 35){
							cancel();
						}
						List<Entity> en = player.getNearbyEntities(hitbox, hitbox, hitbox);
						for(Entity e : en){
							if(e instanceof LivingEntity && e != player){
								((LivingEntity)e).damage(dano);
								cancel();
							}
						}
					}
				}.runTaskLater(SmashMobs.getInstance(), 1);
			}else{
				SkillEventUtil.soundCooldownSkill(player);
			}
		}
	}

}
