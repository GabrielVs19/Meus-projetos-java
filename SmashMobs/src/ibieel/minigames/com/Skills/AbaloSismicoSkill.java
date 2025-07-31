package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import br.com.tlcm.cc.API.CoreDPlayer;


public class AbaloSismicoSkill implements Listener{

	private Skills skill = Skills.ABALO_SISMICO;
	//Max de dano que pode ser acumulado com essa skill
	private double maxDamage = 10.0;
	public SmashMobs plugin;
	public AbaloSismicoSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void damageNearbyPlayersOnFall(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player player = (Player) e.getEntity();
			if(e.getCause() == DamageCause.FALL){
				if(!CoreDPlayer.isSpectator(player)){
					if(SkillEventUtil.playerHasSkill(player, skill)){
						player.playSound(player.getLocation(), Sound.IRONGOLEM_DEATH, 100, 1);
						for(Entity en : player.getNearbyEntities(3, 3, 3)){
							if(en instanceof LivingEntity){
								((LivingEntity)en).damage((e.getDamage() <= maxDamage ? e.getDamage() : maxDamage));
							}
						}
					}
				}
			}
		}
	}

}
