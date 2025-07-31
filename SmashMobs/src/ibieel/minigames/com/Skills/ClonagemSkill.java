package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Managers.PlayerManager;
import ibieel.minigames.com.Managers.ScoreboardManager1;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.UseSkillUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import java.util.ArrayList;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreDPlayer;

public class ClonagemSkill implements Listener {

	private Skills skill = Skills.CLONAGEM;
	private SkillType skillType = skill.getSkillType();
	//Quantidade de ticks que os porcos ficarão vivos
	int ticks = 100;
	//Porcentagem de dano retido - max 100
	int porcentagem = 15;
	public SmashMobs plugin;
	public ClonagemSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void shoot(PlayerInteractEvent e){
		if(!CoreDPlayer.isSpectator(e.getPlayer()) && SkillEventUtil.rightClickAxe(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
			final Player player = e.getPlayer();
			if(!CooldownManager.isCooldown(player.getName(), skillType)){
				CooldownManager.cooldown(player.getName(), skill);
				UseSkillUtil.add(player, skill, true);
				player.playSound(player.getLocation(), Sound.PIG_DEATH, 100, 1);
				final ArrayList<Entity> porcos = new ArrayList<>();
				for(int i = 0; i<= 7;i++){
					@SuppressWarnings("deprecation")
					Pig porco = (Pig) player.getWorld().spawnCreature(player.getLocation(), EntityType.PIG);
					porco.setAdult();
					porco.setCustomName(ScoreboardManager1.getCor(PlayerManager.getVidas(player.getName())) +  player.getName());
					porco.setCustomNameVisible(true);
					porcos.add(porco);
				}
				new BukkitRunnable(){
					@Override
					public void run() {
						UseSkillUtil.remove(player, skill, true);
						for(Entity e : porcos){
							e.getLocation().getWorld().playEffect(e.getLocation(), Effect.SMOKE, 10);
							e.remove();
						}
						cancel();
					}
				}.runTaskLater(SmashMobs.getInstance(), ticks);
			}else{
				SkillEventUtil.soundCooldownSkill(player);
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
