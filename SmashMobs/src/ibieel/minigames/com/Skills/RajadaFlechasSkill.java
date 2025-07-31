package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.UseSkillUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import java.util.ArrayList;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreDPlayer;

public class RajadaFlechasSkill implements Listener{


	private Skills skill = Skills.RAJADA_DE_FLECHAS;
	private SkillType skillType = skill.getSkillType();
	//Dano de cada flecha
	private double arrowDamage = 3.0;
	//Tempo entre cada projetil (ticks)
	private long tempo = 3;
	public SmashMobs plugin;
	public RajadaFlechasSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void shoot(PlayerInteractEvent e){
		if(!CoreDPlayer.isSpectator(e.getPlayer())){
			if(SkillEventUtil.rightClickSword(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
				final Player player = e.getPlayer();
				if(!UseSkillUtil.using(player, skill)){
					if(!CooldownManager.isCooldown(player.getName(), skillType)){
						UseSkillUtil.add(e.getPlayer(), skill, true);
						final ArrayList<Projectile> flechas = new ArrayList<Projectile>();
						player.setLevel(0);
						new BukkitRunnable(){
							int qnt = 0;
							@Override
							public void run(){
								qnt++;
								player.setExp((float) (((12 - qnt) * 1.0) / 12));
								if(qnt <= 12 && player.isBlocking()){
									@SuppressWarnings("deprecation")
									Projectile flecha = player.shootArrow();
									flecha.setVelocity(flecha.getVelocity().multiply(1.6));
									flechas.add(flecha);
								}else{
									CooldownManager.cooldown(player.getName(), skill);
									UseSkillUtil.remove(player, skill, false);
									player.setLevel(0);
									cancel();
								}
							}
						}.runTaskTimer(SmashMobs.getInstance(), 0, tempo);
						new BukkitRunnable(){
							@Override
							public void run() {
								for(Projectile flecha : flechas){
									flecha.remove();
								}
							}

						}.runTaskLater(SmashMobs.getInstance(), 30);

					}else{
						SkillEventUtil.soundCooldownSkill(player);
					}
				}
			}
		}
	}
	//Altera o dano da flecha para quem possui essa skill
	@SuppressWarnings("deprecation")
	@EventHandler
	public void changeArrowDamage(EntityDamageByEntityEvent e){
		if(e.getCause() == DamageCause.PROJECTILE){
			if(e.getDamager() instanceof Arrow){
				Arrow arrow = (Arrow) e.getDamager();
				if(arrow.getShooter() instanceof Player){
					if(SkillEventUtil.playerHasSkill((((Player)arrow.getShooter())), skill)){
						e.setDamage(arrowDamage);
					}
				}
			}
		}
	}



}
