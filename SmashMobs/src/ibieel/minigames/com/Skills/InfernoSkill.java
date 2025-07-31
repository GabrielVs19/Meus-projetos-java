package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.UseSkillUtil;
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

import br.com.tlcm.cc.API.CoreDPlayer;

public class InfernoSkill implements Listener{

	private Skills skill = Skills.INFERNO;
	private SkillType skillType = skill.getSkillType();
	//Dano de cada projetil 
	private double damageProjectile = 2;
	//Quantidade de projeteis arremessados
	private int qntProjectile = 10;
	//Hitbox do projetil
	double hitbox = 0.4;
	//Tempo entre cada projetil (ticks)
	private long tempo = (long) 3.4;
	//Numero de ticks que vai ser aplicado o fogo para cada projetil
	private int fireTicks = 35;
	//Porcentagem de chance para o projetil alem de dar dano deixar a pessoa com fogo
	private int porcentagem = 25;
	//Efeito do projetil
	private Effect effect = Effect.SMOKE;
	public SmashMobs plugin;
	public InfernoSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void shoot(PlayerInteractEvent e){
		if(!CoreDPlayer.isSpectator(e.getPlayer())){
			if(SkillEventUtil.rightClickSword(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
				final Player player = e.getPlayer();
				if(!UseSkillUtil.using(player, skill)){
					if(!CooldownManager.isCooldown(player.getName(), skillType)){
						UseSkillUtil.add(e.getPlayer(), skill, true);
						player.setLevel(0);
						new BukkitRunnable(){
							int qnt = 0;
							@Override
							public void run(){
								qnt++;
								player.setExp((float) (((qntProjectile - qnt) * 1.0) / qntProjectile));
								if(qnt <= qntProjectile && player.isBlocking()){
									Item powder = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.BLAZE_POWDER));
									powder.setVelocity(player.getEyeLocation().getDirection().multiply(1.6));
									player.playSound(player.getLocation(), Sound.GHAST_FIREBALL, 100, 1);
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
							item.getLocation().getWorld().playEffect(item.getLocation(), effect, 1);
						}
					}else{
						locItem = item.getLocation();
						item.getLocation().getWorld().playEffect(item.getLocation(), effect, 1);
					}
					List<Entity> en = item.getNearbyEntities(hitbox, hitbox, hitbox);
					for(Entity e : en){
						if(e instanceof LivingEntity && e != player){
							int rnd = (int) (Math.random() * 100);
							((LivingEntity)e).damage(damageProjectile);
							if(rnd < porcentagem){
								e.setFireTicks(fireTicks);
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
