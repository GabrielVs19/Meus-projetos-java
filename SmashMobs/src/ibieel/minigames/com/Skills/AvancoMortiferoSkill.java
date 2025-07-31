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
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreDPlayer;
import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.AnimatedBallEffect;
import de.slikey.effectlib.util.ParticleEffect;

public class AvancoMortiferoSkill implements Listener {

	private Skills skill = Skills.AVANCO_MORTIFERO;
	private SkillType skillType = skill.getSkillType();
	//Efeito
	EffectManager em = new EffectManager(EffectLib.instance());
	//Dano
	double damage = 5.0;
	public SmashMobs plugin;
	public AvancoMortiferoSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void shoot(PlayerInteractEvent e){
		if(!CoreDPlayer.isSpectator(e.getPlayer()) && SkillEventUtil.rightClickAxe(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
			final Player player = e.getPlayer();
			if(!CooldownManager.isCooldown(player.getName(), skillType)){
				CooldownManager.cooldown(player.getName(), skill);
				SkillEventUtil.sendUseSkillMessage(player, skill);
				player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLAZE_BREATH, 100, 1);
				final AnimatedBallEffect effect = new AnimatedBallEffect(em);
				effect.setEntity(player);
				effect.particle = ParticleEffect.FLAME;
				effect.particles = 100;
				effect.particlesPerIteration = 50;
				effect.yOffset = -0.9F;
				effect.yFactor = 1;
				effect.size = 0.76F;
				effect.period = 1;
				effect.infinite();
				effect.start();
				new BukkitRunnable(){
					int a = 0;
					@Override
					public void run() {
						a++;
						if(a == 20){
							cancel();
							player.setVelocity(player.getEyeLocation().getDirection().multiply(2.6));
							verificar(player, effect);
						}
						player.setVelocity(player.getVelocity().setY(0.25));
					}
				}.runTaskTimer(SmashMobs.getInstance(), 0, 1);
				verificar(player, effect);

			}else{
				SkillEventUtil.soundCooldownSkill(player);
			}
		}
	}

	public void verificar(final Entity player, final AnimatedBallEffect effect){
		new BukkitRunnable(){
			int ticks = 0;
			@Override
			public void run() {
				ticks++;
				if(ticks == 30){
					effect.cancel();
					cancel();
				}
				((Player) player).playSound(player.getLocation(), Sound.FIRE, 100, 1);
				for(Entity e : player.getNearbyEntities(1, 1, 1)){
					if(e instanceof LivingEntity){
						e.setFireTicks(45); 
						((LivingEntity)e).damage(damage);
					}
				}
			}
		}.runTaskTimer(SmashMobs.getInstance(), 0, 1);
	}

}
