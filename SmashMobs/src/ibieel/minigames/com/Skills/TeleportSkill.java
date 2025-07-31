package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.effect.AnimatedBallEffect;
import de.slikey.effectlib.util.ParticleEffect;

import br.com.tlcm.cc.API.CoreDPlayer;

public class TeleportSkill implements Listener{

	private Skills skill = Skills.TELEPORTE;
	private SkillType skillType = skill.getSkillType();
	//Max distancia em blocos q pode se teleportar
	int distancia = 20;
	//Efeito
	EffectManager em = new EffectManager(EffectLib.instance());
	public SmashMobs plugin;
	public TeleportSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void teleport(PlayerInteractEvent e){
		if(!CoreDPlayer.isSpectator(e.getPlayer())){
			final Player player = e.getPlayer();
			if(SkillEventUtil.rightClickAxe(e.getAction(), player) && SkillEventUtil.playerHasSkill(player, skill)){
				if(!CooldownManager.isCooldown(player.getName(), skillType)){
					if (player.getTargetBlock(null, distancia).getLocation().getBlock().getType() != Material.AIR){
						CooldownManager.cooldown(player.getName(), skill);
						SkillEventUtil.sendUseSkillMessage(player, skill);
						player.teleport(player.getTargetBlock(null, distancia - 2).getLocation());
						player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 50.0F, 1.0F);
						final AnimatedBallEffect effect = new AnimatedBallEffect(em);
						effect.type = EffectType.INSTANT;
						effect.setEntity(player);
						effect.particle = ParticleEffect.PORTAL;
						effect.particles = 100;
						effect.particlesPerIteration = 100;
						effect.yOffset = -0.9F;
						effect.yFactor = 1;
						effect.size = 1.0F;
						effect.period = 1;
						effect.start();
					}
				}else{
					SkillEventUtil.soundCooldownSkill(player);
				}
			}
		}
	}

}
