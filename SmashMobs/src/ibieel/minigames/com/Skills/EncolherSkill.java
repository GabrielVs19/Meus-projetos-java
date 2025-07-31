package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;
import ibieel.minigames.com.Util.UseSkillUtil;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.watchers.SlimeWatcher;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreDPlayer;
import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.effect.AnimatedBallEffect;
import de.slikey.effectlib.util.ParticleEffect;

public class EncolherSkill implements Listener {

	private Skills skill = Skills.ENCOLHER;
	private SkillType skillType = skill.getSkillType();
	//Efeito
	EffectManager em = new EffectManager(EffectLib.instance());
	//Quantidade de ticks que vai ficar o encolhimento
	int ticks = 85;
	public SmashMobs plugin;
	public EncolherSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void encolher(PlayerInteractEvent e){
		if(!CoreDPlayer.isSpectator(e.getPlayer()) && SkillEventUtil.rightClickAxe(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
			final Player player = e.getPlayer();
			if(!CooldownManager.isCooldown(player.getName(), skillType) && !UseSkillUtil.using(player, skill)){
				player.playSound(player.getLocation(), Sound.WITHER_SHOOT, 100, 1);
				UseSkillUtil.add(player, skill, true);
				final AnimatedBallEffect effect = new AnimatedBallEffect(em);
				effect.setEntity(player);
				effect.particle = ParticleEffect.HAPPY_VILLAGER;
				effect.particles = 60;
				effect.particlesPerIteration = 60;
				effect.yOffset = -0.9F;
				effect.yFactor = 1;
				effect.type = EffectType.INSTANT;
				effect.size = 0.9F;
				effect.period = 1;
				effect.start();
				final Disguise disguise = DisguiseAPI.getDisguise(player);
				final SlimeWatcher watcher = (SlimeWatcher) disguise.getWatcher();
				watcher.setSize(1);
				new BukkitRunnable(){
					@Override
					public void run() {
						UseSkillUtil.remove(player, skill, true);
						CooldownManager.cooldown(player.getName(), skill);
						effect.cancel();
						watcher.setSize(3);
						cancel();
					}
				}.runTaskLater(SmashMobs.getInstance(), ticks);
			}else{
				SkillEventUtil.soundCooldownSkill(player);
			}
		}
	}


	
}
