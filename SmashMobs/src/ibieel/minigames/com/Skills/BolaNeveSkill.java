package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Managers.PlayerManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import java.util.ArrayList;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreDPlayer;

public class BolaNeveSkill implements Listener {

	private Skills skill = Skills.BOLA_DE_NEVE;
	private SkillType skillType = skill.getSkillType();
	//Faz com que o jogador ao clicar com o botao direito rapido não consiga usar mais de 1 vez a skill
	private ArrayList<String> useSkill = new ArrayList<String>();
	//Dano
	private double snowballDamage = 3.0;
	//Tempo entre cada projetil (ticks)
	private long tempo = 2;
	public SmashMobs plugin;
	public BolaNeveSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void shoot(PlayerInteractEvent e){
		if(!CoreDPlayer.isSpectator(e.getPlayer())){
			if(SkillEventUtil.rightClickSword(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
				final Player player = e.getPlayer();
				if(!useSkill.contains(player.getName())){
					if(!CooldownManager.isCooldown(player.getName(), skillType)){
						useSkill.add(player.getName());
						final ArrayList<Projectile> eggs = new ArrayList<Projectile>();
						SkillEventUtil.sendUseSkillMessage(player, skill);
						player.setLevel(0);
						new BukkitRunnable(){
							int qnt = 0;
							@Override
							public void run(){
								qnt++;
								player.setExp((float) (((12 - qnt) * 1.0) / 12));
								if(qnt <= 12 && player.isBlocking()){
									Projectile egg = player.launchProjectile(Snowball.class, player.getEyeLocation().getDirection());
									egg.setVelocity(egg.getVelocity().multiply(1.4));
									player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 100, 1);
									eggs.add(egg);
								}else{
									CooldownManager.cooldown(player.getName(), skill);
									useSkill.remove(player.getName());
									for(Projectile egg1 : eggs){
										egg1.remove();
									}
									player.setLevel(0);
									cancel();
								}
							}
						}.runTaskTimer(SmashMobs.getInstance(), 0, tempo);
					}else{
						SkillEventUtil.soundCooldownSkill(player);
					}
				}
			}
		}
	}

	//Altera o dano do ovo para quem possui essa skill e faz o knockback
	@SuppressWarnings("deprecation")
	@EventHandler
	public void changeEggDamage(EntityDamageByEntityEvent e){
		if(e.getCause() == DamageCause.PROJECTILE){
			if(e.getDamager() instanceof Snowball){
				Snowball snowball = (Snowball) e.getDamager();
				if(snowball.getShooter() instanceof Player){
					if(PlayerManager.getSkills(((Player)snowball.getShooter()).getName()).contains(skill)){
						e.setDamage(snowballDamage);
						e.getEntity().setVelocity(snowball.getLocation().getDirection().multiply(1.4));
					}
				}
			}
		}
	}

}
