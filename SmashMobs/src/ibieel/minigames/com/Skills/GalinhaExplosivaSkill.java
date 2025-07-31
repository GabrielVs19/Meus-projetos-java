package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreDPlayer;

public class GalinhaExplosivaSkill implements Listener {


	private Skills skill = Skills.GALINHA_EXPLOSIVA;
	private SkillType skillType = skill.getSkillType();
	//Hitbox do projetil
	private double hitbox = 0.4;
	//Efeito do projetil
	private Effect effect = Effect.CLOUD;
	//Tamanho da explosao
	private float explosao = 2.0F;
	//Ticks para explodir sozinha
	private int ticks = 60;
	public SmashMobs plugin;
	public GalinhaExplosivaSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void shoot(PlayerInteractEvent e){
		if(SkillEventUtil.rightClickShovel(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
			final Player player = e.getPlayer();
			if(!CoreDPlayer.isSpectator(player)){
				if(!CooldownManager.isCooldown(player.getName(), skillType)){
					CooldownManager.cooldown(player.getName(), skill);
					SkillEventUtil.sendUseSkillMessage(player, skill);
					Location loc = player.getEyeLocation();
					Entity galinha = player.getWorld().spawnEntity(loc, EntityType.CHICKEN);
					galinha.setVelocity(player.getEyeLocation().getDirection().multiply(2.4));
					player.playSound(player.getLocation(), Sound.CHICKEN_HURT, 100, 1);
					verificar(((Entity)player), galinha);
					player.playSound(player.getLocation(), Sound.CHICKEN_HURT, 100, 1);
				}else{
					SkillEventUtil.soundCooldownSkill(player);
				}
			}
		}
	}

	public void verificar(final Entity player, final Entity entity){
		new BukkitRunnable(){
			int qnt = 0;
			@Override
			public void run() {
				if(entity != null){
					qnt++;
					if(qnt <= ticks){
						entity.getLocation().getWorld().playEffect(entity.getLocation(), effect, 10);
						List<Entity> en = entity.getNearbyEntities(hitbox, hitbox, hitbox);
						for(Entity e : en){
							if(e instanceof LivingEntity && e != player){
								entity.getLocation().getWorld().createExplosion(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), explosao, false, false);
								e.setVelocity(e.getLocation().getDirection().multiply(-2.0));
								entity.remove();
								cancel();
							}
						}
					}else{
						entity.getLocation().getWorld().createExplosion(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), explosao, false, false);
						entity.remove();
						cancel();
					}
				}
			}
		}.runTaskTimer(SmashMobs.getInstance(), 0, 1);
	}


}
