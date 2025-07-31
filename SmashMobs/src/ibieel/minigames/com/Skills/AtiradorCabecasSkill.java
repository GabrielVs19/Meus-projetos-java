package ibieel.minigames.com.Skills;

import java.util.ArrayList;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import br.com.tlcm.cc.API.CoreD;
import br.com.tlcm.cc.API.CoreDPlayer;

public class AtiradorCabecasSkill implements Listener {

	private Skills skill = Skills.ATIRADOR_DE_CABECAS;
	private SkillType skillType = skill.getSkillType();
	//Dano
	private double damage = 6.0;
	//Lista de cabeças usadas por esta skill para setar o dano
	private ArrayList<Projectile> skulls = new ArrayList<>();
	public SmashMobs plugin;
	public AtiradorCabecasSkill(SmashMobs plugin){
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
						Projectile skull = player.launchProjectile(WitherSkull.class, player.getEyeLocation().getDirection());
						skulls.add(skull);
					player.playSound(player.getLocation(), Sound.WITHER_SHOOT, 100, 1);
				}else{
					SkillEventUtil.soundCooldownSkill(player);
				}
			}
		}
	}

	@EventHandler
	public void removeSkulls(EntityDeathEvent e){
		if(e.getEntity() instanceof WitherSkull){
			Projectile skull = (Projectile) e.getEntity();
			if(skulls.contains(skull)){
				skulls.remove(skull);
			}
		}
	}

	@EventHandler
	public void changeSkullDamage(EntityDamageByEntityEvent e){
		if(CoreD.isRunning()){
			if(e.getDamager() instanceof WitherSkull){
				Projectile skull = (Projectile) e.getDamager();
				if(skulls.contains(skull)){
					e.setDamage(e.getDamage() <= damage ? e.getDamage() : damage);
					skulls.remove(skull);
				}
			}
		}
	}

}
