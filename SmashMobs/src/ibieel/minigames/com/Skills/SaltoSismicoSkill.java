package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.UseSkillUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.AnimatedBallEffect;
import de.slikey.effectlib.util.ParticleEffect;

import br.com.tlcm.cc.API.CoreDPlayer;

public class SaltoSismicoSkill implements Listener {

	private Skills skill = Skills.SALTO_SISMICO;
	private SkillType skillType = skill.getSkillType();
	//Altura max que é arremessado
	private double altura = 2.1;
	//Tempo maximo em ticks que o cara pode carregar a skill
	private int ticks = 40;
	//Usa-se (altura - 1) / ticks
	private double multiplicadorPorTick = (altura - 1) / ticks;
	//Efeito
	EffectManager em = new EffectManager(EffectLib.instance());
	public SmashMobs plugin;
	public SaltoSismicoSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void launchPlayer(PlayerInteractEvent e){
		if(!CoreDPlayer.isSpectator(e.getPlayer())){
			if(SkillEventUtil.rightClickSword(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
				final Player player = e.getPlayer();
				if(!UseSkillUtil.using(e.getPlayer(), skill)){
					if(!CooldownManager.isCooldown(player.getName(), skillType)){
						UseSkillUtil.add(player, skill, true);
						final AnimatedBallEffect effectball = new AnimatedBallEffect(em);
						player.setLevel(0);
						new BukkitRunnable(){
							int qnt = 0;
							double alt = 1;
							@Override
							public void run(){
								qnt++;
								player.setExp((float) (((ticks - qnt) * 1.0) / ticks));
								if(qnt <= ticks && player.isBlocking()){
									alt += multiplicadorPorTick;
									effectball.setEntity(player);
									effectball.particle = ParticleEffect.FIREWORKS_SPARK;
									effectball.particles = 50;
									effectball.particlesPerIteration = 25;
									effectball.yOffset = -0.9F;
									effectball.yFactor = 1;
									effectball.size = 1.1F;
									effectball.period = 1;
									effectball.infinite();
									effectball.start();
									player.playSound(player.getLocation(), Sound.FIZZ, 10, 1);
								}else{
									CooldownManager.cooldown(player.getName(), skill);
									player.setVelocity(player.getVelocity().setY((alt <= altura ? alt : altura)));
									player.playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 100, 1);
									UseSkillUtil.remove(player, skill, false);
									effectball.cancel();
									player.setLevel(0);
									cancel();
								}
							}
						}.runTaskTimer(SmashMobs.getInstance(), 0, 1);
					}else{
						SkillEventUtil.soundCooldownSkill(player);
					}
				}
			}
		}
	}

}
