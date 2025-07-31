package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import org.bukkit.Effect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import br.com.tlcm.cc.API.CoreDPlayer;

public class VenenoSkill implements Listener {

	private Skills skill = Skills.VENENO;
	//Numero de ticks do veneno
	private int venenoTicks = 35;
	//Porcentagem de chance de dar o veneno
	private int porcentagem = 25;
	public SmashMobs plugin;
	public VenenoSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void applyPoison(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Player){
			Player player = (Player) e.getDamager();
			if(e.getCause() == DamageCause.ENTITY_ATTACK){
				if(!CoreDPlayer.isSpectator(player)){
					if(SkillEventUtil.playerHasSkill(player, skill)){
						int rnd = (int) (Math.random() * 100);
						if(rnd < porcentagem){
							if(e.getEntity() instanceof LivingEntity){
								((LivingEntity)e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, venenoTicks, 1));
								e.getEntity().getLocation().getWorld().playEffect(e.getEntity().getLocation(), Effect.MAGIC_CRIT, 1);
							}
						}
					}
				}
			}
		}
	}

}
