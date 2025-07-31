package ibieel.minigames.com.Skills;

import ibieel.minigames.com.SmashMobs;
import ibieel.minigames.com.Managers.CooldownManager;
import ibieel.minigames.com.Util.SkillEventUtil;
import ibieel.minigames.com.Util.SkillsUtil.SkillType;
import ibieel.minigames.com.Util.SkillsUtil.Skills;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import br.com.tlcm.cc.API.CoreDPlayer;
import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;

public class TerremotoSkill implements Listener {

	private Skills skill = Skills.TERREMOTO;
	private SkillType skillType = skill.getSkillType();
	//Efeito
	EffectManager em = new EffectManager(EffectLib.instance());
	//Distacia max q os jogadores vao ser arremessados
	float distanciaMax = 1.6F;
	//Dano
	double dano = 4.5;
	public SmashMobs plugin;
	public TerremotoSkill(SmashMobs plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void shoot(PlayerInteractEvent e){
		if(!CoreDPlayer.isSpectator(e.getPlayer()) && SkillEventUtil.rightClickAxe(e.getAction(), e.getPlayer()) && SkillEventUtil.playerHasSkill(e.getPlayer(), skill)){
			final Player player = e.getPlayer();
			if(!CooldownManager.isCooldown(player.getName(), skillType)){
				SkillEventUtil.sendUseSkillMessage(player, skill);
				CooldownManager.cooldown(player.getName(), skill);
				final HashMap<Location, Material> blocks = new HashMap<>();
				final Location eyeLoc = player.getEyeLocation();
				eyeLoc.setY(player.getEyeLocation().getY() - 1);
				final Vector vector = eyeLoc.toVector();
				new BukkitRunnable(){
					int qnt = 0;
					@Override
					public void run() {
						qnt++;
						if(qnt == 8){
							cancel();
						}
						vector.add(player.getEyeLocation().getDirection());
						Location loc = new Location(player.getWorld(), vector.getX(), eyeLoc.getY(), vector.getZ());
						int stop = 0; 
						while(loc.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR){
							stop++;
							if(stop == 2){
								break;
							}
							loc.setY(loc.getY() - 1);
						}
						boolean a = false;
						while(!a){
							if(loc.getBlock().getType() != Material.AIR ){
								loc.setY(loc.getY() + 1);
							}else{
								a = true;
							}
						}
						blocks.put(loc, player.getWorld().getBlockAt(loc).getType());
						player.getWorld().getBlockAt(loc).setType(Material.DIRT);
						loc.getWorld().playEffect(loc, org.bukkit.Effect.STEP_SOUND, Material.DIRT);
						for(Entity ent : SkillEventUtil.getNearbyEntitiesFromLocation(loc, 2)){
							if(ent instanceof LivingEntity && ent != player){
								((LivingEntity)ent).damage(dano);
								ent.setVelocity(ent.getLocation().getDirection().multiply(-1.2D).setY(distanciaMax));
							}
						}
					}
				}.runTaskTimer(SmashMobs.getInstance(), 0, 1);
				new BukkitRunnable(){
					@Override
					public void run() {
						for(Entry<Location, Material> en : blocks.entrySet()){
							en.getKey().getWorld().getBlockAt(en.getKey()).setType(en.getValue());
							en.getKey().getWorld().playEffect(en.getKey(), org.bukkit.Effect.STEP_SOUND, Material.DIRT);
						}
					}
				}.runTaskLater(SmashMobs.getInstance(), 80);
			}else{
				SkillEventUtil.soundCooldownSkill(player);
			}
		}
	}

}
