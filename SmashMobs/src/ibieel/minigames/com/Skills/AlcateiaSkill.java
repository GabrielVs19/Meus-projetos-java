package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;
import ibieel.minigames.com.Util.UseSkillUtil;

import java.util.ArrayList;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreDPlayer;

public class AlcateiaSkill implements Listener {

	private Skills skill = Skills.ALCATEIA;
	private SkillType skillType = skill.getSkillType();
	//Quantidade de ticks que os lobos vao ficar vivos
	int ticks = 180;
	public SmashMobs plugin;
	public AlcateiaSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void wolfSpawn(PlayerInteractEvent e){
		if(!CoreDPlayer.isSpectator(e.getPlayer()) && SkillEventUtil.rightClickAxe(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
			final Player player = e.getPlayer();
			if(!CooldownManager.isCooldown(player.getName(), skillType) && !UseSkillUtil.using(player, skill)){
				player.playSound(player.getLocation(), Sound.WOLF_BARK, 100, 1);
				UseSkillUtil.add(player, skill, true);
				final ArrayList<Entity> wolfs = new ArrayList<>();
				for(int i = 0; i <= 3; i++){
					Wolf wolf = (Wolf) player.getWorld().spawnCreature(player.getLocation(), EntityType.WOLF);
					wolf.setOwner(player);
					wolf.setAngry(true);
					wolf.setAdult();
					wolf.setMaxHealth(30);
					wolf.setHealth(30);
					wolfs.add(wolf);
				}
				new BukkitRunnable(){
					@Override
					public void run() {
						UseSkillUtil.remove(player, skill, true);
						CooldownManager.cooldown(player.getName(), skill);
						for(Entity e : wolfs){
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
	
}
