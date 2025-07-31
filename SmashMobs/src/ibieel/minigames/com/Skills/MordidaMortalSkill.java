package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.ClassManager;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Managers.PlayerManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;
import ibieel.minigames.com.Util.UseSkillUtil;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

import br.com.tlcm.cc.API.CoreDPlayer;

public class MordidaMortalSkill implements Listener {

	private Skills skill = Skills.MORDIDA_MORTAL;
	private SkillType skillType = skill.getSkillType();
	public SmashMobs plugin;
	public MordidaMortalSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void wolfSpawn(PlayerInteractEvent e){
		if(!CoreDPlayer.isSpectator(e.getPlayer()) && SkillEventUtil.rightClickSword(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
			final Player player = e.getPlayer();
			if(!CooldownManager.isCooldown(player.getName(), skillType)){
				UseSkillUtil.add(player, skill, true);
				CooldownManager.cooldown(player.getName(), skill);
				player.playSound(player.getLocation(), Sound.WOLF_DEATH, 100, 1);
			}else{
				SkillEventUtil.soundCooldownSkill(player);
			}
		}
	}
	
	@EventHandler
	public void changeAttackDamage(EntityDamageByEntityEvent e){
			if(e.getDamager() instanceof Player){
				Player player = (Player) e.getDamager();
					if(e.getCause() == DamageCause.ENTITY_ATTACK && UseSkillUtil.using(player, skill)){
						UseSkillUtil.remove(player, skill, false);
						double playerDamage = ClassManager.getClass((PlayerManager.getClasse(player.getName()))).getAttackDamage() * 2; 
						e.setDamage(e.getDamage() * 2 <=  playerDamage ? e.getDamage() * 2 : playerDamage);
				}
		}
	}
	
}
