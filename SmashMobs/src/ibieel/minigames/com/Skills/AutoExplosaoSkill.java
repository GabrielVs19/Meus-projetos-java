package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;
import ibieel.minigames.com.Util.UseSkillUtil;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreDPlayer;
import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.AnimatedBallEffect;
import de.slikey.effectlib.util.ParticleEffect;

public class AutoExplosaoSkill implements Listener {

	private Skills skill = Skills.AUTO_EXPLOSAO;
	private SkillType skillType = skill.getSkillType();
	//Lista pra cancelar dano da explosao
	private ArrayList<String> cancelDamage = new ArrayList<String>();
	//Tamanho max da explosao
	private float maxExplosao = 3.8F;
	//Tamanho min da explosão
	private float minExplosao = 1.3F;
	//Tempo maximo em ticks que o cara pode carregar a skill
	private int ticks = 60;
	//Usa-se explosao / ticks
	private double multiplicadorPorTick = (maxExplosao - minExplosao) / ticks;
	//Efeito
	EffectManager em = new EffectManager(EffectLib.instance());
	public SmashMobs plugin;
	public AutoExplosaoSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void explode(PlayerInteractEvent e){
		final Player player = e.getPlayer();
		if(!UseSkillUtil.using(player, skill) && !CoreDPlayer.isSpectator(e.getPlayer()) && SkillEventUtil.rightClickSword(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
				if(!CooldownManager.isCooldown(player.getName(), skillType)){
					UseSkillUtil.add(player, skill, false);
					SkillEventUtil.sendUseSkillMessage(player, skill);
					final AnimatedBallEffect effect = new AnimatedBallEffect(em);
					effect.setEntity(player);
					effect.particle = ParticleEffect.LARGE_SMOKE;
					effect.particles = 150;
					effect.particlesPerIteration = 20;
					effect.yOffset = -0.9F;
					effect.yFactor = 1;
					effect.size = 1.0F;
					effect.period = 1;
					effect.infinite();
					effect.start();
					player.setLevel(0);
					new BukkitRunnable(){
						int qnt = 0;
						float alt = 0;
						@Override
						public void run(){
							qnt++;
							player.setExp((float) (((ticks - qnt) * 1.0) / ticks));
							if(qnt <= ticks && player.isBlocking()){
								alt += multiplicadorPorTick;
								player.playSound(player.getLocation(), Sound.CREEPER_HISS, 100, 1);
							}else{
								player.setLevel(0);
								CooldownManager.cooldown(player.getName(), skill);
								cancelDamage.add(player.getName());
								UseSkillUtil.remove(player, skill, false);
								effect.cancel();
								player.getLocation().getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), minExplosao + (minExplosao + alt <= maxExplosao ? alt : maxExplosao), false, false);
								player.setVelocity(player.getLocation().getDirection().multiply(1.2D).setY(1.5D));
								cancel();
							}
						}
					}.runTaskTimer(SmashMobs.getInstance(), 0, 1);
				}else{
					SkillEventUtil.soundCooldownSkill(player);
				}
			}
		}
	@EventHandler
	public void cancelMove(PlayerMoveEvent e){
		if(UseSkillUtil.using(e.getPlayer(), skill)){
			Location loc = e.getFrom();
			loc.setPitch(e.getTo().getPitch());
			loc.setYaw(e.getTo().getYaw());
			e.getPlayer().teleport(loc);
		}
	}

	@EventHandler
	public void cancelExplosionDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player player = (Player) e.getEntity();
			if((e.getCause() == DamageCause.ENTITY_EXPLOSION || e.getCause() == DamageCause.BLOCK_EXPLOSION) && cancelDamage.contains(player.getName())){
				e.setCancelled(true);
				cancelDamage.remove(player.getName());
			}
		}
	}

}
