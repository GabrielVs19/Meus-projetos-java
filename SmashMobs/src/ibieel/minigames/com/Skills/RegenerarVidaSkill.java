package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.UseSkillUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreDPlayer;
import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.AnimatedBallEffect;
import de.slikey.effectlib.util.ParticleEffect;

public class RegenerarVidaSkill implements Listener {

	private Skills skill = Skills.REGENERAR_VIDA;
	private SkillType skillType = skill.getSkillType();
	//1 coraçao a cada qnts ticks
	private int ticksRegen = 10;
	//Tempo maximo em ticks que o cara pode carregar a skill
	private int ticks = 60;
	//Efeito
	EffectManager em = new EffectManager(EffectLib.instance());
	public SmashMobs plugin;
	public RegenerarVidaSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void launchPlayer(PlayerInteractEvent e){
		if(!CoreDPlayer.isSpectator(e.getPlayer())){
			if(SkillEventUtil.rightClickSword(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
				final Player player = e.getPlayer();
				if(!UseSkillUtil.using(player, skill)){
					if(!CooldownManager.isCooldown(player.getName(), skillType)){
						UseSkillUtil.add(player, skill, true);
						player.playSound(player.getLocation(), Sound.WITHER_IDLE, 10, 1);
						final AnimatedBallEffect effectball = new AnimatedBallEffect(em);
						player.setLevel(0);
						new BukkitRunnable(){
							int qnt = 0;
							int vida = 0;
							@Override
							public void run(){
								qnt++;
								vida++;
								player.setExp((float) (((ticks - qnt) * 1.0) / ticks));
								if(qnt <= ticks && player.isBlocking()){
									effectball.setEntity(player);
									effectball.particle = ParticleEffect.DRIP_LAVA;
									effectball.particles = 40;
									effectball.particlesPerIteration = 20;
									effectball.yOffset = -0.9F;
									effectball.yFactor = 1;
									effectball.size = 0.7F;
									effectball.period = 1;
									effectball.infinite();
									effectball.start();
									player.getLocation().getWorld().playSound(player.getLocation(), Sound.WITHER_IDLE, 10, 1);
									if(vida == ticksRegen){
										vida = 0;
										if(((Damageable)player).getHealth() + 2 <= ((Damageable)player).getMaxHealth()){
											player.setHealth(((Damageable)player).getHealth() + 2);
										}
									}
								}else{
									CooldownManager.cooldown(player.getName(), skill);
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
