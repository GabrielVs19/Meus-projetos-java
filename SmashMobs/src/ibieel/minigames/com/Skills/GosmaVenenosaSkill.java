package ibieel.minigames.com.Skills;

import java.util.ArrayList;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.tlcm.cc.API.CoreDPlayer;

public class GosmaVenenosaSkill implements Listener {

	private Skills skill = Skills.GOSMA_VENENOSA;
	//Numero de ticks do veneno
	private int venenoTicks = 50;
	//Porcentagem de chance de dar o veneno
	private int porcentagem = 15;
	public SmashMobs plugin;
	public GosmaVenenosaSkill(SmashMobs plugin){
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
								((LivingEntity)e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, venenoTicks, 1));
								e.getEntity().getLocation().getWorld().playEffect(e.getEntity().getLocation(), Effect.MAGIC_CRIT, 1);
								setEntityEffect(e.getEntity());
							}
						}
					}
				}
			}
		}
	}
	
	public void setEntityEffect(final Entity e){
		final ArrayList<Item> items = new ArrayList<>();
		new BukkitRunnable(){
			int a = 0;
			@Override
			public void run() {
				a += 5;
				for(Item i : items){
					i.remove();
				}
				if(a >= venenoTicks){
					cancel();
				}else{
					Item i = e.getWorld().dropItemNaturally(((LivingEntity)e).getEyeLocation(), new ItemStack(Material.SLIME_BALL));
					items.add(i);
				}
			}
		}.runTaskTimer(SmashMobs.getInstance(), 0, 5);
		
	}
	
}
