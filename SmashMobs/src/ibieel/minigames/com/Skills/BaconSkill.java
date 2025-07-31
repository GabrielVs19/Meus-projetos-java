package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import java.util.List;

import org.bukkit.Effect;
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
import org.bukkit.util.Vector;

import br.com.tlcm.cc.API.CoreDPlayer;

public class BaconSkill implements Listener {

	private Skills skill = Skills.BACON;
	private SkillType skillType = skill.getSkillType();
	//Hitbox do projetil
	private double hitbox = 0.4;
	//Efeito do projetil
	private Effect effect = Effect.CLOUD;
	//Tamanho explosao
	private long explosao = (long) 1.5;
	public SmashMobs plugin;
	public BaconSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void shoot(PlayerInteractEvent e){
		if(SkillEventUtil.rightClickSword(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
			final Player player = e.getPlayer();
			if(!CoreDPlayer.isSpectator(player)){
				if(!CooldownManager.isCooldown(player.getName(), skillType)){
					CooldownManager.cooldown(player.getName(), skill);
					SkillEventUtil.sendUseSkillMessage(player, skill);
					Item bacon = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.PORK));
					bacon.setVelocity(player.getEyeLocation().getDirection().multiply(1.6));
					Item bacon1 = player.getWorld().dropItem(player.getEyeLocation().add(new Vector(0, 1, 0)), new ItemStack(Material.PORK));
					bacon1.setVelocity(player.getEyeLocation().getDirection().multiply(1.6));
					player.playSound(player.getLocation(), Sound.PIG_IDLE, 100, 1);
					verificar(((Entity)player), bacon);
					verificar(((Entity)player), bacon1);
				}else{
					SkillEventUtil.soundCooldownSkill(player);
				}
			}
		}
	}


	public void verificar(final Entity player, final Item item){
		new BukkitRunnable(){
			Location locItem = item.getLocation();
			int qnt = 0;
			@Override
			public void run() {
				Location locItem2 = item.getLocation();
				if(item != null){
					if(locItem.getX() == locItem2.getX() && locItem.getZ() == locItem2.getZ()){
						if(qnt == 3){
							item.remove();
							cancel();
						}else{
							qnt++;
							item.getLocation().getWorld().playEffect(item.getLocation(), effect, 1);
						}
					}else{
						locItem = item.getLocation();
						item.getLocation().getWorld().playEffect(item.getLocation(), effect, 1);
					}
					List<Entity> en = item.getNearbyEntities(hitbox, hitbox, hitbox);
					for(Entity e : en){
						if(e instanceof LivingEntity && e != player){
							item.getLocation().getWorld().createExplosion(item.getLocation().getX(), item.getLocation().getY(), item.getLocation().getZ(), explosao, false, false);
							e.setVelocity(item.getLocation().getDirection().multiply(2.3));
							item.remove();
							cancel();
						}
					}
				}
			}
		}.runTaskTimer(SmashMobs.getInstance(), 0, 1);
	}
	
}
