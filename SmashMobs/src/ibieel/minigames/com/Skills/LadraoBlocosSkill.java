package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.UseSkillUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreDPlayer;

public class LadraoBlocosSkill implements Listener {

	private Skills skill = Skills.LADRAO_DE_BLOCOS;
	private SkillType skillType = skill.getSkillType();
	//Velocidade max que é arremessado
	private double vel = 2.3;
	//Tempo maximo em ticks que o cara pode carregar a skill
	private int ticks = 30;
	//Usa-se (vel - 1) / ticks
	private double multiplicadorPorTick = (vel - 1) / ticks;
	//Hitbox do bloco
	private double hitbox = 1.0;
	//Dano
	private double damage = 7.5;
	ArrayList<FallingBlock> fbl = new ArrayList<>();
	public SmashMobs plugin;
	public LadraoBlocosSkill(SmashMobs plugin){
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
						new BukkitRunnable(){
							int qnt = 0;
							double alt = 1;
							@SuppressWarnings("deprecation")
							@Override
							public void run(){
								qnt++;
								if(qnt <= ticks && player.isBlocking()){
									alt += multiplicadorPorTick;
									player.getLocation().getWorld().playEffect(player.getLocation(), Effect.FIREWORKS_SPARK, 10);
									player.playSound(player.getLocation(), Sound.FIZZ, 10, 1);
								}else{
									CooldownManager.cooldown(player.getName(), skill);
									Byte blockData = 0x0;
									FallingBlock fb = player.getWorld().spawnFallingBlock(player.getEyeLocation(), Material.ENDER_STONE, blockData);
									fb.setDropItem(false);
									fb.setVelocity(player.getEyeLocation().getDirection().multiply((alt <= vel ? alt : vel)));
									player.getLocation().getWorld().playSound(player.getLocation(), Sound.DIG_GRASS, 100, 1);
									UseSkillUtil.remove(player, skill, false);
									fbl.add(fb);
									verificar(player, fb);
									cancel();
								}
							}
						}.runTaskTimer(SmashMobs.getInstance(), 0, 1);
					}else{
						SkillEventUtil.soundCooldownSkill(player);
					}
				}
			}
		}
	}

	@EventHandler
	public void biel(final EntityChangeBlockEvent e){
		if(e.getBlock().getType() == Material.ENDER_STONE || e.getTo() == Material.ENDER_STONE){
			e.setCancelled(true);
			new BukkitRunnable(){
				@Override
				public void run() {
					e.getBlock().setType(Material.AIR);
					fbl.remove(e.getEntity());
				}
			}.runTaskLater(SmashMobs.getInstance(), 60);
		}
	}

	public void verificar(final Entity player, final FallingBlock item){
		new BukkitRunnable(){
			int a = 0;
			@Override
			public void run() {
				a++;
				if(a == 80){
					try{
						item.remove();
					}catch(Exception e){}
					cancel();
				}
				if(fbl.contains(item)){
					List<Entity> en = item.getNearbyEntities(hitbox, hitbox, hitbox);
					for(Entity e : en){
						if(e instanceof LivingEntity && e != player){
							((LivingEntity)e).damage(damage);
							item.remove();
						}
					}
				}else{
					cancel();
				}
			}
		}.runTaskTimer(SmashMobs.getInstance(), 0, 1);
	}

}
