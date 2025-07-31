package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import java.util.ArrayList;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.watchers.SlimeWatcher;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreDPlayer;
import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.AnimatedBallEffect;
import de.slikey.effectlib.util.ParticleEffect;

public class ExplosaoDeMassaSkill implements Listener {

	private Skills skill = Skills.EXPLOSAO_DE_MASSA;
	private SkillType skillType = skill.getSkillType();
	//Lista pra cancelar dano da explosao
	private ArrayList<String> cancelDamage = new ArrayList<String>();
	//Explosao
	private float explosao = 3.8F;
	//Efeito
	EffectManager em = new EffectManager(EffectLib.instance());
	public SmashMobs plugin;
	public ExplosaoDeMassaSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void explode(PlayerInteractEvent e){
		final Player player = e.getPlayer();
		if(!CoreDPlayer.isSpectator(e.getPlayer()) && SkillEventUtil.rightClickSword(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
			if(!CooldownManager.isCooldown(player.getName(), skillType)){
				SkillEventUtil.sendUseSkillMessage(player, skill);
				CooldownManager.cooldown(player.getName(), skill);
				cancelDamage.add(player.getName());
				final Disguise disguise = DisguiseAPI.getDisguise(player);
				final SlimeWatcher watcher = (SlimeWatcher) disguise.getWatcher();
				final AnimatedBallEffect effect = new AnimatedBallEffect(em);
				effect.setEntity(player);
				effect.particle = ParticleEffect.HAPPY_VILLAGER;
				effect.particles = 150;
				effect.particlesPerIteration = 150;
				effect.yOffset = -0.9F;
				effect.yFactor = 1;
				effect.size = 2.5F;
				effect.period = 1;
				effect.start();
				new BukkitRunnable(){
					int a = 0;
					@Override
					public void run() {
						a++;
						player.playSound(player.getLocation(), Sound.SLIME_ATTACK, 100, 1);
						watcher.setSize(3 + a);
						if(a >= 5){
							effect.cancel();
							watcher.setSize(3);
							player.getLocation().getWorld().createExplosion(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), explosao, false, false);
							cancel();
						}
					}
				}.runTaskTimer(SmashMobs.getInstance(), 0, 4);
			}else{
				SkillEventUtil.soundCooldownSkill(player);
			}
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
