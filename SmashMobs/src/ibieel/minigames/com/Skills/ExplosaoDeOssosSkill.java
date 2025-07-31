package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreDPlayer;
import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;

public class ExplosaoDeOssosSkill implements Listener {

	private Skills skill = Skills.EXPLOSAO_DE_OSSOS;
	private SkillType skillType = skill.getSkillType();
	//Efeito
	EffectManager em = new EffectManager(EffectLib.instance());
	//Distacia max q os jogadores vao ser arremessados
	float distanciaMax = -2.4F;
	public SmashMobs plugin;
	public ExplosaoDeOssosSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void shoot(PlayerInteractEvent e){
		if(!CoreDPlayer.isSpectator(e.getPlayer()) && SkillEventUtil.rightClickAxe(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
			final Player player = e.getPlayer();
			if(!CooldownManager.isCooldown(player.getName(), skillType)){
				SkillEventUtil.sendUseSkillMessage(player, skill);
				player.playSound(player.getLocation(), Sound.SKELETON_HURT, 100, 1);
				CooldownManager.cooldown(player.getName(), skill);
				final ArrayList<Item> ossos = new ArrayList<>();
				Location location = player.getEyeLocation();
				for(float x = -1; x <= 1; x++){
					for(float y = -1; y <= 1; y++){
						for(float z = -1; z <= 1; z++){
							location.setDirection(new org.bukkit.util.Vector(x, y, z));
							Item osso = player.getWorld().dropItem(location, new ItemStack(Material.BONE));
							osso.setVelocity(location.getDirection());
							ossos.add(osso);
						}
					}
				}
				Location loc2 = player.getLocation();
				for(Entity en : player.getNearbyEntities(5, 5, 5)){
					if(!(en instanceof Item) && en instanceof LivingEntity){
						loc2.setDirection(player.getLocation().subtract(en.getLocation()).toVector());
						en.setVelocity(loc2.getDirection().multiply(distanciaMax));
						((LivingEntity)en).damage(4.0);
					}
				}
				new BukkitRunnable(){
					@Override
					public void run() {
						for(Item item : ossos){
							if(item != null){
								item.remove();
							}
						}
						cancel();
					}
				}.runTaskLater(SmashMobs.getInstance(), 20 * 2);
			}else{
				SkillEventUtil.soundCooldownSkill(player);
			}
		}
		}


	}
