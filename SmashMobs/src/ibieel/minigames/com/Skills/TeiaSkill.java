package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.UseSkillUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import java.util.List;

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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreDPlayer;
import de.slikey.effectlib.util.ParticleEffect;

public class TeiaSkill implements Listener {

	private Skills skill = Skills.TEIA;
	private SkillType skillType = skill.getSkillType();
	//Dano de cada projetil 
	private double damageProjectile = 3.0;
	//Quantidade de projeteis arremessados
	private int qntProjectile = 10;
	//Hitbox do projetil
	private double hitbox = 0.4;
	//Tempo entre cada projetil (ticks)
	private long tempo = 3;
	//Numero de ticks que vai ser aplicado o slow para cada projetil
	private int slowTicks = 35;
	//Porcentagem de chance para o projetil alem de dar dano deixar a pessoa com slow
	private int porcentagem = 35;
	//Efeito do projetil
	private ParticleEffect effect = ParticleEffect.MAGIC_CRIT;
	public SmashMobs plugin;
	public TeiaSkill(SmashMobs plugin){
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
						player.setLevel(0);
						new BukkitRunnable(){
							int qnt = 0;
							@Override
							public void run(){
								qnt++;
								player.setExp((float) (((qntProjectile - qnt) * 1.0) / qntProjectile));
								if(qnt <= qntProjectile && player.isBlocking()){
									Item powder = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.WEB));
									powder.setVelocity(player.getEyeLocation().getDirection().multiply(1.6));
									player.playSound(player.getLocation(), Sound.SPIDER_IDLE, 100, 1);
									verificar(((Entity)player), powder);
								}else{
									CooldownManager.cooldown(player.getName(), skill);
									UseSkillUtil.remove(player, skill, false);
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
							effect.display(item.getLocation(), 50);
						}
					}else{
						locItem = item.getLocation();
						effect.display(item.getLocation(), 50);
					}
					List<Entity> en = item.getNearbyEntities(hitbox, hitbox, hitbox);
					for(Entity e : en){
						if(e instanceof LivingEntity && e != player){
							int rnd = (int) (Math.random() * 100);
							((LivingEntity)e).damage(damageProjectile);
							if(rnd < porcentagem){
								((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, slowTicks, 1));
							}
							item.remove();
							cancel();
						}
					}
				}
			}
		}.runTaskTimer(SmashMobs.getInstance(), 0, 1);
	}

}
