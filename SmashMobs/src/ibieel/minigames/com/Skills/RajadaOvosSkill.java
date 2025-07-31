package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.UseSkillUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import java.util.ArrayList;

import org.bukkit.Sound;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreDPlayer;

public class RajadaOvosSkill implements Listener{

	private Skills skill = Skills.RAJADA_DE_OVOS;
	private SkillType skillType = skill.getSkillType();
	//Dano de cada ovo
	private double eggDamage = 3.0;
	//Tempo entre cada projetil (ticks)
	private long tempo = 2;
	public SmashMobs plugin;
	public RajadaOvosSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void shoot(PlayerInteractEvent e){
			if(!CoreDPlayer.isSpectator(e.getPlayer())){
				if(SkillEventUtil.rightClickSword(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
					final Player player = e.getPlayer();
					if(!UseSkillUtil.using(player, skill)){
						if(!CooldownManager.isCooldown(player.getName(), skillType)){
							UseSkillUtil.add(player, skill, true);
							final ArrayList<Projectile> eggs = new ArrayList<Projectile>();
							player.setLevel(0);
							new BukkitRunnable(){
								int qnt = 0;
								@Override
								public void run(){
									qnt++;
									player.setExp((float) (((12 - qnt) * 1.0) / 12));
									if(qnt <= 12 && player.isBlocking()){
											Projectile egg = player.launchProjectile(Egg.class, player.getEyeLocation().getDirection());
											egg.setVelocity(egg.getVelocity().multiply(1.6));
											player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 100, 1);
											eggs.add(egg);
									}else{
										CooldownManager.cooldown(player.getName(), skill);
										UseSkillUtil.remove(player, skill, false);
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

	//Cancela o spawn de galinhas bebe com o projectil da skill
	@EventHandler
	public void cancelChickenBabySpawn(CreatureSpawnEvent e){
		if(e.getEntityType() == EntityType.CHICKEN){
			if(e.getSpawnReason() == SpawnReason.EGG){
				e.setCancelled(true);
			}
		}
	}

	//Altera o dano do ovo para quem possui essa skill e faz o knockback
	@SuppressWarnings("deprecation")
	@EventHandler
	public void changeEggDamage(EntityDamageByEntityEvent e){
		if(e.getCause() == DamageCause.PROJECTILE){
			if(e.getDamager() instanceof Egg){
				Egg egg = (Egg) e.getDamager();
				if(egg.getShooter() instanceof Player){
					if(SkillEventUtil.playerHasSkill(((Player)egg.getShooter()), skill)){
						e.setDamage(eggDamage);
						e.getEntity().setVelocity(egg.getLocation().getDirection().multiply(1.55));
					}
				}
			}
		}
	}

}
