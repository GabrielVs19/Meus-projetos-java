package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.tlcm.cc.API.CoreDPlayer;

public class UivoSkill implements Listener {

	private Skills skill = Skills.UIVO;
	private SkillType skillType = skill.getSkillType();
	public SmashMobs plugin;
	public UivoSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void wolfSpawn(PlayerInteractEvent e){
		if(!CoreDPlayer.isSpectator(e.getPlayer()) && SkillEventUtil.rightClickShovel(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
			final Player player = e.getPlayer();
			if(!CooldownManager.isCooldown(player.getName(), skillType)){
				SkillEventUtil.sendUseSkillMessage(player, skill);
				CooldownManager.cooldown(player.getName(), skill);
				player.playSound(player.getLocation(), Sound.WOLF_HOWL, 100, 1);
				player.playSound(player.getLocation(), Sound.WOLF_HOWL, 100, 1);
				player.playSound(player.getLocation(), Sound.WOLF_HOWL, 100, 1);
				for(Entity en : player.getNearbyEntities(3, 3, 3)){
					if(en instanceof LivingEntity && en != player){
						((LivingEntity)en).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 120, 3));
						((LivingEntity)en).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, 0));
					}
				}
			}else{
				SkillEventUtil.soundCooldownSkill(player);
			}
		}
	}

}
