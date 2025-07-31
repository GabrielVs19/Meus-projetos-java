package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.ClassManager;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Managers.PlayerManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.UseSkillUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreDPlayer;
import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.AnimatedBallEffect;
import de.slikey.effectlib.util.ParticleEffect;

public class FuriaSkill implements Listener {

	private Skills skill = Skills.FURIA;
	private SkillType skillType = skill.getSkillType();
	//Efeito
	EffectManager em = new EffectManager(EffectLib.instance());
	//Quantidade de ticks que vai ficar o efeito
	int ticks = 100;
	//Porcentagem de dano retido - max 100
	int porcentagem = 10;
	public SmashMobs plugin;
	public FuriaSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void shoot(PlayerInteractEvent e){
		if(!CoreDPlayer.isSpectator(e.getPlayer()) && SkillEventUtil.rightClickShovel(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
			final Player player = e.getPlayer();
			if(!CooldownManager.isCooldown(player.getName(), skillType) && !UseSkillUtil.using(player, skill)){
				player.playSound(player.getLocation(), Sound.ZOMBIE_PIG_ANGRY, 100, 1);
				player.playSound(player.getLocation(), Sound.ZOMBIE_PIG_ANGRY, 100, 1);
				UseSkillUtil.add(player, skill, true);
				final AnimatedBallEffect effect = new AnimatedBallEffect(em);
				effect.setEntity(player);
				effect.particle = ParticleEffect.ANGRY_VILLAGER;
				effect.particles = 5;
				effect.particlesPerIteration = 5;
				effect.yOffset = -1.1F;
				effect.yFactor = 1;
				effect.size = 0.4F;
				effect.period = 4;
				effect.infinite();
				effect.start();
				new BukkitRunnable(){
					@Override
					public void run() {
						UseSkillUtil.remove(player, skill, true);
						CooldownManager.cooldown(player.getName(), skill);
						effect.cancel();
						cancel();
					}
				}.runTaskLater(SmashMobs.getInstance(), ticks);
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
						double playerDamage = ClassManager.getClass((PlayerManager.getClasse(player.getName()))).getAttackDamage() * 1.2; 
						e.setDamage(e.getDamage() * 1.2 <=  playerDamage ? e.getDamage() * 1.2 : playerDamage);
				}
		}
	}

	@EventHandler
	public void changeDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player player = (Player) e.getEntity();
			if(!CoreDPlayer.isSpectator(player)){
				if(SkillEventUtil.playerHasSkill(player, skill)){
					if(UseSkillUtil.using(player, skill)){
						e.setDamage(e.getDamage() - (e.getDamage() / (100 / porcentagem)));
					}
				}
			}
		}
	}
	
}
